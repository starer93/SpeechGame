package com.cymars.speechgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	private String readfromfile = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setButtons();
		readFileFromInternalStorage();
	    TextView title = (TextView) findViewById(R.id.title);
	    title.setText(readfromfile);
	}

	
	private void setButtons()
	{
		Button startButton = (Button)findViewById(R.id.button_start);	
		Button tutButton = (Button)findViewById(R.id.button_tut);	
		Button levelButton = (Button) findViewById(R.id.button_level_select);
	    startButton.setOnClickListener(new View.OnClickListener() {

	      @Override
	      public void onClick(View view) {
	        Intent intent = new Intent(MainActivity.this, LevelOne.class);
	        startActivity(intent);
	      }

	    });
	    
	    levelButton.setOnClickListener(new View.OnClickListener() {

		      @Override
		      public void onClick(View view) {
		        Intent intent = new Intent(MainActivity.this, LevelSelectActivity.class);
		        startActivity(intent);
		      }

		    });
	    tutButton.setOnClickListener(new View.OnClickListener() {

		      @Override
		      public void onClick(View view) {
		        Intent intent = new Intent(MainActivity.this, Tutorial.class);
		        startActivity(intent);
		      }

		    });
	  }
	
	private void readFileFromInternalStorage() {
		  String eol = System.getProperty("line.separator");
		  BufferedReader input = null;
		  try {
		    input = new BufferedReader(new InputStreamReader(openFileInput("myfile")));
		    String line;
		    StringBuffer buffer = new StringBuffer();
		    while ((line = input.readLine()) != null) {
		    buffer.append(line + eol);
		    readfromfile = buffer.toString();
		    }
		  } catch (Exception e) {
		     e.printStackTrace();
		  } finally {
		  if (input != null) {
		    try {
		    input.close();
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		    }
		  }
		} 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
