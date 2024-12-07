import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.io.*;

/**
 * This class represents a player in the game. Each player manages their hand, performs turns, 
 * checks for a win, logs actions, and notifies other players upon winning.
 */
class Player extends Thread {
    private final int playerNumber;
    private final List<Card> hand;
    private final CardDeck leftDeck;
    private final CardDeck rightDeck;
    private final AtomicBoolean gameEnded;
    private final BufferedWriter logWriter;
    private final Player[] players; 

    public Player(int playerNumber, File gameFolder, CardDeck leftDeck, CardDeck rightDeck, 
                  AtomicBoolean gameEnded, Player[] players) {
        this.playerNumber = playerNumber;
        this.hand = new ArrayList<>();
        this.leftDeck = leftDeck;
        this.rightDeck = rightDeck;
        this.gameEnded = gameEnded;
        this.players = players; 
        try {
            File outputFile = new File(gameFolder, "player" + playerNumber + "_output.txt");
            logWriter = new BufferedWriter(new FileWriter(outputFile, true));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create output file for player " + playerNumber, e);
        }
    }


    /**
     * Returns player number as integer
     * @return player number
     */
    public int getPlayerNumber() {
        return playerNumber;
    }


    /**
     * Adds a card to the player's hand.
     * @param card the card to be added to the hand
     */
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


    /**
     * Checks if the player has won the game by having four cards of the same denomination in hand.
     * @return true if the player has four cards of the same denomination, otherwise false
     */
    public boolean hasWon() {
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


    /**
     * Executes a player's turn by drawing a card from the left deck, discarding a card to the right deck,
     * and logging the actions. The turn is synchronized to ensure thread safety. The method checks if the 
     * player has won after each turn and ends the game if so.
     * @throws InterruptedException if the thread is interrupted during sleep
     */   
    @Override
    public void run() {
        while (!gameEnded.get() && !Thread.currentThread().isInterrupted()) {
            try {
                // Draw-discard as an atomic action
                synchronized (this) {
                    Card drawnCard;
                    synchronized (leftDeck) {
                        drawnCard = leftDeck.removeCard();
                    }
                    addCardToHand(drawnCard);
                    logAction("draws " + drawnCard + " from Deck " + leftDeck.getDeckId());

                    // Discard a card to the right deck
                    Card discardedCard = discardCard();
                    synchronized (rightDeck) {
                        rightDeck.addCard(discardedCard);
                    }
                    logAction("discards " + discardedCard + " to Deck " + rightDeck.getDeckId());
                    logAction("Current hand: " + getHandAsString());
                    logAction("");
                }

                // Check for win condition
                if (hasWon()) {
                    gameEnded.set(true);
                    logAction("Wins");
                    logAction("Exits");
                    logAction("final hand: " + getHandAsString());
                    notifyPlayersOfWin(this);
                    break;
                }

                // Simulate turn delay
                Thread.sleep(500);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }


    /**
     * Notifies all players (except the winner) that the specified player has won, logs the exit message 
     * for each player, and displays their hand.
     * @param winner player who has won the game
     */
    public void notifyPlayersOfWin(Player winner) {
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


    /**
     * Logs the specified action for the player both to a log file and the console.
     * If the action is empty, a blank line is logged. Otherwise, the action is logged with the player's number.
     * @param action the action description to log
     */
    public void logAction(String action) {
        try {
            if (action.isEmpty()) {
                logWriter.write("\n");
                System.out.println(); //manual terminal inspection
            } else {
                logWriter.write("Player " + playerNumber + " " + action + "\n");
                System.out.println("Player " + playerNumber + " " + action);  //manual terminal inspection
            }
            logWriter.flush();
        } catch (IOException e) {
            System.out.println("Failed to log action for player " + playerNumber);
            e.printStackTrace();
        }
    }


    /**
     * Closes the log file writer if it is not already closed.
     * @throws IOException if an I/O error occurs while closing the log writer
     */
    public void closeLog() throws IOException {
        if (logWriter != null) {
            logWriter.close();  
        }
    }

    /**
     * Ends the game by closing the log file. 
     * Catches and prints any I/O errors encountered during log file closure.
     */
    public void endGame() {
        try {
            closeLog();  
        } catch (IOException e) {
            System.out.println("Failed to close log file.");
        }
    }

}
