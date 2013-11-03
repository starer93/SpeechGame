package com.cymars.speechgame;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.speech.RecognizerIntent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class LevelTwo extends Activity implements OnClickListener{

	protected static final int RESULT_SPEECH = 1;
	private static final String RED = "red";
	private static final String GREEN= "green";

    private LinkedList<String> wordList = new LinkedList<String>();
    private LinkedList<Button> listOfButtons = new LinkedList<Button>();
    private LinkedList<Button>  selectedButtons = new LinkedList<Button> ();
    private TextView voiceInput, score, wordsLeftList, words, numberOfWordsLeftToFind;
	
    private int scores = 0;
	private String input,word;
	private Random random = new Random();
    private TextImporter textImporter;
    int first = 0;
	int second = 1;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level_one);
		init();
		setTextImporter();
	    setGrid();
	}
	
	private void init()
	{
		score = (TextView) findViewById(R.id.text_score);
		numberOfWordsLeftToFind = (TextView)findViewById (R.id.num_of_words_left);
        wordsLeftList = (TextView) findViewById(R.id.left_word_list);
        voiceInput = (TextView) findViewById(R.id.input);
        words = (TextView) findViewById(R.id.words);
        
        input = "";
        word = "";
        Button btnSpeak = (Button) findViewById(R.id.speak_button);
        btnSpeak.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				speakTouch();
			}
		});
        
		Button clear_btn = (Button) findViewById(R.id.clear_button);
		clear_btn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
                clearButton();
			}
		});
	}
	
	private void setTextImporter()
	{
        try
        {
            textImporter= new TextImporter("words_three_letters.txt", getApplicationContext());
            numberOfWordsLeftToFind.setText("" + textImporter.getNumberOfWordsInList());
        }
        catch (IOException e) {
        	 String error = e.getCause().toString();
             Toast.makeText(getApplicationContext(), "Invalid file", Toast.LENGTH_SHORT);
             e.printStackTrace(); 
        }
	}
	
	private void getRandom()
	{ 
		Random random = new Random();
		while(listOfButtons.get(first).getHighlightColor() == Color.WHITE)
		{
			listOfButtons.get(first).setTag(GREEN);
			listOfButtons.get(first).setTextColor(Color.GREEN);
			first = random.nextInt(listOfButtons.size());
			break;
		}
		while(listOfButtons.get(first).getHighlightColor() == Color.WHITE)
		{
			listOfButtons.get(first).setTag(RED);
			listOfButtons.get(first).setTextColor(Color.RED);
			first = random.nextInt(listOfButtons.size());;
			break;
		}	
	}
	
	private void speakTouch()
	{
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

		try 
		{
			startActivityForResult(intent, RESULT_SPEECH);
		} 
		catch (ActivityNotFoundException a) 
		{
			Toast t = Toast.makeText(getApplicationContext(),
					"Ops! Your device doesn't support Speech to Text",
					Toast.LENGTH_SHORT);
			t.show();
		}
		for(Button button: listOfButtons)
		{
			button.setBackgroundColor(Color.WHITE);
			button.setHighlightColor(Color.WHITE);
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.level_one, menu);
		return true;
	}

	private void setGrid()
	{
		LinearLayout layout = (LinearLayout) findViewById(R.id.grid);
		layout.setOrientation(LinearLayout.VERTICAL);
		textImporter.makeRandomisedCharArray(100);
        LinkedList<String> lettersForGrid = textImporter.getCharList();
		for(int i = 0; i < 7; i ++)
		{
			LinearLayout row = new LinearLayout(this);
			row.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			for (int j = 0; j < 7; j++) {
				Button btnTag = new Button(this);
				btnTag.setLayoutParams(new LayoutParams(
			    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				btnTag.setText(lettersForGrid.pollFirst());
				btnTag.setId(j + 1 + i);
				btnTag.setOnClickListener(this);
				row.addView(btnTag);
                listOfButtons.add(btnTag);
			}
			layout.addView(row);
		}
	    randomiseGrid();

	}

    private void updateWordList()
    {
        String wordsForList = "";
        int i = 0;
        for(String word: wordList)
        {
            wordsForList += word + " ";
            i++;
            if(i == 7)
            {
                wordsForList += "\n";
                i = 0;
            }
        }
        wordsLeftList.setText(wordsForList);
    }
    private void randomiseGrid()
    {
        LinkedList<String> charsForGrid = new LinkedList<String>();
        wordList = textImporter.getWordsInGrid();
        updateWordList();
        for (Button button : listOfButtons) charsForGrid.add(button.getText().toString());
        LinkedList<String> charList = new LinkedList<String>();
        while(!charsForGrid.isEmpty())
        {
        	if(charsForGrid.isEmpty())
        		break;
            int index = random.nextInt(charsForGrid.size());
            if(index != 0)
            	index--;
            charList.add(String.valueOf(charsForGrid.get(index)));
            charsForGrid.remove(index);
        }
        int i = 0;

        while(!charList.isEmpty())
        {
            Button b = listOfButtons.get(i);
            b.setText(charList.pollFirst());
            b.setBackgroundColor(0);
            i++;
        }
    }
    
    private void resetButtonText()
    {
        textImporter.makeRandomisedCharArray(selectedButtons.size());
        LinkedList<String> newLetters = textImporter.getCharList();
        for(Button b:selectedButtons)
        {
            b.setBackgroundColor(0);
            b.setClickable(true);
        }
        while(!selectedButtons.isEmpty())
        {
            Button btn = selectedButtons.pollFirst();
            btn.setText(newLetters.pollFirst());
        }
        randomiseGrid();
    }
    
    public void addScore()
    {
    	scores += 50;
    	for(int i = 0; i < selectedButtons.size(); i++)
    	{			
    		if(selectedButtons.get(i).getTag() == RED)
    		{
    			scores -=5;
    		}
    		else if(selectedButtons.get(i).getTag() == GREEN)
    		{
    			scores += 10;
    		}		
    	}
       score.setText("Score: " + scores);
       
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) 
		{
		   case RESULT_SPEECH: 
		   {
		      if (resultCode == RESULT_OK && null != data) 
		      {
				ArrayList<String> text = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				input = text.get(0).toUpperCase();
				voiceInput = (TextView) findViewById(R.id.input);
				voiceInput.setText(input);
				if(wordList.contains(word))
	            {
                    String wordToCheck = word;
	                if(wordToCheck.equals(input))
                    {
                        addScore();
	                	textImporter.removeWord(word);
	                    resetButtonText();
                        String integer = (String) numberOfWordsLeftToFind.getText();
                        int i = Integer.parseInt(integer);
                        i--;
                        numberOfWordsLeftToFind.setText("" + i);
                        word = "";	
                        updateWordList();
                        clearText();
                        isComplete(i);
                    }
	                else
	                    Toast.makeText(getApplicationContext(), "Did you say that wrong?", Toast.LENGTH_SHORT);
	            }
	            else
	            {
	                Toast.makeText(getApplicationContext(), "That word isn't in the list!", Toast.LENGTH_SHORT);
	            }
			}
			break;
		}

		}
	}
	
	private void isComplete(int left)
	{
		if(left == 0)
		{
			writeFileToInternalStorage();
		 try { 
			// We need to get the instance of the LayoutInflater 
			LayoutInflater inflater = (LayoutInflater) LevelTwo.this 
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
			View layout = inflater.inflate(R.layout.popupwindow,(ViewGroup)

			findViewById(R.id.popup_element)); 
			PopupWindow pwindo = new PopupWindow(layout, 350, 350, true); 
			pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

			Button mainButton = (Button) layout.findViewById(R.id.btn_main);
			mainButton.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v){
					backMain();
				}
			});
			Button nextButton =(Button) layout.findViewById(R.id.button_next);
			nextButton.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
	                nextLevel();
				}
			});
			
		 }
		 catch(Exception e)
		 {
			 
		 }
		}
	}
	
	private void nextLevel()
	{
		Intent goToNextActivity = new Intent(getApplicationContext(), LevelThree.class);
		startActivity(goToNextActivity);
	}
	
	private void backMain()
	{
		Intent inten = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(inten);
	}

	 private void writeFileToInternalStorage() {
		 
		 FileOutputStream outputStream;
		 String filename = "myfile";
		 String string = "2";
		 try {
			 outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
			 outputStream.write(string.getBytes());
			 outputStream.close();
   	    	} 
		 	catch (Exception e) {
		 		e.printStackTrace();
   	    	}
   	} 
	 
	@Override
	public void onClick(View v) {
		words = (TextView) findViewById(R.id.words);
        Button b = (Button) v;
        if(listOfButtons.contains(b))
        {
            setGridButton(b);
        }
        getRandom();
	}
	
	private void clearText()
	{
		word = "";
        words.setText(word);
        input = "";
        voiceInput.setText("");
        for(Button button:listOfButtons)
        {
        	button.setBackgroundColor(0);
        }
	}

	 private void clearButton() 
	 {
		 clearText();
            selectedButtons.clear();
            for(Button button: listOfButtons)
            {
	            button.setTextColor(Color.BLACK);
	            button.setClickable(true);
            }
	    }

	    private void setGridButton(Button b) 
	    {
	        Button last = null;
	        if(!selectedButtons.contains(b))
	        {
	            last = selectedButtons.peekLast();
	            selectedButtons.add(b);
	            b.setBackgroundColor(Color.BLUE);
	            word += b.getText();
	            words.setText(word);
	            if(last != null)
	            {
	            	last.setBackgroundColor(Color.MAGENTA);
	            	last.setClickable(false);
	            }
	        }
	        else
	        {
	        	String replacement = "";
	        	for(int i= 0; i < word.length()-1; i++)
	        		replacement += word.charAt(i);
	            
	        	word = replacement;
	            words.setText(word);
	            selectedButtons.remove(b);
	            b.setBackgroundColor(0);
	            last = selectedButtons.peekLast();
	            
	            if(last!=null)
	            {
	               last.setBackgroundColor(Color.BLUE);
	               last.setClickable(true);
	            }
	        }
	    }
}