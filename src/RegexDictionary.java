import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.regex.*;


public class RegexDictionary {

	HashSet<String> dictionary;
	
	
	public RegexDictionary(String fileName) {		
		dictionary = readDictionaryFromFile(fileName);	
		
	}

	private HashSet<String> readDictionaryFromFile(String fileName) {
		HashSet<String> wordList = new HashSet<String>();
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(fileName));
			String textLine;
			while((textLine = br.readLine()) != null) {				
				Pattern regexLineStartsWithInteger = Pattern.compile("\\d"); 
				Matcher lineWithWord = regexLineStartsWithInteger.matcher(textLine);
				
				if (lineWithWord.matches()) {
					System.out.println("found a match!");
					String word = extractWordFromStringLine(textLine);
					System.out.println(word);
					if (word != null)
						wordList.add(word);
				}								
			}
		} catch (IOException e){
			System.err.println("File not found " + e);		
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return wordList;
	}
	
	private String extractWordFromStringLine(String stringLine) {
		System.out.println(stringLine);
		String[] lineSplit = stringLine.split("<>:");
		if (!lineSplit[1].equals("egennamn")) {
			if (!lineSplit[3].equals(""))
				return lineSplit[3];
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
