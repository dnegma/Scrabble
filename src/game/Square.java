package game;

import java.util.HashSet;

import dictionary.Dawg;

/**
 * Class for representing a square on the board. Each square has a maximum of
 * four neighbors, less if the square happens to be located near an edge. The
 * square keeps track of where on the board it is located, what content it is
 * carrying, and its neighbors. Each square also has a cross-check set. Which is
 * the set of legal characters to be placed. This set changes as the surrounding
 * does, since a word has to be formed in whatever direction.
 * 
 * @author diana
 * 
 */
public class Square {
	static final char INVALID_SQUARE = (char) -1;
	static final char BUSY_SQUARE = 0;
	static final char EMPTY = 48;
	static final char TWO_LETTER_BONUS = 49;
	static final char THREE_LETTER_BONUS = 50;
	static final char TWO_WORD_BONUS = 51;
	static final char THREE_WORD_BONUS = 52;
	static final char CENTER_SQUARE = 53;

	private char content;
	private int row;
	private int column;

	private boolean isAnchor;
	private boolean transposed;

	private HashSet<Character> crosschecks;
	Square nextLeft;
	Square nextRight;
	Square nextUp;
	Square nextDown;

	public Square(char content, int row, int column) {
		this.crosschecks = new HashSet<Character>();
		this.content = content;
		this.row = row;
		this.column = column;

	}

	/**
	 * Set the four neighbors. This to make it easier to "walk the board" when
	 * searching for possible moves.
	 * 
	 * @param left
	 * @param right
	 * @param up
	 * @param down
	 */
	public void setNeighbours(Square left, Square right, Square up, Square down) {

		this.nextLeft = left;
		this.nextRight = right;
		this.nextUp = up;
		this.nextDown = down;

	}

	/**
	 * Set content of this square.
	 * 
	 * @param content
	 */
	public void setContent(char content) {
		this.content = content;

		if (nextDown != null && !nextDown.containsLetter())
			nextDown.calculateCrossChecks();
		if (nextUp != null && !nextUp.containsLetter())
			nextUp.calculateCrossChecks();
		if (nextLeft != null && !nextLeft.containsLetter())
			nextLeft.calculateCrossChecks();
		if (nextRight != null && !nextRight.containsLetter())
			nextRight.calculateCrossChecks();
	}

	/**
	 * Calculate which letters are legal to place on this square. This depends
	 * on the neighbors, since a word has to be formed in any direction.
	 */
	private void calculateCrossChecks() {
		String word;
		if (nextDown != null && nextDown.containsLetter())
			word = verticalWord("", this, 1);
		else if (nextUp != null && nextUp.containsLetter())
			word = verticalWord("", this, -1);
		else
			return;

		crosschecks.clear();
		char[] alphabet = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
				'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'R', 'S', 'T', 'U',
				'V', 'X', 'Y', 'Z', 'Å', 'Ä', 'Ö' };
		for (int i = 0; i < alphabet.length; i++) {
			if (Dawg.findWord(word))
				crosschecks.add(alphabet[i]);
		}
	}

	/**
	 * Help method for calculating cross check set. Find which vertical word is
	 * already in place, to later see what extensions can be made in any
	 * direction of the found word.
	 * 
	 * @param word
	 * @param square
	 * @param increment
	 * @return
	 */
	private String verticalWord(String word, Square square, int increment) {
		if (!square.containsLetter())
			return word;

		if (increment < 0)
			return verticalWord(word, square, increment) + square.getContent();
		else
			return square.getContent() + verticalWord(word, square, increment);
	}

	/**
	 * Check if this square contains a letter.
	 * 
	 * @return True if letter is placed on this square.
	 */
	public boolean containsLetter() {
		return content > CENTER_SQUARE;
	}

	/**
	 * Get content of square, can be either a letter, bonus or just an empty
	 * normal square.
	 * 
	 * @return
	 */
	public char getContent() {
		return content;
	}

	public boolean isAnchor() {
		return isAnchor;
	}

	public void setAnchor(boolean isAnchor) {
		this.isAnchor = isAnchor;
	}

	/**
	 * Get cross-check set.
	 * 
	 * @return
	 */
	public HashSet<Character> getCrossChecks() {
		return crosschecks;
	}

	/**
	 * Get which column this square is located on the board.
	 * 
	 * @return
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * Get which row this square is located on the board.
	 * 
	 * @return
	 */
	public int getRow() {
		return row;
	}

	public void transposeSquare() {
		transposed = true;
		swapRowColumn();
		swapNeighbors();
	}

	private void swapNeighbors() {
		Square tmp = nextDown;
		nextDown = nextRight;
		nextRight = tmp;

		tmp = nextUp;
		nextUp = nextLeft;
		nextUp = tmp;
	}

	public void swapRowColumn() {
		int tmp = row;
		row = column;
		column = tmp;
	}

	public Square getNextRight(boolean transposed) {
		return (transposed) ? nextDown : nextRight;
	}

	public Square getNextLeft(boolean transposed) {
		return (transposed) ? nextUp : nextLeft;
	}

	public Square getNextDown(boolean transposed) {
		return (transposed) ? nextRight : nextDown;
	}

	public Square getNextUp(boolean transposed) {
		return (transposed) ? nextLeft : nextUp;
	}
}
