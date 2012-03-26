package game;


public class Game {
	private static final String GAME_LANGUAGE = "swedish";
	
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
	
	public boolean makeMove(Move move) {

		int startRow = move.getStartRow();
		int startColumn = move.getStartColumn();
		
		char[] tilesToPlace = move.getWord();
		
		int columnSize = move.getColumnSize();
		int rowSize = move.getRowSize();
		
		for (int row = startRow; row < rowSize; row++) {
			for (int column = startColumn; column < columnSize; column++) {
				// place word?
			}
		}

		return false;
	}
	
	public static void main(String[] args) {
		new Game();
	}
	
}
