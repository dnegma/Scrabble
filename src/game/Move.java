package game;

public class Move {
	private static final byte HORIZONTAL = 0;
	private static final byte VERTICAL = 1;
	
	private char[] word;
	private int startRow;
	private int startColumn;
	private int columnSize;
	private int rowSize;

	public Move(char[] word, int index, byte direction) {
		this.word = word;
		if (direction == HORIZONTAL) {
			this.columnSize = word.length;
			this.rowSize = 0;
		} else if (direction == VERTICAL) {
			this.rowSize = word.length;
			this.columnSize = 0;
		}
	}

	public char[] getWord() {
		return word;
	}

	public void setWord(char[] word) {
		this.word = word;
	}

	public int getStartRow() {
		return this.startRow;
	}

	public int getStartColumn() {
		return this.startColumn;
	}

	public int getColumnSize() {
		return columnSize;
	}

	public int getRowSize() {
		return rowSize;
	}

}
