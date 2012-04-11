package dictionary;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Hashtable;

public class Trie {
	private static int dictionarySize;

	private static Node rootNode = new Node('#');

	/**
	 * Create a Trie from text file with list of words.
	 * 
	 * @param String fileName
	 */
	public static void initTrie(String fileName) {
		long startTime = System.currentTimeMillis();
		HashSet<String> dictionary = RegexDictionary
				.readDictionaryFromFile(fileName);
		long endTime = System.currentTimeMillis();
		System.out.println("Regex run time: " + (endTime - startTime)
				+ " milliseconds.");
		
		// Find out the mean length of words
		int totalWordLength = 0;
		int maxWordLength = 0;
		String maxWord = "";
		int minWordLength = 100;
		String minWord = "";
		int nrTwoWords = 0;
		for (String word : dictionary) {
			totalWordLength = totalWordLength + word.length();
			if (word.length() > maxWordLength) {
				maxWordLength = word.length();
				maxWord = word;
			}
			if (word.length() < minWordLength && word.length() >= 2) {
				minWordLength = word.length();
				minWord = word;
			}
			if (word.length() == 2)
				nrTwoWords = nrTwoWords + 1;

		}
		System.out.println(totalWordLength + " total length");
		int dicSize = dictionary.size();
		System.out.println(dictionary.size() + " words");
		float meanLength = (float) totalWordLength / (float) dicSize;
		System.out.println(meanLength + "  mean length value of word");
		System.out.println(maxWordLength + " max word length.");
		System.out.println(maxWord + " max word.");
		System.out.println(minWordLength + " min word length.");
		System.out.println(minWord + " min word");
		System.out.println(nrTwoWords + " nr two letter words");

		addWordsToTrie(dictionary);
	}

	public static void initTrie(HashSet<String> dictionary) {
		addWordsToTrie(dictionary);
	}

	private static void addWordsToTrie(HashSet<String> dictionary) {
		long startTime = System.currentTimeMillis();
		for (String word : dictionary) {
			char[] wordarray = word.toUpperCase().toCharArray();
			addWordToTrie(rootNode, wordarray, 0);
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Trie build time: " + (endTime - startTime)
				+ " milliseconds.");
		dictionarySize = dictionary.size();
		System.out.println(dictionarySize + " words in dictionary.");
	}

	/**
	 * Serialize the dictionary saved as HashSet to hard disk.
	 * 
	 * @param fileName
	 */
	public static void saveDictionaryToDisk(String fileName) {

		HashSet<String> dictionary = RegexDictionary
				.readDictionaryFromFile(fileName);
		try {
			FileOutputStream fileOut = new FileOutputStream(fileName + ".ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(dictionary);
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
		System.out.println("Saved dictionary to disk. " + dictionary.size()
				+ " words in dictionary "
				+ fileName);
	}

	public static HashSet<String> loadDictionaryFromDisk(String fileName) {
		HashSet<String> dictionary = new HashSet<String>();
		try {
			FileInputStream fileIn = new FileInputStream(fileName + ".ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			dictionary = (HashSet<String>) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException i) {
			i.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return dictionary;
	}

	/**
	 * Add a word to the Trie. Creates new Node objects for letters not found.
	 * 
	 * @param Node
	 *            currentNode
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

	public static int getDictionarySize() {
		return dictionarySize;
	}
}
