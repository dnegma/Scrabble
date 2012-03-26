package game;

public class Move {
	public static final byte HORIZONTAL = 0;
	public static final byte VERTICAL = 1;
	
	private char[] word;
	private int startRow;
	private int startColumn;
	private byte direction;

	/**
	 * Create a new move 
	 * 
	 * @param word
	 * @param startRow
	 * @param startColumn
	 * @param direction HORIZONTAL or VERTICAL
	 */
	public Move(char[] word, int startRow, int startColumn, byte direction) {
		this.word = word;
		this.direction = direction;
		this.startRow = startRow;
		this.startColumn = startColumn;		
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

}
