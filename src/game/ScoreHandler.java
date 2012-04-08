package game;

import player.Player;
import board.Square;
import dictionary.Alphabet;

public class ScoreHandler {

	/**
	 * Calculate the score of a certain move, taking bonus in consideration.
	 * 
	 * @param move
	 * @return
	 */
	public static int scoreOf(Move move) {
		Square square = move.getEndSquare();

		int wordBonus = 1;
		int usedTiles = 0;
		int adjWordScores = 0;
		int score = 0;
		LetterChain lc = move.getLetterChain();
		while (lc.getPrevious() != null) {
			char letter = lc.getLetter();
			int letterScore = getLetterScore(square.getContent(), letter);
			if (lc.isOnRack()) {
				usedTiles++;
				int squareWordBonus = getWordBonus(square.getContent(), letter);
				boolean transposed = move.isTransposed();

				String wordUp = square.verticalWord(square
						.getNextUp(transposed), false, transposed);
				String wordDown = square.verticalWord(square
						.getNextDown(transposed), true, transposed);

				int scoreOfAdjacentWord = scoreOf(wordUp) + scoreOf(wordDown);

				if (scoreOfAdjacentWord > 0) {
					adjWordScores = adjWordScores
							+ (letterScore + scoreOfAdjacentWord)
							* squareWordBonus;
				}
				wordBonus = wordBonus * squareWordBonus;
			}
			score = score + letterScore;
			lc = lc.getPrevious();
			square = square.getNextLeft(move.isTransposed());
		}
		score = score * wordBonus + adjWordScores;

		if (usedTiles == Player.MAX_TILES_ON_HAND)
			score = score + 50;
		return score;
	}

	/**
	 * Calculate score of a word where each letter has a certain score level.
	 * 
	 * @param word
	 * @return
	 */
	public static int scoreOf(String word) {
		char[] wordArray = word.toCharArray();
		return scoreOf(wordArray);
	}

	public static int scoreOf(char[] word) {
		int score = 0;
		for (Character c : word)
			score = score + Alphabet.getLetterPoint(c);

		return score;
	}

	public static int getWordBonus(char squareInfo, char letter) {
		switch (squareInfo) {
		case Square.THREE_WORD_BONUS:
			return 3;
		case Square.TWO_WORD_BONUS:
			return 2;
		default:
			return 1;
		}
	}

	public static int getLetterScore(char squareInfo, char letter) {
		int letterScore = Alphabet.getLetterPoint(letter);
		switch (squareInfo) {
		case Square.THREE_LETTER_BONUS:
			return letterScore * 3;
		case Square.TWO_LETTER_BONUS:
			return letterScore * 2;
		default:
			return letterScore;
		}
	}
}
