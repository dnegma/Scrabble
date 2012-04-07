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
		// board = new Square[BOARD_SIZE][BOARD_SIZE];
		// board[7][7] = TWO_LETTER_BONUS;
	}

	private void initBoard() {
		board = new Square[BOARD_SIZE][BOARD_SIZE];
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int column = 0; column < BOARD_SIZE; column++) {
				board[row][column] = new Square('.', row, column);
				if (row == 7 && column == 7) {
					Square square = board[row][column];
					square.setContent(Square.CENTER_SQUARE, false);
					square.setAnchor(true);
					square.initCrossCheck();

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
	 * @param row zero-indexed
	 * @param column zero-indexed
	 * @return true if square is occupied
	 */
	public boolean isOccupiedSquare(Square square) {
		if (square.containsLetter())
			return true;
		return false;
	}

	/**
	 * 
	 * @param column zero-indexed
	 * @param row zero-indexed
	 * @return
	 */
	public Square getSquare(int row, int column) {
		// Square square = board[row][column];
		return board[row][column];
	}
	
	public void printBoard() {
		for (Square[] row : this.board) {
			for (Square cell : row) {
				// if (cell.getContent() == Square.BUSY_SQUARE)
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

	public char placeTileReversed(char letter, int letterIndex, int wordLength,
			int row,
			int column, boolean transposed) {
		int colIndex = column + (wordLength - letterIndex - 1);
		Square square = board[row][colIndex];
		if (square.containsLetter())
			return Square.BUSY_SQUARE;

		square.setAnchor(false);
		square.setContent(letter, transposed);
		return letter;
	}
}
