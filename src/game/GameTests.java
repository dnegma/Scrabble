package game;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import player.BalanceOnRackPlayer;
import player.HighScoreWordPlayer;
import player.Player;
import board.Board;
import dictionary.Alphabet;
import dictionary.Trie;

public class GameTests extends Game {

	/**
	 * Start a new game. Parameter deciding which player should start.
	 * 
	 * @param player1StartsPlaying
	 */
	public GameTests(boolean player1StartsPlaying, Player player1Type,
			Player player2Type, Board boardType) {
		super(player1StartsPlaying, player1Type, player2Type, boardType);
	}

	@Override
	/**
	 * Play the game. Alternate between the two players and print the board's
	 * current state between each move.
	 */
	public GameResult play() {
		Player player1 = getPlayer1();
		Player player2 = getPlayer2();
		while (!gameOver()) {
			Player player = (getTurn() < 0) ? player1 : player2;
			boolean successfulMove = playTurn(player);
			if (!successfulMove)
				incrementPass();
			else
				resetPasses();
			player.resetParameters();
			setTurn(-getTurn());
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

		return result;

	}

	public static void main(String[] args) {
		Alphabet.initializeAlphabet(GAME_LANGUAGE);
		Trie.initDawg("dictionary/dsso-1.52_utf8.txt");

		List<GameResult> results = new ArrayList<GameResult>();
		Player player1Type = null;
		Player player2Type = null;
		Board boardType;

		int nrOfGames = 100;
		for (int i = 0; i < nrOfGames; i++) {

			boardType = new Board();
			player1Type = new BalanceOnRackPlayer(boardType);
			player2Type = new HighScoreWordPlayer(boardType);

			boolean player1StartsPlaying;

			if (i > (nrOfGames / 2))
				player1StartsPlaying = true;
			else
				player1StartsPlaying = false;

			GameResult result = new GameTests(player1StartsPlaying,
					player1Type, player2Type, boardType).play();
			results.add(result);
		}
		String player1Name = player1Type.getName();
		String player2Name = player2Type.getName();
		Date date = Calendar.getInstance().getTime();
		String currentTime = DateFormat.getTimeInstance().format(date);
		String currentDate = DateFormat.getDateInstance().format(date);
		String dateTime = currentDate + "_" + currentTime;
		String fileName = player1Name + "_" + player2Name + "_" + nrOfGames
				+ "_" + dateTime;
		String filePath = player1Name + "_" + player2Name + "/" + nrOfGames
				+ "/";

		GameTests.printResultsToFile(results, fileName, filePath);
	}

	public static void printResultsToFile(List<GameResult> results,
			String fileName, String filePath) {
		String baseFilePath = "results/";
		String fullPathTofile = baseFilePath + filePath + fileName;

		PrintWriter pw = null;
		try {
			pw = new PrintWriter(fullPathTofile);
			for (GameResult gameResult : results) {
				pw.write(gameResult.toString() + "\n");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (pw != null)
				pw.close();
		}
	}
}
