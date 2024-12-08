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
3. Follow instructions on terminal to play the gmae.

## Given Pack Files
**These files can be found in the 'Res' folder**. In each pack, denominations are included up to a point where the total number of cards, calculated as 4 copies of each denomination, satisfies the 8*N rule, where N is the number of players. This ensures that the total number of cards in the pack aligns with the required card count for the game, and confirms that it is legal for the denomination of a card to exceed N. For example:

> In a 3-player game, the given pack will include denominations up to 6, since 4 copies of each denomination (4 x 6 = 24 cards) matches the total card count required by the 8*N requirement (3 x 8 = 24).

### Simple Multiplayer Packs
- **2_players.txt**: 2-player game.
- **3_players.txt**: 3-player game.
- **6_player.txt**: 6 player game.
- **8_player.txt**: 8 player game.

### Valid Scenario Packs
- **2_players_horizontal_w_gap.txt**: Values arranged in a continuous horizontal sequence with gaps between them.
- **3_players_miw.txt**: Multiple immediate win, requires commenting out shuffle method in loadAndShuffle().
- **3_players_p1_iw_nonpref_denom**: Player 1 immediate win with non-preffered denomination, requires commenting out shuffle method in loadAndShuffle().
- **3_players_p1_iw_pref_denom**: Player 1 immediate win with preffered denomination, requires commenting out shuffle method in loadAndShuffle().
- **4_players_with_Zeroes**: Contains zeroes as a pack value.

### Invalid File Input Packs
- **2_players _w_gap.txt**: Pack containing empty space in between values.
- **2_players_horizontal_no_gap.txt**: Pack containing values arranged in a continuous horizontal sequence, with no gaps between them.
- **2_players_w_negative_integer.txt**: Pack containing negative integers.
- **2_players_w_special_character.txt**: Pack containing special characters.
- **2_players_w_string_number.txt**: Pack containing string values.

