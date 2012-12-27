package com.mos7af.mp3quran;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "mp3quran";
 
    // Labels table name
    private static final String TABLE_LABELS = "bf2_playlists";
 
    // Labels Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_PLAYLISTS_NAME = "playlists_name";
    private static final String KEY_PLAYLISTS_TIME = "playlists_added_time";
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
    	// Category table create query
    	String CREATE_PLAYLISTS_TABLE = "CREATE TABLE IF NOT EXISTS bf2_playlist_suras (" +
    			"vid INTEGER PRIMARY KEY AUTOINCREMENT, " +
    			"reciterId TEXT, " +
    			"playlistId TEXT," +
    			"suraId TEXT," +
    			"suraNameAr TEXT," +
    			"suraNameEn TEXT," +
    			"suraPlaceLookupAr TEXT," +
    			"suraPlaceLookupEn TEXT," +
    			"suraSoundPath TEXT," +
    			"reciterNameAr TEXT," +
    			"reciterNameEn TEXT," +
    			"reciterImage TEXT" +
    			");" ;
    	db.execSQL(CREATE_PLAYLISTS_TABLE);	
    	String CREATE_PLAYLISTSSuras_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_LABELS + "("
        		+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_PLAYLISTS_NAME + " TEXT," + KEY_PLAYLISTS_TIME + " TEXT)";
    	db.execSQL(CREATE_PLAYLISTSSuras_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LABELS);
 
        // Create tables again
        onCreate(db);
    }
    
    /**
     * Inserting new lable into lables table
     * */
    public void insertPlaylist(String label){
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	ContentValues values = new ContentValues();
    	values.put(KEY_PLAYLISTS_NAME, label);
    	values.put(KEY_PLAYLISTS_TIME, "10-10-2010");
    	// Inserting Row
        db.insert(TABLE_LABELS, null, values);
        
        db.close(); // Closing database connection
    }
    
    public void UpdatePlaylist(String id , String label){
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	ContentValues values = new ContentValues();
    	values.put(KEY_PLAYLISTS_NAME, label);
        db.update(TABLE_LABELS, values, KEY_ID+"="+id, null);
        
        db.close(); // Closing database connection
    }
    /**
     * Getting all labels
     * returns list of labels
     * */
    public ArrayList<HashMap<String, String>> getAllPlaylists()
    {
		
    	ArrayList<HashMap<String, String>> surasList = new ArrayList<HashMap<String, String>>();
    	
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LABELS;
     
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	
            	HashMap<String, String> map = new HashMap<String, String>();
            	
        		map.put("playlistId", cursor.getString(0));
        		map.put("playlistName", cursor.getString(1));
        	
        		surasList.add(map);
            } while (cursor.moveToNext());
        }
        
        // closing connection
        cursor.close();
        db.close();
    	
    	// returning lables
    	return surasList;
    }
    public boolean  deletePlaylist(String playlistId)
    {
    	boolean result = false;
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete("bf2_playlist_suras","playlistId="+playlistId, null);
        result =  db.delete(TABLE_LABELS, KEY_ID + "="+playlistId, null)>0;
        db.close();
        return result;
    	
    }
    public boolean  deletePlaylistSura(String playlistId,String suraId)
    {
    	boolean result = false;
        SQLiteDatabase db = this.getReadableDatabase();
        result=  db.delete("bf2_playlist_suras","playlistId="+playlistId + " AND suraId=" +suraId , null)>0;
        db.close();
        return result;
    	
    }
   
    
    public void InsertPlayListSura(HashMap<String, String> sura,String playlistId)
	{
    	
		SQLiteDatabase db = this.getWritableDatabase();
    	ContentValues values = new ContentValues();
    	values.put("reciterId", sura.get("reciterId"));
    	values.put("playlistId", playlistId);
    	values.put("suraId", sura.get("suraId"));
    	values.put("suraNameAr", sura.get("suraNameAr"));
    	values.put("suraNameEn", sura.get("suraNameEn"));
    	values.put("suraPlaceLookupAr", sura.get("suraPlaceLookupAr"));
    	values.put("suraPlaceLookupEn", sura.get("suraPlaceLookupEn"));
    	values.put("suraSoundPath", sura.get("suraSoundPath"));
    	values.put("reciterNameAr", sura.get("reciterNameAr"));
    	values.put("reciterNameEn", sura.get("reciterNameEn"));
    	values.put("reciterImage", sura.get("reciterImage"));
    	
        db.insert("bf2_playlist_suras", null, values);
        
        db.close();
			
		
	}
    public ArrayList<HashMap<String, String>> getPlaylistSuras(String playlistId)
    {
    	ArrayList<HashMap<String, String>> surasList = new ArrayList<HashMap<String, String>>();
	    	
        
        String selectQuery = "select  * from  bf2_playlist_suras WHERE playlistId = " + playlistId;
     
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	
            	HashMap<String, String> sura = new HashMap<String, String>();
            	
            	sura.put("reciterId", cursor.getString(1));
            	sura.put("playlistId", cursor.getString(2));
            	sura.put("suraId", cursor.getString(3));
        		sura.put("suraNameAr", cursor.getString(4));
        		sura.put("suraNameEn", cursor.getString(5));
        		sura.put("suraPlaceLookupAr", cursor.getString(6));
        		sura.put("suraPlaceLookupEn", cursor.getString(7));
        		sura.put("suraSoundPath", cursor.getString(8));
        		sura.put("reciterNameAr", cursor.getString(9));
        		sura.put("reciterNameEn", cursor.getString(10));
        		sura.put("reciterImage", cursor.getString(11));
        	
        		surasList.add(sura);
            } while (cursor.moveToNext());
        }
        
        // closing connection
        cursor.close();
        db.close();
    	
    	// returning lables
    	return surasList;
    }
    
}
