package com.group5.juggermatch;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class MatchTimer extends Activity {
	
	/*****
	 *  State 1: Pre-match 
	 *  State 2: Running timer 
	 *  State 3: Timer Paused 
	 *  State 4: Half Complete 
	 *  State 5: Match Complete
	 */
	
	private int state = 1;
	private ImageView rollBack;
	private ImageView fastForward;
	private ImageView playPause;
	private TextView stonesCounter;
	private int stonesRemaining;
	private int stonesTotal;
	private TextView halvesCounter;
	private int halvesRemaining;
	private int halvesTotal;
	private TextView teamNameA;
	private TextView teamNameB;
	private String teamAstring;
	private String teamBstring;
	private ImageView timerView;
	private ImageView teamAGoal;
	private ImageView teamBGoal;
	private ImageView countDown;
	private TextView matchFinish;
	private TextView teamAScore;
	private TextView teamBScore;
	private int teamAScoreValue=0;
	private int teamBScoreValue=0;
	private long stoneDuration = 1500;
	private boolean trainingMode=false;
	
	private int stonesValuePrevious;
	private int teamAScorePrevious;
	private int teamBScorePrevious;
	
	private long matchStartTime;
	private long matchEndTime;
	private String location;
	private Timer timer;
	private MediaPlayer mPlayer;
	private AnimationDrawable countdownAnimation;
	private AnimationDrawable timerAnimation;

	SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_match_timer);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String stoneDurationIn = prefs.getString("stone_duration", "1500");
        stoneDuration = Long.parseLong(stoneDurationIn);
		timerView = (ImageView) findViewById(R.id.timerbackground);
		timerView.setBackgroundResource(R.drawable.timercircle);
		rollBack = (ImageView) findViewById(R.id.rollbackbutton);
		fastForward = (ImageView) findViewById(R.id.fastforwardbutton);
		playPause = (ImageView) findViewById(R.id.playpausebutton);
		teamAGoal = (ImageView) findViewById(R.id.score_control_a);
		teamBGoal = (ImageView) findViewById(R.id.score_control_b);
		matchFinish = (TextView) findViewById(R.id.finishbutton);
		countDown = (ImageView) findViewById(R.id.statusarea);
		
		teamAScore = (TextView) findViewById(R.id.team_a_score);
		teamAScore.setText(""+teamAScoreValue);
		teamBScore = (TextView) findViewById(R.id.team_b_score);
		teamBScore.setText(""+teamBScoreValue);
		teamNameA = (TextView) findViewById(R.id.teama);
		teamNameB = (TextView) findViewById(R.id.teamb);	
		halvesCounter = (TextView) findViewById(R.id.timerHalves);	
		stonesCounter = (TextView) findViewById(R.id.timerStones);	
		buildAnimation();
		
		
		
		Intent intent = getIntent();
		
		if (intent.hasExtra("teamAvar")){
        teamAstring = intent.getStringExtra("teamAvar");
		}
		else
		{
	    teamAstring = "TEAM A";
		}
		
		if (intent.hasExtra("teamBvar")){
	        teamBstring = intent.getStringExtra("teamBvar");
			}
			else
			{
		    teamBstring = "TEAM B";
			}
		
		
		setTeamNames();
        
		int halves;
		if (intent.hasExtra("halvesVar")){
        halves = intent.getExtras().getInt("halvesVar");
		}
		else{
		halves = 2;
		}
		
		
        halvesRemaining = halves;
        halvesTotal = halves;
        halvesCounter.setText(halvesRemaining + "/" + halvesTotal);
        
        
        
        int stones;
        if(intent.hasExtra("stonesVar")){
        stones = intent.getExtras().getInt("stonesVar");
        } else {
        	stones = 100;
        }
        
        
        stonesRemaining = stones;
        stonesTotal = stones;
        stonesCounter.setPaintFlags(stonesCounter.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        stonesCounter.setText(stonesRemaining + "/" + stonesTotal);
        
        if(intent.hasExtra("booleanVar")){
        trainingMode =  intent.getExtras().getBoolean("booleanVar");
        }
        
        if(intent.hasExtra("locationVar")){
        location =  intent.getExtras().getString("locationVar");
        }
        else{
        location = "Location Unspecified";
        }
        
        String soundString = prefs.getString("sound_choose","highbeep");
        
        if(soundString.equals("drum")){
        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.drum);
        }
        else if(soundString.equals("beat")){
        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.beat);
        } else{
        
        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.highbeep);
               
        }
        
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        timerView = (ImageView) findViewById(R.id.timerbackground);


        

        if (trainingMode==true){
        	rollBack.setVisibility(View.INVISIBLE);
        	fastForward.setVisibility(View.INVISIBLE);
        	stonesCounter.setVisibility(View.INVISIBLE);
        	halvesCounter.setVisibility(View.INVISIBLE);
        	matchFinish.setVisibility(View.INVISIBLE);
        	
        }
        
        if (halvesTotal%2!=0){
        	teamAGoal.setBackgroundColor(Color.rgb(0,0,255));
        	teamAScore.setBackgroundColor(Color.rgb(0,0,255));
        	teamBGoal.setBackgroundColor(Color.rgb(255,0,0));
        	teamBScore.setBackgroundColor(Color.rgb(255,0,0));
        	
        }
        
        
    	playPause.setOnClickListener(new OnClickListener() {
    	    @Override
    	    public void onClick(View v) {
    	    	if(state==2 || state==4 || state==5){
    	    		pauseTimer();
    	    	} else if(state==1){
    	    		matchStartTime=System.currentTimeMillis();
    	    			if(prefs.getBoolean("use_countdown",false)==true){
    	    				countDown.setBackgroundResource(R.drawable.countdownanimation);
    	    				countdownAnimation = (AnimationDrawable) countDown.getBackground();
    	    				countdownAnimation.start();
    	    				timer = new Timer();
    	    				timer.schedule(new countDownTask(), 1500, 1500);
    	    			}else {
    	    				startTimer();
    	    			}
    	   		 
    	    	}else if(state==3){
    	   		 if(prefs.getBoolean("use_countdown",false)==true){
    				 countDown.setBackgroundResource(R.drawable.countdownanimation);
    				 countdownAnimation = (AnimationDrawable) countDown.getBackground();
    				 countdownAnimation.start();
    				 timer = new Timer();
    				 timer.schedule(new countDownTask(), 1500, 1500);
    				 
     	    		
    			 } else {
    				 startTimer();
    			 }
    	    }
    	    }});
    	
    	matchFinish.setOnClickListener(new OnClickListener() {
    	   @Override
    	    public void onClick(View v) {
    	    	if(state==4){
    	            nextHalf();
    	    	}
    	    	else if(state==5){
    	    		endMatch();
    	    	}
    	    }
    	});
    	
    	teamAGoal.setOnClickListener(new OnClickListener() {
    	    @Override
    	    public void onClick(View v) {
    	    	if(state==2 && trainingMode==false){
    	            pauseTimer();
    	            teamAScoreValue++;
    	    		teamAScore.setText(""+teamAScoreValue);
    	    	}
    	    	else {
    	    		teamAScoreValue++;
    	    		teamAScore.setText(""+teamAScoreValue);
    	    	}
    	    }
    	});
    	
    	teamAGoal.setOnLongClickListener(new OnLongClickListener() {
    	    @Override
    	    public boolean onLongClick(View v) {

    	    		teamAScoreValue--;
    	    		teamAScore.setText(""+teamAScoreValue);

    	    	return true;
    	    }
    	});
    	
    	
    	teamBGoal.setOnClickListener(new OnClickListener() {
    	    @Override
    	    public void onClick(View v) {
    	    	if(state==2 && trainingMode==false){
    	            pauseTimer();
    	            teamBScoreValue++;
    	    		teamBScore.setText(""+teamBScoreValue);
    	    	}
    	    	else{
    	    		teamBScoreValue++;
    	    		teamBScore.setText(""+teamBScoreValue);
    	    	}
    	    }
    	});
    	
    	teamBGoal.setOnLongClickListener(new OnLongClickListener() {
    	    @Override
    	    public boolean onLongClick(View v) {

    	    		teamBScoreValue--;
    	    		teamBScore.setText(""+teamBScoreValue);

    	    	return true;
    	    }
    	});

    	
    	rollBack.setOnClickListener(new OnClickListener() {
    	    @Override
    	    public void onClick(View v) {
	    		if (halvesRemaining==0){
	    			halvesRemaining=1;
	    	        halvesCounter.setText(halvesRemaining + "/" + halvesTotal);
	    		}
    	    	pauseTimer();
    	    	stonesRemaining=stonesValuePrevious;
    	    	teamAScoreValue=teamAScorePrevious;
    	    	teamBScoreValue=teamBScorePrevious;
    	        stonesCounter.setText(stonesRemaining + "/" + stonesTotal);
	    		teamAScore.setText(""+teamAScoreValue);
	    		teamBScore.setText(""+teamBScoreValue);
    	    }
    	});
    	
    	fastForward.setOnClickListener(new OnClickListener() {
    	    @Override
    	    public void onClick(View v) {
    	    	if(state==1){
    	    		matchStartTime=System.currentTimeMillis();
    	    		state=3;
    	    	}
    	    	if(halvesRemaining>1){
    	    		nextHalf();
    	    	} else if (halvesRemaining>0) {
    	    		pauseTimer();
    	    		stonesRemaining=0;
    	            stonesCounter.setText(stonesRemaining + "/" + stonesTotal);
    	            matchOver();
    	    		
    	    	}
    	    		
    	    }
    	});
	
	}
	
	@Override protected void onPause(){
		
		PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
	    boolean isScreenOn = powerManager.isScreenOn();
	    
		if(state==2 && isScreenOn){
			pauseTimer();
		}
		super.onPause();
	}
	
	
	private void buildAnimation(){
		timerAnimation= new AnimationDrawable();
		int totalduration = (int) stoneDuration;
		int frameduration = (totalduration-50)/30;
		timerAnimation.addFrame(getResources().getDrawable(R.drawable.timerframe30), frameduration);
		timerAnimation.addFrame(getResources().getDrawable(R.drawable.timerframe1), frameduration);
		timerAnimation.addFrame(getResources().getDrawable(R.drawable.timerframe2), frameduration);
		timerAnimation.addFrame(getResources().getDrawable(R.drawable.timerframe3), frameduration);
		timerAnimation.addFrame(getResources().getDrawable(R.drawable.timerframe4), frameduration);
		timerAnimation.addFrame(getResources().getDrawable(R.drawable.timerframe5), frameduration);
		timerAnimation.addFrame(getResources().getDrawable(R.drawable.timerframe6), frameduration);
		timerAnimation.addFrame(getResources().getDrawable(R.drawable.timerframe7), frameduration);
		timerAnimation.addFrame(getResources().getDrawable(R.drawable.timerframe8), frameduration);
		timerAnimation.addFrame(getResources().getDrawable(R.drawable.timerframe9), frameduration);
		timerAnimation.addFrame(getResources().getDrawable(R.drawable.timerframe10), frameduration);
		timerAnimation.addFrame(getResources().getDrawable(R.drawable.timerframe11), frameduration);
		timerAnimation.addFrame(getResources().getDrawable(R.drawable.timerframe12), frameduration);
		timerAnimation.addFrame(getResources().getDrawable(R.drawable.timerframe13), frameduration);
		timerAnimation.addFrame(getResources().getDrawable(R.drawable.timerframe14), frameduration);
		timerAnimation.addFrame(getResources().getDrawable(R.drawable.timerframe15), frameduration);
		timerAnimation.addFrame(getResources().getDrawable(R.drawable.timerframe16), frameduration);
		timerAnimation.addFrame(getResources().getDrawable(R.drawable.timerframe17), frameduration);
		timerAnimation.addFrame(getResources().getDrawable(R.drawable.timerframe18), frameduration);
		timerAnimation.addFrame(getResources().getDrawable(R.drawable.timerframe19), frameduration);
		timerAnimation.addFrame(getResources().getDrawable(R.drawable.timerframe20), frameduration);
		timerAnimation.addFrame(getResources().getDrawable(R.drawable.timerframe21), frameduration);
		timerAnimation.addFrame(getResources().getDrawable(R.drawable.timerframe22), frameduration);
		timerAnimation.addFrame(getResources().getDrawable(R.drawable.timerframe23), frameduration);
		timerAnimation.addFrame(getResources().getDrawable(R.drawable.timerframe24), frameduration);
		timerAnimation.addFrame(getResources().getDrawable(R.drawable.timerframe25), frameduration);
		timerAnimation.addFrame(getResources().getDrawable(R.drawable.timerframe26), frameduration);
		timerAnimation.addFrame(getResources().getDrawable(R.drawable.timerframe27), frameduration);
		timerAnimation.addFrame(getResources().getDrawable(R.drawable.timerframe28), frameduration);
		timerAnimation.addFrame(getResources().getDrawable(R.drawable.timerframe29), frameduration);
	
	}
	private void startTimer(){
		
		 timer = new Timer();
		 timer.schedule(new RemindTask(),
		 stoneDuration, //initial delay
		 stoneDuration); //subsequent rate
		timerView.setBackgroundDrawable(getResources().getDrawable(R.drawable.timercircle));
		timerView.setBackgroundDrawable(timerAnimation);
		timerAnimation.start();
		
		if(trainingMode==false){
		teamAGoal.setImageDrawable(getResources().getDrawable(R.drawable.scorerun));
		teamBGoal.setImageDrawable(getResources().getDrawable(R.drawable.scorerun));
		if(halvesRemaining>0)
		{
		halvesCounter.setTextColor(Color.rgb(0,200,0));
		stonesCounter.setTextColor(Color.rgb(0,200,0));
		}
		}
		state=2;
		playPause.setImageDrawable(getResources().getDrawable(R.drawable.pause));
		stonesValuePrevious=stonesRemaining;
		teamAScorePrevious=teamAScoreValue;
		teamBScorePrevious=teamBScoreValue;

	
	}
	
	private void pauseTimer(){
		if(state==2 || state==4 || state==5){
		timer.cancel();
		timerAnimation.stop();
		}
		if(halvesRemaining>0){
		halvesCounter.setTextColor(Color.rgb(255,255,0));
		stonesCounter.setTextColor(Color.rgb(255,255,0));
		state=3;
		} else {
			stonesValuePrevious=stonesRemaining;
			teamAScorePrevious=teamAScoreValue;
			teamBScorePrevious=teamBScoreValue;
			playPause.setVisibility(View.INVISIBLE);
			rollBack.setVisibility(View.INVISIBLE);
		}
		
		timerView.setBackgroundResource(R.drawable.timercircle);		
		playPause.setImageDrawable(getResources().getDrawable(R.drawable.play));
		teamAGoal.setImageDrawable(getResources().getDrawable(R.drawable.scoreplus));
		teamBGoal.setImageDrawable(getResources().getDrawable(R.drawable.scoreplus));		
	}
	
	private void halfOver(){
		if(trainingMode==false){
		state=4;
		halvesCounter.setTextColor(Color.rgb(200,0,0));
		stonesCounter.setTextColor(Color.rgb(200,0,0));
		matchFinish.setText(getResources().getString(R.string.end_half));
		matchFinish.setBackgroundColor(Color.rgb(155,155,0));
		}
	}
	
	private void matchOver(){
		if(trainingMode==false){
		if(state==3){
			playPause.setVisibility(View.INVISIBLE);
		}
		state=5;
		halvesCounter.setTextColor(Color.rgb(200,0,0));
		stonesCounter.setTextColor(Color.rgb(200,0,0));
		halvesRemaining--;
        halvesCounter.setText(halvesRemaining + "/" + halvesTotal);
		matchFinish.setText(getResources().getString(R.string.end_match));
		matchFinish.setBackgroundColor(Color.rgb(150,0,0));
		}
	}
	
	private void nextHalf(){
		if(trainingMode==false){
		pauseTimer();
		matchFinish.setText("");
		matchFinish.setBackgroundColor(Color.rgb(0,0,0));
		halvesRemaining--;
        halvesCounter.setText(halvesRemaining + "/" + halvesTotal);
        
        if(prefs.getBoolean("stones_carry",false)==true){
            stonesRemaining=stonesTotal+stonesRemaining;
		 } else {
        stonesRemaining=stonesTotal;
		 }
        stonesCounter.setText(stonesRemaining + "/" + stonesTotal);
        state=3;

        int temp = 0;
        temp = teamAScoreValue;
        teamAScoreValue=teamBScoreValue;
        teamBScoreValue=temp;
		teamAScore.setText(""+teamAScoreValue);
        teamBScore.setText(""+teamBScoreValue);
        String tempstring = teamAstring;
        teamAstring = teamBstring;
        teamBstring = tempstring;
        setTeamNames();
        
        if (halvesTotal%2==0){
        	teamAGoal.setBackgroundColor(Color.rgb(255,0,0));
        	teamAScore.setBackgroundColor(Color.rgb(255,0,0));
        	teamBGoal.setBackgroundColor(Color.rgb(0,0,255));
        	teamBScore.setBackgroundColor(Color.rgb(0,0,255));	
        } else {
            teamAGoal.setBackgroundColor(Color.rgb(0,0,255));
        	teamAScore.setBackgroundColor(Color.rgb(0,0,255));
        	teamBGoal.setBackgroundColor(Color.rgb(255,0,0));
        	teamBScore.setBackgroundColor(Color.rgb(255,0,0));
		}

		}
	}
	
	private void endMatch(){
		matchEndTime=System.currentTimeMillis();
		pauseTimer();
        Intent theIntent = new Intent(MatchTimer.this, MatchResult.class);
        theIntent.putExtra("team1", teamAstring);
        theIntent.putExtra("team2", teamBstring);
        theIntent.putExtra("score1", teamAScoreValue);
        theIntent.putExtra("score2", teamBScoreValue);
        theIntent.putExtra("start_time_stamp", matchStartTime);
        theIntent.putExtra("end_time_stamp", matchEndTime);
        theIntent.putExtra("location", location);
        startActivity(theIntent);
	}
	
	private void setTeamNames(){
        if(teamAstring.length()>=7){
        String truncStringA = teamAstring.substring(0,7);
        teamNameA.setText(truncStringA);
        } else {
        String truncStringA = teamAstring;
        teamNameA.setText(truncStringA);
        }
        if(teamBstring.length()>=7){
        String truncStringB= teamBstring.substring(0,7);
        teamNameB.setText(truncStringB);
        } else {
        String truncStringB= teamBstring;
        teamNameB.setText(truncStringB);

        }
		
		
	}

	 private class RemindTask extends TimerTask {

		 public void run() {
		 mPlayer.start();
		 stonesRemaining--;
			 
		 runOnUiThread(new Runnable(){

             @Override
             public void run(){
            	 countDown.setBackgroundResource(0);
            	 timerAnimation.stop();
            	 timerView.setBackgroundDrawable(getResources().getDrawable(R.drawable.timercircle));
            	 timerView.setBackgroundDrawable(timerAnimation);
         		timerAnimation.start();
            	 
        	     stonesCounter.setText(stonesRemaining + "/" + stonesTotal);
        		 if(stonesRemaining==0 && halvesRemaining>1){
        			 halfOver();
        		 } else if (stonesRemaining==0 && halvesRemaining==1){
        			 matchOver();
        		 }
             }
          });
		 }
		 }
	 
	 private class countDownTask extends TimerTask {

		 public void run() {
			 
		 runOnUiThread(new Runnable(){

             @Override
             public void run(){
            	timer.cancel();
            	countdownAnimation.stop();
 	    		startTimer();
 	    		
             }
          });
		 }
		 
		 
		 }


	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	

}


  
