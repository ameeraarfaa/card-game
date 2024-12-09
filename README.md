# Multiplayer Card Game
## Overview
This Java terminal-based card game simulates a multiplayer card game where players draw and discard cards in turns. The game runs with multiple players, each managing their own hand, while competing to meet the win conditions.

## Project Structure
- Card.java: Class representing a single card object.
- CardDeck.java: Class encapsulating the attributes and behaviours of a list of cards, known as a Deck.
- Player.java: Class encapsulating the attribtues and behaviours of a player, including their gameplay strategy.
- CardGame.java: Main executable class that handles user input and gameflow.

## Installing & Playing
1. Clone or download the repository to your local machine.
2. Running the Game
   - <ins>Using Executable Class (CardGame)</ins>:
     - Navigate to the 'bin' directory.
     - Run the following command to start the game.
        ```
         java CardGame
        ```
   - <ins>Using JAR file</ins>:
     - Navigate to the 'build' directory.
     - Run the following command.
        ```
         java -jar cards.jar
        ```
3. Follow instructions on terminal to play the game.
   - **Entering the Number of Players**:
     - Ensure that the number is 2 or greater, as the game requires at least two players
   - **Providing the Card Pack File**:
     - Place the card pack file in the res folder, located at the root of the project directory.
     - In addition to the provided files, users may also add their own packs into the 'res' file.
     - When prompted, type the file name (e.g., pack.txt) and press Enter.

## Given Pack Files
**These files can be found in the 'Res' folder**. In each pack, denominations are included up to a point where the total number of cards, calculated as 4 copies of each denomination, satisfies the 8*N rule, where N is the number of players. This ensures that the total number of cards in the pack aligns with the required card count for the game, and confirms that it is legal for the denomination of a card to exceed N. For example:

> In a 3-player game, the given pack will include denominations up to 6, since 4 copies of each denomination (4 x 6 = 24 cards) matches the total card count required by the 8*N requirement (3 x 8 = 24).

### Simple Multiplayer Packs
These packs simulate normal gameplay for testing with various numbers of players, ranging from the minimum (2 players) to a larger group (8 players), designed for functional and stress testing.
- **2_players.txt**: 2-player game for basic testing.
- **3_players.txt**: 3-player game for moderate multiplayer scenarios.
- **6_player.txt**: 6 player game for moderate multiplayer scenarios.
- **8_player.txt**: 8 player game for stress testing extended multiplayer scenarios.

### Valid Scenario Packs
These packs test specific scenarios that should still be accepted by the game, focusing on edge cases and unique gameplay conditions.
- **2_players_horizontal_w_gap.txt**: A 2-player game with values arranged horizontally and blank lines between them.
- **3_players_miw.txt**: A 3-player game where multiple players (i.e. 1 and 2) achieve an immediate win; requires commenting out the shuffle method in loadAndShuffle().
- **3_players_p1_iw_nonpref_denom**: A 3-player game where Player 1 wins immediately with a non-preferred denomination; requires commenting out the shuffle method in loadAndShuffle().
- **3_players_p1_iw_pref_denom**: A 3-player game where Player 1 wins immediately with their preferred denomination; requires commenting out the shuffle method in loadAndShuffle().
- **4_players_with_Zeroes**: A 4-player game pack containing zeroes among the pack values to test handling of zero-value cards.
  
### Invalid File Input Packs
These packs are intentionally invalid and should not be accepted by the game, triggering specific error messages and requiring the user to re-enter a file path.
- **2_players _w_gap.txt**: A 2-player game pack with unexpected blank spaces in between values.
- **2_players_horizontal_no_gap.txt**: A 2-player game pack with values arranged horizontally but without blank lines between them, causing format errors.
- **2_players_w_negative_integer.txt**: A 2-player game pack containing negative integers.
- **2_players_w_special_character.txt**:  2-player game pack with special characters among the values.
- **2_players_w_string_number.txt**: A 2-player game pack containing string values (e.g., "one" instead of 1).

## Testing
All test files are located in the testing folder. Each test file corresponds to a specific class to ensure comprehensive unit testing.
- **CardTest.java**: Contains unit tests for the Card class.
- **CardDeckTest.java**: Contains unit tests for the CardDeck class.
- **PlayerTest.java**: Contains unit tests for the Player class.
- **CardGameTest.java**: Contains unit tests for the CardGame class.
- **CardGameTestSuiteTest.java**: A test suite that runs all the test classes together.

### Running Tests
1.	Navigate to the testing folder.
2.	Compile the test files.
   ```
   javac testing/*.java
   ```
4.	Run the CardGameTestSuite.java file to execute all tests simultaneously.
   ```
   java testing.CardGameTestSuite
   ```

### Notes
- Ensure that all dependencies and libraries required for testing (e.g., JUnit) are correctly set up in your development environment.
- Any updates to the main classes should be accompanied by updates to the respective test files.
