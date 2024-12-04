/*
 * This class represents a deck of cards in a game.
 */
public class CardDeck{

    private List<Card> deckCards; 
    private int deckId; 
    private int deckSize; 
    private boolean isEmpty; //potential to remove if not utilised

    public CardDeck(int deckId, List<Card> cards) {
        this.deckId = deckId;
        this.deck = new LinkedList<>(cards); // Use LinkedList for efficient removal from top and addition to bottom
        this.deckSize = deck.size();
        this.isEmpty = deck.isEmpty();
    }

    //Getter Methods
    public int getDeckId(){
        return deckId;
    }

    public int getDeckSize(){
        return deckSize;
    }

    public List<Card> getDeckCards(){
        return deckCards;
    }
    
    //Additional Methods

    /**
     * Checks state of deck.
     * @return true if the deck is empty, false if it contains one or more cards
     */
    public boolean isDeckEmpty(){
        return isEmpty;
    }

    /**
     * Draws and removes the top card from the deck.
     * @return the card drawn from the top of the deck
     * @throws IllegalStateException if the deck is empty
     */
    public Card drawCard(){
        if (isDeckEmpty()) {
            throw new IllegalStateException("Cannot draw from an empty deck.");
        }
        Card drawnCard = deck.remove(0); // Removes the first card (top of the deck)
        deckSize--;
        isEmpty = deckSize == 0; // Update deck status
        return drawnCard;
    }

    /**
     * Discards a card by adding it to the bottom of the deck.
     * @param card the card to be discarded into the deck
     */
    public void discardCard(Card card){
        deck.add(card); // Adds to the end of the list (bottom of the deck)
        deckSize++;
        isEmpty = false; 
    }

    /**
     * Retrieves the card on top of the deck without removing it.
     * @return the card on top of the deck
     * @throws IllegalStateException if the deck is empty
     */
    public Card getTopCard(){
        if (isDeckEmpty()) {
            throw new IllegalStateException("Deck is empty, no top card.");
        }
        return deck.get(0); 
    }

    /**
     * Returns the denominations of the cards in the deck as a space-separated string.
     * @return a string representing the denominations of the cards in the deck
     */
    public String getDeckCardsAsString(){
        StringBuilder deckString = new StringBuilder();
        
        for (Card card : deckCards) {
            deckString.append(card.getDenomAsString()).append(" ");
        }
        
        if (deckString.length() > 0) {
            deckString.deleteCharAt(deckString.length() - 1);
        }

        return deckString.toString();
    }

}