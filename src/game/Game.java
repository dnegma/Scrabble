package game;

public class Game {
	private static final byte HORIZONTAL = 0;
	private static final byte VERTICAL = 1;
	
	Board board;
	Player player1;
	Player player2;
	
	int score1;
	int score2;
	
	public Game() {
		// player1 = new Player();
		// player2 = new Player();
		board = new Board();
		
	}
	
	public boolean play() {
		return false;
	}
	
	public boolean placeWord(char[] word, int anchorIndex, byte direction) {
		return false;
	}
	
	
}
