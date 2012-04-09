package player;

import game.LetterChain;
import game.Move;
import game.ScoreHandler;
import board.Board;
import board.Square;

public class BonusSquareOnlyPlayer extends Player{
	private Move nextMove;
	private int highestBonus;
	private int highestScore;
	
	public BonusSquareOnlyPlayer(Board board) {
		super(board);
	}

	@Override
	public void saveLegalMoveIfBest(String partWord, LetterChain lc,
			Square endSquare, boolean transposed) {
		
		Square square = endSquare;
		int letterIndex = partWord.length() - 1;

		int bonus = 0;
		while (letterIndex >= 0 && square != null) {
			char content = square.getContent();
			bonus = calculateHighestBonus(content, bonus);
			square = square.getNextLeft(transposed);
		}

		Move move = new Move(partWord.toCharArray(), lc, endSquare, transposed);
		if (bonus > highestBonus || nextMove == null) {
			setNextMove(move);
			setHighestBonus(bonus);
		} else if (bonus == 0 && highestBonus == 0) {
			int score = ScoreHandler.scoreOf(move);
			if (score > highestScore) {
				setNextMove(move);
				highestScore = score;
			}
		}
	}

	private int calculateHighestBonus(char content, int bonus) {
		switch (content) {
		case Square.THREE_LETTER_BONUS:
			return (bonus > 3) ? bonus : 3;
		case Square.TWO_LETTER_BONUS:
			return (bonus > 2) ? bonus : 2;
		case Square.THREE_WORD_BONUS:
			return (bonus > 6) ? bonus : 6;
		case Square.TWO_WORD_BONUS:
			return (bonus > 4) ? bonus : 4;
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
	public void setNextMove(Move move) {
		this.nextMove = move;
	}

	@Override
	public Move getNextMove() {
		return this.nextMove;
	}

	@Override
	public void resetParameters() {
		this.highestBonus = 0;
		this.highestScore = 0;
	}

	@Override
	public String getName() {
		String className = this.getClass().getSimpleName();
		return className;
	}
}
