package com.udel;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import com.ud.R;
import com.udel.DbAdapter;
import com.udel.UDelEdit;


import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class UDel extends ListActivity {
	private static final String LOGCAT = null;

	private static final int ACTIVITY_CREATE=0;
	private static final int ACTIVITY_EDIT=1;
	

	private static final int INSERT_ID = Menu.FIRST;
	private static final int DELETE_ID = Menu.FIRST + 1;

	private DbAdapter mDbHelper;
	Context context;

	int size = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	    
		setContentView(R.layout.building_list);
	
	    
	     
		File sdDir = Environment.getExternalStorageDirectory();
		String pospath = sdDir.getAbsolutePath()+"/UDBuildingPositions.txt";
		try
		{
			mDbHelper = new DbAdapter(this);
	        mDbHelper.open();
	        

	        try
	        {
	        	File file = new File("/sdcard/new1.txt");
	        	if(!file.exists()) 
	        	{
	        		file.createNewFile();
	        		        	/*BufferedReader in=new BufferedReader(new InputStreamReader((InputStream)
	        			context.getResources().openRawResource(R.raw.udbuildingpos)));*/
	    		FileInputStream fstream = new FileInputStream(pospath);
	    		DataInputStream in= new DataInputStream(fstream);
	    		BufferedReader br= new BufferedReader(new InputStreamReader(in));
	        		String line;   
	        		String code, name;
	        		double latitude,longitude;
	        		while ((line = in.readLine()) != null) 
	        		{
	        			String[] var = line.split(":");
	        			code = var[0];
	        			name = var[1];
	        			latitude = Double.valueOf(var[2]);
	        			longitude = Double.valueOf(var[3]);
	        			
	        			mDbHelper.createBuilding(code, name, latitude, longitude);
	        
	        		
	        		}Log.d(LOGCAT,"INSERTED");
	        		in.close();
	        	//}
	        		
	        	//fillData();
	        }
	        }
	        catch(NotFoundException ne)
	        {
	        	ne.printStackTrace();
	        }
	        catch(Exception e)
	        {
	        	e.printStackTrace(System.out);
	        }
	        
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		
		Button searchButton = (Button) findViewById(R.id.searchbtn);		
		searchButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				EditText searchtext = (EditText) findViewById(R.id.search);
				fillData(searchtext.getText().toString());
				
			}

		});

		fillData();
		registerForContextMenu(getListView());
	}

	
	private void fillData() {
		Cursor buildingsCursor = mDbHelper.fetchAllData();
		startManagingCursor(buildingsCursor);

		// Create an array to specify the fields we want to display in the list (only TITLE)
		String[] from = new String[]{DbAdapter.KEY_NAME,DbAdapter.KEY_CODE, DbAdapter.KEY_LATITUDE, DbAdapter.KEY_LONGITUDE};

		// and an array of the fields we want to bind those fields to (in this case just text1)
		int[] to = new int[]{R.id.text1/*,R.id.text2,R.id.text3,R.id.text4,R.id.text5,R.id.text6*/};

		// Now create a simple cursor adapter and set it to display
		SimpleCursorAdapter buildings = 
				new SimpleCursorAdapter(this, R.layout.building_row, buildingsCursor, from, to);
		setListAdapter(buildings);
	}

	private void fillData(String value) {
		Cursor buildingsCursor = mDbHelper.fetchAllData(value);
		startManagingCursor(buildingsCursor);

		// Create an array to specify the fields we want to display in the list (only TITLE)
		String[] from = new String[]{DbAdapter.KEY_NAME,DbAdapter.KEY_CODE, DbAdapter.KEY_LATITUDE, DbAdapter.KEY_LONGITUDE};

		// and an array of the fields we want to bind those fields to (in this case just text1)
		int[] to = new int[]{R.id.text1/*,R.id.text2,R.id.text3,R.id.text4,R.id.text5,R.id.text6*/};

		// Now create a simple cursor adapter and set it to display
		SimpleCursorAdapter buildings = 
				new SimpleCursorAdapter(this, R.layout.building_row, buildingsCursor, from, to);
		setListAdapter(buildings);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, INSERT_ID, 0, R.string.menu_insert);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()) {
		case INSERT_ID:
			createNote();
			return true;
		}

		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DELETE_ID, 0, R.string.menu_delete);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case DELETE_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			mDbHelper.deleteNote(info.id);
			fillData();
			return true;
		
		
		}
		return super.onContextItemSelected(item);
	}

	private void createNote() {
		Intent i = new Intent(this, UDelEdit.class);
		startActivityForResult(i, ACTIVITY_CREATE);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(this, UDelEdit.class);
		i.putExtra(DbAdapter.KEY_ROWID, id);
		startActivityForResult(i, ACTIVITY_EDIT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		fillData();}
		
		
	}
	
		
	

