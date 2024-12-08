# Multiplayer Card Game
## Overview
This Java terminal-based card game simulates a multiplayer card game where players draw and discard cards in turns. The game runs with multiple players, each managing their own hand, while competing to meet the win conditions.

## Project Structure
- Card.java: Class representing a single card object.
- CardDeck.java: Class encapsulating the attributes and behaviours of a list of cards, known as a Deck.
- Player.java: Class encapsulating the attribtues and behaviours of a player, including their gameplay strategy.
- CardGame.java: Main executable class that handles user input and gameflow.

## Installation
1. Clone or download the repository to your local machine.
2. Running the Game
  - From Command Line:
      - Navigate to the 'bin' directory.
      - Run the following command to start the game.
        '''
        java CardGame
        '''
  - As JAR file:
      - Navigate to the 'build' directory.
      - Run the following command.
        '''
        java -jar cards.jar
        '''
3. Follow instructions on Terminal

## Given Pack Files
These files can be found in the 'Res' folder. In each pack, denominations are included up to a point where the total number of cards, calculated as 4 copies of each denomination, satisfies the 8*N rule, where N is the number of players. This ensures that the total number of cards in the pack aligns with the required card count for the game, and confirms that it is legal for the denomination of a card to exceed N. For example:

> In a 3-player game, the given pack will include denominations up to 6, since 4 copies of each denomination (4 x 6 = 24 cards) matches the total card count required by the 8*N requirement (3 x 8 = 24).

### Simple Multiplayer Packs
- 2_players.txt: 2-player game
- 3_players.txt: 3-player game
- 6_player.txt: 6 player game
- 8_player.txt: 8 player game

### Invalid File Input Packs

### Specific Scenario Packs

