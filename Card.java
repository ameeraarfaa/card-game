/*
 * This class represents a single card object in the game.
 * It includes the card's value and the deck it belongs to.
 */
public class Card {

    private final int denom; //card denomination, abbreviated as 'denom'

    public Card(int denom){
        if (denom <= 0) {
            throw new IllegalArgumentException("Card denomination must be a positive integer.");
        }
        this.denom = denom;
    }

    /**
     * Getter method for card denomination.
     *
     * @return the card denomination
     */
    public int getDenom() {
        return denom;
    }


    /**
     * Returns the card's denomination as a string.
     * @return the denomination of the card
     */
    @Override
    public String toString() {
        return Integer.toString(denom);  // Returns the card's denomination as a string
    }

    
}

