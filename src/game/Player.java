package game;

public abstract class Player {
	private static final int NUMBER_TILES_ON_HAND = 8;
	protected byte[] tilesOnHand;
	
	int score;
	
	public Player() {
		tilesOnHand = new byte[NUMBER_TILES_ON_HAND];

	}
	
	public char[] placeWord() {
		char[] word = generateWord();
		removeTilesFromHand(word);
		getTiles(word.length);
		return word;
	}
	
	private boolean getTiles(int newTiles) {
		return false;
	}
	
	private boolean removeTilesFromHand(char[] tiles) {
		return false;
	}
	
	public abstract Move generateMove();
	public abstract char[] generateWord();

}
