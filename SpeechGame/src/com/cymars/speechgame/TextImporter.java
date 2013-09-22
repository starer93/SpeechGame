package com.cymars.speechgame;

import java.io.*;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.Random;

import android.content.Context;
import android.content.res.AssetManager;

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
    Context context;

    public TextImporter(String directory, Context context) throws IOException {
    	this.context = context;
        WordList (directory);
    }

    private void WordList (String directory) throws IOException {

        InputStream    fis;
        BufferedReader br;
        String         line;

        AssetManager am = context.getAssets();
		 fis = am.open("words.txt");
        br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
        while ((line = br.readLine()) != null) {
            wordList.add(line);
        }
        br.close();
        br = null;
        fis = null;
        wordsInList = wordList.size();
    }

    public void makeCharArray(int lettersToGet) {
        LinkedList<String>wordToMakeArray = new LinkedList<String>();
        Random random = new Random();
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
					char y = (char) ((int)'a' + x);
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
                    break;
                }
            }
            i--;
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

