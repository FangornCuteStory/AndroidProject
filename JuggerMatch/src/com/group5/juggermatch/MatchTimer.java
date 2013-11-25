package com.group5.juggermatch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class MatchTimer extends Activity {
	
	/***** 
	 *  State 0: Training mode
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_match_timer);
		
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
				
        String teamA = getIntent().getExtras().getString("teamAvar");
        String teamB = getIntent().getExtras().getString("teamBvar");
        teamNameA.setText(teamA.substring(0,7));
        teamNameB.setText(teamB.substring(0,7));

        int halves = getIntent().getExtras().getInt("halvesVar");
        halvesRemaining = halves;
        halvesTotal = halves;
        halvesCounter.setText(halvesRemaining + "/" + halvesTotal);
        int stones = getIntent().getExtras().getInt("stonesVar");
        stonesRemaining = stones;
        stonesTotal = stones;
        stonesCounter.setPaintFlags(stonesCounter.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        stonesCounter.setText(stonesRemaining + "/" + stonesTotal);
        trainingMode =  getIntent().getExtras().getBoolean("booleanVar");
        location =  getIntent().getExtras().getString("locationVar");

        
        
    	playPause.setOnClickListener(new OnClickListener() {
    	    @Override
    	    public void onClick(View v) {
    	    	if(state==2){
    	    		pauseTimer();
    	    	}
    	    	else if(state==1){
    	    		matchStartTime=System.currentTimeMillis()/1000;
    	    		startTimer();
    	    	}
    	    	else if(state==3){
    	    		startTimer();    	    		
    	    	}
    	    }
    	});
    	
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
    	    	if(state==2){
    	            pauseTimer();
    	            teamAScoreValue++;
    	    		teamAScore.setText(""+teamAScoreValue);
    	    	}
    	    	else if(state== 1 || state == 3){
    	    		teamAScoreValue++;
    	    		teamAScore.setText(""+teamAScoreValue);
    	    	}
    	    }
    	});
    	
    	teamAGoal.setOnLongClickListener(new OnLongClickListener() {
    	    @Override
    	    public boolean onLongClick(View v) {
    	    	if(state==2){
    	            pauseTimer();
    	            teamAScoreValue--;
    	    		teamAScore.setText(""+teamAScoreValue);
    	   
    	    	}
    	    	else if(state== 1 || state == 3){
    	    		teamAScoreValue--;
    	    		teamAScore.setText(""+teamAScoreValue);
    	    	
    	    	}
    	    	return true;
    	    }
    	});
    	
    	
    	teamBGoal.setOnClickListener(new OnClickListener() {
    	    @Override
    	    public void onClick(View v) {
    	    	if(state==2){
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
    	    	if(state==2){
    	            pauseTimer();
    	            teamBScoreValue--;
    	    		teamBScore.setText(""+teamBScoreValue);
    	   
    	    	}
    	    	else{
    	    		teamBScoreValue--;
    	    		teamBScore.setText(""+teamBScoreValue);
    	    	
    	    	}
    	    	return true;
    	    }
    	});

		
	}
	
	private void startTimer(){
		if(trainingMode=false){
		teamAGoal.setImageDrawable(getResources().getDrawable(R.drawable.scorerun));
		teamBGoal.setImageDrawable(getResources().getDrawable(R.drawable.scorerun));
		halvesCounter.setTextColor(Color.rgb(0,200,0));
		stonesCounter.setTextColor(Color.rgb(0,200,0));
		}
		state=2;
		playPause.setImageDrawable(getResources().getDrawable(R.drawable.pause));
		stonesValuePrevious=stonesRemaining;
		teamAScorePrevious=teamAScoreValue;
		teamBScorePrevious=teamBScoreValue;
		//start timer operation
	}
	
	private void pauseTimer(){
		if(trainingMode=false){
		halvesCounter.setTextColor(Color.rgb(255,255,0));
		stonesCounter.setTextColor(Color.rgb(255,255,0));
		}
		state=3;
		playPause.setImageDrawable(getResources().getDrawable(R.drawable.play));
		
		if(teamAScoreValue!=0){
		teamAGoal.setImageDrawable(getResources().getDrawable(R.drawable.scoreplus));
		} else {
		teamAGoal.setImageDrawable(getResources().getDrawable(R.drawable.zeroscoreplus));
		}
		
		if(teamBScoreValue!=0){
		teamBGoal.setImageDrawable(getResources().getDrawable(R.drawable.scoreplus));
		} else {
		teamBGoal.setImageDrawable(getResources().getDrawable(R.drawable.zeroscoreplus));
		}
		
		//stop timer operation
		
	}
	
	private void halfOver(){
		if(trainingMode=false){
		state=4;
		halvesCounter.setTextColor(Color.rgb(200,0,0));
		stonesCounter.setTextColor(Color.rgb(200,0,0));
		matchFinish.setText(getResources().getString(R.string.end_half));
		matchFinish.setBackgroundColor(Color.rgb(155,155,0));
		}
	}
	
	private void matchOver(){
		if(trainingMode=false){
		state=5;
		halvesCounter.setTextColor(Color.rgb(200,0,0));
		stonesCounter.setTextColor(Color.rgb(200,0,0));
		matchFinish.setText(getResources().getString(R.string.end_match));
		matchFinish.setBackgroundColor(Color.rgb(255,0,0));
		}
	}
	
	private void nextHalf(){
		if(trainingMode=false){
		pauseTimer();
		matchFinish.setText("");
		matchFinish.setBackgroundColor(Color.rgb(0,0,0));
		halvesRemaining--;
        halvesCounter.setText(halvesRemaining + "/" + halvesTotal);
        stonesRemaining=stonesTotal;
        stonesCounter.setText(stonesRemaining + "/" + stonesTotal);
        state=3;
		}
	}
	
	private void endMatch(){
		matchEndTime=System.currentTimeMillis()/1000;
		pauseTimer();
        Intent theIntent = new Intent(MatchTimer.this, MatchResult.class);
        theIntent.putExtra("teamAname", teamNameA.getText().toString());
        theIntent.putExtra("teamBname", teamNameB.getText().toString());
        theIntent.putExtra("teamAscore", teamAScoreValue);
        theIntent.putExtra("teamBscore", teamBScoreValue);
        theIntent.putExtra("matchStartTime", matchStartTime);
        theIntent.putExtra("matchEndTime", matchEndTime);
        theIntent.putExtra("matchLocation", location);
        startActivity(theIntent);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	



}
