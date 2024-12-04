import java.util.*;
import java.util.concurrent.*;
import java.io.*;

public class Player implements Runnable {

    private final int playerID;
    private final int preferredDenomination;
    private List<Card> playerHand;
    
    private final CardDeck leftDeck;
    private final CardDeck rightDeck;

    private static volatile boolean winnerAnnounced = false;
    private static volatile int winnerID = -1;

    private final CyclicBarrier startBarrier;  // Synchronizes the start of all players
    private static final Object logLock = new Object();

    private BufferedWriter writer;

    // Constructor to initialize the player
    public Player(int playerID, int preferredDenomination, List<Card> playerHand, CardDeck leftDeck,
                  CardDeck rightDeck, CyclicBarrier startBarrier) {
        this.playerID = playerID;
        this.preferredDenomination = preferredDenomination;
        this.playerHand = new ArrayList<>(playerHand);
        this.leftDeck = leftDeck;
        this.rightDeck = rightDeck;
        this.startBarrier = startBarrier;

        // Initialize writer for player-specific output file
        try {
            this.writer = new BufferedWriter(new FileWriter("player" + playerID + "_output.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Getter Methods
    /**
     * Returns the player's unique index.
     * @return the player index (e.g., 1, 2, 3, ...)
     */
    public int getPlayerIndex() {
        return playerID;
    }

    /**
     * Returns the list of cards in the player's hand.
     * @return the list of cards in the hand
     */
    public List<Card> getHand() {
        return playerHand;
    }

    // Method to populate the hand
    public void populateHand(List<Card> packCards) {
        int cardIndex = 0;

        // Round-robin distribution of cards from the pack
        while (playerHand.size() < 4 && cardIndex < packCards.size()) {
            playerHand.add(packCards.get(cardIndex));  // Add a card to the player's hand
            cardIndex++;  // Move to the next card
            if (cardIndex == packCards.size()) {
                break;  // Stop if there are no more cards in the pack
            }
        }
    }

    // Method to analyze the hand and decide which card to discard
    public int analyzeHand() {
        // Find the first card that doesn't match the preferred denomination
        for (int i = 0; i < playerHand.size(); i++) {
            Card card = playerHand.get(i);
            if (card.getDenom() != preferredDenomination) {
                return i;  // Return the index of the card to discard
            }
        }
        return -1;  // Return -1 if all cards match the preferred denomination
    }


    /**
     * Returns the player's preferred card denomination.
     * @return the preferred denomination (e.g., 1 for player 1, 2 for player 2, ...)
     */
    public int getPreferredDenomination() {
        return preferredDenomination;
    }

    // Logging method
    private void logAction(String action) {
        synchronized (logLock) {
            try {
                writer.write(action);
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Check if the player has won
    private boolean checkWinningCondition() {
        if (playerHand.size() == 4) {
            int firstCardDenomination = playerHand.get(0).getDenom();
            for (Card card : playerHand) {
                if (card.getDenom() != firstCardDenomination) {
                    return false;
                }
            }
            announceWinner();
            return true;
        }
        return false;
    }

    // Announce the winner
    private synchronized void announceWinner() {
        if (!winnerAnnounced) {
            winnerAnnounced = true;
            winnerID = playerID;
            logAction("Player " + playerID + " wins!");
        }
    }

    // Draw card from the left deck
    public void drawCard() {
        synchronized (leftDeck) {
            if (!leftDeck.isDeckEmpty()) {
                Card drawnCard = leftDeck.drawCard();
                playerHand.add(drawnCard);
                logAction("Player " + playerID + " draws card: " + drawnCard.getDenom());
            } else {
                logAction("Player " + playerID + " cannot draw card, left deck is empty.");
            }
        }
    }

    // Discard card to the right deck
    public void discardCard() {
        synchronized (rightDeck) {
            Card cardToDiscard = null;
            for (Card card : playerHand) {
                if (card.getDenom() != preferredDenomination) {
                    cardToDiscard = card;
                    break;
                }
            }

            if (cardToDiscard != null) {
                playerHand.remove(cardToDiscard);
                rightDeck.discardCard(cardToDiscard);
                logAction("Player " + playerID + " discards card: " + cardToDiscard.getDenom());
            } else {
                logAction("Player " + playerID + " has no cards to discard.");
            }
        }
    }

    // The run method that handles the player's actions
    @Override
    public void run() {
        try {
            startBarrier.await();  // Synchronize start

            // Populate the player's hand
            while (playerHand.size() < 4) {
                drawCard();
            }

            // Check if the player won after their hand is populated
            if (checkWinningCondition()) {
                return;  // End game if player wins
            }

            // Game loop: draw and discard until a winner is found
            while (!winnerAnnounced) {
                // Draw and discard cards
                drawCard();
                discardCard();

                // Check if the player won after the draw and discard
                if (checkWinningCondition()) {
                    return;  // End game if player wins
                }
            }
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
