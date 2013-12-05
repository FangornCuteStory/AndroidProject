package com.group5.juggermatch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

public class MatchInfo extends Activity {
	
	private DatabaseOperations db;
//	private static final String TAG = "JUGGER";
	private long match_id;
	private boolean dialog_showing = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match_info);
	
		db = new DatabaseOperations(this);		
		 
		 Bundle bundle = getIntent().getExtras(); 
		 if  (bundle != null){
			 match_id = bundle.getLong("match_id", 1); 
			showInfo();
		}
	}
    
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
    //saving dialog showing state 		
	    super.onSaveInstanceState(savedInstanceState);
	    if (dialog_showing) {	    	   
	    	savedInstanceState.putBoolean("dialog",dialog_showing);
	    };
	}  
		
    
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	  
	     if (savedInstanceState.containsKey("dialog")){
	    	
	    	 boolean showing = savedInstanceState.getBoolean("dialog");	     
	    	 if (showing) {
	    		 AlertDialog dialog = confirmDeletion();
	            	dialog_showing = true;
	            	dialog.show();            	 
	    	 }        	 
	      }
	     super.onRestoreInstanceState(savedInstanceState);
	 }   
	

	private void showInfo() {
		 db.open(); 
		 Match currentMatch = db.getMatch(match_id);
		 db.close();
		 SimpleDateFormat day_formatter = new SimpleDateFormat("EEEE", Locale.getDefault());
		 SimpleDateFormat date_formatter = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
	     SimpleDateFormat time_formatter = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()); 
	     ((TextView)findViewById(R.id.match_day)).setText(day_formatter.format(new Date(currentMatch.getStartTime())));
	     ((TextView)findViewById(R.id.match_date)).setText(date_formatter.format(new Date(currentMatch.getStartTime())));
	     
	     //set up teams names
	     TextView team1_txt = (TextView) findViewById(R.id.team1_name);
	     team1_txt.setText(currentMatch.getTeam1());
	        
	     TextView team2_txt = (TextView)findViewById(R.id.team2_name);
	     team2_txt.setText(currentMatch.getTeam2());

	     //set up scores
	     TextView score1_txt = (TextView)findViewById(R.id.team1_score);
	     score1_txt.setText("" + currentMatch.getScoreTeam1());
	        
	     TextView score2_txt = (TextView)findViewById(R.id.team2_score);
	     score2_txt.setText("" + currentMatch.getScoreTeam2());
	        
	     // start and end time 
	     ((TextView)findViewById(R.id.start_time)).setText(time_formatter.format(new Date(currentMatch.getStartTime())));      
	     ((TextView)findViewById(R.id.end_time)).setText(time_formatter.format(new Date(currentMatch.getEndTime())));    
	        
	     //location
	     TextView location_txt = (TextView)findViewById(R.id.location);
	     location_txt.setText(currentMatch.getLocation());	  
	}

	
	private void deleteMatch(long m_id) {
		// deleting match results from database 
	    	db.open();
	    	boolean success = db.deleteMatch(m_id);
	    	db.close();		
	    	if (success) 
	  			Toast.makeText(MatchInfo.this, getResources().getString(R.string.delete_ok),
	  					Toast.LENGTH_SHORT).show();
	    	else
	    	    Toast.makeText(MatchInfo.this, getResources().getString(R.string.delete_failed),
	    	    		Toast.LENGTH_SHORT).show();
	}
	

	public void onClickButton(View view){

		switch(view.getId()) {
            case R.id.button_back: 
//            	dialog_showing = false; 
            	finish();               // activity ends and we return to the calling activity
            	break;
            case R.id.button_delete:
            	AlertDialog dialog = confirmDeletion();
            	dialog_showing = true;           	
            	dialog.show();
            	
            	break;	
	    }
	 }
	
	
    public AlertDialog confirmDeletion(){
   	 	    // create and return a dialog instance 
	    AlertDialog dialog = new AlertDialog.Builder(MatchInfo.this)
	        .setTitle(getResources().getString(R.string.delete_dialog_title))
	        .setMessage(getResources().getString(R.string.delete_dialog_message))
	        .setCancelable(false)
	        .create();
	    dialog.setButton(
	            DialogInterface.BUTTON_NEGATIVE,    
	            getResources().getString(R.string.delete_dialog_negative_button),
	            new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog,int which) {     
	                	dialog_showing = false;
	                }
	            });
    	dialog.setButton(
	            DialogInterface.BUTTON_POSITIVE,    
	            getResources().getString(R.string.delete_dialog_positive_button),
	            new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                	dialog_showing = false;
	                	long m_id = match_id;
	                	deleteMatch(m_id);
	                	finish();                
	                }
	          }); 	    
	    return dialog;  
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
	            Intent intentsettings = new Intent(MatchInfo.this, Matchpreferences.class);
	            startActivity(intentsettings);
	            return true;
	        case R.id.help:
	        	Intent intenthelp = new Intent(MatchInfo.this, Help.class);
	            startActivity(intenthelp);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
