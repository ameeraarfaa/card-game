import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.io.*;

class Player extends Thread {
    private final int playerNumber;
    private final List<Card> hand;
    private final CardDeck leftDeck;
    private final CardDeck rightDeck;
    private final AtomicBoolean gameEnded;
    private final BufferedWriter logWriter;
    private final Player[] players; // Shared list of players

    public Player(int playerNumber, File gameFolder, CardDeck leftDeck, CardDeck rightDeck, 
                  AtomicBoolean gameEnded, Player[] players) {
        this.playerNumber = playerNumber;
        this.hand = new ArrayList<>();
        this.leftDeck = leftDeck;
        this.rightDeck = rightDeck;
        this.gameEnded = gameEnded;
        this.players = players; // Initialize shared players list
        try {
            File outputFile = new File(gameFolder, "player" + playerNumber + "_output.txt");
            logWriter = new BufferedWriter(new FileWriter(outputFile, true));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create output file for player " + playerNumber, e);
        }
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    @Override
    public void run() {
        while (!gameEnded.get() && !Thread.currentThread().isInterrupted()) {
            try {
                // Draw a card from the left deck
                Card drawnCard;
                synchronized (leftDeck) {
                    drawnCard = leftDeck.removeCard();
                }
                addCardToHand(drawnCard);
                //logAction("");
                logAction("Player " + playerNumber + " draws " + drawnCard + " from Deck " + leftDeck.getDeckId());
    
                // Discard a card to the right deck
                Card discardedCard = discardCard();
                synchronized (rightDeck) {
                    rightDeck.addCard(discardedCard);
                }
                logAction("Player " + playerNumber + " discards " + discardedCard + " to Deck " + rightDeck.getDeckId());
                logAction("Current hand: " + getHandAsString());
                logAction("");
    

                // Check for win condition
                if (hasWon()) {
                    gameEnded.set(true);
                    logAction("Wins");
                    notifyPlayersOfWin(this);
                    break;
                }

                // Simulate turn delay
                Thread.sleep(100);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void notifyPlayersOfWin(Player winner) {
        for (Player otherPlayer : players) {
            if (!otherPlayer.equals(winner)) {
                synchronized (otherPlayer) {
                    // Correct format: "Player X has informed player Y that player X has won"
                    otherPlayer.logAction(
                        "Player " + winner.playerNumber + " has informed player " +
                        otherPlayer.playerNumber + " that player " + winner.playerNumber + " has won"
                    );
                    otherPlayer.logAction("exits");
                    otherPlayer.logAction("hand: " + otherPlayer.getHandAsString());
                }
            }
        }
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
            System.out.println("Failed to log action for player " + playerNumber);
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
            closeLog();  
        } catch (IOException e) {
            System.out.println("Failed to close log file.");
        }
    }

}
