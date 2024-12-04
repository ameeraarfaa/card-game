/*
 * This class manages the game players' hand.
 * It handles [...].
 * This class extends [...] to allow for mutli-threading.
 */
public class Player {

    private int playerIndex; 
    private List<Card> hand; 
    private int preferredDenomination; // could be removed and set as a local variable

    private final CardDeck leftDeck;
    private final CardDeck rightDeck;
    
    private String playerOutputFile; 

    // Constructor to initialize the player with index and output file path
    public Player(int playerIndex, String outputFile) {
        this.playerIndex = playerIndex;
        this.preferredDenomination = playerIndex; 
        this.outputFile = outputFile;
        this.hand = null; // Initially empty and to be populated during game execution
        
    }

    //Getter Methods
    /**
     * Returns the player's unique index.
     * @return player index 
     */
    public int getPlayerIndex(){
        return playerIndex;
    }

    /**
     * Returns the list of cards in the player's hand as card object.
     * @return list of card objects in player's hand.
     */
    public List<Card> getHand(){
        return hand;
    }

    //Additional Methods

    /**
     * This method distributes cards into each player's hand
     * upon intialisation of the game. 
     */
    public void populateHand(){
        //to-do
    }

    public void drawCard(){
        //to-do
    }

    public int analyseHand(){
        //to-do
    }

    public void dropCard(){
        //to-do
    }

    public boolean wonGame(){
        //to-do
    }

    public void writePlayerFile(){

    }
}