package com.group5.juggermatch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddMatch extends Activity {
 
	private DatabaseOperations db;
	private static final String TAG = "JUGGER";
	private String team1;
	private String team2;
	private int score1;
	private int score2;
	private long s_time;
	private long e_time;
	private String location;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_match);
		db = new DatabaseOperations(AddMatch.this);
		db.open();
		
		 Bundle bundle = getIntent().getExtras(); 
		 if  (bundle != null){
			showRecievedItem(bundle);
		}
	}

	private void showRecievedItem(Bundle bundle) {
		// Extracting values received from another activity from bundle
		team1 = bundle.getString("team1");
		team2 = bundle.getString("team2");
        score1 = bundle.getInt("score1",0);
        score2 = bundle.getInt("score2",0);
        s_time = bundle.getLong("end_time_stamp",0);
        Log.d(TAG,"ADDMATCH: from intent start time = " + s_time);
        e_time = bundle.getLong("end_time_stamp",0);
        Log.d(TAG,"ADDMATCH: from intent end time = " + e_time);
        location = bundle.getString("location");
        
        //Displaying match results in the corresponding fields of the view
        
        SimpleDateFormat date_formatter = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        SimpleDateFormat time_formatter = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()); 
                
        ((EditText) findViewById(R.id.date)).setText(date_formatter.format(new Date(s_time)));
  
        ((EditText) findViewById(R.id.start_time)).setText(time_formatter.format(new Date(s_time)));
  
        ((EditText) findViewById(R.id.end_time)).setText(time_formatter.format(new Date(e_time))); 
  
        ((EditText) findViewById(R.id.team1)).setText(team1);
   
        ((EditText) findViewById(R.id.team2)).setText(team2);
          
        ((EditText) findViewById(R.id.score_team1)).setText("" + score1);
  
        ((EditText) findViewById(R.id.score_team2)).setText("" + score2);
  
        ((EditText) findViewById(R.id.location)).setText(location);
    
       
	}	
	
		public Match createMatchObj() {
		//create match object
        Match match = new Match();
        match.setTeam1(team1);
        match.setTeam2(team2);
        match.setScoreTeam1(score1);
        match.setScoreTeam2(score2);
        match.setStartTime(s_time);
        match.setEndTime(e_time);
        match.setLocation(location);
		return match;	
	}
	
		
	public void onClickButton(View view){
		 String message;
		switch(view.getId()) {
	      case R.id.button_save:      			
	          Match match_add = createMatchObj();
	          db.open();
	          long id = db.insertMatch(match_add);       // id is an id of the inserted record, if its value equals -1 an error took place 
	          if (id != -1) {
	        	    message = getResources().getString(R.string.insert_ok);   
	          }
	         else 
	        	 message = getResources().getString(R.string.insert_failed);
	          Toast.makeText(AddMatch.this, message, Toast.LENGTH_LONG).show(); 
	         db.close();
	         finish();
		     break;
	       case R.id.button_reject:
	    	   message = getResources().getString(R.string.insert_rejected);
	    	   Toast.makeText(AddMatch.this, message, Toast.LENGTH_LONG).show(); 
		      db.close();		 
              finish();    // activity ends and we return to the calling activity
	          break;
		}    
  }
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_match, menu);
		return true;
	}

}
