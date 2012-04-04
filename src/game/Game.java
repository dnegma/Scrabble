package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dictionary.Dawg;


public class Game {
	private static final String GAME_LANGUAGE = "swedish";
	
	private static Board board;
	private static List<Character> tilesInBag;
	
	private static Player player1;	
	private static Player player2;
	private int turn;
	private int nrOfPasses;
	
	/**
	 * Start a new game. Parameter deciding which player should start.
	 * 
	 * @param player1StartsPlaying
	 */
	public Game(boolean player1StartsPlaying) {
		board = new Board();
		player1 = new HighScoreWordPlayer(board);
		player2 = new HighScoreWordPlayer(board);
		Alphabet.initializeAlphabet(GAME_LANGUAGE);
		Dawg.initDawg("dictionary/dsso-1.52_utf8.txt");
		tilesInBag = initTileBag();
		giveTilesToPlayer(player1);
		giveTilesToPlayer(player2);
		this.nrOfPasses = 0;
		this.turn = (player1StartsPlaying) ? -1 : 1;
		play();
	}
	
	/**
	 * Initialize tile bag. There should be a fixed amount of each letter in the
	 * beginning, so add them to a list and shuffle the list for some degree of
	 * randomness.
	 * 
	 * @return
	 */
	private List<Character> initTileBag() {
		List<Character> tilesInBag = new ArrayList<Character>();
		for (char letter : Alphabet.letterAmounts.keySet()) {
			int amount = Alphabet.getLetterAmount(letter);
			for (int i = 0 ; i < amount; i++) {
				tilesInBag.add(letter);
			}
		}
		Collections.shuffle(tilesInBag);
		return tilesInBag;
	}

	/**
	 * Play the game. Alternate between the two players and print the board's
	 * current state between each move.
	 */
	public void play() {
		while (!gameOver()) {
			Player player = (turn < 0) ? player1 : player2;
			boolean successfulMove = playTurn(player);
			if (!successfulMove)
				incrementPass();
			else 
				resetPasses();
			turn = -turn;
			board.printBoard();
			System.out.println();
			System.out.println("----------------------------------");
			System.out.println();
		}
		System.out.println("Game over!");
	}

	private boolean playTurn(Player player) {
		Move move = player.generateMove();
		
		if (move == null)
			return false;
		// Check if word fits onto board.
		if (board.isOutsideBoardLimits(move.getEndRow(), move.getEndColumn()))
			return false;
		
		int points = makeMove(move, player);
		player.addPointsToScore(points);
		giveTilesToPlayer(player);
		return true;
	}
	/**
	 * Game's over when there's no tiles left to pickup and one player have placed out all their 
	 * tiles OR both players have passed 2 times each in a row.	 
	 * @return
	 */
	public boolean gameOver() {
		if (getNrOfPasses() >= 4 || 
				(tilesInBag.isEmpty() && 
						(player1.placedAllTiles() || player2.placedAllTiles())))
			return true;
		
		return false;	
	}
	
	/**
	 * Make the move. Place a word on the board, letters already located on the
	 * board should not be touched. Different scores are retrieved from the
	 * moves depending on several factors, the score is calculated and returned
	 * for this move.
	 * 
	 * @param move
	 * @param player
	 * @return
	 */
	public int makeMove(Move move, Player player) {

		// Get word and where to place on board 
		char[] tilesToPlace = move.getWord();
		int startRow = move.getStartRow();
		int startColumn = move.getStartColumn();	
		int endRow = move.getEndRow();
		int endColumn = move.getEndColumn();
		
		byte direction;
		if (endColumn == startColumn)
			direction = Move.VERTICAL;
		else
			direction = Move.HORIZONTAL;
		// count points
		int wordPoints = 0;
		int wordBonus = 1;		
		
		
		// if ()
		// board.getSquare(startRow - 1, startColumn - 1).setAnchor(true);
		
		// Place word on board
		int tile = 0;
		for (int row=startRow; row<=endRow; row++) {
			for (int column=startColumn; column<=endColumn; column++){							
				if (!board.isOccupiedSquare(row, column)) {
					char letter = tilesToPlace[tile];
					char squareInfo = board.placeTile(letter, row, column,
							(direction == Move.VERTICAL));
					switch (squareInfo) {
					case Square.TWO_LETTER_BONUS:
						wordPoints = wordPoints + Alphabet.getLetterPoint(letter) * 2;
						break;
					case Square.THREE_LETTER_BONUS:
						wordPoints = wordPoints + Alphabet.getLetterPoint(letter) * 3;
						break;
					case Square.TWO_WORD_BONUS:
						wordBonus = wordBonus * 2;
						break;
					case Square.THREE_WORD_BONUS:
						wordBonus = wordBonus * 3;
						break;
					case Square.BUSY_SQUARE:
						if (direction == Move.HORIZONTAL)
							row = row - 1;
						else
							column = column - 1;
						continue;
					}
					// get next tile
					tile = tile + 1;
					player.removeTileFromHand(letter);
				}	
				// no bonus square. just add the letter point
				wordPoints = wordPoints
						+ Alphabet.getLetterPoint(board.getSquare(row, column)
								.getContent());
			}
		}		
		// Add (possibly) word bonuses
		wordPoints = wordPoints * wordBonus;
		
		// Add (possibly) 50 points if the player uses all tiles on hand
		if (player.placedAllTiles())
			wordPoints = wordPoints + 50;

		// add score to player
		//player.addPointsToScore(wordPoints);
		
		return wordPoints;
	}

	/**
	 * Give new tiles to the player after he did a move. The player will receive
	 * as many tiles as he needs until the bag is empty.
	 * 
	 * @param player
	 */
	public void giveTilesToPlayer(Player player) {
		int nrOfTilesNeeded = player.getNumberOfNewTilesNeeded();
		int tilesLeftInBag = tilesInBag.size();
		
		for (int i = 0; (i < tilesLeftInBag) && (i < nrOfTilesNeeded); i++) {
			giveOneTileToPlayerAndRemoveItFromBag(player);
		}		
	}
	
	/**
	 * Give one tile to a player and remove it from the common bag.
	 * 
	 * @param player
	 */
	private void giveOneTileToPlayerAndRemoveItFromBag(Player player) {
		char letter = tilesInBag.remove(0);
		player.receiveTile(letter);
	}

	public static void main(String[] args) {
		new Game(false);
	}
	
	/**
	 * Reset the number of passes that have been made. A pass is made when a
	 * player cannot make a move. Four passes in a row between the two players
	 * are allowed before the game ends.
	 */
	public void resetPasses() {
		this.nrOfPasses = 0;
	}

	/**
	 * Increment pass if player passed.
	 */
	public void incrementPass() {
		this.nrOfPasses = nrOfPasses + 1;
	}
	
	/**
	 * Get number of passes that have been made.
	 * 
	 * @return number of passes
	 */
	public int getNrOfPasses() {
		return this.nrOfPasses;
	}
}
