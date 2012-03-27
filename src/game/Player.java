package game;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
	public static final int MAX_TILES_ON_HAND = 8;
	List<Character> tilesOnHand;
	//protected byte[] tilesOnHand;
	
	private static Board board;
	private int score;
	
	public Player(Board board) {		
		this.tilesOnHand = new ArrayList<Character>(MAX_TILES_ON_HAND);
	}
	
	public char[] placeWord() {
		char[] word = generateWord();
		// removeTileFromHand(word);
		getTiles(word.length);
		return word;
	}

	
	public int getNumberOfTilesOnHand() {
		return this.tilesOnHand.size();
	}
	
	public int getNumberOfNewTilesNeeded() {
		return MAX_TILES_ON_HAND - this.tilesOnHand.size();
	}
 
	private boolean removeTileFromHand(char letter) {
		int charIndex = findLetterIndexInTilesOnHand(letter);
		
		if (charIndex < 0)
			return false;
		
		this.tilesOnHand.remove(charIndex);
		return true;
	}

	/**
	 * @param letter
	 * @return
	 */
	private int findLetterIndexInTilesOnHand(char letter) {
		int size = tilesOnHand.size();
		for (int i = 0; i < size; i++) {
			if (letter == tilesOnHand.get(i)) {
				return i;
			}
		}
		return -1;
	}
	
	public void addPointsToScore(int points) {
		this.score = this.score + points;
	}
	/**
	 * @param player
	 * @return
	 */
	public boolean placedAllTiles() {
		return tilesOnHand.size() == 0;
	}
	public abstract Move generateMove();

	/**
	 * Receive and store one new tile to hand.
	 * @param letter
	 */
	public void receiveTile(char letter) {
		tilesOnHand.add(letter);		
	}
	

}
