package game;


public class HighScoreWordPlayer extends Player {

	public HighScoreWordPlayer(Board board) {
		super(board);
	}

	@Override
	public void saveLegalMoveIfBest(String partWord, int row, int column, boolean transposed) {
		if (scoreOf(partWord) > scoreOf(nextMove)) {
			byte direction = (transposed) ? Move.VERTICAL : Move.HORIZONTAL;
			nextMove = new Move(partWord.toCharArray(), row, column,
					direction);
		}
	}

	/**
	 * Calculate the score of a certain move, taking only the letters in
	 * consideration.
	 * 
	 * @param move
	 * @return
	 */
	private int scoreOf(Move move) {
		if (move == null)
			return 0;

		char[] word = move.getWord();
		int score = 0;

		for (Character c : word)
			score = score + Alphabet.getLetterPoint(c);

		return score;
	}

	/**
	 * Calculate score of a word where each letter has a certain score level.
	 * 
	 * @param partWord
	 * @return
	 */
	private int scoreOf(String partWord) {
		char[] word = partWord.toCharArray();
		int score = 0;

		for (Character c : word)
			score = score + Alphabet.getLetterPoint(c);

		return score;
	}

}
