package com.udel;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.util.Log;

public class DirectionsParser {

    static InputStream inputstream = null;
    static JSONObject jObj = null;
    static String json = "";
    // constructor
    public DirectionsParser() {
    }
    public String getJSONFromUrl(String url) {

    	Log.d("test","inside JSONFromUrl");
        // Making HTTP request
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            
            HttpPost httpPost = new HttpPost(url);
           
            HttpResponse httpResponse = httpClient.execute(httpPost);
           
            HttpEntity httpEntity = httpResponse.getEntity();
          
            inputstream = httpEntity.getContent();           

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
        	Log.d("test","Doing buffered reader work");
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputstream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            Log.d("test","Making JSON obj");
            json = sb.toString();
            inputstream.close();
        } catch (Exception e) {
            Log.e("Error", "Error converting result" + e.toString());
        }
     
        return json;

    }
}