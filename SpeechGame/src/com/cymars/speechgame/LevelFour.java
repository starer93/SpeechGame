package com.cymars.speechgame;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class LevelFour extends Activity implements OnClickListener {

	protected static final int RESULT_SPEECH = 1;
	
    private LinkedList<String> wordList = new LinkedList<String>();
    private LinkedList<Button> listOfButtons = new LinkedList<Button>();
    private LinkedList<Button> selectedButtons = new LinkedList<Button>();
    private TextView voiceInput, score, wordsLeftList, words, numberOfWordsLeftToFind;
	
    private GridCreator creator;
    private int scores = 0;
	private String input,word;

	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level_one);
		init();
		setTextImporter();
		setGrid();
		numberOfWordsLeftToFind.setText(wordList.size() +"");
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
		InputStream    fis;
        BufferedReader br;
        String         line;
        
        AssetManager am = this.getAssets();
        try {
			fis = am.open("words_level_four.txt");
			br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
	        while ((line = br.readLine()) != null) 
	        {
	            line = line.toUpperCase();
	            wordList.add(line);
	        }
	        br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        updateList();       
        creator = new GridCreator(wordList, 10);
        
	}
	
	private void updateList()
	{
		String listOfWord = "";
        for(String s: wordList)
        {
        	listOfWord += s + "  ";
        }
        wordsLeftList.setText(listOfWord);
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
		String s =  creator.toString();
		LinearLayout layout = (LinearLayout) findViewById(R.id.grid);
		layout.setOrientation(LinearLayout.VERTICAL);
        String[] chars = s.split("");
        int k = 1;
		for(int i = 0; i < 10; i ++)
		{
			LinearLayout row = new LinearLayout(this);
			row.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			for (int j = 0; j < 10; j++) {
				Button btnTag = new Button(this);
				btnTag.setLayoutParams(new LayoutParams(
			    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				btnTag.setText(chars[k]);
				btnTag.setId(j + 1 + i);
				btnTag.setOnClickListener(this);
				btnTag.setBackgroundColor(0);
				row.addView(btnTag);
                listOfButtons.add(btnTag);
                k++;
			}
			layout.addView(row);
		}
	}
	
	private void isComplete(int left)
	{
		if(left == 0)
		{
			writeFileToInternalStorage();
		 try { 
			// We need to get the instance of the LayoutInflater 
			LayoutInflater inflater = (LayoutInflater) LevelFour.this 
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
			View layout = inflater.inflate(R.layout.popupwindow,(ViewGroup)

			findViewById(R.id.popup_element)); 
			PopupWindow pwindo = new PopupWindow(layout, 350, 350, true); 
			pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

			TextView cong = (TextView) findViewById(R.id.txtView_complete);
		    cong.setText("Congrautations!! \nYou have complete all levels");
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
		Intent goToNextActivity = new Intent(getApplicationContext(), LevelOne.class);
		startActivity(goToNextActivity);
	}
	
	private void backMain()
	{
		Intent inten = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(inten);
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
                        String integer = (String) numberOfWordsLeftToFind.getText();
                        int i = Integer.parseInt(integer);
                        i--;
                        numberOfWordsLeftToFind.setText("" + i);
                        addScore(10);
                        score.setText("Score: " + scores);
                        wordList.remove(wordToCheck);
                        updateList();
                        reset();
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
	
    private void reset()
    {
    	clearText();
    	for(Button button: listOfButtons)
    	{
    		button.setBackgroundColor(0);
    	}
    	selectedButtons.clear();
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
	            button.setBackgroundColor(0);
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
