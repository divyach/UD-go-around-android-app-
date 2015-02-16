package com.udel;


import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DbAdapter {
	

    public static final String KEY_CODE = "code";
	public static final String KEY_NAME = "name";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_ROWID = "_id";

	private static final String LOGCAT = null;
	private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private final Context mCtx;
    
private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
        	
           super(context, "UDMAP222", null, 1);
           
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        	
        	String query;
	        query =  "create table UDbuilding (_id integer primary key autoincrement, "+ "code text not null, "
	    	        + "name text not null, "+"latitude real not null, "+"longitude real not null);";
	        db.execSQL(query);
	        Log.d(LOGCAT,"UDbuilding Created");

	       
	        Log.d(LOGCAT,"inserting");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { String query;
        query = "DROP TABLE IF EXISTS UDbuilding";
        db.execSQL(query);
        onCreate(db);
	}
    }
    
    public DbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }
    public long createBuilding(String code, String name, double latitude, double longitude) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CODE, code);
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_LATITUDE, latitude);
        initialValues.put(KEY_LONGITUDE, longitude);

        return mDb.insert("UDbuilding", null, initialValues);
    }

    public long write(Data d) {
        ContentValues initialValues = new ContentValues();
     
        initialValues.put(KEY_CODE, d.code);
        initialValues.put(KEY_NAME, d.name);
        initialValues.put(KEY_LATITUDE, d.latitude);
        initialValues.put(KEY_LONGITUDE, d.longitude);



        return mDb.insert("UDbuilding", null, initialValues);
    }
    
    public boolean deleteNote(long rowId) {

        return mDb.delete("UDbuilding", KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    public Cursor fetchAllData() {

        return mDb.query("UDbuilding", new String[] {"_id",KEY_CODE,KEY_NAME,KEY_LATITUDE,KEY_LONGITUDE
             }, null, null, null, null, null);
    }
    
    public Cursor fetchAllData(String value) {

        String query = "select _id"+","+KEY_CODE+","+KEY_NAME+","+KEY_LATITUDE+","+KEY_LONGITUDE+" from UDbuilding"+
        		" where "+KEY_NAME+" like '%"+value+"%' or "+KEY_CODE+" like '%"+value+"%' ";
    	return mDb.rawQuery(query, null);
    }
    
    public String getCode(long rowId)
    {
    	String query = "select "+KEY_CODE+" from UDbuilding where "+KEY_ROWID+"='"+rowId+"';";
    	Cursor c = mDb.rawQuery(query,null);
    	if(c.getCount()>0)
    	{	
    		c.moveToFirst();
    		return c.getString(0);
    	}
    	return null;
    }
    
    public Cursor fetchNote(long rowId) throws SQLException {

        Cursor mCursor =

            mDb.query(true, "UDbuilding", new String[] {KEY_CODE,KEY_NAME, KEY_LATITUDE, KEY_LONGITUDE,
                    }, KEY_ROWID + "=" + rowId, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    
    public boolean updateNote(long rowId, String code, String name, Double latitude, Double longitude ) {
        ContentValues args = new ContentValues();
        args.put(KEY_CODE, code);
        args.put(KEY_NAME, name);
        args.put(KEY_LATITUDE, latitude);
        args.put(KEY_LONGITUDE, longitude);


        return mDb.update("UDbuilding", args, KEY_ROWID + "=" + rowId, null) > 0;
    }
 
  
    public ArrayList<String> search(String code)
	 {
		 ArrayList<String> latlong = new ArrayList<String>();
        Cursor c1 =mDbHelper.getReadableDatabase().query("UDbuilding",new String[]{"_id","code","name","latitude","longitude"}, "code"+" = '"+code+"'", null, null, null,null);
        if(c1 != null) 
        {
           while(c1.moveToNext()) {
       	 latlong.add(c1.getString(3));
            latlong.add(c1.getString(4));
            latlong.add(c1.getString(2));
        }
          
        }
        
        return latlong;
                 
	 }
    
}
