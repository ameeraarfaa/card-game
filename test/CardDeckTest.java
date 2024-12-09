import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.*;

public class CardDeckTest {

    private CardDeck cardDeck;
    private File gameFolder;

    @Before
    public void setUp() {
        // Create a temporary folder for the game logs
        gameFolder = new File("test_logs_cardDeckTest");
        if (!gameFolder.exists()) {
            gameFolder.mkdir();
        }

        // Create a new deck with deckId = 1
        cardDeck = new CardDeck(1, gameFolder);
    }

    // Test for the constructor and deck size
    @Test
    public void testCardDeckInitialization() {
        assertEquals(1, cardDeck.getDeckId());  // Verify the deck ID
        assertEquals(0, cardDeck.getDeckSize());  // Deck should be empty initially
    }

    // Test for isDeckEmpty()
    @Test
    public void testIsDeckEmpty() {
        assertTrue(cardDeck.isDeckEmpty());  // Should be empty initially
        cardDeck.addCard(new Card(5));  // Add a card
        assertFalse(cardDeck.isDeckEmpty());  // Should not be empty after adding a card
    }

    // Test for addCard()
    @Test
    public void testAddCard() {
        Card card = new Card(10);
        cardDeck.addCard(card);
        assertEquals(1, cardDeck.getDeckSize());
        assertEquals(card, cardDeck.getTopCard());
    }

    // Test for removeCard() (normal case)
    @Test
    public void testRemoveCard() {
        Card card1 = new Card(5);
        Card card2 = new Card(10);
        cardDeck.addCard(card1);
        cardDeck.addCard(card2);

        assertEquals(2, cardDeck.getDeckSize());  // Two cards in the deck
        Card removedCard = cardDeck.removeCard();
        assertEquals(card1, removedCard);  // The top card should be removed
        assertEquals(1, cardDeck.getDeckSize());  // Deck size should now be 1
    }

    // Test for removeCard() (exception when deck is empty)
    @Test(expected = IllegalStateException.class)
    public void testRemoveCardWhenEmpty() {
        cardDeck.removeCard();  // This should throw an exception as the deck is empty
    }

    // Test for getTopCard() (normal case)
    @Test
    public void testGetTopCard() {
        Card card = new Card(5);
        cardDeck.addCard(card);
        assertEquals(card, cardDeck.getTopCard());  // The top card should be the one added
    }

    // Test for getTopCard() (exception when deck is empty)
    @Test(expected = IllegalStateException.class)
    public void testGetTopCardWhenEmpty() {
        cardDeck.getTopCard();  // This should throw an exception as the deck is empty
    }

    // Test for getDeckCardsAsString()
    @Test
    public void testGetDeckCardsAsString() {
        assertEquals("No cards in the deck", cardDeck.getDeckCardsAsString());  // Should return this when empty

        cardDeck.addCard(new Card(3));
        cardDeck.addCard(new Card(5));
        cardDeck.addCard(new Card(7));

        assertEquals("3 5 7", cardDeck.getDeckCardsAsString());  // Should return "3 5 7"
    }

    // Test for logDeckToFile() (mocking file writing)
    @Test
    public void testLogDeckToFile() throws IOException {

        // Mocking deck contents and file writing
        cardDeck.addCard(new Card(10));
        cardDeck.addCard(new Card(20));
        cardDeck.addCard(new Card(30));

        // File name based on the deckId
        File deckFile = new File(gameFolder, "deck1_output.txt");

        // Call the method that logs the deck to a file
        cardDeck.logDeckToFile(gameFolder);

        // Verify that the file was created
        assertTrue("Deck log file was not created", deckFile.exists());

        // Read the file contents
        BufferedReader reader = new BufferedReader(new FileReader(deckFile));
        String line = reader.readLine();
        reader.close();

        // Expected log contents based on the cards added (just numbers)
        String expectedLog = "deck1 contents: 10 20 30";

        // Verify that the log contents match the expected result
        assertEquals("Log file contents do not match the expected output.", expectedLog, line);
    }

    // Test for logging failure when file creation fails (mock failure in constructor)
    @Test
    public void testLogDeckToFileWhenFileFails() {
        // Simulate a failure in file creation by passing an invalid folder path
        cardDeck = new CardDeck(2, new File("thisfolderneverexists"));
        try {
            cardDeck.logDeckToFile(gameFolder);  // Should not throw an exception, should handle gracefully
        } catch (Exception e) {
            fail("Logging should not fail here");
        }
    }

    // Test for negative denomination card (constructor test)
    @Test(expected = IllegalArgumentException.class)
    public void testCardNegativeDenom() {
        new Card(-1);  // This should throw an IllegalArgumentException
    }
}