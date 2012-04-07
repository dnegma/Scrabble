package player;

import game.LetterChain;
import game.Move;

import java.util.HashSet;

import board.Board;
import board.Square;

public class BalanceOnRackPlayer extends Player {
	public static final float RATIO = 5f / 8f;
	private HashSet<Character> vowels = new HashSet<Character>();
	private Move nextMove;
	private float ratioDifference;
	public BalanceOnRackPlayer(Board board) {
		super(board);
		initVowels();
		resetParameters();
	}

	private void initVowels() {
		char[] vowelChars = new char[] { 'A', 'E', 'I', 'O', 'U', 'Y' };
		for (char c : vowelChars) {
			vowels.add(c);
		}
	}

	@Override
	public void setNextMove(Move move) {
		this.nextMove = move;
	}

	@Override
	public Move getNextMove() {
		return this.nextMove;
	}

	@Override
	public void saveLegalMoveIfBest(String partWord, LetterChain lc,
			Square endSquare, boolean transposed) {

		int nrVowels = 0;
		int nrConsonants = 0;

		for (char c : tilesOnHand) {
			if (vowels.contains(c))
				nrVowels = nrVowels + 1;
			else
				nrConsonants = nrConsonants + 1;
		}

		float newRatio = (float) nrVowels / (float) nrConsonants;

		float newRatioDifference = Math.abs(newRatio - RATIO);
		if (newRatioDifference < ratioDifference) {
			setNextMove(new Move(partWord.toCharArray(), lc, endSquare,
					transposed));
			ratioDifference = newRatioDifference;
		}

	}

	@Override
	public void resetParameters() {
		this.ratioDifference = tilesOnHand.size();
	}

}
