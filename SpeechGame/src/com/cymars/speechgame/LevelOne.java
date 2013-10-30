package com.cymars.speechgame;
//
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.speech.RecognizerIntent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class LevelOne extends Activity implements View.OnClickListener {
	protected static final int RESULT_SPEECH = 1;

    private LinkedList<String> wordList = new LinkedList<String>();
    private LinkedList<Button> listOfButtons = new LinkedList<Button>();
    private LinkedList<Button> selectedButtons = new LinkedList<Button>();
    private TextView voiceInput, score, wordsLeftList, words, numberOfWordsLeftToFind;
	
    private int scores = 0;
	private String input,word;
	private Random random = new Random();
    private TextImporter textImporter;
    
	@Override	
	protected void onCreate(Bundle savedInstanceState) 
	{
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
	
	private void isComplete(int left)
	{
		if(left == 0)
		{
		 try { 
			// We need to get the instance of the LayoutInflater 
			LayoutInflater inflater = (LayoutInflater) LevelOne.this 
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
		Intent goToNextActivity = new Intent(getApplicationContext(), LevelTwo.class);
		startActivity(goToNextActivity);
	}
	
	private void backMain()
	{
		Intent inten = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(inten);
	}
	
	private void setTextImporter()
	{
        try
        {
            textImporter= new TextImporter("words_four_letters.txt", getApplicationContext());
            numberOfWordsLeftToFind.setText("" + textImporter.getNumberOfWordsInList());
        }
        catch (IOException e) {
             Toast.makeText(getApplicationContext(), "Invalid file", Toast.LENGTH_SHORT);
             e.printStackTrace(); 
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
	}

	private void setGrid()
	{
		LinearLayout layout = (LinearLayout) findViewById(R.id.grid);
		layout.setOrientation(LinearLayout.VERTICAL);
		textImporter.makeRandomisedCharArray(16);
        LinkedList<String> lettersForGrid = textImporter.getCharList();
		for(int i = 0; i < 4; i ++)
		{
			LinearLayout row = new LinearLayout(this);
			row.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			for (int j = 0; j < 4; j++) {
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
        for(String leftWord: wordList)
        {  
        	wordsForList += leftWord + "  ";
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
            b.setBackgroundColor(Color.WHITE);
            i++;
        }
    }
    
    private void writeFileToInternalStorage() {
    	  String eol = System.getProperty("line.separator");
    	  BufferedWriter writer = null;
    	  try {
    	    writer = 
    	      new BufferedWriter(new OutputStreamWriter(openFileOutput("myfile", MODE_WORLD_WRITEABLE)));
    	    writer.write("You got Achievement 1!" + eol);
    	  } catch (Exception e) {
    	      e.printStackTrace();
    	  } finally {
    	    if (writer != null) {
    	    try {
    	      writer.close();
    	    } catch (IOException e) {
    	      e.printStackTrace();
    	    }
    	    }
    	  }
    	} 
    
    private void resetButtonText()
    {
        textImporter.makeRandomisedCharArray(selectedButtons.size());
        LinkedList<String> newLetters = textImporter.getCharList();
        for(Button b:selectedButtons)
        {
            b.setBackgroundColor(Color.WHITE);
            b.setClickable(true);
        }
        while(!selectedButtons.isEmpty())
        {
            Button btn = selectedButtons.pollFirst();
            btn.setText(newLetters.pollFirst());
        }
        randomiseGrid();
    }
    
    private void addScore(int value)
    {
       scores += value;
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
				
				if(text.get(0) !=null)
			    {
					input = text.get(0).toUpperCase();
				}
				
				voiceInput.setText(input);
				if(wordList.contains(word))
	            {
                    String wordToCheck = word;
	                if(wordToCheck.equals(input))
                    {
	                	writeFileToInternalStorage();
	                	textImporter.removeWord(word);
	                    resetButtonText();
                        String integer = (String) numberOfWordsLeftToFind.getText();
                        int i = Integer.parseInt(integer);
                        i--;
                        numberOfWordsLeftToFind.setText("" + i);
                        addScore(10);
                        score.setText("Score: " + scores);
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

	@Override
	public void onClick(View v) {
		words = (TextView) findViewById(R.id.words);
        Button b = (Button) v;
        if(listOfButtons.contains(b))
        {
            setGridButton(b);
        }
	}
	
	private void clearText()
	{
		word = "";
        words.setText("");
        input = "";
        voiceInput.setText("");
	}

	 private void clearButton() 
	 {  
		 clearText();
            for(Button button: listOfButtons)
            {
	            button.setBackgroundColor(Color.WHITE);
	            button.setClickable(true);
            }
            selectedButtons.clear();
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
	            b.setBackgroundColor(1);
	            last = selectedButtons.peekLast();
	            
	            if(last!=null)
	            {
	               last.setBackgroundColor(Color.BLUE);
	               last.setClickable(true);
	            }
	        }
	    }

}
