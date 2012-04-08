package player;

import game.LetterChain;
import game.Move;
import game.ScoreHandler;
import board.Board;
import board.Square;


public class HighScoreWordPlayer extends Player {

	private Move nextMove;
	public HighScoreWordPlayer(Board board) {
		super(board);
	}

	@Override
	public void saveLegalMoveIfBest(String partWord, LetterChain wn,
			Square endSquare, boolean transposed) {

		int lastLetter = partWord.length() - 1;
		if (!endSquare.crossCheckContains(partWord.charAt(lastLetter)))
			return;
		if (nextMove == null
				|| ScoreHandler.scoreOf(partWord) > ScoreHandler
						.scoreOf(nextMove.getWord())) {
			this.setNextMove(new Move(partWord.toCharArray(), wn, endSquare,
					transposed));
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
	public void resetParameters() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		String className = this.getClass().getSimpleName();

		return className;
	}

}
