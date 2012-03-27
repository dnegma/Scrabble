package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class Game {
	private static final String GAME_LANGUAGE = "swedish";
	
	private static Board board;
	private static List<Character> tilesInBag;
	
	private static Player player1;	
	private static Player player2;
	
	private int nrOfPasses;
	
	public Game() {
//		player1 = new Player();
//		player2 = new Player();
		board = new Board();
		Alphabet.initializeAlphabet(GAME_LANGUAGE);
		this.tilesInBag = initTileBag();
		this.nrOfPasses = 0;
		
	}
	
	private List<Character> initTileBag() {
		List<Character> tilesInBag = new ArrayList<Character>();
		for (char letter : Alphabet.letterAmounts.keySet()) {
			int amount = Alphabet.getLetterAmount(letter);
			for (int i = 0 ; i < amount; i++) {
				tilesInBag.add(letter);
			}
		}
		Collections.shuffle(tilesInBag);
		return tilesInBag;
	}

	public void play() {
		
	}

	public boolean gameOver() {
		if (tilesInBag.isEmpty() && player1.getNrOfPasses() >= 2 || )
	}
	
	/**
	 * Place a word on the board
	 * @param move
	 * @return
	 */
	public boolean makeMove(Move move, Player player) {

		// Get word and where to place on board 
		char[] tilesToPlace = move.getWord();		
		int startRow = move.getStartRow();
		int startColumn = move.getStartColumn();	
		int endRow = move.getEndRow();
		int endColumn = move.getEndColumn();
		
		// count points
		int wordPoints = 0;
		int wordBonus = 1;		
		
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
		if (player.placedAllTiles())
			wordPoints = wordPoints + 50;

		// add score to player
		player.addPointsToScore(wordPoints);
		
		return true;
	}

	public void giveTilesToPlayer(Player player) {
		int nrOfTilesNeeded = player.getNumberOfNewTilesNeeded();
		int tilesLeftInBag = tilesInBag.size();
		
		for (int i = 0; (i < tilesLeftInBag) && (i < nrOfTilesNeeded); i++) {
			giveOneTileToPlayerAndRemoveItFromBag(player);
		}		
	}
	
	
	private void giveOneTileToPlayerAndRemoveItFromBag(Player player) {
		char letter = tilesInBag.remove(0);
		player.receiveTile(letter);
	}

	public static void main(String[] args) {
		new Game();
	}
	
	public void incrementPass() {
		this.nrOfPasses = nrOfPasses + 1;
	}
	
	public int getNrOfPasses() {
		return this.nrOfPasses;
	}
}
