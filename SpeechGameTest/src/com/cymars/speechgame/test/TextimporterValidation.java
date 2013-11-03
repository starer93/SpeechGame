package com.cymars.speechgame.test;

import com.cymars.speechgame.GridCreator;
import com.cymars.speechgame.MainActivity;
import com.cymars.speechgame.TextImporter;

import junit.framework.TestCase;
import android.test.ActivityInstrumentationTestCase2;

public class TextimporterValidation extends ActivityInstrumentationTestCase2<MainActivity> {

	private final char[] alphabet = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 
			  'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 
			  'U', 'V', 'W', 'X', 'Y', 'Z' };
	
	TextImporter importer;
	GridCreator creator;
	public TextimporterValidation() {
		super("com.cymars.speechgame", MainActivity.class);
	}
// set up unit test
	protected void setUp() throws Exception {
		super.setUp();
		MainActivity mainactivity = getActivity();
		importer = new TextImporter("Test.txt",mainactivity.getApplicationContext());
		creator = new GridCreator(importer.getWordList(), 10);
	}
	
	//make sure number of words in file has all imported to the WordList in TextImporter.java
	public void testWordCount()
	{
       assertEquals(1, importer.getNumberOfWordsInList());//test if the words.txt contains the correct number of words. 
	}
	
	//This tested against to make sure the TextImporter.java contain the right words for the game
	public void testWordInGrid()
	{
		assertEquals(true,importer.getWordList().contains("TEST"));
	}
	//This method tested that the GridCreator.java have the correct number of 
	public void testGridCreator()
	{
		assertEquals(10, creator.getGridLength());
	}
	//make sure flip() is working properly that flip the string around
	public void testFlip()
	{
		assertEquals("ABCDE", creator.flip("EDCBA"));
	}
	//make sure the char array in GirdCreator has only char from  'a' - 'z'
	public void testOutOfIndex()
	{
		
		assertEquals(true, checkLetters(creator.nextChar()));
	}
	
	private boolean checkLetters(char input)
	{
		for(int i = 0; i < alphabet.length; i++)
		{
			if(alphabet[i] == input)
			{
				return true;
			}
		}
		return false;
	}

}
