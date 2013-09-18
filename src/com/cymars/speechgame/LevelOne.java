package com.cymars.speechgame;

import java.util.ArrayList;
import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LevelOne extends Activity implements View.OnClickListener{
	protected static final int RESULT_SPEECH = 1;

	private Button btnSpeak;
	private TextView score;
	private String input;
	private Button[] buttons = new Button[100];
	TextView words;
	String word = "";
	String[] letters = { "a", "b", "c", "d", "e", "f", "g", "h", "i","k" };
	Random random = new Random();

	@Override	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level_one);
		score = (TextView) findViewById(R.id.text_score);
		btnSpeak= (Button) findViewById(R.id.speak_button);
		
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
		
		for(int i = 0; i < 5; i ++)
		{
			LinearLayout row = new LinearLayout(this);
			row.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			for (int j = 0; j < 10; j++) {
				int ran = random.nextInt(8);
				Button btnTag = new Button(this);
				btnTag.setLayoutParams(new LayoutParams(
			    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				btnTag.setText(letters[ran]);
				btnTag.setId(j + 1);
				btnTag.setOnClickListener(this);
				row.addView(btnTag);
			}
			layout.addView(row);
		}
	
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RESULT_SPEECH: {
			if (resultCode == RESULT_OK && null != data) {

				ArrayList<String> text = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

				input = text.get(0);
			}
			break;
		}

		}
	}

	@Override
	public void onClick(View v) {
		words = (TextView) findViewById(R.id.words);
		Button b = (Button) v;
		word += b.getText();
		words.setText(word);
		
	}
}
