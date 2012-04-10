package player;

import game.LetterChain;
import game.Move;
import game.ScoreHandler;
import board.Board;
import board.Square;

public class HighScoreRoundPlayer extends Player {
	private Move nextMove;
	private int highestScore;
	public HighScoreRoundPlayer(Board board) {
		super(board);
		resetParameters();
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

		Move move = new Move(partWord.toCharArray(), lc, endSquare, transposed);
		int score = ScoreHandler.scoreOf(move);

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
		return className;
	}

}
