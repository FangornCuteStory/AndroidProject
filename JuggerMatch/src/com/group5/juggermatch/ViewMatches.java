package com.group5.juggermatch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewMatches extends Activity {
	
	private DatabaseOperations db;
	List<Match> matches = new ArrayList<Match>(); 
    
 //   final private static int VIEW_MATCH_INFO = 1;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_match);
		db = new DatabaseOperations(ViewMatches.this);
		db.open();
		
		populateListOfMatches();
        populateListView();
		reactionOnItemClick();		
		
	}
	
//adapter for the listview item
	private class MatchListAdapter extends ArrayAdapter<Match> {
		public MatchListAdapter() {
			super(ViewMatches.this, R.layout.item_for_list_view, matches);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// 
			View itemView = convertView;

			if (itemView == null) {
				itemView = getLayoutInflater().inflate(R.layout.item_for_list_view, parent, false);
			}

			// Find the match to work with.
			Match currentMatch = matches.get(position);
			// Fill the view
			
			// Team1:
			TextView team1_txt = (TextView) itemView.findViewById(R.id.item_team1);
			team1_txt.setText(currentMatch.getTeam1());
			
			// Team2:
			TextView team2_txt = (TextView) itemView.findViewById(R.id.item_team2);
			team2_txt.setText(currentMatch.getTeam2());
					
			SimpleDateFormat day_formatter = new SimpleDateFormat("EEE", Locale.getDefault());
			SimpleDateFormat date_formatter = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());       
			
			
			// Day of the week:			
			TextView day_txt = (TextView) itemView.findViewById(R.id.item_match_day);
			day_txt.setText(day_formatter.format(new Date(currentMatch.getStartTime())));
			
			//Date:
			TextView date_txt = (TextView) itemView.findViewById(R.id.item_match_date);
			date_txt.setText(date_formatter.format(new Date(currentMatch.getStartTime())));
				
			//Scores:
			TextView score1_txt = (TextView) itemView.findViewById(R.id.item_score_team1);
			score1_txt.setText("" + currentMatch.getScoreTeam1());
			
			TextView score2_txt = (TextView) itemView.findViewById(R.id.item_score_team2);
			score2_txt.setText("" + currentMatch.getScoreTeam2());
			
			TextView location_txt = (TextView) itemView.findViewById(R.id.item_match_location);
			location_txt.setText(currentMatch.getLocation());
			
			return itemView;
		}

	}
	
	@Override
	public void onRestart(){
		super.onRestart();
         
		  populateListOfMatches();
          populateListView();
          reactionOnItemClick();									   
   }

	private void populateListOfMatches() {
		//populate from db 
		String message;
		db = new DatabaseOperations(ViewMatches.this);
		db.open();			
		matches = db.getAllMatches();		
		db.close();
		if (matches.size() == 0)
	    { 
			message = "Database is empty, please insert matches results !";
		    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();	
		};
	}
	

	private void populateListView() {
		// 
		ArrayAdapter<Match> adapter = new MatchListAdapter();
		ListView list = (ListView) findViewById(R.id.list_of_matches);
		list.setAdapter(adapter);
		
	}

	private void reactionOnItemClick() {
		// 
		//When any list item (module) is clicked 
		
		ListView list = (ListView) findViewById(R.id.list_of_matches);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked,
					int position, long id) {
				Match clickedMatch = matches.get(position);
				String message;
				long match_id = clickedMatch.getId();
				message = "position" + position + " is clicked, match id: " + match_id;
				Toast.makeText(ViewMatches.this, message, Toast.LENGTH_LONG).show();
				db.open();
				Match currentMatch = db.getMatch(match_id);
				db.close();
                 	}

		});
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_match, menu);
		return true;
	}
	
	
	
	

}
