package game;

import board.Square;

public class WordNode {

	char letter;
	Square square;
	WordNode previousNode;

	public WordNode(Square square, WordNode previousNode, char letter) {
		this.square = square;
		this.previousNode = previousNode;
		this.letter = letter;
	}
}
