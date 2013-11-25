package com.group5.juggermatch;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

public class timerTask extends Activity {

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

                 
         public Beep(){}; / Not sure this actually does anything
            
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


  
