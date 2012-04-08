package player;

import game.LetterChain;
import game.Move;
import game.ScoreHandler;

import java.util.HashSet;

import board.Board;
import board.Square;

public class BalanceOnRackWithZPowerPlayer extends Player {
	public static final int VOWELS = 3;
	public static final int PENALTY_FACTOR = 1;

	private HashSet<Character> vowels = new HashSet<Character>();
	private Move nextMove;
	private int highestScore;
	public BalanceOnRackWithZPowerPlayer(Board board) {
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
		int zPenalty = 10;

		boolean zTileLeftOnHand = false;
		for (char tile : tilesOnHand) {
			if (tile == 'Z')
				zTileLeftOnHand = true;
			if (vowels.contains(tile))
				nrVowels = nrVowels + 1;
			else
				nrConsonants = nrConsonants + 1;
		}

		int penalty = Math.abs(nrVowels - VOWELS) * PENALTY_FACTOR;

		Move move = new Move(partWord.toCharArray(), lc, endSquare, transposed);
		int score = ScoreHandler.scoreOf(move) - penalty;
		if (zTileLeftOnHand)
			score = score - zPenalty;

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
		String ratio = BalanceOnRackWithZPowerPlayer.VOWELS + "of"
				+ Player.MAX_TILES_ON_HAND;
		return className + "_" + ratio + "ratio_" + PENALTY_FACTOR + "penalty";
	}

}
