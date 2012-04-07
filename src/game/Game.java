package game;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import player.HighScoreWordPlayer;
import player.Player;
import board.Board;
import board.Square;
import dictionary.Alphabet;
import dictionary.Trie;


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
		Trie.initDawg("dictionary/dsso-1.52_utf8.txt");
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
		for (char letter : Alphabet.alphabet) {
			int amount = Alphabet.getLetterAmount(letter);
			for (int i = 0; i < amount; i++)
				tilesInBag.add(letter);
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
			player.printRack();
			board.printBoard();
			System.out.println();			
			printTilesInBag();
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

		int points = makeMove(move, player);
		System.out.println("Score: " + points);
		player.addPointsToScore(points);
		System.out.println("Total score: " + player.getScore());
		giveTilesToPlayer(player);
		System.out.println(move.getWord());
		return true;
	}
	/**
	 * Game's over when there's no tiles left to pickup and one player have placed out all their 
	 * tiles OR both players have passed 2 times each in a row.	 
	 * @return
	 */
	public boolean gameOver() {
		if (player1.placedAllTiles() && tilesInBag.isEmpty())
			return true;
		if (player2.placedAllTiles() && tilesInBag.isEmpty())
			return true;
		if (getNrOfPasses() >= 4)
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
		int wordScore = 0;
		int wordBonus = 1;
		int adjacentWordsBonus = 0;
		LetterChain wn = move.getLetterChain();

		int letterIndex = 0;
		Square sq = move.getEndSquare();
		while (wn.getPrevious() != null && sq.getContent() != Square.WALL) {

			char letter = wn.letter;
			boolean isTransposed = move.isTransposed();

			if (!sq.containsLetter()) {
				char squareInfo = board.placeTile(letter, sq, isTransposed);
				wordScore = wordScore + getLetterBonus(squareInfo, letter);
				wordBonus = wordBonus * getWordBonus(squareInfo, letter);
				
				// Calculate adjacent word
				// Tiles going down
				boolean adjacentWord = false;
				Square adjacentSquareDown = sq.getNextDown(isTransposed);
				while (adjacentSquareDown.containsLetter()) {
					char adjacentLetter = adjacentSquareDown.getContent();
					int adjacentLetterPoint = Alphabet
							.getLetterPoint(adjacentLetter);
					adjacentWordsBonus += adjacentLetterPoint;
					// Get next tile down
					adjacentSquareDown = adjacentSquareDown
							.getNextDown(isTransposed);
					adjacentWord = true;
				}
				// Tiles going up
				Square adjacentSquareUp = sq.getNextUp(isTransposed);
				while (adjacentSquareUp.containsLetter()) {
					char adjacentLetter = adjacentSquareUp.getContent();
					int adjacentLetterPoint = Alphabet
							.getLetterPoint(adjacentLetter);
					adjacentWordsBonus += adjacentLetterPoint;
					// Get next tile up
					adjacentSquareUp = adjacentSquareUp.getNextUp(isTransposed);
					adjacentWord = true;
				}
				if (adjacentWord) {
					// Add square letter point to adjacent word
					adjacentWordsBonus += getLetterBonus(squareInfo, letter);
					// if there's a bonus, add it to the adjacent word
					adjacentWordsBonus = adjacentWordsBonus
							* getWordBonus(squareInfo, letter);
				}
				player.removeTileFromHand(letter);
				wn = wn.getPrevious();
			} else {
				// Add point from existing letter on square.
				wordScore = wordScore + Alphabet.getLetterPoint(letter);
				wn = wn.getPrevious();
			}
			letterIndex = letterIndex + 1;
			sq = sq.getNextLeft(move.isTransposed());
		}
		return (wordScore * wordBonus) + adjacentWordsBonus;
	}


	private int getWordBonus(char squareInfo, char letter) {
		switch (squareInfo) {
			case Square.THREE_WORD_BONUS:
				return 3;
			case Square.TWO_WORD_BONUS:
				return 2;
			default:
				return 1;
		}
	}

	private int getLetterBonus(char squareInfo, char letter) {
		int letterScore = Alphabet.getLetterPoint(letter);
		switch (squareInfo) {
			case Square.THREE_LETTER_BONUS:
				return letterScore * 3;
			case Square.TWO_LETTER_BONUS:
				return letterScore * 2;
			default:
				return letterScore;
		}
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
		Collections.shuffle(tilesInBag);
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

	public void printTilesInBag() {
		int tilesLeft = tilesInBag.size();
		for (int i = 0; i < tilesLeft; i++) {
			System.out.print(tilesInBag.get(i));
		}
		System.out.println();
	}
}
