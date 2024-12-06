import java.io.*;
import java.util.*;

/**
 * This class represents a deck of cards in a game.
 */
public class CardDeck {

    private List<Card> deck;
    private int deckId;
    private BufferedWriter logWriter;

    // Constructor that accepts the game folder
    public CardDeck(int deckId, File gameFolder) {
        this.deckId = deckId;
        this.deck = new ArrayList<>(); // Initialize the deck as empty
        try {
            // Create the log file inside the provided gameFolder
            logWriter = new BufferedWriter(new FileWriter(new File(gameFolder, "deck" + deckId + "_output.txt")));
        } catch (IOException e) {
            System.out.println("Failed to create log file for deck " + deckId);
        }
    }

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
    
    /**
     * Returns the unique identifier for this deck.
     * @return the deck ID
     */
    public int getDeckId() {
        return deckId;
    }

    /**
     * Returns the number of cards currently in the deck.
     * @return the size of the deck
     */
    public int getDeckSize() {
        return deck.size();
    }

    /**
     * Provides access to the cards in the deck.
     * @return the list of cards in the deck
     */
    public List<Card> getDeckCards() {
        return deck;
    }

    /**
     * Checks if the deck is empty.
     * @return true if the deck is empty, false otherwise
     */
    public boolean isDeckEmpty() {
        return deck.isEmpty();
    }

    /**
     * Draws and removes the top card from the deck.
     * @return the card drawn from the top of the deck
     * @throws IllegalStateException if the deck is empty
     */
    public synchronized Card removeCard() {
        if (isDeckEmpty()) {
            throw new IllegalStateException("Deck " + deckId + " is empty: Cannot draw from an empty deck.");
        }
        return deck.remove(0); 
    }

    /**
     * Discards a card by adding it to the bottom of the deck.
     * @param card the card to be discarded into the deck
     */
    public synchronized void addCard(Card card) {
        deck.add(card); 
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
        if (deck.isEmpty()) {
            return "No cards in the deck";  
        }

        StringBuilder deckString = new StringBuilder();
        for (Card card : deck) {
            deckString.append(card.toString()).append(" "); 
        }
        deckString.deleteCharAt(deckString.length() - 1);  
        return deckString.toString();
    }

    public void logDeckToFile(File gameFolder) {
    try {
        File deckFile = new File(gameFolder, "deck" + deckId + "_output.txt");
 
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(deckFile))) {
            writer.write("deck" + deckId + " contents: " + getDeckCardsAsString());
            writer.newLine();
            writer.flush();
        }
    } catch (IOException e) {
        System.out.println("Failed to write deck file.");
    }
}

    

    
}
