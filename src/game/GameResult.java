package game;


public class GameResult {
	private String player1;
	private String player2;
	private int player1score;
	private int player2score;

	public GameResult(String player1, int player1score, String player2,
			int player2score) {
		this.setPlayer1(player1);
		this.setPlayer2(player2);
		this.setplayer1Score(player1score);
		this.setplayer2Score(player2score);
	}

	public void setplayer2Score(int score) {
		this.player2score = score;
	}

	public int getLoserScore() {
		return Math.min(player1score, player2score);
	}

	public void setplayer1Score(int score) {
		this.player1score = score;
	}

	public int getWinnerScore() {
		return Math.max(player1score, player2score);
	}

	public void setPlayer1(String player) {
		this.player1 = player;
	}

	public String getWinner() {
		if (player1score > player2score)
			return player1;
		else if (player2score > player1score)
			return player2;
		else
			return "DRAW";
	}

	public void setPlayer2(String player) {
		this.player2 = player;
	}

	public String getLoser() {
		if (player1score < player2score)
			return player1;
		else if (player2score < player1score)
			return player2;
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
	
}