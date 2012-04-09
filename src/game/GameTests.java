package game;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import player.BalanceOnRackPlayer;
import player.HighScoreWordPlayer;
import player.Player;
import board.Board;
import dictionary.Alphabet;
import dictionary.Trie;

public class GameTests extends Game {

	private static int NR_OF_GAMES = 100;

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		Alphabet.initializeAlphabet(GAME_LANGUAGE);
		Trie.initTrie("dictionary/dsso-1.52_utf8.txt");
		
		List<GameResult> results = new ArrayList<GameResult>();
		Player player1Type = null;
		Player player2Type = null;
		Board boardType;
		
		for (int i = 0; i < NR_OF_GAMES; i++) {
			
			boardType = new Board();
			
			player1Type = new BalanceOnRackPlayer(boardType);
			player2Type = new HighScoreWordPlayer(boardType);
			
			boolean player1StartsPlaying;
			
			if (i > (NR_OF_GAMES / 2))
				player1StartsPlaying = true;
			else
				player1StartsPlaying = false;
			
			GameResult result = new GameTests(player1StartsPlaying,
					player1Type, player2Type, boardType).play();
			results.add(result);
		}
		String player1Name = player1Type.getClass().getSimpleName();
		String player2Name = player2Type.getClass().getSimpleName();
		Date date = Calendar.getInstance().getTime();
		String currentTime = DateFormat.getTimeInstance().format(date);
		String currentDate = DateFormat.getDateInstance().format(date);
		String dateTime = currentDate + "_" + currentTime;
		String fileName = player1Type.getName() + "_" + player2Type.getName()
		+ "_" + NR_OF_GAMES + "_" + dateTime + ".txt";
		String filePath = player1Name + "_" + player2Name + "/" + NR_OF_GAMES
		+ "/";
		
		long endTime = System.currentTimeMillis();
		int runTimeSeconds = (int) ((endTime - startTime) / 1000);
		GameTests.printResultsToFile(results, fileName, filePath,
				runTimeSeconds);
		System.out.println("KLAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAR!");
	}
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
				player2String, score2, isPlayer1StartsPlaying());

		return result;

	}


	public static void printResultsToFile(List<GameResult> results,
			String fileName, String filePath, int runTime) {

		String baseFilePath = "results/";
		String fullPathTofile = baseFilePath + filePath + fileName;

		int player1wins = 0;
		int player2wins = 0;
		int player1winsStartedPlaying = 0;
		int player2winsStartedPlaying = 0;
		String player1Name = results.get(0).getPlayer1();
		String player2Name = results.get(0).getPlayer2();

		int draws = 0;

		List<Integer> player1results = new ArrayList<Integer>();
		List<Integer> player2results = new ArrayList<Integer>();

		for (GameResult gameResult : results) {
			String winner = gameResult.getWinner();
			boolean player1StartedPlaying = gameResult.isPlayer1Started();
			if (winner.equals(gameResult.getPlayer1())) {
				player1results.add(gameResult.getWinnerScore());
				player2results.add(gameResult.getLoserScore());
				player1wins = player1wins + 1;
				if (player1StartedPlaying)
					player1winsStartedPlaying = player1winsStartedPlaying + 1;
			} else if (winner.equals(gameResult.getPlayer2())) {
				player2results.add(gameResult.getWinnerScore());
				player1results.add(gameResult.getLoserScore());
				player2wins = player2wins + 1;
				if (!player1StartedPlaying)
					player2winsStartedPlaying = player2winsStartedPlaying + 1;
			} else
				draws = draws + 1;
		}

		Collections.sort(player1results);
		Collections.sort(player2results);

		int medianResultPlayer1 = player1results.get(player1results.size() / 2);
		int medianResultPlayer2 = player2results.get(player2results.size() / 2);

		int totalPlayer1Result = 0;
		for (int result : player1results)
			totalPlayer1Result = totalPlayer1Result + result;
		int meanResultPlayer1 = totalPlayer1Result / player1results.size();

		int totalPlayer2Result = 0;
		for (int result : player2results)
			totalPlayer2Result = totalPlayer2Result + result;
		int meanResultPlayer2 = totalPlayer2Result / player2results.size();

		PrintWriter pw = null;
		
		try {
			pw = new PrintWriter(fullPathTofile);
			pw.write("Runtime: " + runTime + " seconds");
			pw.write("Dictionary size: " + Trie.getDictionarySize() + " words.");
			pw.write("\n\n");
			pw.write("Total wins: \n");
			if (player1wins >= player2wins)
				pw.write(player1Name + " " + player1wins + " - " + player2wins
						+ " " + player2Name);
			else if (player2wins > player1wins)
				pw.write(player2Name + " " + player2wins + " - " + player1wins
						+ " " + player1Name);
			pw.write("\n\n");
			pw.write("Draws: " + draws);
			pw.write("\n\n");
			pw.write("Wins when started playing: \n");
			if (player1winsStartedPlaying >= player2winsStartedPlaying)
				pw.write(player1Name + " " + player1winsStartedPlaying + " - "
						+ player2winsStartedPlaying + " " + player2Name);
			else if (player2wins > player1wins)
				pw.write(player2Name + " " + player2winsStartedPlaying + " - "
						+ player1winsStartedPlaying + " " + player1Name);
			pw.write("\n\n");

			pw.write("Mean values:\n");
			pw.write(meanResultPlayer1 + " " + player1Name + "\n");
			pw.write(meanResultPlayer2 + " " + player2Name);
			pw.write("\n\n");

			pw.write("Median values:\n");
			pw.write(medianResultPlayer1 + " " + player1Name + "\n");
			pw.write(medianResultPlayer2 + " " + player2Name);
			pw.write("\n\n");
			
			pw.write("Highest value:\n");
			pw.write(Collections.max(player1results) + " " + player1Name + "\n");
			pw.write(Collections.max(player2results) + " " + player2Name);
			pw.write("\n\n");
			
			pw.write("Lowest value:\n");
			pw.write(Collections.min(player1results) + " " + player1Name + "\n");
			pw.write(Collections.min(player2results) + " " + player2Name);
			pw.write("\n\n");
			
			pw.write("Games:\n");
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
