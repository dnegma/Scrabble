package game;


public class Game {
	private static final String GAME_LANGUAGE = "swedish";
	
	private static Board board;
	
	private static Player player1;
	private int player1score;
	
	private static Player player2;
	private int player2score;
	
	public Game() {
		// player1 = new Player();
		// player2 = new Player();
		board = new Board();
	}
	
	public boolean play() {
		return false;
	}

	/**
	 * Place a word on the board
	 * @param move
	 * @return
	 */
	public boolean makeMove(Move move) {

		// Get word and where to place on board 
		char[] tilesToPlace = move.getWord();		
		int startRow = move.getStartRow();
		int startColumn = move.getStartColumn();
		byte direction = move.getDirection();
		int wordLength = tilesToPlace.length;
		
		// points counters
		int wordPoints = 0;
		int wordBonus = 1;
		
		
		// Decide which direction to place word
		int endRow;
		int endColumn;
		if (direction == Move.HORIZONTAL) {
			endRow = startRow;
			endColumn = startColumn + wordLength;			
		} else {
			// Vertical
			endRow = startRow + wordLength;
			endColumn = startColumn;
		}
		
		// Check if word fits onto board.
		if (board.isOutsideBoardLimits(endRow, endColumn))
			return false;
		
		// Place word on board
		int tile = 0;
		for (int row=startRow; row<=endRow; row++) {
			for (int column=startColumn; column<=endColumn; column++){							
				if (!board.isOccupiedSquare(row, column)) {
					char letter = tilesToPlace[tile];
					byte squareInfo = board.placeTile(letter, row, column);
					switch (squareInfo) {
					case Board.TWO_LETTER_BONUS:
						wordPoints = wordPoints + Alphabet.getLetterPoint(letter) * 2;
						break;
					case Board.THREE_LETTER_BONUS:
						wordPoints = wordPoints + Alphabet.getLetterPoint(letter) * 3;
						break;
					case Board.TWO_WORD_BONUS:
						wordBonus = wordBonus * 2;
						break;
					case Board.THREE_WORD_BONUS:
						wordBonus = wordBonus * 3;
						break;						
					default:
						// no bonus square. just add the letter point
						wordPoints = wordPoints + Alphabet.getLetterPoint(letter);
						break;
					}
				}	
				// get next tile
				tile = tile + 1;
				
			}
		}		
		// Add (possibly) word bonuses
		wordPoints = wordPoints * wordBonus;
		// Add (possibly) 50 points if the player uses all tiles on hand
		
		
		return true;
	}
	
	public static void main(String[] args) {
		new Game();
	}
	
}
