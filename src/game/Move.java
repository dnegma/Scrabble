package game;

public class Move {
	public static final byte HORIZONTAL = 0;
	public static final byte VERTICAL = 1;
	
	private char[] word;
	private int startRow;
	private int startColumn;
	private byte direction;
	private int endRow;
	private int endColumn;
	
	WordNode wn;
	/**
	 * Create a new move 
	 * 
	 * @param word
	 * @param startRow
	 * @param startColumn
	 * @param direction HORIZONTAL or VERTICAL
	 */
	public Move(char[] word, WordNode wn, int startRow, int startColumn,
			byte direction) {
		this.word = word;
		this.direction = direction;
		this.startRow = startRow;
		this.startColumn = startColumn;		
		this.wn = wn;
		
		// Decide which direction to place word	
		if (direction == Move.HORIZONTAL) {
			this.endRow = startRow;
			this.endColumn = startColumn + word.length - 1;
		} else {
			// Vertical
			this.endRow = startRow + word.length - 1;
			this.endColumn = startColumn;
		}
	}

	public char[] getWord() {
		return this.word;
	}

	public int getStartRow() {
		return this.startRow;
	}

	public int getStartColumn() {
		return this.startColumn;
	}
	
	public byte getDirection() {
		return this.direction;
	}
	
	public int getEndRow() {
		return this.endRow;
	}
	
	public int getEndColumn() {
		return this.endColumn;
	}

}
