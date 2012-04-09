package player;

import game.LetterChain;
import game.Move;
import game.ScoreHandler;

import java.util.HashSet;

import board.Board;
import board.Square;
import dictionary.Alphabet;

public class BonusSquareAndBalanceOnRackPlayer extends Player {
	public static final int VOWELS = 3;
	public static final int PENALTY_FACTOR = 1;
	private HashSet<Character> vowels = new HashSet<Character>();
	private int highestBonus;
	private Move nextMove;
	private int highestScore;

	public BonusSquareAndBalanceOnRackPlayer(Board board) {
		super(board);
		initVowels();
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

		// check ratio of vowels left on hand
		int nrVowels = 0;
		int nrConsonants = 0;

		for (char c : tilesOnHand) {
			if (vowels.contains(c))
				nrVowels = nrVowels + 1;
			else
				nrConsonants = nrConsonants + 1;
		}

		int penalty = Math.abs(nrVowels - VOWELS) * PENALTY_FACTOR;

		Move move = new Move(partWord.toCharArray(), lc, endSquare, transposed);
		int score = ScoreHandler.scoreOf(move) - penalty;

		// check bonus
		Square square = endSquare;
		int letterIndex = partWord.length() - 1;

		int bonus = 0;
		while (letterIndex >= 0 && square != null) {
			char content = square.getContent();
			char letter = partWord.charAt(letterIndex);
			bonus = calculateHighestBonus(partWord, bonus, content, letter);
			square = square.getNextLeft(transposed);
		}

		if (score > getHighestScore() || bonus > getHighestBonus()
				|| nextMove == null) {
			setNextMove(move);
			setHighestBonus(bonus);
			setHighestScore(score);
		}
	}

	protected int getHighestScore() {
		return highestScore;
	}

	protected void setHighestScore(int highestScore) {
		this.highestScore = highestScore;
	}

	private int calculateHighestBonus(String partWord, int bonus, char content,
			char letter) {
		switch (content) {
		case Square.THREE_LETTER_BONUS:
			int letterBonus = Alphabet.getLetterPoint(letter) * 3;
			bonus = (letterBonus > bonus) ? letterBonus : bonus;
			break;
		case Square.TWO_LETTER_BONUS:
			letterBonus = Alphabet.getLetterPoint(letter) * 2;
			bonus = (letterBonus > bonus) ? letterBonus : bonus;
			break;
		case Square.THREE_WORD_BONUS:
			int wordBonus = ScoreHandler.scoreOf(partWord) * 3;
			bonus = (wordBonus > bonus) ? wordBonus : bonus;
			break;
		case Square.TWO_WORD_BONUS:
			wordBonus = ScoreHandler.scoreOf(partWord) * 2;
			bonus = (wordBonus > bonus) ? wordBonus : bonus;
			break;
		}
		return bonus;
	}

	public int getHighestBonus() {
		return highestBonus;
	}

	public void setHighestBonus(int highestBonus) {
		this.highestBonus = highestBonus;
	}

	@Override
	public void resetParameters() {
		this.highestScore = 0;
		this.highestBonus = 0;
	}

	@Override
	public String getName() {
		String className = this.getClass().getSimpleName();
		String ratio = BonusSquareAndBalanceOnRackPlayer.VOWELS + "of"
				+ Player.MAX_TILES_ON_HAND;
		return className + "_" + ratio + "ratio_" + PENALTY_FACTOR + "penalty";
	}

}
