package game;

import board.Square;


public class LetterChain {

	private char letter;
	private LetterChain previousLetter;
	private LetterChain nextLetter;
	private Square square;
	private boolean onRack;


	public LetterChain(LetterChain previousNode, char letter, boolean onRack) {
		this.previousLetter = previousNode;
		this.letter = letter;
		this.setSquare(square);
		this.setOnRack(onRack);
	}

	public LetterChain getPrevious() {
		return previousLetter;
	}

	public void setPrevious(LetterChain previousLetter) {
		this.previousLetter = previousLetter;
	}

	public LetterChain getNextLetter() {
		return nextLetter;
	}

	public void setNextLetter(LetterChain nextLetter) {
		this.nextLetter = nextLetter;
	}

	public char getLetter() {
		return letter;
	}

	public void setLetter(char letter) {
		this.letter = letter;
	}

	public Square getSquare() {
		return square;
	}

	public void setSquare(Square square) {
		this.square = square;
	}

	public void setOnRack(boolean onRack) {
		this.onRack = onRack;
	}

	public boolean isOnRack() {
		return onRack;
	}


}
