package player;

import game.LetterChain;
import game.Move;
import board.Board;
import board.Square;

public class BalanceOnRackPlayer extends Player {

	private Move nextMove;
	public BalanceOnRackPlayer(Board board) {
		super(board);
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
	public int getHighestBonus() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setHighestBonus(int bonus) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveLegalMoveIfBest(String partWord, LetterChain lc,
			Square endSquare, boolean transposed) {

	}

}
