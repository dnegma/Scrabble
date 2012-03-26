package game;

public abstract class Player {
	private static final int NUMBER_TILES_ON_HAND = 8;
	protected byte[] tilesOnHand;
	
	int score;
	private static Board board;
	
	public Player(Board board) {
		
		tilesOnHand = new byte[NUMBER_TILES_ON_HAND];
	}
	
	public char[] placeWord() {
		char[] word = generateWord();
		// removeTileFromHand(word);
		getTiles(word.length);
		return word;
	}
	
	private boolean getNewTiles(int nrOfTilesToGet) {
		return false;
	}
	
	private boolean removeTileFromHand(char tile) {
		int size = tilesOnHand.length;
		int charIndex = findCharIndexInTilesOnHand(tile);
		
		if (charIndex < 0)
			return false;
		
		byte[] newHand = new byte[size-1];
		System.arraycopy(tilesOnHand, 0, newHand, 0, charIndex);
		System.arraycopy(tilesOnHand, charIndex+1, newHand, charIndex+1, newHand.length-charIndex);
		tilesOnHand = newHand;
		return true;
	}

	/**
	 * @param tile
	 * @return
	 */
	private int findCharIndexInTilesOnHand(char tile) {
		int size = tilesOnHand.length;
		for (int i = 0; i < size; i++) {
			if (tile == (char) tilesOnHand[i]) {
				return i;
			}
		}
		return -1;
	}
	
	public abstract Move generateMove();
}
