package com.cymars.speechgame;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class LevelSelectActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level_select);
		setButtons();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.level_select, menu);
		return true;
	}
	
	private void setButtons()
	{
		Button buttonOne = (Button)findViewById(R.id.button_levelOne);	
		Button buttonTwo = (Button)findViewById(R.id.button_levelTwo);	
		Button buttonThree = (Button)findViewById(R.id.button_levelThree);	
		Button buttonFour = (Button)findViewById(R.id.button_levelFour);		
	    buttonOne.setOnClickListener(new View.OnClickListener() {

	      @Override
	      public void onClick(View view) {
	        Intent intent = new Intent(LevelSelectActivity.this, LevelOne.class);
	        startActivity(intent);
	      }

	    });
	    buttonTwo.setOnClickListener(new View.OnClickListener() {

		      @Override
		      public void onClick(View view) {
		        Intent intent = new Intent(LevelSelectActivity.this, LevelTwo.class);
		        startActivity(intent);
		      }

		    });
	    buttonThree.setOnClickListener(new View.OnClickListener() {

		      @Override
		      public void onClick(View view) {
		        Intent intent = new Intent(LevelSelectActivity.this, LevelThree.class);
		        startActivity(intent);
		      }

		    });
	    buttonFour.setOnClickListener(new View.OnClickListener() {

		      @Override
		      public void onClick(View view) {
		        Intent intent = new Intent(LevelSelectActivity.this, LevelFour.class);
		        startActivity(intent);
		      }

		    });
	}

}
