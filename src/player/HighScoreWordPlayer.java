package player;

import game.LetterChain;
import game.Move;
import game.ScoreHandler;
import board.Board;
import board.Square;


public class HighScoreWordPlayer extends Player {

	private Move nextMove;
	private int highestBonus;
	public HighScoreWordPlayer(Board board) {
		super(board);
	}

	@Override
	public void saveLegalMoveIfBest(String partWord, LetterChain wn,
			Square endSquare, boolean transposed) {

		int lastLetter = partWord.length() - 1;
		if (!endSquare.crossCheckContains(partWord.charAt(lastLetter)))
			return;
		if (ScoreHandler.scoreOf(partWord) > ScoreHandler.scoreOf(nextMove)) {
			nextMove = new Move(partWord.toCharArray(), wn, endSquare,
					transposed);
		}
	}

	@Override
	public void setNextMove(Move move) {
		this.nextMove = move;
	}

	@Override
	public Move getNextMove() {
		return nextMove;
	}

	@Override
	public int getHighestBonus() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setHighestBonus(int bonus) {
		// TODO Auto-generated method stub

	}


}
