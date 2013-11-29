package com.group5.juggermatch;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

public class timerTask extends timerTask {

	

public timerTask(int stonesRemaining){
	setStones(stonesRemaining);
};


public void setStones(int stonesRemaining){
	
	
};
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


  
