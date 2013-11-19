package com.group5.juggermatch;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class MatchTimer extends Activity {

Timer timer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.buzz); 
	   
	  
        timer = new Timer();
        timer.schedule(new RemindTask(),
	               0,        //initial delay
	               1*3000);  //subsequent rate
	    
	}

		 
	 public Beep(){};
	    
	     class RemindTask extends TimerTask {

	    	    		public void run() {
	    	    	
	            	MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.beep);	
	        		mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
	        		mPlayer.start();
	    					
	            }
	      }
	     

protected void onPause(){

    super.onPause();
    timer.cancel();

}


  
