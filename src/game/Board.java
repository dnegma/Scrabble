package game;

public class Board {
	static final byte EMPTY = 48;
	static final byte TWO_LETTER_BONUS = 49;
	static final byte THREE_LETTER_BONUS = 50;
	static final byte TWO_WORD_BONUS = 51;
	static final byte THREE_WORD_BONUS = 52;
	static final byte CENTER_SQUARE = 53;
	
	byte[][] board;
	static final int BOARD_SIZE = 15;
	
	/**
	 * Create a new board
	 */
	Board()
	{
		board = new byte[BOARD_SIZE][BOARD_SIZE];
		board[7][7] = TWO_LETTER_BONUS;
	}
	/**
	 *  
	 * @param letter
	 * @param column zero-indexed
	 * @param row zero-indexed
	 * @return square info
	 */
	public byte placeTile(char letter, int row, int column) {
		byte squareInfo = board[row][column]; 
		board[row][column] = (byte)letter;
		
		return squareInfo;
	}
	public boolean isOutsideBoardLimits(int row, int column) {
		return column >= BOARD_SIZE || row >= BOARD_SIZE || column < 0 || row < 0;
	}
	
	/**
	 * 
	 * @param row zero-indexed
	 * @param column zero-indexed
	 * @return true if square is occupied
	 */
	public boolean isOccupiedSquare(int row, int column) {
		byte square = board[row][column];
		if (square >= CENTER_SQUARE)
			return true;
		return false;
	}

	/**
	 * 
	 * @param column zero-indexed
	 * @param row zero-indexed
	 * @return
	 */
	public byte getSquareContent(int row, int column) {
		if (!isOutsideBoardLimits(row, column))
			return board[row][column];
		return 0;
	}
	
	public void printBoard() {
		for (byte[] row : this.board) {
			for (byte cell : row) {
				
				String letter = new String();
				System.out.print(cell + "\t");
			}
			System.out.println();
		}
	}
	
	public byte[][] getBoard() {
		return this.board;
	}
}
