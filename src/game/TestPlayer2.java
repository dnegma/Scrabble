package game;

import java.util.ArrayList;
import java.util.List;

public class TestPlayer2 extends Player {

	List<Move> moves;

	public TestPlayer2(Board board) {
		super(board);
		initMoves();
	}

	private void initMoves () {
		moves = new ArrayList<Move>();
		// moves.add(new Move("AGA".toCharsArray(), 7, 10, Move.VERTICAL));
		
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
	public void saveLegalMoveIfBest(String partWord, WordNode wn, int row,
			int column, boolean transposed) {
		// TODO Auto-generated method stub

	}
}
