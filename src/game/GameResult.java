package game;

import player.Player;


public class GameResult {
	private Player player1;
	private Player player2;

	private boolean player1Started;

	public GameResult(Player player1, Player player2, boolean player1started) {
		this.setPlayer1(player1);
		this.setPlayer2(player2);
		this.setPlayer1Started(player1started);
	}

	public int getLoserScore() {
		return Math.min(player1.getScore(), player2.getScore());
	}

	public int getWinnerScore() {
		return Math.max(player1.getScore(), player2.getScore());
	}

	public void setPlayer1(Player player) {
		this.player1 = player;
	}

	public String getWinner() {
		if (player1.getScore() > player2.getScore())
			return player1.getName();
		else if (player2.getScore() > player1.getScore())
			return player2.getName();
		else
			return "DRAW";
	}

	public void setPlayer2(Player player) {
		this.player2 = player;
	}

	public String getLoser() {
		if (player1.getScore() < player2.getScore())
			return player1.getName();
		else if (player2.getScore() < player1.getScore())
			return player2.getName();
		else
			return "DRAW";
	}

	public void printResult() {
		String winner = getWinner();

		if (winner.equals("DRAW")) {
			System.out.println(winner);
		} else {

		}
	}
	

	@Override
	public String toString() {
		return getWinner() + "\t" + getWinnerScore() + "\t" + getLoserScore()
				+ "\t" + getLoser();
	}

	public String getPlayer2Name() {
		return player2.getName();
	}

	public String getPlayer1Name() {
		return player1.getName();
	}

	public Player getPlayer2() {
		return player2;
	}

	public Player getPlayer1() {
		return player1;
	}
	
	public boolean isPlayer1Started() {
		return player1Started;
	}

	public void setPlayer1Started(boolean player1Started) {
		this.player1Started = player1Started;
	}

}