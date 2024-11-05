import java.util;

public class Card {

    //Attributes
    private int denomination;
    

    //Constructor
    private Card(int denomination){
        this.denomination = denomination; //Make sure is non-negative intege 
    }

    //Methods
    public int getCardDenomination(){
        return denomination;
    }


}