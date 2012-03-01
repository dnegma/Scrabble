import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for importing words from text file into a dictionary. The file is read
 * line by line, and the basic form of each word is saved into a hashset
 * representing the dictionary.
 * 
 * @author Diana Gren, Frej Connolly.
 * 
 */
public class RegexDictionary {

	HashSet<String> dictionary;
	
	public RegexDictionary(String fileName) {		
		dictionary = new HashSet<String>(readDictionaryFromFile(fileName));
		printDictionary();
		System.out.println("Size of dictionary: " + dictionary.size()
				+ " words.");
		
	}

	/**
	 * Read file and import words into hashset.
	 * 
	 * @param fileName Name of file to import.
	 * @return Hashset containing all words.
	 */
	private HashSet<String> readDictionaryFromFile(String fileName) {
		HashSet<String> wordList = new HashSet<String>();
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(fileName));
			String textLine;
			while((textLine = br.readLine()) != null) {				
				Pattern regexLineStartsWithInteger = Pattern
						.compile("\\d+r\\d+<\\w+>\\w+:(\\w*\\W*)*");
				Matcher lineWithWord = regexLineStartsWithInteger.matcher(textLine);
				
				if (lineWithWord.matches()) {
					String word = extractWordFromStringLine(textLine);
					if (word != null) {
						wordList.add(word);
					}
				}								
			}
		} catch (IOException e){
			System.err.println("File not found " + e);		
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return wordList;
	}
	
	/**
	 * Extract a word in basic form from an input line received when reading the
	 * file.
	 * 
	 * @param stringLine Line to extract word from.
	 * @return Extracted word.
	 */
	private String extractWordFromStringLine(String stringLine) {
		String[] lineSplit = stringLine.split("[<|>|:]");
		if (!lineSplit[1].equals("egennamn")) {
			if (!lineSplit[2].equals(""))
				return lineSplit[2];
		}
		return null; 
	}
	
	public void printDictionary() {
		StringBuilder output = new StringBuilder();
		for (String word : dictionary) {
			output.append(word + "\n");
		}
		System.out.println(output.toString());
	}
	
	public static void main(String[] args) {
		new RegexDictionary("dictionary/dsso-1.52_utf8.txt");
	}
}
