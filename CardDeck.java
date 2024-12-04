import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * This class represents a deck of cards in a game.
 */
public class CardDeck {

    private List<Card> deck; 
    private int deckId;

    /**
     * Constructs a new card deck with the given ID.
     * Initializes the deck as empty.
     *
     * @param deckId the unique identifier for this deck
     */
    public CardDeck(int deckId) {
        this.deckId = deckId;
        this.deck = new ArrayList<>(); // Initialize an empty deck
    }

    // Getter Methods

    /**
     * Returns the unique identifier for this deck.
     * 
     * @return the deck ID
     */
    public int getDeckId() {
        return deckId;
    }

    /**
     * Returns the number of cards currently in the deck.
     * 
     * @return the size of the deck
     */
    public int getDeckSize() {
        return deck.size();
    }

    /**
     * Provides access to the cards in the deck.
     * 
     * @return the list of cards in the deck
     */
    public List<Card> getDeckCards() {
        return deck;
    }

    // Additional Methods

    /**
     * Checks if the deck is empty.
     * 
     * @return true if the deck is empty, false otherwise
     */
    public boolean isDeckEmpty() {
        return deck.isEmpty();
    }

    /**
     * Draws and removes the top card from the deck.
     * 
     * @return the card drawn from the top of the deck
     * @throws IllegalStateException if the deck is empty
     */
    public Card drawCard() {
        if (isDeckEmpty()) {
            throw new IllegalStateException("Cannot draw from an empty deck.");
        }
        return deck.remove(0); // Removes the first card (top of the deck)
    }

    /**
     * Discards a card by adding it to the bottom of the deck.
     * 
     * @param card the card to be discarded into the deck
     */
    public void discardCard(Card card) {
        deck.add(card); // Adds to the end of the list (bottom of the deck)
    }

    /**
     * Retrieves the card on top of the deck without removing it.
     * 
     * @return the card on top of the deck
     * @throws IllegalStateException if the deck is empty
     */
    public Card getTopCard() {
        if (isDeckEmpty()) {
            throw new IllegalStateException("Deck is empty, no top card.");
        }
        return deck.get(0); 
    }

    /**
     * Returns the denominations of the cards in the deck as a space-separated string.
     * @return a string representing the denominations of the cards in the deck
     */
    public String getDeckCardsAsString() {
        StringBuilder deckString = new StringBuilder();
        
        for (Card card : deck) {
            deckString.append(card.getDenomAsString()).append(" ");
        }
        
        if (deckString.length() > 0) {
            deckString.deleteCharAt(deckString.length() - 1); // Remove trailing space
        }

        return deckString.toString();
    }

    /**
     * Logs the current state of the deck to a file at the end of the game.
     * e.g.) "deck<deckId>_output.txt".
     */
    public void logDeckToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("deck" + deckId + "_output.txt"))) {
            writer.write("deck" + deckId + " contents: " + getDeckCardsAsString());
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}