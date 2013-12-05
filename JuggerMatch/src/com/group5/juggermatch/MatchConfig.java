package com.group5.juggermatch;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MatchConfig extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match_config);
		// Show the Up button in the action bar.
		setupActionBar();
		final Context context = getApplicationContext(); //for use in validation error display
		
		final EditText teamA = (EditText) findViewById(R.id.teamA_editText); //Team A input
		teamA.addTextChangedListener(new TextWatcher(){ 				//Team A validation
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count)
				{}

				@Override
				public void beforeTextChanged(CharSequence s,int start,int count,int after)
				{}

				@Override
				public void afterTextChanged(Editable s)
				{

				String filtered_str = s.toString();

					if (filtered_str.matches(".*[^A-Za-z^0-9].*")) { //alphanumeric values

					filtered_str = filtered_str.replaceAll("[^A-Za-z^0-9]", "");

					s.clear();
					
					s.append(filtered_str);

					// s.insert(0, filtered_str);

					Toast.makeText(context,
						"Only letters and numbers are allowed!",
						Toast.LENGTH_SHORT).show();

				}

			}    
		});
		final EditText teamB = (EditText) findViewById(R.id.teamB_editText); //Team B input
		teamB.addTextChangedListener(new TextWatcher(){ 				//Team B validation
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{}

			@Override
			public void beforeTextChanged(CharSequence s,int start,int count,int after)
			{}

			@Override
			public void afterTextChanged(Editable s)
			{

			String filtered_str = s.toString();

				if (filtered_str.matches(".*[^A-Za-z^0-9].*")) { 	//alphanumeric values

				filtered_str = filtered_str.replaceAll("[^A-Za-z^0-9]", "");

				s.clear();
				
				s.append(filtered_str);

				// s.insert(0, filtered_str);

				Toast.makeText(context,
					"Only letters and numbers are allowed!",
					Toast.LENGTH_SHORT).show();

			}

		}    
	});
		final EditText halves = (EditText) findViewById(R.id.halves_editText); //halves input
		halves.addTextChangedListener(new TextWatcher(){ 						//halves validation
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{}

			@Override
			public void beforeTextChanged(CharSequence s,int start,int count,int after)
			{}

			@Override
			public void afterTextChanged(Editable s)
			{
			int no=Integer.parseInt(s.toString());
			if(no>9)								//numeric limit
			{
				s.replace(0,s.length(), "9");

		
				
				Toast.makeText(context,
					"9 max value!",
					Toast.LENGTH_SHORT).show();
			}
				
		}    
	}); 

		final EditText stones = (EditText) findViewById(R.id.stones_editText); //stones input
		stones.addTextChangedListener(new TextWatcher(){ 						//Stones validation
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{}

			@Override
			public void beforeTextChanged(CharSequence s,int start,int count,int after)
			{}

			@Override
			public void afterTextChanged(Editable s)
			{
			int no=Integer.parseInt(s.toString());
			if(no>999)								//numeric limit
			{
				s.replace(0,s.length(), "999");

		
				
				Toast.makeText(context,
					"999 max value!",
					Toast.LENGTH_SHORT).show();
			}
				
		}    
	}); 
		final EditText location = (EditText) findViewById(R.id.location_editText); //location input
		location.addTextChangedListener(new TextWatcher(){ 				//location validation
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{}

			@Override
			public void beforeTextChanged(CharSequence s,int start,int count,int after)
			{}

			@Override
			public void afterTextChanged(Editable s)
			{

			String filtered_str = s.toString();

				if (filtered_str.matches(".*[^A-Za-z^0-9].*")) {  	//alphanumeric values

				filtered_str = filtered_str.replaceAll("[^A-Za-z^0-9]", "");

				s.clear();
				
				s.append(filtered_str);

				// s.insert(0, filtered_str);

				Toast.makeText(context,
					"Only letters and numbers are allowed!",
					Toast.LENGTH_SHORT).show();

			}

		}    
	});
		
		Button start = (Button) findViewById(R.id.start_button);
		
	start.setOnClickListener(new OnClickListener() {
		
		
		
			@Override
			public void onClick(View v) {
				
				if(teamA.getText().length() < 1){ //IF teamA is blank
					Toast.makeText(context,
							"Team A name not long enough",
							Toast.LENGTH_SHORT).show();
				}
				else if (teamB.getText().length() < 1){ //IF teamB is blank
					Toast.makeText(context,
							"Team B name not long enough",
							Toast.LENGTH_SHORT).show();
				}
				else if (stones.getText().length() < 1){ //IF stones is blank
					Toast.makeText(context,
							"Stones not long enough",
							Toast.LENGTH_SHORT).show();
				}
				else if (halves.getText().length() < 1){ //IF halves is blank
					Toast.makeText(context,
							"Halves not long enough",
							Toast.LENGTH_SHORT).show();
				}
				else if (location.getText().length() < 1){ //IF location is blank
					Toast.makeText(context,
							"Location name not long enough",
							Toast.LENGTH_SHORT).show();
				}
				else{
				    //Do match
				
				
				boolean training = false;
				
				Intent theIntent = new Intent(MatchConfig.this, MatchTimer.class);
				
				theIntent.putExtra("teamAvar", teamA.getText().toString());
				theIntent.putExtra("teamBvar", teamB.getText().toString());
				theIntent.putExtra("halvesVar", Integer.parseInt(halves.getText().toString()));
				theIntent.putExtra("stonesVar", Integer.parseInt(stones.getText().toString()));
				theIntent.putExtra("locationVar", location.getText().toString());
				theIntent.putExtra("trainingVar", training);
				
				startActivity(theIntent);
				
				//this finish() will close the MatchConfig Activity when start button will be pressed
				finish();
			
				}
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
