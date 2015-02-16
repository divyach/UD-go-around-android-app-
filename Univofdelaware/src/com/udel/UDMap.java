package com.udel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ud.R;

public class UDMap extends FragmentActivity implements TextToSpeech.OnInitListener{
	
	private GoogleMap googleMap;
	private LocationManager locationManager;
	private DbAdapter mDbHelper;
	private TextToSpeech texttospeech;
   // private String provider;
    
    double latitude1;
    double longitude1;
    double latitude2;
    double longitude2;
    String src;
    String dest;
    String lat1;
    String long1;
    String lat2;
    String long2;
   
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		handleIntent(getIntent());
		texttospeech = new TextToSpeech(this, this);

		 
		
		 Button Speech= (Button) findViewById(R.id.buttonspeakDirections);
		 Speech.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				speakOut(src,dest);
			}
		});
		
		Button Satview = (Button) findViewById(R.id.buttonSatview);		
		Satview.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				ShowSat(googleMap);
			
				
			}});
			

			Button Mapview = (Button) findViewById(R.id.buttonMapview);		
			Mapview.setOnClickListener(new View.OnClickListener() {

				public void onClick(View view) {
					ShowMap(googleMap);
					
				}
		});
		
		
       
		
	}
	public void onDestroy() {
	      
        if (texttospeech != null) {
        	texttospeech.stop();
        	texttospeech.shutdown();
        }
        super.onDestroy();
    }
	
	public void onInit(int arg0) {
		// TODO Auto-generated method stub
		 if (arg0 == TextToSpeech.SUCCESS) {
			 
	            int result = texttospeech.setLanguage(Locale.US);
	 
	            if (result == TextToSpeech.LANG_MISSING_DATA
	                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
	                Log.e("TTS", "This Language is not supported");
	            } 
	            else 
	            {
	                speakOut(src, dest);
	            }
	 
	        } 
	        else 
	        {
	            Log.e("text to speech", "failed");
	        }
		
	}
	
	 public void speakOut(String source, String destination) {
		 
	        String text = ""+ source +" .Your destination is "+ destination +"";
	        //String text2= "your destination is "+ destination;
	 
	        texttospeech.speak(text, 2, null);
	        //tts.speak(text2, 2, null);
	}
	protected void ShowSat(GoogleMap googleMap2) {
		
		 googleMap2.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		
		// TODO Auto-generated method stub
		
	}
	protected void ShowMap(GoogleMap googleMap2) {
		
		 googleMap2.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		
		// TODO Auto-generated method stub
		
	}
	@Override
    protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);      
	    setIntent(intent);
	    handleIntent(intent);
    }
	
	private void handleIntent(Intent intent) {

        //destination
        lat1 = intent.getStringExtra("latitude1");
        long1 = intent.getStringExtra("longitude1");
        src = intent.getStringExtra("building1");
        lat2 = intent.getStringExtra("latitude2");
        long2 = intent.getStringExtra("longitude2");
        dest = intent.getStringExtra("building2");
        
        try {

            //GPS
        	
            LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            LocationListener mlocListener = new MyLocationListener();
            mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
            Location location = mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            
            if(location != null) {
            lat1 = String.valueOf(location.getLatitude());	
            long1 = String.valueOf(location.getLongitude());
         
            src = "You are at latitude" + lat1 + "and longitude" + long1 + "";
            
            
            }
            
            
            
            // Loading map
            initilizeMap(lat1,long1,lat2,long2,src,dest);
         
            
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	private void initilizeMap(String lat1, String long1, String lat2, String long2, String source, String destination) {
        if (googleMap == null) {
        	
        	FragmentManager fmanager = getSupportFragmentManager();
            Fragment fragment = fmanager.findFragmentById(R.id.map);
            SupportMapFragment supportmapfragment = (SupportMapFragment)fragment;
            googleMap = supportmapfragment.getMap();
            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
           
  
            
           latitude1 = Double.parseDouble(lat1);
           longitude1 = Double.parseDouble(long1);
           latitude2 = Double.parseDouble(lat2);
           longitude2 = Double.parseDouble(long2);
            float[] results = new float[1];
           
    
            // create marker
            MarkerOptions marker1 = new MarkerOptions().position(new LatLng(latitude1, longitude1)).title(source);
            marker1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

            // adding marker
            googleMap.addMarker(marker1);

            
         // create marker
            MarkerOptions marker2 = new MarkerOptions().position(new LatLng(latitude2, longitude2)).title(destination);

            // adding marker
            googleMap.addMarker(marker2);
            
           
            
           /* CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude1,longitude1),14);
    		googleMap.animateCamera(update);*/
    		CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    new LatLng(latitude1,longitude1)).zoom(14).build();
    		 
     
    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
     

 
            Location.distanceBetween(latitude1, longitude1, latitude2, longitude2, results);

            String url = makeURL(latitude1,longitude1,latitude2,longitude2);
            
            connectAsyncTask myTask = new connectAsyncTask(url,this); 
            myTask.execute();
          //  speakOut(source,destination);
            
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Can't create maps", Toast.LENGTH_SHORT)
                        .show();
            }
            
            
        }
    }
	 public String makeURL (double sourcelat, double sourcelog, double destlat, double destlog ){
	        StringBuilder urlString = new StringBuilder();
	        urlString.append("http://maps.googleapis.com/maps/api/directions/json");
	        urlString.append("?origin=");// from
	        urlString.append(Double.toString(sourcelat));
	        urlString.append(",");
	        urlString
	                .append(Double.toString( sourcelog));
	        urlString.append("&destination=");// to
	        urlString
	                .append(Double.toString( destlat));
	        urlString.append(",");
	        urlString.append(Double.toString( destlog));
	        urlString.append("&sensor=false&mode=driving&alternatives=true");
	        return urlString.toString();
	 }
	 
	 class connectAsyncTask extends AsyncTask<Void, Void, String>{
	       private ProgressDialog progressDialog;
	       String url="";
	       private Context context;
	       String urlPass = makeURL(latitude1,longitude1,latitude2,longitude2);
	       connectAsyncTask(String urlPass, Context context){
	    	   this.context = context;
	           url = urlPass;
	       }
	       @Override
	       protected void onPreExecute() {
	           // TODO Auto-generated method stub
	           super.onPreExecute();
	           progressDialog = new ProgressDialog(UDMap.this);
	           progressDialog.setMessage("Loading the best way...");
	           progressDialog.setIndeterminate(true);
	           progressDialog.show();
	       }
	       @Override
	       protected String doInBackground(Void... params) {
	           DirectionsParser jParser = new DirectionsParser();
	           String json = jParser.getJSONFromUrl(url);
	           return json;
	       }
	       @Override
	       protected void onPostExecute(String result) {
	           super.onPostExecute(result);   
	           progressDialog.hide();        
	           if(result!=null){
	               drawPath(result);
	           }
	       }
	   }
	 public void drawPath(String  result) {

	        try {
	                //Tranform the string into a json object

	               final JSONObject json = new JSONObject(result);
	               JSONArray routeArray = json.getJSONArray("routes");
	               JSONObject routes = routeArray.getJSONObject(0);
	               JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
	               String encodedString = overviewPolylines.getString("points");

	               List<LatLng> list = decodePoly(encodedString);

	               for(int z = 0; z<list.size()-1;z++){
	                    LatLng src= list.get(z);
	                    LatLng dest= list.get(z+1);
	                    Polyline line = googleMap.addPolyline(new PolylineOptions()
	                    .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude,   dest.longitude))
	                    .width(2)
	                    .color(Color.RED).geodesic(true));
	                }
	               //speakOut(src, dest);

	        } 
	        catch (JSONException e) {

	        }
	    } 

	    private List<LatLng> decodePoly(String encoded) {

	        List<LatLng> poly = new ArrayList<LatLng>();
	        int index = 0, len = encoded.length();
	        int lat = 0, lng = 0;

	        while (index < len) {
	            int b, shift = 0, result = 0;
	            do {
	                b = encoded.charAt(index++) - 63;
	                result |= (b & 0x1f) << shift;
	                shift += 5;
	            } while (b >= 0x20);
	            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	            lat += dlat;

	            shift = 0;
	            result = 0;
	            do {
	                b = encoded.charAt(index++) - 63;
	                result |= (b & 0x1f) << shift;
	                shift += 5;
	            } while (b >= 0x20);
	            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	            lng += dlng;

	            LatLng p = new LatLng( (((double) lat / 1E5)),
	                     (((double) lng / 1E5) ));
	            poly.add(p);
	        }

	        return poly;
	    }
	    
	    @Override
	    protected void onResume() {
	        super.onResume();
	    }

	 


	    public class MyLocationListener implements LocationListener
	    {
	    	


	    double latitude;
	    double longitude;
	        
	    public double getLatitude() {
	    	return latitude;
	    }

	    public void setLatitude(double latitude) {
	    	this.latitude = latitude;
	    }

	    public double getLongitude() {
	    	return longitude;
	    }

	    public void setLongitude(double longitude) {
	    	this.longitude = longitude;
	    }

	    @Override
	    public void onLocationChanged(Location loc)
	    {
	    loc.getLatitude();
	    setLatitude(loc.getLatitude());
	    loc.getLongitude();
	    setLongitude(loc.getLongitude());

	    }


	    @Override
	    public void onProviderDisabled(String provider)
	    {
	    Toast.makeText( getApplicationContext(),"Gps Disabled",Toast.LENGTH_SHORT ).show();
	    }


	    @Override
	    public void onProviderEnabled(String provider)
	    {
	    Toast.makeText( getApplicationContext(),"Gps Enabled", Toast.LENGTH_SHORT).show();
	    }


	    @Override
	    public void onStatusChanged(String provider, int status, Bundle extras)
	    {
	    }
	   

	    }
		
}



	   

	    
	