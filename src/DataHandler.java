import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataHandler {
	public static final String DIRECTORY = "results/BalancePlayer_HighScoreWordPlayer/1000/8-ratio";
	
	static PlayerStats player1Stat;
	static PlayerStats player2Stat;

	static int draws;
	List<Integer> medianValues = new ArrayList<Integer>();

	public static void main(String[] args) {
		File directory = new File(DIRECTORY);
		String[] filename = directory.list();

		setPlayerNames();
		BufferedReader br;
		
		try{
			int laps = filename.length;
			for (int i = 0; i < laps; i++) {
				String file = DIRECTORY + "/" + filename[i];
				br = new BufferedReader(new FileReader(file));

				String line = br.readLine();

				while (line != null && !line.startsWith("Games")) {
					if (line.startsWith("Total wins"))
						addWins(br.readLine());
					else if (line.startsWith("Draws"))
						addDraws(line);
					else if (line.startsWith("Wins when"))
						addStartWins(br.readLine());
					else if (line.startsWith("Mean"))
						addMean(br.readLine(), br.readLine());
					else if (line.startsWith("Median"))
						addToMedianList(br.readLine(), br.readLine());
					else if (line.startsWith("Highest "))
						setHighestValue(br.readLine(), br.readLine());
					else if (line.startsWith("Lowest"))
						setLowestValue(br.readLine(), br.readLine());
					line = br.readLine();
				}
			}
			player1Stat.meanScore = player1Stat.meanScore / 10;
			player2Stat.meanScore = player2Stat.meanScore / 10;

			calculateMedian(player1Stat);
			calculateMedian(player2Stat);
			
			printToFile(player1Stat, player2Stat);
		} catch (IOException e) {

		}
	}

	private static void printToFile(PlayerStats player1Stat,
			PlayerStats player2Stat) throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(DIRECTORY + "/results.compiled");

		write(player1Stat, pw);
		pw.write("\n\n");
		write(player2Stat, pw);
		pw.write("\n\nDraws: " + draws);
		pw.close();
	}

	private static void write(PlayerStats playerStat, PrintWriter pw) {
		pw.write("\nPlayer: " + playerStat.name);
		pw.write("\nTotal wins: " + playerStat.totalwins);
		pw.write("\nWins when started: " + playerStat.startWins);
		pw.write("\nMean score: " + playerStat.meanScore);
		pw.write("\nMedian score: " + playerStat.medianScore);
		pw.write("\nHighest score: " + playerStat.highestScore);
		pw.write("\nLowest score: " + playerStat.lowestScore);
	}

	private static void calculateMedian(PlayerStats playerStat) {
		Collections.sort(playerStat.medianList);
		
		int size = playerStat.medianList.size();
		playerStat.medianScore = playerStat.medianList.get(size / 2);
	}

	private static void setPlayerNames() {
		String[] directory = DIRECTORY.split("/|_");
		
		player1Stat = new PlayerStats();
		player2Stat = new PlayerStats();
		for (String s : directory) {
			if (s.startsWith("Ba") || s.startsWith("Hi")) {
				if (player1Stat.name == null)
					player1Stat.name = s;
				else if (player2Stat.name == null) {
					player2Stat.name = s;
				}
			}
		}
	}

	private static void addToMedianList(String line1, String line2) {
		String[] args = line1.split(" ");
		player1Stat.medianList.add(Integer.parseInt(args[0]));

		args = line2.split(" ");
		player2Stat.medianList.add(Integer.parseInt(args[0]));
	}

	private static void setHighestValue(String line1, String line2) {
		String[] args = line1.split(" ");
		int highest = Integer.parseInt(args[0]);
		if (highest > player1Stat.highestScore)
			player1Stat.highestScore = highest;

		args = line2.split(" ");
		highest = Integer.parseInt(args[0]);
		if (highest > player2Stat.highestScore)
			player2Stat.highestScore = highest;
	}

	private static void setLowestValue(String line1, String line2) {
		String[] args = line1.split(" ");
		int lowest = Integer.parseInt(args[0]);
		if (lowest < player1Stat.lowestScore)
			player1Stat.lowestScore = lowest;

		args = line2.split(" ");
		lowest = Integer.parseInt(args[0]);
		if (lowest < player2Stat.lowestScore)
			player2Stat.lowestScore = lowest;
	}

	private static void addMean(String line1, String line2) {
		String[] args = line1.split(" ");
		player1Stat.meanScore += Integer.parseInt(args[0]);

		args = line2.split(" ");
		player2Stat.meanScore += Integer.parseInt(args[0]);
	}

	private static void addStartWins(String line) {
		String[] args = line.split(" ");
		player2Stat.startWins += Integer.parseInt(args[1]);
		player1Stat.startWins += Integer.parseInt(args[3]);
	}

	private static void addDraws(String line) {
		String[] args = line.split(" ");
		draws += Integer.parseInt(args[1]);
	}

	private static void addWins(String line) {
		String[] args = line.split(" ");
		player2Stat.totalwins += Integer.parseInt(args[1]);
		player1Stat.totalwins += Integer.parseInt(args[3]);
	}

	private static class PlayerStats {
		String name;
		int totalwins;
		int startWins;
		int highestScore;
		int lowestScore = Integer.MAX_VALUE;
		int meanScore;
		int medianScore;
		List<Integer> medianList = new ArrayList<Integer>();

		public PlayerStats() {

		}
	}

}
