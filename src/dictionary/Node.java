package dictionary;
import java.util.Hashtable;


public class Node {
	char letter;
	Hashtable<Character, Node> children;
	boolean eow;
	
	public Node(char letter) {
		this.letter = letter;
		children = new Hashtable<Character, Node>();
	}
	
	public Hashtable<Character, Node> getChildren() {
		return children;
	}
	
	public boolean isEow() {
		return eow;
	}
	
	public void setEow() {
		eow = true;
	}
	
	public char getLetter() {
		return letter;
	}
	public Node addChild(Character letter) {
		Node child = new Node(letter);
		children.put(letter, child);
		return child;
	}
		
}
