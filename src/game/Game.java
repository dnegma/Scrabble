package game;


import gui.ScrabbleGUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import player.BalanceOnRackPlayer;
import player.HighScoreWordPlayer;
import player.Player;
import board.Board;
import board.Square;
import dictionary.Alphabet;
import dictionary.Trie;


public class Game {
	public static final String GAME_LANGUAGE = "swedish";

	ScrabbleGUI gui;
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
	public Game(boolean player1StartsPlaying, Player player1Type,
			Player player2Type, Board boardType) {
		board = boardType;
		player1 = player1Type;
		player2 = player2Type;
		tilesInBag = initTileBag();
		giveTilesToPlayer(player1);
		giveTilesToPlayer(player2);
		this.nrOfPasses = 0;
		this.turn = (player1StartsPlaying) ? -1 : 1;
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
	public GameResult play() {
		setGUI(new ScrabbleGUI(board, player1, player2));

		while (!gameOver()) {
			Player player = (turn < 0) ? player1 : player2;
			boolean successfulMove = playTurn(player);
			if (!successfulMove)
				incrementPass();
			else 
				resetPasses();
			player.resetParameters();
			player.printRack();
			gui.updateBoard();
			gui.updateScores(player, turn);

			turn = -turn;
			System.out.println("Total score: " + player.getScore());
			board.printBoard();
			System.out.println();			
			printTilesInBag();
			System.out.println("----------------------------------");
			System.out.println();
		}
		int score1, score2;
		score1 = player1.getScore();
		score2 = player2.getScore();
		for (Character letter : player1.getTilesOnHand()) {
			score1 = score1 - Alphabet.getLetterPoint(letter);
		}
		for (Character letter : player2.getTilesOnHand()) {
			score2 = score2 - Alphabet.getLetterPoint(letter);
		}

		String player1String = player1.getClass().getSimpleName();
		String player2String = player2.getClass().getSimpleName();
		GameResult result = new GameResult(player1String, score1,
				player2String, score2);
		

		//		
		// if (score1 > score2){
		// result = new GameResult(player1String, player2String, score1,
		// score2);
		// // System.out.println("Player 1 wins! " + score1 + " - " + score2);
		// else if (score2 == score1)
		// new GameResult(player1)
		// // System.out.println("Draw! " + score1 + " - " + score2);
		// else
		// new Game
		// // System.out.println("Player 2 wins! " + score2 + " - " + score1);

		return result;
	}

	protected boolean playTurn(Player player) {
		Move move = player.generateMove();

		if (move == null)
			return false;
		// Check if word fits onto board.

		int points = makeMove(move, player);
		// System.out.println("Score: " + points);
		player.addPointsToScore(points);
		if (player.placedAllTiles())
			player.addPointsToScore(50);
		// System.out.println("Total score: " + player.getScore());
		giveTilesToPlayer(player);
		// System.out.println(move.getWord());
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

		LetterChain wn = move.getLetterChain();

		int score = ScoreHandler.scoreOf(move);
		int letterIndex = 0;
		Square sq = move.getEndSquare();
		while (wn.getPrevious() != null && sq.getContent() != Square.WALL) {

			char letter = wn.getLetter();
			boolean isTransposed = move.isTransposed();

			if (!sq.containsLetter()) {
				char squareInfo = board.placeTile(letter, sq, isTransposed);

				player.removeTileFromHand(letter);
				wn = wn.getPrevious();
			} else {
				wn = wn.getPrevious();
			}
			letterIndex = letterIndex + 1;
			sq = sq.getNextLeft(move.isTransposed());
		}
		return score;
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
		Alphabet.initializeAlphabet(GAME_LANGUAGE);
		Trie.initDawg("dictionary/dsso-1.52_utf8.txt");
		
		Board boardType = new Board();
		Player player1Type = new BalanceOnRackPlayer(boardType);
		Player player2Type = new HighScoreWordPlayer(boardType);
		boolean player1StartsPlaying = true;

		GameResult result = new Game(player1StartsPlaying, player1Type,
				player2Type, boardType).play();
		result.printResult();
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
	
	protected void setGUI(ScrabbleGUI gui) {
		this.gui = gui;
	}

	protected Player getPlayer1() {
		return player1;
	}

	protected Player getPlayer2() {
		return player2;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}
}
