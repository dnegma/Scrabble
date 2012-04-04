package game;

import java.util.ArrayList;
import java.util.List;

public class TestPlayer1 extends Player {
	List<Move> moves;

	public TestPlayer1(Board board) {
		super(board);
		initMoves();
	}

	private void initMoves () {
		moves = new ArrayList<Move>();
		moves.add(new Move("HEJ".toCharArray(), 7, 7, Move.VERTICAL));
		moves.add(new Move("ATT".toCharArray(), 7, 7, Move.HORIZONTAL));
		
	}
	@Override
	public Move generateMove() {
		if (moves.isEmpty())
			return null;
		return moves.remove(0);
	}

	@Override
	public void saveLegalMoveIfBest(String partWord, int row, int column) {
		// TODO Auto-generated method stub

	}

}
