package com.group5.juggermatch;

import android.graphics.drawable.AnimationDrawable;

public class timerAnimationDrawable extends AnimationDrawable{
	
	private volatile int duration;
	private int currentFrame;
	
	public timerAnimationDrawable() {
	    currentFrame = 0;
	}

	@Override
	public void run() {

	    int n = getNumberOfFrames();
	    currentFrame++;
	    if (currentFrame >= n) {
	        currentFrame = 0;
	    }

	    selectDrawable(currentFrame);
	    scheduleSelf(this, duration);
	}

	public void setDuration(int duration)
	{
	    this.duration = duration;
	    unscheduleSelf(this);
	    selectDrawable(currentFrame);
	    scheduleSelf(this, duration);
	}

	

}
