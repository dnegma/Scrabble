package game;


public class LetterChain {

	char letter;
	LetterChain previousLetter;
	LetterChain nextLetter;


	public LetterChain(LetterChain previousNode, char letter) {
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


}
