import java.util.Hashtable;


public class Node {
	Hashtable<Character, Node> children;
	boolean eow;
	
	public Node () {
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
	
	public Node addChild(Character letter) {
		Node child = new Node(); 
		children.put(letter, child);
		return child;
	}
	
	
	
	
}
