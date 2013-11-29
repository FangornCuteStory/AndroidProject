package com.group5.juggermatch;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper{	
	//SQLiteOpenHelper is a helper class in Android to manage database creation and version management
	// All static variables
    // Database version
    private static final int DATABASE_VERSION = 2;
 
    // Database name
    private static final String DATABASE_NAME = "jugger_matches";
 
    // Table name
    static final String DATABASE_TABLE = "matches";
 
    // Modules table columns names
     static final String KEY_ID = "_id";
     static final String KEY_TEAM1 = "team_1";
     static final String KEY_TEAM2 = "team_2";
     static final String KEY_SCORE_TEAM1 = "score_team_1";
     static final String KEY_SCORE_TEAM2 = "score_team_2";
     static final String KEY_START_TIME_STAMP= "start_time";
     static final String KEY_END_TIME_STAMP = "end_time";
     static final String KEY_LOCATION = "location";
    
  // Database creation SQL statement
     private static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + "(" + 
                           KEY_ID + " integer primary key autoincrement, " + 
    		               KEY_TEAM1 + " text not null, " + 
                           KEY_TEAM2 + " text not null, " +
    		               KEY_SCORE_TEAM1 + " integer not null, " +
                           KEY_SCORE_TEAM2 + " integer not null, " + 
    		               KEY_START_TIME_STAMP + " integer not null," +
                           KEY_END_TIME_STAMP + " integer not null," +
                           KEY_LOCATION + " text);";
    
    public DatabaseHelper(Context context) {	
    	super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
    	//Creates a new database if the required database is not present		
		db.execSQL(DATABASE_CREATE);
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Method is called when the database needs to be upgraded
		Log.w(DatabaseHelper.class.getName(),
		        "Upgrading database from version " + oldVersion + " to "
		            + newVersion + ", which will destroy all old data");
		    db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
		    onCreate(db);
	}

}
