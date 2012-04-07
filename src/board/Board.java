package board;

public class Board {
	
	Square[][] board;
	// byte[][] board;
	public static final int BOARD_SIZE = 15;
	
	/**
	 * Create a new board
	 */
	public Board()
	{
		initBoard();
	}

	private void initBoard() {
		board = new Square[BOARD_SIZE][BOARD_SIZE];
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int column = 0; column < BOARD_SIZE; column++) {
				board[row][column] = new Square('.', row, column);
				if (row == 7 && column == 7) {
					Square square = board[row][column];
					square.setCenterSquare();
					square.setAnchor(true);
				}
			}
		}

		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int column = 0; column < BOARD_SIZE; column++) {
				Square square = board[row][column];
				Square left = (column <= 0) ? new Square(Square.WALL, -1, -1)
						: board[row][column - 1];
				Square right = (column >= BOARD_SIZE - 1) ? new Square(
						Square.WALL, -1, -1)
						: board[row][column + 1];
				Square up = (row <= 0) ? new Square(Square.WALL, -1, -1)
						: board[row - 1][column];
				Square down = (row >= BOARD_SIZE - 1) ? new Square(Square.WALL,
						-1, -1)
						: board[row + 1][column];

				square.setNeighbours(left, right, up, down);
			}
		}
	}
	
	/**
	 * 
	 * @param letter
	 * @param column
	 *            zero-indexed
	 * @param row
	 *            zero-indexed
	 * @param transposed
	 * @return square info
	 */
	public char placeTile(char letter, Square square, boolean transposed) {

		square.setAnchor(false);
		square.setContent(letter, transposed);
		return letter;
	}

	public boolean isOutsideBoardLimits(Square square) {
		return square.getContent() == Square.WALL;
	}
	
	/**
	 * 
	 * @param column zero-indexed
	 * @param row zero-indexed
	 * @return
	 */
	public Square getSquare(int row, int column) {
		return board[row][column];
	}
	
	/**
	 * Print board. The board is printed in the following format.
	 *  [A-Z]	= letter
	 *  . 		= empty square
	 *  
	 * . . . H E J . . .
	 * . . . A . O . . . 
	 * . . . T U B . . . 
	 * . . . T . B . . . 
	 */
	public void printBoard() {
		for (Square[] row : this.board) {
			for (Square cell : row) {
				System.out.print(cell.getContent() + "\t");
			}
			System.out.println();
			System.out.println();
		}
	}
	
	public Square[][] getBoard() {
		return this.board;
	}

	public Square[][] getTransposedBoard() {
		Square[][] transposed = new Square[BOARD_SIZE][BOARD_SIZE];
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int column = 0; column < BOARD_SIZE; column++) {
				transposed[column][row] = board[row][column];
			}
		}
		return transposed;
	}

	/**
	 * Calculate all cross check sets for the anchor squares before hand,
	 * depending on if the board is transposed or not.
	 * 
	 * @param transposed
	 */
	public void calculateAllCrossChecks(boolean transposed) {
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int column = 0; column < BOARD_SIZE; column++) {
				Square square = board[row][column];
				if (square.isAnchor())
					square.calculateCrossCheckSet(transposed);
			}
		}
	}
}
