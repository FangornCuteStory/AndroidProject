package com.group5.juggermatch;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class MatchTimer extends Activity {

	@Override
+  protected void onCreate(Bundle savedInstanceState) {
 +      super.onCreate(savedInstanceState);
 +  //    setContentView(R.layout.buzz);  Can set content view to whatever, will have to see others layouts and code. 
 +     
 +        timer = new Timer();
 +        timer.schedule(new RemindTask(),
 +                 1500,        //initial delay
 +                 1*1500);  //subsequent rate, both will be a variable of type float or long or something
 +        }
 +  
 +       Timer timer;
 +     
 +   public MatchTimer(){};//Constructor Declaration. Looking at this I'm not sure its actually used anywhere. 
 +      
 +       class RemindTask extends TimerTask {
  
 +                public void run() { 
 +              
 +                MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.beep);  
 +              mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
 +              mPlayer.start();//All straightforward,plays a file somehow.
 +                
 +              }
 +        }
  }
