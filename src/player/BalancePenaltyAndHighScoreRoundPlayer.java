package player;

import game.LetterChain;
import game.Move;
import game.ScoreHandler;

import java.util.HashSet;

import board.Board;
import board.Square;

public class BalancePenaltyAndHighScoreRoundPlayer extends Player {
	public static final int VOWELS = 2;
	public static final int PENALTY_FACTOR = 2;

	private HashSet<Character> vowels = new HashSet<Character>();
	private Move nextMove;
	private int highestScore;
	public BalancePenaltyAndHighScoreRoundPlayer(Board board) {
		super(board);
		initVowels();
		resetParameters();
	}

	private void initVowels() {
		char[] vowelChars = new char[] { 'A', 'E', 'I', 'O', 'U', 'Y', 'Å',
				'Ä', 'Ö' };
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

		// Add penalty points if word leaves bad vowels ratio on hand
		int penalty = Math.abs(nrVowels - VOWELS) * PENALTY_FACTOR;

		Move move = new Move(partWord.toCharArray(), lc, endSquare, transposed);
		int score = ScoreHandler.scoreOf(move) - penalty;

		if (score > highestScore) {
			setNextMove(move);
			highestScore = score;
		}

	}

	@Override
	public void resetParameters() {
		this.highestScore = 0;
	}

	@Override
	public String getName() {
		String className = this.getClass().getSimpleName();
		String ratio = BalancePenaltyAndHighScoreRoundPlayer.VOWELS + "of"
				+ Player.MAX_TILES_ON_HAND;
		return className + "_" + ratio + "ratio_" + PENALTY_FACTOR + "penalty";
	}

}
