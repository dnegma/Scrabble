package player;

import game.LetterChain;
import game.Move;

import java.util.ArrayList;
import java.util.List;

import board.Board;
import board.Square;
import dictionary.Node;
import dictionary.Trie;

public abstract class Player {
	public static final int MAX_TILES_ON_HAND = 8;
	public static final int CENTER_INDEX = 7;
	List<Character> tilesOnHand;

	private Board board;
	private int score;
	protected Move nextMove;

	public Player(Board board) {		
		this.board = board;
		this.tilesOnHand = new ArrayList<Character>(MAX_TILES_ON_HAND);
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

		generate(board.getBoard(), false);
		// generate(board.getTransposedBoard(), true);
		return nextMove;
	}

	/**
	 * If the center square is still empty, it means it is the first round.
	 * 
	 * @return
	 */
	private boolean firstRound() {
		return board.getSquare(CENTER_INDEX, CENTER_INDEX).getContent() == Square.CENTER_SQUARE;
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
				if (!square.isAnchor()) {
					limit = limit + 1;
				} else {
					LetterChain wn = new LetterChain(null, '.');
					if (square.getNextLeft(transposed).containsLetter()) {
						String word = findWordToLeft(square
								.getNextLeft(transposed), transposed);
						Node startNode = Trie.getNodeForWord(word);

						extendRight(word, word, wn, startNode, square, square,
								transposed);
					} else {
						leftPart("", wn, Trie.getRootNode(), limit, square,
								square, transposed);
					}
					limit = 0;
				}
			}
			limit = 0;
		}
	}

	private String findWordToLeft(Square square, boolean transposed) {
		String word = "";
		Square sq = square;
		while (sq != null && sq.containsLetter()) {
			word = sq.getContent() + word;
			sq = sq.getNextLeft(transposed);
		}
		return word;
	}

	/**
	 * Find left part of anchor square and extend to right from each anchor.
	 * 
	 * @param partWord
	 * @param node
	 * @param limit
	 * @param anchor
	 */
	public void leftPart(String partWord, LetterChain lc, Node node, int limit,
			Square square, Square anchor,
			boolean transposed) {
		// Square anchor = null;
		extendRight("", partWord, lc, node, anchor, anchor, transposed);
		if (limit > 0) {
			for (Node nextNode : node.getChildren().values()) {
				char letter = nextNode.getLetter();
				if (tilesOnHand.contains(letter)) {
					int index = findLetterIndexInTilesOnHand(letter);
					tilesOnHand.remove(index);
					Square toLeft = anchor.getNextLeft(transposed);
					// if (toLeft == null)
					// System.out.println();


					// lc.setSquare(toLeft);
					LetterChain nextLc = new LetterChain(lc, letter);
					leftPart(partWord + letter, nextLc,
							nextNode, limit - 1,
							toLeft, anchor, transposed);
					tilesOnHand.add(letter);
				}
			}
		}
	}

	/**
	 * Extend a word to the right.
	 * 
	 * @param partWord
	 * @param lc
	 * @param node
	 * @param square
	 */
	private void extendRight(String prefix, String partWord, LetterChain lc,
			Node node,
			Square square, Square endSquare,
			boolean transposed) {
		if (square == null)
			return;
		if (!square.containsLetter()) {
			// if (node == null)
			// System.out.println();
			if (node.isEow() && !partWord.equals(prefix)
					&& square.crossCheckContains(node.getLetter())) {
				saveLegalMoveIfBest(partWord, lc, endSquare, transposed);
			}
			for (Node nextNode : node.getChildren().values()) {
				char letter = nextNode.getLetter();
				if (tilesOnHand.contains(letter)
						&& square.getCrossChecks().contains(letter)) {
					int index = findLetterIndexInTilesOnHand(letter);
					tilesOnHand.remove(index);
					Square toRight = square.getNextRight(transposed);
					// if (toRight == null)
					// System.out.println();
					LetterChain nextLc = new LetterChain(lc, letter);
					extendRight(prefix, partWord + letter, nextLc,
							node.getChildren().get(letter),
							toRight, square, transposed);
					tilesOnHand.add(letter);
				}
			}
		} else {
			char letter = square.getContent();
			if (node.getChildren().containsKey(letter)) {
				Square toRight = square.getNextRight(transposed);
				// if (toRight == null)
				// System.out.println();
				LetterChain nextLc = new LetterChain(lc, letter);
				extendRight(prefix, partWord + letter,
						nextLc, node
						.getChildren().get(letter),
						toRight, square, transposed);
			}
		}
	}

	/**
	 * When a legal move is found, save it while continuing searching. Which one
	 * to save depends on the player. Longest word, bonus tiles are two examples
	 * of what could be taken in consideration when deciding which move to keep.
	 * 
	 * @param partWord
	 * @param lc
	 * @param row
	 * @param column
	 */
	public abstract void saveLegalMoveIfBest(String partWord, LetterChain lc,
			Square endSquare, boolean transposed);

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
