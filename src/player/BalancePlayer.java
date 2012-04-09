package player;

import game.LetterChain;
import game.Move;

import java.util.HashSet;

import board.Board;
import board.Square;

public class BalancePlayer extends Player{

		public static final int VOWELS = 0;
		public static final int PENALTY_FACTOR = 2;
		public static final float RATIO = VOWELS / (float) Player.MAX_TILES_ON_HAND;

		private HashSet<Character> vowels = new HashSet<Character>();
		private Move nextMove;
		private float ratioDifference;
		
		public BalancePlayer(Board board) {
			super(board);
			initVowels();
			resetParameters();
		}

		private void initVowels() {
			char[] vowelChars = new char[] { 'A', 'E', 'I', 'O', 'U', 'Y', 'Å',
					'Ä', 'Ö' };
			for (char c : vowelChars) {
				vowels.add(c);
			}
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

			int nrVowels = 0;
			int nrConsonants = 0;

			for (char c : tilesOnHand) {
				if (vowels.contains(c))
					nrVowels = nrVowels + 1;
				else
					nrConsonants = nrConsonants + 1;
			}

			float ratio = (float) nrVowels / (float) tilesOnHand.size();
			
			float difference = Math.abs(RATIO - ratio);
			
			Move move = new Move(partWord.toCharArray(), lc, endSquare, transposed);
			if (difference < ratioDifference) {
				setNextMove(move);
				ratioDifference = difference;
			}

		}

		@Override
		public void resetParameters() {
			this.ratioDifference = Integer.MAX_VALUE;
		}

		@Override
		public String getName() {
			String className = this.getClass().getSimpleName();
			String ratio = VOWELS + "of"
					+ Player.MAX_TILES_ON_HAND;
			return className + "_" + ratio + "ratio_" + PENALTY_FACTOR + "penalty";
		}

	}


