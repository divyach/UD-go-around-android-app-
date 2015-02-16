package com.udel;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;



import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import android.app.Activity;
import android.app.ProgressDialog;

import android.os.Bundle;
import android.os.Handler;

import android.view.Menu;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ud.R;

public class Calender extends Activity {
	ListView lv;
	   
	    private String rssFeed = "http://events.udel.edu/calendar.xml?event_types[]=8128";
	    private TextView textview;
		ArrayList<String> myEvents;
		ArrayAdapter<String> listEvents;
		final Handler mHandler = new Handler();
	    
	    @Override
	    protected void onCreate(Bundle savedInstanceState) 
	    {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.site);
	        lv = (ListView) findViewById(R.id.listview1);
	    	myEvents = new ArrayList<String>();
	    	
	       
	        startLongRunningOperation();

		}
	    

		// Create runnable for posting
		final Runnable mUpdateResults = new Runnable() {
			public void run() {
				updateResultsInUi();
			}
		};

		private void updateResultsInUi() {
			listEvents = new ArrayAdapter<String>(Calender.this,android.R.layout.simple_list_item_1, myEvents);
			lv.setAdapter(listEvents);

			System.out.println("binding");
		}
		protected void startLongRunningOperation() {
			Thread downloadThread = new Thread() {
				public void run() {
					Document doc;
					try {
						doc = Jsoup.connect(rssFeed).get();
						Elements spans = doc.select("title");
					

						for (Element title : spans) {
							myEvents.add(title.text());
						}
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					mHandler.post(mUpdateResults);
				}
			};
			downloadThread.start();
		}
		
	
}
		