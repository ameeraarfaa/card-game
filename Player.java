import java.util.*;
import java.io.*;

class Player extends Thread {
    private int playerNumber;
    private List<Card> hand;
    private BufferedWriter logWriter;

    public Player(int playerNumber) {
        this.playerNumber = playerNumber;
        this.hand = new ArrayList<>();
        try {
            logWriter = new BufferedWriter(new FileWriter("player" + playerNumber + "_output.txt"));
        } catch (IOException e) {
            System.out.println("Failed to create log file for player " + playerNumber);
        }
    }

    public void run() {
        // Player performs actions in the thread, e.g., drawing cards, discarding
    }

    public void addCardToHand(Card card) {
        hand.add(card);
    }    

    /**
     * Method that chooses and discards a card to deck.
     * @return card to discard to deck
     */
    public Card discardCard() {
        for (Card card : hand) {
            if (card.getDenom() != playerNumber) {
                hand.remove(card);
                return card;
            }
        }
        throw new IllegalStateException("Player has won and should not discard any more cards.");
    }

    public boolean hasWon() {
        // Check if the player has four of the same card denomination in hand
        Map<Integer, Integer> cardCount = new HashMap<>();
        for (Card card : hand) {
            cardCount.put(card.getDenom(), cardCount.getOrDefault(card.getDenom(), 0) + 1);
            if (cardCount.get(card.getDenom()) == 4) {
                return true; 
            }
        }
        return false; 
    }
    
    /**
     * Returns the denominations of the cards in the hand as a space-separated string.
     * @return a string representing the denominations of the cards in the hand
     */
    public String getHandAsString() {
        if (hand.isEmpty()) {
            return "No cards in the hand";  
        }

        StringBuilder handString = new StringBuilder();
        for (Card card : hand) {
            handString.append(card.toString()).append(" "); 
        }
        handString.deleteCharAt(handString.length() - 1);  
        return handString.toString();
    }

    public void logAction(String action) {
        try {
            if (action.isEmpty()) {
                logWriter.write("\n");
            } else {
                logWriter.write("Player " + playerNumber + " " + action + "\n");
            }
            logWriter.flush(); 
        } catch (IOException e) {
            System.out.println("Failed to log action.");
            e.printStackTrace(); 
        }
    }

    public void closeLog() throws IOException {
        if (logWriter != null) {
            logWriter.close();  
        }
    }
    

    public void endGame() {
        try {
            logWriter.close();
        } catch (IOException e) {
            System.out.println("Failed to close log file.");
        }
    }

    public int getPlayerNumber() {
        return playerNumber;
    }
}