package player;

import game.LetterChain;
import game.Move;
import game.ScoreHandler;
import board.Board;
import board.Square;
import dictionary.Alphabet;

public class BonusSquarePlayer extends Player {

	private Move nextMove;
	private int highestBonus = 0;
	public BonusSquarePlayer(Board board) {
		super(board);
	}

	@Override
	public void saveLegalMoveIfBest(String partWord, LetterChain lc,
			Square endSquare, boolean transposed) {
		
		Square square = endSquare;
		int letterIndex = partWord.length() - 1;

		int bonus = 0;
		while (letterIndex >= 0) {
			char content = square.getContent();
			char letter = partWord.charAt(letterIndex);
			bonus = calculateHighestBonus(partWord, bonus, content, letter);
			square = square.getNextLeft(transposed);
		}

		if (bonus > highestBonus || nextMove == null) {
			setNextMove(partWord, lc, endSquare, transposed);
			this.highestBonus = bonus;
		}
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
		}
		return bonus;
	}

	@Override
	public int getHighestBonus() {
		return highestBonus;
	}

	@Override
	public void setHighestBonus(int bonus) {
		this.highestBonus = bonus;
	}

	@Override
	public void setNextMove(Move move) {
		this.nextMove = move;
	}

	@Override
	public Move getNextMove() {
		return this.nextMove;
	}

}
