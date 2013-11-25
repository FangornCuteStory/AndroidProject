package com.group5.juggermatch;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;

public class MatchConfig extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match_config);
		// Show the Up button in the action bar.
		setupActionBar();
		
		final EditText teamA = (EditText) findViewById(R.id.teamA_textView);
		final EditText teamB = (EditText) findViewById(R.id.teamB_textView);
		final EditText halves = (EditText) findViewById(R.id.halves_textView);
		final EditText stones = (EditText) findViewById(R.id.stones_textView);
		final EditText location = (EditText) findViewById(R.id.location_textView);
		
		Button start = (Button) findViewById(R.id.start_button);
		
	start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				boolean training = false;
				
				Intent theIntent = new Intent(MatchConfig.this, MatchTimer.class);
				theIntent.putExtra("teamAvar", teamA.getText().toString());
				theIntent.putExtra("teamBvar", teamB.getText().toString());
				theIntent.putExtra("halvesVar", halves.getText().toString());
				theIntent.putExtra("stonesVar", stones.getText().toString());
				theIntent.putExtra("locationVar", location.getText().toString());
				theIntent.putExtra("booleanVar", training);
				startActivity(theIntent);
				
				//this finish() will close the MatchConfig Activity when start button will be pressed
				finish();
			}
		});
		
		Button back = (Button) findViewById(R.id.exit_button);
		
	back.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
		
		//finish() will close the MatchConfig Activity when back button will be pressed
			finish();
		}
	});

}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
