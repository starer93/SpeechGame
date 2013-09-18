package com.cymars.speechgame.test;

import com.cymars.speechgame.MainActivity;
import com.jayway.android.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;

public class FirstTest extends ActivityInstrumentationTestCase2<MainActivity> {

	private Solo solo;
	public FirstTest() {
		super("com.cymars.speechgame", MainActivity.class);
		// TODO Auto-generated constructor stub
	}

	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(),getActivity());
	}
	
	public void testInitat()
	{
	    solo.assertCurrentActivity("Check on first activity", MainActivity.class);
	}

}
