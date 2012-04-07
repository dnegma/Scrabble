package dictionary;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import board.Square;

public class Bonus {

	private static HashMap<Square, Character> bonuses = new HashMap<Square, Character>();

	public static void initializeBonus(String bonusSystem) {

		String filename = "bonus/" + bonusSystem + ".txt";

		try {
			bonuses = readBonusesFromFile(filename);
		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException: Could not find file: "
					+ filename);
		} catch (IOException e) {
			System.err.println("IOException: Could not read from file!");	
		}
		/*
		 * for (Square key : bonuses.keySet()) System.out.println(key.getRow() +
		 * " " + key.getColumn() + " = " + bonuses.get(key));
		 * System.out.println("bonusen för (1, 3) är = " + getBonus(1, 3));
		 * System.out.println("bonusen för (0, 0) är = " + getBonus(0, 0));
		 * System.out.println("bonusen för (1, 5) är = " + getBonus(1, 5));
		 */
	}

	/**
	 * Get the bonus character.
	 * 
	 * @param row
	 * @param column
	 * @return char or Square.EMPTY if no bonus was found
	 */
	public static char getBonus(int row, int column) {
		Square sq = new Square(Square.EMPTY, row, column);
		Character bonusChar = bonuses.get(sq);
		if (bonusChar == null) {
			return Square.EMPTY;
		}
		return bonusChar;
	}

	private static HashMap<Square, Character> readBonusesFromFile(
			String filename)
			throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filename));

		Character[] bonusCharacter = new Character[] {
				Square.THREE_WORD_BONUS,
				Square.TWO_WORD_BONUS,
				Square.THREE_LETTER_BONUS,
				Square.TWO_LETTER_BONUS
		};
		int bonusIndex = 0;
		
		HashMap<Square, Character> bonusList = new HashMap<Square, Character>();

		String line;
		while ((line = reader.readLine()) != null) {
			// skip comments
			if (line.startsWith("#"))
				continue;

			// Read all bonus squares in file for one bonus
			int numberOfBonusSquares = Integer.parseInt(line.trim());
			for (int i = 0; i < numberOfBonusSquares; i++) {
				line = reader.readLine();
				String[] lineContents = line.split(" ");

				int row = Integer.parseInt(lineContents[0]);
				int column = Integer.parseInt(lineContents[1]);
				Square sq = new Square(Square.EMPTY, row, column);
				bonusList.put(sq, bonusCharacter[bonusIndex]);
			}
			// use next bonus character
			bonusIndex = bonusIndex + 1;
		}
		return bonusList;
	}
}
