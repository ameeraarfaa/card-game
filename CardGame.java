import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.io.*;
import java.text.SimpleDateFormat;

/**
 * This is the executable class that handles
 * game initialisation, player actions and overall game flow.
 */
public class CardGame {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        int numOfPlayers;
        String packFilePath;

        // Step 1: Input number of players
        while (true) {
            System.out.print("Please enter the number of players:\n");
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
            System.out.print("Please enter the location of pack to load:\n");
            packFilePath = scanner.nextLine().trim();

            if (packFilePath.startsWith("\"") && packFilePath.endsWith("\"")) {
                packFilePath = packFilePath.substring(1, packFilePath.length() - 1);
            }

            // Check if the file exists 
            File file = new File(packFilePath);
            if (!file.exists()) {
                System.out.println("File not found: " + packFilePath);
                continue;  
            } 

            // Validate content
            if (validateFile(packFilePath, numOfPlayers)) {
                break;
            } 
        }

        System.out.println("\nNumber of Players: " + numOfPlayers + " | " + "File Path: " + packFilePath);

        // Step 3: Start the game
        runGame(packFilePath, numOfPlayers);
        scanner.close();
    }


    /**
     * Runs the game by initialising players, decks, and distributing cards. 
     * It handles the game loop, player actions, logging, and game termination. 
     * After the game ends, it logs the results and player actions to output files.
     *
     * @param packFilePath the path to the file containing the card pack
     * @param numOfPlayers the number of players in the game
     * @throws InterruptedException if the game thread is interrupted during execution
     */
    public static void runGame(String packFilePath, int numOfPlayers) throws InterruptedException {
        ArrayList<Card> pack = new ArrayList<>();
        Player[] players = new Player[numOfPlayers];
        CardDeck[] decks = new CardDeck[numOfPlayers];
        AtomicBoolean gameEnded = new AtomicBoolean(false);

        System.out.println("---------------- GAME START ----------------\n");

        // Generate a folder for .txt output files
        SimpleDateFormat sdf = new SimpleDateFormat("HH-mm-ss_yyyy-MM-dd");
        String folderName = "game_" + sdf.format(new Date());
        File gameFolder = new File(folderName);
        if (!gameFolder.exists()) {
            gameFolder.mkdirs();
        }

        // Load and shuffle the card pack
        loadAndShufflePack(packFilePath, pack);
        System.out.println("Loaded & Shuffled Pack: " + pack + "\n"); //debugging

        // Initialise decks and players
        for (int i = 0; i < numOfPlayers; i++) {
            decks[i] = new CardDeck(i + 1, gameFolder);
        }

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
        }

        // Distribute remaining cards to decks
        for (int i = 4 * numOfPlayers; i < 8 * numOfPlayers; i++) {
            decks[i % numOfPlayers].addCard(pack.get(i));
        }

        // Check for immediate winners after cards are dealt
        for (Player p : players) {
            if (p.hasWon()) {
                gameEnded.set(true);
                p.logAction("Wins");  
                p.logAction("Exits");
                p.logAction("final hand: " + p.getHandAsString());
                p.notifyPlayersOfWin(p); 
                break;  
            }
        }

        // **DEBUGGING: Print the initial cards in each deck before players begin drawing and discarding**
        System.out.println("");
        for (int i = 0; i < numOfPlayers; i++) {
            System.out.println("Deck " + (i + 1) + " Initial Cards: " + decks[i].getDeckCardsAsString());
        }

        System.out.println("");

        // Start player threads
        for (Player p : players) {
            p.start();
            System.out.println("Player " + p.getPlayerNumber() + " thread started.");
            //printGameState(players, decks);
        }

        System.out.println("");

        // Game loop
        while (!gameEnded.get()) {
            Thread.sleep(500); 
        }

        // Print the directory where the folder is created
        System.out.println("\n.txt output files can be found at: " + gameFolder.getAbsolutePath());
        
        // End game: Stop all player threads and log deck contents
        for (Player p : players) {
            p.endGame();
            try {
                p.closeLog();
            } catch (IOException e) {
                System.out.println("Error closing player " + p.getPlayerNumber() + "'s log file.");
            }
        }

        //Print winner to terminal
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
     * Loads a pack of cards from a file and shuffles them.
     * Each card is represented by an integer value read from the file, 
     * and the pack is shuffled to randomize the order.
     *
     * @param packFilePath the path to the file containing the card values
     * @param pack the list to store the loaded and shuffled cards
     */
    private static void loadAndShufflePack(String packFilePath, ArrayList<Card> pack) {
        try (Scanner scanner = new Scanner(new File(packFilePath))) {
            // Step 1: Load the pack into a list of Card objects
            while (scanner.hasNextInt()) {
                int cardValue = scanner.nextInt();
                if (cardValue < 0) {
                    throw new IllegalArgumentException("Card denomination must be a non-negative integer (>= 0).");
                }
                pack.add(new Card(cardValue));  
            }
    
            // Step 2: Shuffle the pack to randomize card order
            //Collections.shuffle(pack);  
        } catch (FileNotFoundException e) {
            System.out.println("Failed to load pack from file.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Validates the card pack file by checking for negative values, non-numeric entries,
     * and ensuring the file contains the correct number of cards for the given number of players.
     *
     * @param packFilePath the path to the card pack file
     * @param numOfPlayers the number of players in the game
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

    

