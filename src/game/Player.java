package game;

import java.util.ArrayList;
import java.util.List;

import dictionary.Dawg;
import dictionary.Node;

public abstract class Player {
	public static final int MAX_TILES_ON_HAND = 8;
	public static final int CENTER_INDEX = 7;
	List<Character> tilesOnHand;
	//protected byte[] tilesOnHand;
	
	private Board board;
	private int score;
	protected Move nextMove;

	public Player(Board board) {		
		this.board = board;
		this.tilesOnHand = new ArrayList<Character>(MAX_TILES_ON_HAND);
	}
	
	public char[] placeWord() {
//		char[] word = generateWord();
		// removeTileFromHand(word);
//		getTiles(word.length);
		return null;
	}

	/**
	 * Generate move. Limit is set by how many non-anchor squares there are to
	 * the left of the anchor, this is the maxmimum word we can find. As soon as
	 * we find the anchor, we start searching for left parts.
	 * 
	 * @return
	 */
	public Move generateMove() {
		nextMove = null;
		
		if (firstRound()) {
			leftPart("", Dawg.getRootNode(), CENTER_INDEX,
					board.getSquare(CENTER_INDEX, CENTER_INDEX), false);
		} else {
			generate(board.getBoard(), false);
			generate(board.getTransposedBoard(), true);
		}
		return nextMove;
	}

	/**
	 * If the center square is still empty, it means it is the first round.
	 * 
	 * @return
	 */
	private boolean firstRound() {
		return !board.getSquare(7, 7).containsLetter();
	}

	/**
	 * Extracted method for generating moves depending on how the board is
	 * located. WE want to be able to find words vertically as well as
	 * horizontally. Therefore, we make a search with the normal board and
	 * another one with the board transposed.
	 * 
	 * @param board
	 * @param transposed
	 */
	private void generate(Square[][] board, boolean transposed) {
		int limit = 0;
		for (int row = 0; row < Board.BOARD_SIZE; row++) {
			for (int column = 0; column < Board.BOARD_SIZE; column++) {
				Square square = board[row][column];
				if (!square.containsLetter()) {
					limit = limit + 1;
				} else {
					if (column > 0) {
						Square toLeft = board[row][column]
								.getNextLeft(transposed);
						leftPart("", Dawg.getRootNode(), limit, toLeft,
								transposed);
					}
				}
			}
			limit = 0;
		}
	}
	/**
	 * Find left part of anchor square and extend to right from each anchor.
	 * 
	 * @param partWord
	 * @param node
	 * @param limit
	 * @param anchor
	 */
	public void leftPart(String partWord, Node node, int limit, Square anchor,
			boolean transposed) {
		// Square anchor = null;
		extendRight(partWord, node, anchor, transposed);
		if (limit > 0) {
			for (Node nextNode : node.getChildren().values()) {
				char letter = nextNode.getLetter();
				if (tilesOnHand.contains(letter)) {
					int index = findLetterIndexInTilesOnHand(letter);
					tilesOnHand.remove(index);
					Square toLeft = anchor.getNextLeft(transposed);
					leftPart(partWord + letter, nextNode, limit - 1,
							toLeft, transposed);
					tilesOnHand.add(letter);
				}
			}
		}
	}

	/**
	 * Extend a word to the right.
	 * 
	 * @param partWord
	 * @param node
	 * @param square
	 */
	private void extendRight(String partWord, Node node, Square square,
			boolean transposed) {
		if (!square.containsLetter()) {
			if (node.isEow()) {
				saveLegalMoveIfBest(partWord,
						square.getRow(), square.getColumn());
			}
			for (Node nextNode : node.getChildren().values()) {
				char letter = nextNode.getLetter();
				if (tilesOnHand.contains(letter)
						&& square.getCrossChecks().contains(letter)) {
					int index = findLetterIndexInTilesOnHand(letter);
					tilesOnHand.remove(index);
					Square toRight = square.getNextRight(transposed);
					extendRight(partWord + letter, nextNode,
							toRight, transposed);
					tilesOnHand.add(letter);
				}
			}
		} else {
			char letter = square.getContent();
			if (node.getChildren().containsKey(letter)) {
				Square toRight = square.getNextRight(transposed);
				extendRight(partWord + letter, node.getChildren().get(letter),
						toRight, transposed);
			}
		}
	}

	/**
	 * When a legal move is found, save it while continuing searching. Which one
	 * to save depends on the player. Longest word, bonus tiles are two examples
	 * of what could be taken in consideration when deciding which move to keep.
	 * 
	 * @param partWord
	 * @param row
	 * @param column
	 */
	public abstract void saveLegalMoveIfBest(String partWord, int row,
			int column);

	/**
	 * Number on tiles the player has in the rack.
	 * 
	 * @return
	 */
	public int getNumberOfTilesOnHand() {
		return this.tilesOnHand.size();
	}
	
	/**
	 * How many tiles the player is missing and should pick up for the next
	 * round.
	 * 
	 * @return
	 */
	public int getNumberOfNewTilesNeeded() {
		return MAX_TILES_ON_HAND - this.tilesOnHand.size();
	}
 
	/**
	 * Remove tile from rack.
	 * 
	 * @param letter
	 * @return
	 */
	public boolean removeTileFromHand(char letter) {
		int charIndex = findLetterIndexInTilesOnHand(letter);
		
		if (charIndex < 0)
			return false;
		
		this.tilesOnHand.remove(charIndex);
		return true;
	}

	/**
	 * Find the index of a letter on the rack (list). This to be able to remove
	 * the letter from the list with tha found index.
	 * 
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
	
	/**
	 * Add points to our score.
	 * 
	 * @param points
	 */
	public void addPointsToScore(int points) {
		this.score = this.score + points;
	}

	/**
	 * If the player is out of tiles on the rack it means he played them all.
	 * That qualifies for an extra bonus.
	 * 
	 * @return
	 */
	public boolean placedAllTiles() {
		return tilesOnHand.size() == 0;
	}


	/**
	 * Receive tile and add to rack.
	 * 
	 * @param letter
	 */
	public void receiveTile(char letter) {
		tilesOnHand.add(letter);		
	}

}
