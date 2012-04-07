package game;

import board.Square;

public class Move {

	private char[] word;
	private Square endSquare;
	private boolean transposed;
	
	private LetterChain letterChain;

	/**
	 * Create a new move
	 * 
	 * @param word
	 * @param transposed
	 * @param startRow
	 * @param startColumn
	 * @param direction
	 *            HORIZONTAL or VERTICAL
	 */
	public Move(char[] word, LetterChain letterChain, Square endSquare,
			boolean transposed) {
		this.setWord(word);
		this.setLetterChain(letterChain);
		this.setEndSquare(endSquare);
		this.setTransposed(transposed);

	}

	public void setTransposed(boolean transposed) {
		this.transposed = transposed;
	}

	public boolean isTransposed() {
		return transposed;
	}

	public void setEndSquare(Square endSquare) {
		this.endSquare = endSquare;
	}

	public Square getEndSquare() {
		return endSquare;
	}

	public void setLetterChain(LetterChain letterChain) {
		this.letterChain = letterChain;
	}

	public LetterChain getLetterChain() {
		return letterChain;
	}

	public void setWord(char[] word) {
		this.word = word;
	}

	public char[] getWord() {
		return word;
	}

}
