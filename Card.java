/*
 * This class represents a single card object in the game.
 * It includes the card's value and the deck it belongs to.
 */
public class Card {

    private final int denom; // attribute represents card denomination, abbreviated as 'denom'

    // Constructor
    public Card(int denom){
        if (denom <= 0) {
            throw new IllegalArgumentException("Card denomination must be a positive integer.");
        }
        this.denom = denom;
    }

    // Getter for card denominatino
    public int getDenom(){
        return denom;
    }

    // Method to get the denomination as a string
    public String getDenomAsString(){
        return Integer.toString(denom);
    }
}

