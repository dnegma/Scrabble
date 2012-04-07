package player;

import game.LetterChain;
import game.Move;
import game.ScoreHandler;
import board.Board;
import board.Square;


public class HighScoreWordPlayer extends Player {

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



}
