import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.io.*;
import java.text.SimpleDateFormat;

/**
 * Main CardGame class for initializing and managing the game.
 */
public class CardGame {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        int numOfPlayers;
        String packFilePath;

        // Step 1: Input number of players
        while (true) {
            System.out.print("Enter the number of players (at least 2): ");
            String input = scanner.nextLine().trim();
            try {
                numOfPlayers = Integer.parseInt(input);
                if (numOfPlayers >= 2) break;
                System.out.println("Invalid number of players. Please enter a number greater than or equal to 2.\n");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input type. Please enter a valid number (e.g. 2).\n");
            }
        }
        

        // Step 2: Input and validate file path for card pack
        while (true) {
            System.out.print("Enter the location of pack to load: ");
            packFilePath = scanner.nextLine().trim();

            // Remove quotation marks if present
            if (packFilePath.startsWith("\"") && packFilePath.endsWith("\"")) {
                packFilePath = packFilePath.substring(1, packFilePath.length() - 1);
            }

            // Check if the file exists before attempting to validate its contents
            File file = new File(packFilePath);
            if (!file.exists()) {
                System.out.println("File not found: " + packFilePath);
                continue;  
            } 

            // If file exists, then validate its content
            if (validateFile(packFilePath, numOfPlayers)) {
                break;  // If the file is valid, exit the loop
            } 
        }

        System.out.println("\nNumber of Players: " + numOfPlayers + " | " + "File Path: " + packFilePath);
        System.out.println("Starting Game...");

        // Step 3: Start the game
        runGame(packFilePath, numOfPlayers);
    }

    public static void runGame(String packFilePath, int numOfPlayers) throws InterruptedException {
        ArrayList<Card> pack = new ArrayList<>();
        Player[] players = new Player[numOfPlayers];
        CardDeck[] decks = new CardDeck[numOfPlayers];
        AtomicBoolean gameEnded = new AtomicBoolean(false);

        // Generate a folder name based on the current date and time
        SimpleDateFormat sdf = new SimpleDateFormat("HH-mm-ss_yyyy-MM-dd");
        String folderName = "game_" + sdf.format(new Date());

        // Create output folder in directory
        File gameFolder = new File(folderName);
        if (!gameFolder.exists()) {
            gameFolder.mkdirs();
        }

        // Load and shuffle the card pack
        loadAndShufflePack(packFilePath, pack);

        // **Debugging: Print the shuffled pack**
        System.out.println("Shuffled Pack: " + pack);

        // Initialize decks
        for (int i = 0; i < numOfPlayers; i++) {
            decks[i] = new CardDeck(i + 1, gameFolder);
        }

        // Initialize players
        for (int i = 0; i < numOfPlayers; i++) {
            CardDeck leftDeck = decks[i];
            CardDeck rightDeck = decks[(i + 1) % numOfPlayers];
            players[i] = new Player(i + 1, gameFolder, leftDeck, rightDeck, gameEnded, players);
        }

        // Distribute cards to players' hands
        for (int i = 0; i < 4 * numOfPlayers; i++) {
            players[i % numOfPlayers].addCardToHand(pack.get(i));
        }

        // Log players' initial hands
        for (Player p : players) {
            p.logAction("initial hand " + p.getHandAsString());
            p.logAction("");
        }

        // **Debugging: Print the initial cards in each player hand before drawing and discarding**
        System.out.println("");
        for (int i = 0; i < numOfPlayers; i++) {
            System.out.println("Player " + (i + 1) + " Initial Hand: " + players[i].getHandAsString());
        }

        // Distribute remaining cards to decks
        for (int i = 4 * numOfPlayers; i < 8 * numOfPlayers; i++) {
            decks[i % numOfPlayers].addCard(pack.get(i));
        }

        // **Debugging: Print the initial cards in each deck before players begin drawing and discarding**
        System.out.println("");
        for (int i = 0; i < numOfPlayers; i++) {
            System.out.println("Deck " + (i + 1) + " Initial Cards: " + decks[i].getDeckCardsAsString());
        }

        // Start player threads
        for (Player p : players) {
            p.start();
        }

        // Game loop
        while (!gameEnded.get()) {
            // Game continues as long as no player has won
            Thread.sleep(100); // Adjust this delay if necessary
        }

        // Print the directory where the folder is created
        System.out.println("\nFile Output Path: " + gameFolder.getAbsolutePath());

        // End game: Stop all player threads and log deck contents
        for (Player p : players) {
            p.endGame();
            try {
                p.closeLog();
            } catch (IOException e) {
                System.out.println("Error closing player " + p.getPlayerNumber() + "'s log file.");
            }
        }

        // **Debugging: Print the winner**
        System.out.println("");
        for (Player p : players) {
            if (p.hasWon()) {
                System.out.println("Player " + p.getPlayerNumber() + " Wins!");
                break;
            }
        }

        // Log deck contents
        for (CardDeck d : decks) {
            d.logDeckToFile(gameFolder);
        }
    }

    /**
     * Loads card denominations from a specified file and adds them as Card objects to the given list.
     * Then, shuffles the list to randomize the card order.
     *
     * @param packFilePath the path to the file containing the card denominations
     * @param pack the list to store the loaded and shuffled Card objects
     * @throws IllegalArgumentException if a non-positive denomination is encountered in the file
     * @throws FileNotFoundException if the specified file is not found
     */
    private static void loadAndShufflePack(String packFilePath, ArrayList<Card> pack) {
        try (Scanner scanner = new Scanner(new File(packFilePath))) {
            // Step 1: Load the pack into a list of Card objects
            while (scanner.hasNextInt()) {
                int cardValue = scanner.nextInt();
                if (cardValue <= 0) {
                    throw new IllegalArgumentException("Card denomination must be a positive integer.");
                }
                pack.add(new Card(cardValue));  
            }
        
            // Step 2: Shuffle the pack to randomise card order
            Collections.shuffle(pack);  
        } catch (FileNotFoundException e) {
            System.out.println("Failed to load pack from file.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
        
    
    /**
     * Validates the contents of a card pack file.
     * Checks that all values are positive integers and that the file contains exactly 8 * number of players values.
     *
     * @param packFilePath the path to the card pack file
     * @param numOfPlayers the number of players, used to calculate the expected number of cards in the file
     * @return true if the file is valid, false otherwise
     */
    public static Boolean validateFile(String packFilePath, int numOfPlayers) {
        ArrayList<Integer> pack = new ArrayList<>();
        
        try (Scanner scanner = new Scanner(new File(packFilePath))) {
            while (scanner.hasNext()) {
                if (scanner.hasNextInt()) {
                    int value = scanner.nextInt();
                    if (value < 0) {
                        System.out.println("Invalid File: Pack contains negative integers in file\n");
                        return false;
                    }
                    pack.add(value);
                } else {
                    // If non-integer values are found
                    System.out.println("Invalid File: Pack contains non-numeric values in file\n");
                    return false;
                }
            }
    
            // Validate pack size
            int expectedSize = 8 * numOfPlayers;
            if (pack.size() < expectedSize) {
                System.out.println("Invalid File: Pack size too small (expected " + expectedSize + ", found " + pack.size() + ") in file\n");
                return false;
            }
            if (pack.size() > expectedSize) {
                System.out.println("Invalid File: Pack size too big (expected " + expectedSize + ", found " + pack.size() + ") in file\n");
                return false;
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error reading file: " + packFilePath + ". " + e.getMessage());
            return false;
        }
    }
    
    
}
