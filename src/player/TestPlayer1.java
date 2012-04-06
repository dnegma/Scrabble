package player;

import game.LetterChain;
import game.Move;

import java.util.ArrayList;
import java.util.List;

import board.Board;
import board.Square;



public class TestPlayer1 extends Player {
	List<Move> moves;

	public TestPlayer1(Board board) {
		super(board);
		initMoves();
	}

	private void initMoves () {
		moves = new ArrayList<Move>();
		// moves.add(new Move("HEJ".toCharArray(), 7, 7, Move.VERTICAL));
		// moves.add(new Move("ATT".toCharArray(), 7, 7, Move.HORIZONTAL));
		
	}
	@Override
	public Move generateMove() {
		if (moves.isEmpty())
			return null;
		return moves.remove(0);
	}

	public void saveLegalMoveIfBest(String partWord, int row, int column,
			boolean transposed) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveLegalMoveIfBest(String partWord, LetterChain wn,
			Square square, boolean transposed) {
		// TODO Auto-generated method stub

	}

}
