package com.cymars.speechgame;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class LevelTwo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level_two);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.level_two, menu);
		return true;
	}

}
