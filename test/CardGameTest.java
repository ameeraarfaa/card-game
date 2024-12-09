import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;

public class CardGameTest {

    // Paths to your test files
    private String validFilePath = "card-game/res/2_players.txt";
    private String invalidNegativeFilePath = "card-game/res/2_players_w_negative_integer.txt";
    private String invalidNonNumericFilePath = "card-game/res/2_players_w_string_number.txt";
    private String invalidSizeFilePath = "card-game/res/6_players.txt";
    private String invalidSizeTooSmallFilePath = "card-game/res/3_players.txt";
    private String invalidSizeTooBigFilePath = "card-game/res/8_players.txt";
    private String invalidSizeWithGapVerticalFilePath = "card-game/res/2_players _w_gap.txt";
    private String invalidSizeWithNoGapHorizontal = "card-game/res/2_players_horizontal_no_gap.txt";
    private String invalidSizeWithGapHorizontal = "card-game/res/2_players_horizontal_w_gap.txt";



    @Test
    public void testValidFile() {
        // Assuming 2 players, so expected pack size is 16 (8 * 2)

        assertTrue(CardGame.validateFile(validFilePath, 2));
    }

    @Test
    public void testFileWithNegativeValues() {
        // Should return false due to negative values in the pack
        assertFalse(CardGame.validateFile(invalidNegativeFilePath, 2));
    }

    @Test
    public void testFileWithNonNumericValues() {
        // Should return false due to non-numeric values in the pack
        assertFalse(CardGame.validateFile(invalidNonNumericFilePath, 2));
    }

    @Test
    public void testFileWithIncorrectSize() {
        // Assuming 2 players, expected pack size should be 16
        // Test file with incorrect size (e.g., less than or greater than 16)
        assertFalse(CardGame.validateFile(invalidSizeFilePath, 2));
    }

    @Test
    public void testFileWithTooSmallSize() {
        // Assuming 2 players, expected pack size should be 16
        assertFalse(CardGame.validateFile(invalidSizeTooSmallFilePath, 8));
    }

    @Test
    public void testFileWithTooLargeSize() {
        // Assuming 2 players, expected pack size should be 16
        assertFalse(CardGame.validateFile(invalidSizeTooBigFilePath, 2));
    }

    @Test
    public void testFileWithGapVertical() {
        // Assuming 2 players, expected pack size should be 16
        assertFalse(CardGame.validateFile(invalidSizeWithGapVerticalFilePath, 2));
    }

    @Test
    public void testFileWithNoGapHorizontal() {
        // Assuming 2 players, expected pack size should be 16
        assertFalse(CardGame.validateFile(invalidSizeWithNoGapHorizontal, 2));
    }

    @Test
    public void testFileWithGapHorizontal() {
        // Assuming 2 players, expected pack size should be 16
        assertTrue(CardGame.validateFile(invalidSizeWithGapHorizontal, 2));
    }
}