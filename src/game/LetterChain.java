package game;

import board.Square;

public class LetterChain {

	char letter;
	Square square;
	LetterChain previousLetter;
	LetterChain nextLetter;


	public LetterChain(Square square, LetterChain previousNode, char letter) {
		this.square = square;
		this.previousLetter = previousNode;
		this.letter = letter;
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

	public LetterChain reversedChain() {
		LetterChain reversed = new LetterChain(getSquare(), null, getLetter());
		LetterChain tmp = this;
		while (tmp.getPrevious() != null) {
			LetterChain tmpprev = tmp.getPrevious();
			tmp.setPrevious(tmp);
			reversed = new LetterChain(tmpprev.getSquare(), reversed,
					tmpprev.getLetter());
			tmp = tmpprev;
		}
		return reversed;
	}

}
