import java.util.HashSet;


public class Dawg {
	
	private Node rootNode;
	public Dawg() {
		rootNode = new Node();
	}
	
	public void initDawg() {
		HashSet<String> dictionary = RegexDictionary.readDictionaryFromFile("dictionary/dsso-1.52_utf8.txt");
//		String[] dictionary = new String[]{"car", "cars", "cat", "cats", "do", "dog", "dogs", "done", "ear", "ears", "eat", "eats"};
		for (String word : dictionary) {
			char[] wordarray = word.toCharArray();
			addWordToDawg(rootNode, wordarray, 0);
		}
		System.out.println(findWord("abortör"));
	}
	
	private void addWordToDawg(Node currentNode, char[] word, int letterIndex) {
		
		if (letterIndex >= word.length) {
			currentNode.setEow();
//			System.out.println("eow");
			return;
		}
		
		char letter = word[letterIndex];
		Node child;
		if (currentNode.getChildren().containsKey(letter)) {
			child = currentNode.getChildren().get(letter);	
//			System.out.println(letter + " found. Following.");
		} else {
			child = currentNode.addChild(letter);			
//			System.out.println(letter + " added.");
		}
		addWordToDawg(child, word, letterIndex + 1);
//		System.out.println("Next!");
	}

	public boolean findWord(String word) {
		char[] wordArray = word.toCharArray();
		return findWordRecursively(rootNode, wordArray, 0);
	}
	
	private boolean findWordRecursively(Node currentNode, char[] word, int letterIndex) {
		if (letterIndex >= word.length) {
			return true;
		}
		
		char letter = word[letterIndex];		
		
		if (currentNode.getChildren().containsKey(letter)) {			
			Node child = currentNode.getChildren().get(letter);			
			return findWordRecursively(child, word, letterIndex + 1);			
		} else {
			return false;
//			if (letterIndex >=)
		}
		
		
		
	}
	
	public static void main (String[] args){
		new Dawg().initDawg();		
	}
}
