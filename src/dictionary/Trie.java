package dictionary;

import java.util.HashSet;
import java.util.Hashtable;

public class Trie {

	private static Node rootNode = new Node('#');

	/**
	 * Create a Trie from text file with list of words.
	 * 
	 * @param String
	 *            fileName
	 */
	public static void initTrie(String fileName) {
		long startTime = System.currentTimeMillis();
		HashSet<String> dictionary = RegexDictionary
				.readDictionaryFromFile(fileName);

		for (String word : dictionary) {
			char[] wordarray = word.toUpperCase().toCharArray();
			addWordToTrie(rootNode, wordarray, 0);
		} 
		long endTime = System.currentTimeMillis();
		System.out.println("Trie build time: " + (endTime - startTime)
				+ " milliseconds.");
		System.out.println(dictionary.size() + " words in dictionary "
				+ fileName);
	}

	/**
	 * Add a word to the Trie. Creates new Node objects for letters not found.
	 * 
	 * @param Node currentNode
	 * @param char[] word inserted to trie
	 * @param int letterIndex index to select letter in word
	 */
	private static void addWordToTrie(Node currentNode, char[] word,
			int letterIndex) {

		if (letterIndex >= word.length) {
			currentNode.setEow();
			return;
		}

		char letter = word[letterIndex];
		Node child;

		if (currentNode.getChildren().containsKey(letter)) {
			child = currentNode.getChildren().get(letter);
		} else {			
			child = currentNode.addChild(letter);			
		}
		addWordToTrie(child, word, letterIndex + 1);
	}

	/**
	 * Search for a word string in the trie.
	 * 
	 * @param word
	 *            search string
	 * @return boolean true if found
	 */
	public static boolean findWord(String word) {
		return findWordRecursively(rootNode, word, 0);
	}

	/**
	 * Help method for recursively search for a word string in the trie.
	 * 
	 * @param currentNode
	 * @param word
	 * @param letterIndex
	 * @return boolean true if found
	 */
	private static boolean findWordRecursively(Node currentNode, String word,
			int letterIndex) {
		// Finished searching through all letters  
		if (letterIndex >= word.length()) {
			if (currentNode.isEow())
				return true;
			else
				return false;
		}

		char letter = word.charAt(letterIndex);

		if (currentNode.getChildren().containsKey(letter)) {	
			// Continue searching for next letter
			Node child = currentNode.getChildren().get(letter);			
			return findWordRecursively(child, word,
					letterIndex + 1);
		} else {
			// Letter not found.
			return false;
		}			
	}

	public static Node getRootNode() {
		return rootNode;
	}

	public static Node getNodeForWord(String word) {
		return getNodeRecursively(rootNode, word.toCharArray(), 0);
	}

	private static Node getNodeRecursively(Node currentNode, char[] word,
			int letterIndex) {
		if (letterIndex >= word.length || currentNode == null)
			return currentNode;


		Hashtable<Character, Node> children = currentNode.getChildren();
		char letter = word[letterIndex];
		Node nextNode = children.get(letter);

		return getNodeRecursively(nextNode, word, letterIndex + 1);
	}
}
