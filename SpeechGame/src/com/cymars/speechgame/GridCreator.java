package com.cymars.speechgame;

import java.util.LinkedList;
import java.util.Random;
/**
 * This class stores words and insert them randomly into a grid and make sure every word is in  a straight line 
 * @author YU
 *
 */
public class GridCreator {
	char[][] data;	// Puzzle without random filler characters
	char[][] dataF; // Puzzle with    random filler characters	
	String[] words;
	
	private Random r = new Random();
	
	private final char[] alphabet = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 
									  'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 
									  'U', 'V', 'W', 'X', 'Y', 'Z' };
	
	public GridCreator(LinkedList<String> wordList, int size) {
		data = new char[size][size];   // size X size grid
		
		for(int i=0; i<data.length; i++)  //fill the grid with space 
			for(int j=0; j<data.length; j++)
				data[i][j] = ' ';
		
		for(String word : wordList) { // add all the words in to words;
			add(word, data);
		}
		
		dataF = fill(data);
	}
	
	/**
	 * Randomly select a letter from 'a' to 'z' 
	 * @return alphabet[c]  a random letter
	 */
	private char nextChar() {
		int c = r.nextInt(26);		
		return alphabet[c];
	}
	
	
	public String toString() {
		StringBuilder ret = new StringBuilder();
		
		for(int i=0; i<data.length; i++) {
			for(int j=0; j<data.length; j++) 
			{
				ret.append(dataF[i][j]);
			}
		}
		
		return ret.toString();
	}
	
	public char[][] getGrid()
	{
		return data;
	}
	/**
	 * Adding a words into the grid
	 * @param word
	 * @param puzzle
	 * @return true if add the word successfully else return false
	 */
	private boolean add(String word, char[][] puzzle) {
		word = word.toUpperCase(); // convert every letter to upper case
		
		char[][] origPuzzle = new char[puzzle.length][puzzle.length];
		for(int i=0; i<puzzle.length; i++)
			for(int j=0; j<puzzle[0].length; j++)
				origPuzzle[i][j] = puzzle[i][j];
		
		for(int tries=0; tries<10000; tries++) 
		{  //tries needs to modify
			Random r = new Random();
			
			int orientation = r.nextInt(2); // 0 = Forwards,   1 = Backwards
			if(orientation == 1) word = flip(word);
			
			int direction   = r.nextInt(2); // 0 = Horizontal, 1 = Vertical
			
			int row			= r.nextInt(puzzle.length - word.length());
			int col			= r.nextInt(puzzle.length - word.length());
			
			int i=0;
			for(i=0; i<word.length(); i++) 
			{
				if(puzzle[row][col] == ' ' || puzzle[row][col] == word.charAt(i)) 
				{
					puzzle[row][col] = word.charAt(i);
					
					if(direction == 0) col++;
					if(direction == 1) row++;
				} 
				else 
				{
					for(int j=i; j>0; j--) 
					{
						if(direction == 0) col--;
						if(direction == 1) row--;			
						puzzle[row][col] = origPuzzle[row][col]; 
					}
					break;
				}
			}
			if(--i > 0) return true;
		}
		return false;
	}
	
	/**
	 * flip the word. e.g "apple" -> "elppa"
	 * @param in
	 * @return
	 */
	private String flip(String in) {
		StringBuilder ret = new StringBuilder();
		for(int i=in.length()-1; i>=0; i--)
			ret.append(in.charAt(i));
		return ret.toString();
	}
		
	/**
	 * Fill the grid.
	 * @param puzzle
	 * @return the puzzle
	 */
	private char[][] fill(char[][] puzzle) {
		char[][] ret = new char[puzzle.length][puzzle.length];
		
		for(int i=0; i<ret.length; i++) {
			for(int j=0; j<ret.length; j++) {
				if(puzzle[i][j] != ' ') {
					ret[i][j] = puzzle[i][j];
				} else {
					ret[i][j] = nextChar();
				}
			}
		}
		return ret;
	}
	
	public void cloneGrid(char[][] grid)
	{
		grid = new char[data.length][];
		for(int i = 0; i < data.length; i++)
		grid[i] = data[i].clone();
	}


}
