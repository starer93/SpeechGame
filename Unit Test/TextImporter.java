import java.io.*;
import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Runaes
 * Date: 19/09/13
 * Time: 5:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class TextImporter {
    LinkedList<String> wordList =  new LinkedList<String>();
    LinkedList<String> wordsInGrid = new LinkedList<String>();
    LinkedList<String> charList = new LinkedList<String>();
    String leftOverWord;
    String leftOverChars = "";
    int wordsInList;

    public TextImporter(String directory) throws IOException {
        WordList (directory);
    }
    
    public void removeWord(String word)
    {
    	wordsInGrid.remove(word);
    }

    private void WordList (String directory) throws IOException 
    {
        BufferedReader br = null;
        InputStream fis;
        String line = "";
        try 
        {
			br = new BufferedReader(new FileReader(directory));
 
			while ((line = br.readLine()) != null) {
				line = line.toUpperCase();
                 wordList.add(line);
			}
 
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
        wordsInList = wordList.size();
    }

    public void makeCharArray(int lettersToGet) {
        LinkedList<String>wordToMakeArray = new LinkedList<String>();
        Random random = new Random();
        boolean open = true;
        for(int i = 0; i < lettersToGet; i++)
        {
            if(leftOverChars.length() > 0)
            {
                for(int j = 0; j < leftOverChars.length(); j++, i++)
                    wordToMakeArray.add("" + leftOverChars.charAt(j));
                leftOverChars = "";
                wordsInGrid.add(leftOverWord);
            }
            String word = "";
            if(!wordList.isEmpty())
                word = wordList.pollFirst();
            else
            {
				for(int j = 0; j < (lettersToGet - i); j ++)
				{
					int x = random.nextInt(25);
					char y = (char) ((int)'A' + x);
                        word += y;
				}
				i = lettersToGet + 1;
            }
            for(int j = 0; j < word.length(); j++, i++)
            {
                 wordToMakeArray.add("" + word.charAt(j));
                if(i == lettersToGet)
                {
                    leftOverWord = word;
                    for(; j < word.length(); j++)
                    {
                        leftOverChars += word.charAt(j);
                    }
                    open = false;
                    break;
                }
            }
            i--;
            if(open)
            wordsInGrid.add(word);
            word = "";

        }
        for(int i = 0; i < wordToMakeArray.size(); i++)
           
            charList.add(String.valueOf(wordToMakeArray.get(i)));
    
    }

    public LinkedList<String> getCharList()
    {
        return charList;
    }

    public LinkedList<String > getWordsInGrid()
    {
        return wordsInGrid;
    }

    public int getNumberOfWordsInList()
    {
        return wordsInList;
    }
}
