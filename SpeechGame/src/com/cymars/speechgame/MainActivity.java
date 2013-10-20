package com.cymars.speechgame;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button orderButton = (Button)findViewById(R.id.button_start);

	    orderButton.setOnClickListener(new View.OnClickListener() {

	      @Override
	      public void onClick(View view) {
	        Intent intent = new Intent(MainActivity.this, LevelOne.class);
	        startActivity(intent);
	      }

	    });
	    
	    Button levelThree = (Button) findViewById(R.id.button_levelThree);
	    levelThree.setOnClickListener(new View.OnClickListener() {

		      @Override
		      public void onClick(View view) {
		        Intent intent = new Intent(MainActivity.this, LevelThree.class);
		        startActivity(intent);
		      }

		    });
	    
	  }
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	

}
