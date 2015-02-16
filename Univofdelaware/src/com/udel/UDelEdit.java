package com.udel;

import java.util.ArrayList;

import com.ud.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UDelEdit extends Activity {

    private EditText mNameText;
    private EditText mCodeText;

    private EditText mLongitude;
    private EditText mLatitude;

    private Long mRowId;
   // private String mName;
    private DbAdapter mDbHelper;
    
	Button buttonAdd;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new DbAdapter(this);
        mDbHelper.open();

        setContentView(R.layout.udel_edit);
        setTitle(R.string.edit_note);

        mCodeText = (EditText) findViewById(R.id.code);
        mNameText = (EditText) findViewById(R.id.name);
        mLongitude = (EditText) findViewById(R.id.longitude);
        mLatitude =  (EditText) findViewById(R.id.latitude);
        


        Button confirmButton = (Button) findViewById(R.id.confirm);
        Button getDirection = (Button) findViewById(R.id.directions);
        
        mRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(DbAdapter.KEY_ROWID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(DbAdapter.KEY_ROWID)
									: null;
		}
    

		populateFields();

        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }

        });
    
    
    getDirection.setOnClickListener (new View.OnClickListener(){

          public void onClick(View arg) {
	   String lat2, long2, name2;

    String data1 = mCodeText.getText().toString();

    ArrayList<String>latlong = mDbHelper.search(data1);
    
    lat2 = latlong.get(0);
    long2 = latlong.get(1);
    name2 =  latlong.get(2);

    
    Intent i = new Intent(getBaseContext(), UDMap.class);

	i.putExtra("latitude2",lat2);
	i.putExtra("longitude2",long2);;
	i.putExtra("building2", name2);
	startActivity(i);
   }
});
    }

    private void populateFields() {
        if (mRowId != null) {
            Cursor note = mDbHelper.fetchNote(mRowId);
            startManagingCursor(note);
            mCodeText.setText(note.getString(
                    note.getColumnIndexOrThrow(DbAdapter.KEY_CODE)));
            mNameText.setText(note.getString(
                    note.getColumnIndexOrThrow(DbAdapter.KEY_NAME)));
            mLatitude.setText(note.getString(note.getColumnIndexOrThrow(DbAdapter.KEY_LATITUDE)));
            mLongitude.setText(note.getString(note.getColumnIndexOrThrow(DbAdapter.KEY_LONGITUDE)));
        
        }
    }
    

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(DbAdapter.KEY_ROWID, mRowId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }

    private void saveState() {
        Data d1 = new Data();
        d1.code = mCodeText.getText().toString();
        d1.name = mNameText.getText().toString();
        d1.latitude = Double.parseDouble(mLatitude.getText().toString());
        d1.longitude = Double.parseDouble(mLongitude.getText().toString());

        

        if (mRowId == null) {
            long id = mDbHelper.write(d1);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            mDbHelper.updateNote(mRowId, d1.code, d1.name, d1.latitude, d1.longitude);
        }
    }

}