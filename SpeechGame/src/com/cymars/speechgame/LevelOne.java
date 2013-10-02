package com.cymars.speechgame;

import java.io.IOException;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LevelOne extends Activity implements View.OnClickListener {
	protected static final int RESULT_SPEECH = 1;

    private Button btnSpeak;
    private LinkedList<String> wordList = new LinkedList<String>();
    private TextView voiceInput;
	private TextView score;
	private int scores = 0;
	private String input;
    private LinkedList<Button> listOfButtons = new LinkedList<Button>();
    private LinkedList<Button> selectedButtons = new LinkedList<Button>();
    private TextView numberOfWordsLeftToFind;
	TextView words;
	String word = "";
	Random random = new Random();
    TextImporter textImporter;
    TextView wordsLeftList;
    
	@Override	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level_one);
		score = (TextView) findViewById(R.id.text_score);
		btnSpeak= (Button) findViewById(R.id.speak_button);
		Button clear_btn = (Button) findViewById(R.id.clear_button);
		clear_btn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
                clearButton();
			}
		});

		numberOfWordsLeftToFind = (TextView)findViewById (R.id.words_left);
        wordsLeftList = (TextView) findViewById(R.id.word_list);
	        try
	        {
	            textImporter= new TextImporter("/assets/words.txt", getApplicationContext());
	            numberOfWordsLeftToFind.setText("" + textImporter.getNumberOfWordsInList());
	        }
	        catch (IOException e) {
	        	String error = e.getCause().toString();
	             Toast.makeText(getApplicationContext(), "Invalid file", Toast.LENGTH_SHORT);
	            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
	        }

		setGrid();
		btnSpeak.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(
						RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

				try {
					startActivityForResult(intent, RESULT_SPEECH);
				} catch (ActivityNotFoundException a) {
					Toast t = Toast.makeText(getApplicationContext(),
							"Ops! Your device doesn't support Speech to Text",
							Toast.LENGTH_SHORT);
					t.show();
				}
			}
		});

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
		textImporter.makeCharArray(50);
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
	    randomiseGrid();

	}

    private void updateWordList()
    {
        String wordsForList = "\t";
        int i = 0;
        for(String word: wordList)
        {
            wordsForList += word + " ";
            i++;
            if(i == 2)
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
        textImporter.makeCharArray(selectedButtons.size());
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
    
    private void addScore(int value)
    {
       scores += value;
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RESULT_SPEECH: {
			if (resultCode == RESULT_OK && null != data) {

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
	                	textImporter.removeWord(word);
	                    resetButtonText();
                        String integer = (String) numberOfWordsLeftToFind.getText();
                        int i = Integer.parseInt(integer);
                        i--;
                        numberOfWordsLeftToFind.setText("" + i);
                        word = "";
                        addScore(10);
                        score.setText("Score: " + scores);
                        updateWordList();
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

	 private void clearButton() {
	        word = "";
            words.setText(word);
            input = "";
            voiceInput.setText("");
	        while(!selectedButtons.isEmpty())
	        {
	            Button b = selectedButtons.pollLast();
	            b.setBackgroundColor(1);
	            b.setClickable(true);
	        }
	    }

	    private void setGridButton(Button b) {
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
	            if(last!=null){
	            last.setBackgroundColor(Color.BLUE);
	            last.setClickable(true);
	            }
	        }
	    }

}
