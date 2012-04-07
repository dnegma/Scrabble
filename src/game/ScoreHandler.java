package game;

import dictionary.Alphabet;

public class ScoreHandler {
	
	/**
	 * Calculate the score of a certain move, taking only the letters in
	 * consideration.
	 * 
	 * @param move
	 * @return
	 */
	public static int scoreOf(Move move) {
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
	public static int scoreOf(String partWord) {
		char[] word = partWord.toCharArray();
		int score = 0;

		for (Character c : word)
			score = score + Alphabet.getLetterPoint(c);

		return score;
	}
}
