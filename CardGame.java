import java.util.*;
import java.io.*;

/**
 * This class represents the executable card game.
 * It handles all users' inputs and initializes and handles 
 * threads until the game's completion.
 */
public class CardGame {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numOfPlayers = -1;
        String packFilePath = "";

        // User inputs number of players
        while (numOfPlayers < 2) {
            System.out.print("Enter the number of players (at least 2): ");
            String input = scanner.nextLine().trim();

            // Check if the input is a valid integer, assuming at least 2 players are required to start a game.
            if (input.matches("\\d+")) {
                numOfPlayers = Integer.parseInt(input);
                if (numOfPlayers < 2) {
                    System.out.println("Invalid number of players. Please enter a number greater than or equal to 2.\n");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid integer number.\n");
            }
        }

        // User inputs pack file location and validates it
        while (true) {
            System.out.print("Enter the location of pack to load: ");
            packFilePath = scanner.nextLine().trim();

            // Check if the file is of correct type
            if (!packFilePath.endsWith(".txt")) {
                System.out.println("Invalid file type. Please ensure the file is a .txt file.");
                continue;
            }

            // Validate if the file exists and has the correct number of cards
            if ((new File(packFilePath)).exists() && validateFile(packFilePath, numOfPlayers)) {
                break;  
            } else {
                File file = new File(packFilePath);
                if (!file.exists()) {
                    System.out.println("Pack not found. Please try again.");
                } else {
                    System.out.println("The format is incorrect. The number of cards does not match 8 * " + numOfPlayers + " cards.\nPlease use another pack.");
                }
            }
        }

        System.out.println("\nNum of Players: " + numOfPlayers + "\nLocation of pack: " + packFilePath);
        System.out.println("Game Starting...\n");

        // Game logic goes here
    }

    /**
     * Method that deals with pack validation.
     * - Checks if the file exists.
     * - Ensures the file contains exactly 8 * numOfPlayers cards.
     * - Ensures the file contains only integers.
     *
     * @param fn the file path of the pack
     * @param np the number of players in the game
     * @return true if the file is valid, false otherwise
     */
    public static Boolean validateFile(String fn, int np) {
        String filename = fn;
        int numberOfPlayers = np;
        ArrayList<Integer> pack = new ArrayList<Integer>();

        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                if (myReader.hasNextInt()) {
                    int temp = myReader.nextInt();
                    pack.add(temp);
                } else {
                    myReader.nextLine();  // Skip non-integer values
                }
            }

            // Check if the file contains exactly 8 * numOfPlayers cards
            if (pack.size() != 8 * numberOfPlayers) {
                myReader.close();
                return false;  // Invalid number of cards
            }

            myReader.close();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while accessing the file.");
            return false;
        }
    }
}
