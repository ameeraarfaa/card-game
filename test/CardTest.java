import static org.junit.Assert.*;
import org.junit.Test;

public class CardTest {

    // Test for getDenom() method
    @Test
    public void testGetDenom() {
        Card card = new Card(5);  // Create a Card object with denomination 5
        int denom = card.getDenom();  // Call getDenom() method
        assertEquals(5, denom);  // Assert that the denomination is 5
    }

    // Test for toString() method
    @Test
    public void testToString() {
        Card card = new Card(7);  // Create a Card object with denomination 7
        String result = card.toString();  // Call toString() method
        assertEquals("7", result);  // Assert that the string representation of the card is "7"
    }

    // Test for exception thrown when negative denomination is passed
    @Test(expected = IllegalArgumentException.class)
    public void testCardNegativeDenom() {
        new Card(-1);  // This should throw an IllegalArgumentException
    }

}