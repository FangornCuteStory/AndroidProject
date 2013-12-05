package com.group5.juggermatch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		
Button match = (Button) findViewById(R.id.match_button);
Button quick_match = (Button) findViewById(R.id.quick_match_button);
Button training = (Button) findViewById(R.id.training_button);
Button view_matches = (Button) findViewById(R.id.viewMatches_button);
PreferenceManager.setDefaultValues(this, R.xml.preferences, false);


	match.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
				
		Intent theIntent = new Intent(MainMenu.this, MatchConfig.class);
		startActivity(theIntent);
		}
		
	});
		
	quick_match.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			
			String teamA = "Team A";
			String teamB = "Team B";
			int halves = 2;
			int stones = 100;
			boolean training = false; 
			
		Intent theIntent = new Intent(MainMenu.this, MatchTimer.class);
			theIntent.putExtra("teamAvar", teamA);
			theIntent.putExtra("teamBvar", teamB);
			theIntent.putExtra("halvesVar", halves);
			theIntent.putExtra("stonesVar", stones);
			theIntent.putExtra("booleanVar", training);
		startActivity(theIntent);
		}
		
	});
	
	
	training.setOnClickListener(new OnClickListener() {

	@Override
	public void onClick(View v) {

	String teamA = "Team A";
	String teamB = "Team B";
	int halves = 2;
	int stones = 100;
	boolean training = true;
			
	Intent theIntent = new Intent(MainMenu.this, MatchTimer.class);
	theIntent.putExtra("teamAvar", teamA);
	theIntent.putExtra("teamBvar", teamB);
	theIntent.putExtra("halvesVar", halves);
	theIntent.putExtra("stonesVar", stones);
	theIntent.putExtra("booleanVar", training);
startActivity(theIntent);
		}
	
	});

	
	view_matches.setOnClickListener(new OnClickListener() {

	@Override
	public void onClick(View v) {
			
	Intent theIntent = new Intent(MainMenu.this, ViewMatches.class);
	startActivity(theIntent);
		}
	
	});
		
}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.settings:
	            Intent intentsettings = new Intent(MainMenu.this, Matchpreferences.class);
	            startActivity(intentsettings);
	            return true;
	        case R.id.help:
	        	Intent intenthelp = new Intent(MainMenu.this, Help.class);
	            startActivity(intenthelp);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
	
	
	public void ExitFromApp(View view) {
		
        finish();
    
	}
}
