package com.cymars.speechgame;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LevelTwo extends Activity implements OnClickListener {

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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level_one);
		init();
		setTextImporter();
		setGrid();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.level_two, menu);
		return true;
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
            textImporter= new TextImporter("words_level_two.txt", getApplicationContext());
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
		textImporter.makeCharArrayInLine();
        LinkedList<String> lettersForGrid = textImporter.getCharList();
		for(int i = 0; i < 5; i ++)
		{
			LinearLayout row = new LinearLayout(this);
			row.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			for (int j = 0; j < 10; j++) {
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
                        String integer = (String) numberOfWordsLeftToFind.getText();
                        int i = Integer.parseInt(integer);
                        i--;
                        numberOfWordsLeftToFind.setText("" + i);
                        addScore(10);
                        score.setText("Score: " + scores);
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
