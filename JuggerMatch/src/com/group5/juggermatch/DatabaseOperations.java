package com.group5.juggermatch;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseOperations {

	private static final String TAG = "JUGGER";
	private SQLiteDatabase db;	
	private DatabaseHelper dbHelper;
	private String[] allColumns = {DatabaseHelper.KEY_ID,
			                       DatabaseHelper.KEY_TEAM1, 
			                       DatabaseHelper.KEY_TEAM2,
			                       DatabaseHelper.KEY_SCORE_TEAM1,
			                       DatabaseHelper.KEY_SCORE_TEAM2, 
			                       DatabaseHelper.KEY_START_TIME_STAMP,
			                       DatabaseHelper.KEY_END_TIME_STAMP,
			                       DatabaseHelper.KEY_LOCATION
	                              };

	
	public DatabaseOperations(Context context) {		
	    dbHelper = new DatabaseHelper(context);
	  }

	//--- opens the database ---	
	  public DatabaseOperations open() throws SQLException {
	    db = dbHelper.getWritableDatabase();
	    return this;
	  }
	//--- closes the database ---
	  public void close() {
	    dbHelper.close();
	  }
	
	//--- insert new record about the match into the database ---	  
		public long insertMatch(Match match){
			//returns the id of the inserted row; if an error occurs it returns -1 
		    ContentValues values = new ContentValues();
			values.put(DatabaseHelper.KEY_TEAM1, match.getTeam1());
		    values.put(DatabaseHelper.KEY_TEAM2, match.getTeam2());
		    values.put(DatabaseHelper.KEY_SCORE_TEAM1, match.getScoreTeam1());
			values.put(DatabaseHelper.KEY_SCORE_TEAM2, match.getScoreTeam2());
			values.put(DatabaseHelper.KEY_START_TIME_STAMP, match.getStartTime());
			values.put(DatabaseHelper.KEY_END_TIME_STAMP, match.getEndTime());
			values.put(DatabaseHelper.KEY_LOCATION, match.getLocation());
		Log.d(TAG,"insertMatch: " + values);	  
			return db.insert(DatabaseHelper.DATABASE_TABLE, null,values);		  		  		  
		  }
		
		//--- delete chosen match from the database ---
	    public boolean deleteMatch(Match match) {
	      //if return value=1 deletion is successful, otherwise deletion failed 		  
			  long id = match.getId();
	          return db.delete(DatabaseHelper.DATABASE_TABLE, DatabaseHelper.KEY_ID + " = " + id, null) > 0;          
		  }
	    
	    //Method override from Carl to fix error
	    
	    public boolean deleteMatch(long id) {
		      //if return value=1 deletion is successful, otherwise deletion failed 		  
		          return db.delete(DatabaseHelper.DATABASE_TABLE, DatabaseHelper.KEY_ID + " = " + id, null) > 0;          
			  }

	    
	  //--- get single match from the database ---
	    public Match getMatch(long id){
	    	Cursor cursor = db.query(DatabaseHelper.DATABASE_TABLE, allColumns, DatabaseHelper.KEY_ID + "=" + id, null, null, null, null, null);	
	    	if (cursor != null)
	            cursor.moveToFirst();
	    	Match match = cursorToMatch(cursor);
	        return match;
	    }
	    
	        public List<Match> getAllMatches() {
			  List<Match> matches = new ArrayList<Match>();
			  Cursor cursor = db.query(DatabaseHelper.DATABASE_TABLE, allColumns,
	                                 null, null, null, null, null);
			  cursor.moveToFirst();
			  while (!cursor.isAfterLast()) {
		     Match match = cursorToMatch(cursor);
			     matches.add(match);                                               
			     cursor.moveToNext();
			  }		    
			  cursor.close();    //close the cursor	
			  return matches;
		}  
	    

		private Match cursorToMatch(Cursor cursor) {
			// data of the list from database
			Match match = new Match();

			match.setId(cursor.getLong(0));
			match.setTeam1(cursor.getString(1));
		    match.setTeam2(cursor.getString(2));
		    match.setScoreTeam1(cursor.getInt(3));
		    match.setScoreTeam2(cursor.getInt(4));
		    match.setStartTime(cursor.getLong(5));
		    match.setEndTime(cursor.getLong(6));
		    match.setLocation(cursor.getString(7));
		    return match;
		}
		  
		//--- update chosen match ---
		public boolean updateMatch(Match match) {
			ContentValues values = new ContentValues();
			long id = match.getId();
			values.put(DatabaseHelper.KEY_TEAM1, match.getTeam1());
		    values.put(DatabaseHelper.KEY_TEAM2, match.getTeam2());
		    values.put(DatabaseHelper.KEY_SCORE_TEAM1, match.getScoreTeam1());
			values.put(DatabaseHelper.KEY_SCORE_TEAM2, match.getScoreTeam2());
			values.put(DatabaseHelper.KEY_START_TIME_STAMP, match.getStartTime());
			values.put(DatabaseHelper.KEY_END_TIME_STAMP, match.getEndTime());
			values.put(DatabaseHelper.KEY_LOCATION, match.getLocation());
			
			return db.update(DatabaseHelper.DATABASE_TABLE, values,DatabaseHelper.KEY_ID  + " = " + id, null) > 0;
		}	
		
		public int getMatchsCount() {
	        String countQuery = "SELECT  * FROM " + DatabaseHelper.DATABASE_TABLE;
	        Cursor cursor = db.rawQuery(countQuery, null);
	        cursor.close();
	 
	        // return count
	        return cursor.getCount();
	    }
	
}
