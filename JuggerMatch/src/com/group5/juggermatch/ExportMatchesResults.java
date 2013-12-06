package com.group5.juggermatch;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
//import android.app.ProgressDialog;
import android.app.ProgressDialog;


public class ExportMatchesResults extends Activity {
  
	private DatabaseOperations db; 
	private static final String TAG = "JUGGER";
	 List<Match> matches = new ArrayList<Match>();

	 
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_export_matches_results);
		
		db = new DatabaseOperations(this);
		db.open();
		
		populateListOfMatches();
        populateListView();
		reactionOnItemClick();
	}

	//adapter for the listview item
		private class MatchListAdapter extends ArrayAdapter<Match> {
			public MatchListAdapter() {
				super(ExportMatchesResults.this, R.layout.item_for_list_view_checked, matches);
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// 
				View itemView = convertView;

				if (itemView == null) {
					itemView = getLayoutInflater().inflate(R.layout.item_for_list_view_checked, parent, false);
				}

				// Find the match to work with.
				Match currentMatch = matches.get(position);
				Log.d(TAG, "In MatchListAdapter item position " + position); 
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
				day_txt.setText(day_formatter.format(new Date(currentMatch.getStartTime())) + ",");
				
				//Date:
				TextView date_txt = (TextView) itemView.findViewById(R.id.item_match_date);
				date_txt.setText(date_formatter.format(new Date(currentMatch.getStartTime())) + ",");
					
				//Scores:
				TextView score1_txt = (TextView) itemView.findViewById(R.id.item_score_team1);
				score1_txt.setText("" + currentMatch.getScoreTeam1());
				
				TextView score2_txt = (TextView) itemView.findViewById(R.id.item_score_team2);
				score2_txt.setText("" + currentMatch.getScoreTeam2());
				
				TextView location_txt = (TextView) itemView.findViewById(R.id.item_match_location);
				location_txt.setText(currentMatch.getLocation());
				
				Log.d(TAG, "In MatchListAdapter location: " + currentMatch.getLocation());
				ImageView check_box_img = (ImageView) itemView.findViewById(R.id.img);
			    
		        if (currentMatch.isChecked())
            	{
            		check_box_img.setImageResource(R.drawable.check_box_on_green);
            	}
            	else
            	{
            		
            		check_box_img.setImageResource(R.drawable.check_box_off);
            	}
				//Log.d(TAG, "In MatchListAdapter after findViewById(R.id.img): ");
			 
			 	//check_box_img.setImageResource(R.drawable.check_box_on);
				//check_box_img.setBackgroundResource(R.drawable.check_box_on);
		
				
				//Log.d(TAG, "In MatchListAdapter after " );
				return itemView;
			}

		}
				
		
		@Override
		public void onRestart(){
			super.onRestart();
	         
			  populateListOfMatches();
	          populateListView();
	          //reactionOnItemClick();									   
	   }
		
		

		private void populateListOfMatches() {
			//populate from db 
			String message;
			db = new DatabaseOperations(ExportMatchesResults.this);
			db.open();			
			matches = db.getAllMatches();		
			db.close();
			if (matches.size() == 0)
		    { 
				message = getResources().getString(R.string.empty_database);						
			    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();	
			}
			else
			{
				 int list_length = matches.size();
				 Log.d(TAG,"inPopulateListOfMatches, size of list: " + list_length);
			}
	
		}
		

		private void populateListView() {
			 
			MatchListAdapter adapter = new MatchListAdapter();  
			ListView list = (ListView) findViewById(R.id.list_of_matches);
			list.setAdapter(adapter);
			
		}

		private void reactionOnItemClick() {
			// 
			//When any list item is clicked 
			
			ListView list = (ListView) findViewById(R.id.list_of_matches);
			list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			list.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View itemClicked,
                            int position, long id)
                    {
                    	ImageView check_box_img = (ImageView) itemClicked.findViewById(R.id.img);    
                    	Match match = matches.get(position);
                    	if (match.isChecked())
                    	{
                    		match.setChecked(false);
                    		check_box_img.setImageResource(R.drawable.check_box_off);
                    	}
                    	else
                    	{
                    		match.setChecked(true);
                    		check_box_img.setImageResource(R.drawable.check_box_on_green);
                    	}
                    	
                  	    db = new DatabaseOperations(ExportMatchesResults.this);
            			db.open();	
            			db.updateMatch(match);
            			db.close();  

                  		    /*Toast.makeText(ExportMatchesResults.this,
                  		      "Clicked on Row with match id: " + match.getId(),
                  		      Toast.LENGTH_LONG).show();*/
                    }
                });	
			
		}
		
		
		
		private void exportSelectedItems()
		{
			String jsonArr = "["; // Mark the beginning of JSON array 
			String comma = ""; // initialize as empty
			String toastMsg = "";
			String responseString = "";
			int c = matches.size();
			
			int c1 = 0;
			for (int i = 0; i < c; i++)
			{
				Match match = matches.get(i);
				
				if (match.isChecked())
				{
					jsonArr = jsonArr + comma
							+ "{\"team_1\":\"" + match.getTeam1() + "\","
							+ "\"team_2\":\"" + match.getTeam2() + "\","
							+ "\"score_team_1\":\"" + match.getScoreTeam1() + "\","
							+ "\"score_team_2\":\"" + match.getScoreTeam2() + "\","
							+ "\"start_time\":\"" + match.getStartTime() + "\","
							+ "\"end_time\":\"" + match.getEndTime() + "\","
							+ "\"location\":\"" + match.getLocation() +"\""
							+ "}";
					comma = ","; // set comma so it gets added at the start of next {} entry
					
					c1++;
				}
			}
			jsonArr = jsonArr + "]"; // Close JSON array at the end
			db = new DatabaseOperations(ExportMatchesResults.this);
			db.open();	
			db.setAllToUnchecked();
			db.close();    
			//Log.d(TAG,"jsonArr: " + jsonArr);
			if (c1 > 0) {
            	// Now send it to the remote site with help of HTTP client
            	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            	StrictMode.setThreadPolicy(policy); 
            	HttpClient httpClient = new DefaultHttpClient();
            	String url = getResources().getString(R.string.remote_DB_url);
            	HttpPost httpPost = new HttpPost(url); // NB, 10.0.2.2 is localhost
            	//List <NameValuePair> nvps = new ArrayList <NameValuePair>();
            	//nvps.add(new BasicNameValuePair("value", jsonArr));
            	try {
				//httpPost.setEntity(new UrlEncodedFormEntity(nvps , HTTP.UTF_8));
	        	//jsonArr = "{\"team_1\":\"value\"}";
	        	StringEntity se = new StringEntity(jsonArr, HTTP.UTF_8);
		        httpPost.setEntity(se);
            	} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
            	}  
            	try {
            		HttpResponse response = httpClient.execute(httpPost);
            		responseString = "\n" +EntityUtils.toString(response.getEntity(), "UTF-8");
            		// writing response to log
            		Log.d("Http Response:", responseString);
            	} catch (ClientProtocolException e) {
            		// writing exception to log
            		e.printStackTrace();
            	} catch (IOException e) {
            		// writing exception to log
            		e.printStackTrace();
            	}
            	
            	toastMsg = "Exporting "+c1+" items";
            }
            else {
            	toastMsg = "Nothing to export";
            }
			
			Toast.makeText(ExportMatchesResults.this,toastMsg + responseString,Toast.LENGTH_LONG).show();		
			//	return toastMsg;
       }
	
		
		public void onClickButton(View view){
			
			switch(view.getId()) {
		      case R.id.button_export:      			
		    /*     String title = getResources().getString(R.string.prog_dialog_title);
		          String message= getResources().getString(R.string.please_wait);
		    	  final ProgressDialog dialog = ProgressDialog.show(this, title, message);
		    	  new Thread(new Runnable(){
		    		  public void run(){
		    			  try{
		    		String message=exportSelectedItems();
		    			   dialog.dismiss();
		    			   Toast.makeText(ExportMatchesResults.this,message,Toast.LENGTH_LONG).show();	   
		    			   
		    			  }catch(Exception e) {
		    				  e.printStackTrace();
		    			  }
		    		  }		  
		    	  }).start();  */
		    	  
		    	  exportSelectedItems();
		    	  finish();
		    	  break;
		       case R.id.button_back:
		    	  finish(); 
		          break;
			}    
	  }

		
}
