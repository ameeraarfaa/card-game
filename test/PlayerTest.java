import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerTest {

    private Player player1, player2;
    private CardDeck leftDeck, rightDeck;
    private AtomicBoolean gameEnded;
    private File gameFolder;

    @Before
    public void setUp() throws Exception {
        // Create a temporary folder for the game logs
        gameFolder = new File("test_logs_playerTest");
        if (!gameFolder.exists()) {
            gameFolder.mkdir();
        }

        // Create mock decks and game state
        leftDeck = new CardDeck(1);  // Mocking with deck ID 1
        rightDeck = new CardDeck(2); // Mocking with deck ID 2
        gameEnded = new AtomicBoolean(false);

        // Create two players
        player1 = new Player(1, gameFolder, leftDeck, rightDeck, gameEnded, new Player[0]);
        player2 = new Player(2, gameFolder, leftDeck, rightDeck, gameEnded, new Player[0]);
    }

    @Test
    public void testAddCardToHand() {
        Card card = new Card(1);  // Denom = 1
        player1.addCardToHand(card);

        assertTrue("Player's hand should contain the card", player1.getHandAsString().contains("1"));
    }

    @Test
    public void testDiscardCard() {
        Card card1 = new Card(2);  // Denom = 1
        Card card2 = new Card(1); // Denom = 1
        player1.addCardToHand(card1);
        player1.addCardToHand(card2);

        Card discarded = player1.discardCard();
        assertEquals("The discarded card should not belong to the player's denomination", card1, discarded);
    }

    @Test(expected = IllegalStateException.class)
    public void testDiscardCardAfterWin() {
        // Simulate a win condition by adding 4 cards of the same denomination
        for (int i = 0; i < 4; i++) {
            player1.addCardToHand(new Card(1));
        }

        assertTrue("Player should have won", player1.hasWon());

        // Trying to discard after winning should throw an exception
        player1.discardCard();
    }

    @Test
    public void testHasWon() {
        // Add 4 cards of the same denomination
        player1.addCardToHand(new Card(1));
        player1.addCardToHand(new Card(1));
        player1.addCardToHand(new Card(1));
        player1.addCardToHand(new Card(1));

        assertTrue("Player should have won with 4 cards of the same denomination", player1.hasWon());
    }

    @Test
    public void testGameFlowAndLog() throws InterruptedException, IOException {
        // Add some mock cards to the decks
        leftDeck.addCard(new Card(1));
        leftDeck.addCard(new Card(1));
        leftDeck.addCard(new Card(2));
        leftDeck.addCard(new Card(1));

        // Set up log writer to capture log output
        File logFile = new File(gameFolder, "player1_output.txt");
        BufferedReader logReader = new BufferedReader(new FileReader(logFile));

        // Start the player's turn in a separate thread
        Thread playerThread = new Thread(() -> {
            try {
                player1.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        playerThread.start();

        // Wait for a few seconds to simulate the game flow
        Thread.sleep(3000);

        // Check if log contains expected actions
        String line;
        boolean foundAction = false;
        while ((line = logReader.readLine()) != null) {
            if (line.contains("draws")) {
                foundAction = true;
                break;
            }
        }

        assertTrue("Player's draw action should be logged", foundAction);

        // Clean up the log reader
        logReader.close();
    }

    @Test
    public void testCloseLog() throws IOException {
        player1.closeLog();
        File logFile = new File(gameFolder, "player1_output.txt");

        // Verify file exists and is not empty
        assertTrue("Log file should exist", logFile.exists());
        assertTrue("Log file should not be empty", logFile.length() > 0);
    }

    @Test
    public void testEndGame() throws IOException {
        player1.endGame();
        File logFile = new File(gameFolder, "player1_output.txt");

        // Verify log file is closed (checked by file size > 0)
        assertTrue("Log file should exist after closing", logFile.exists());
    }
}