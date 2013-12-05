package com.group5.juggermatch;

import android.os.Bundle;
import android.preference.PreferenceActivity;


public class Matchpreferences extends PreferenceActivity {
	
	  @SuppressWarnings("deprecation")
	@Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    addPreferencesFromResource(R.xml.preferences);	  }

}
