package com.udel;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.ud.R;

public class UDelTour extends Activity {

    
	Button udTour;
	Button udCalender;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     

        setContentView(R.layout.front);
    

    
        


      udTour = (Button) findViewById(R.id.tour);
       udCalender = (Button) findViewById(R.id.events);
        
    

	

      udTour.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
            	 Intent i = new Intent(getBaseContext(), UDel.class);
            	 startActivity(i);
            }

        });
    
    
    udCalender.setOnClickListener (new View.OnClickListener(){


        public void onClick(View view) {
    Intent i = new Intent(getBaseContext(), Calender.class);

	startActivity(i);
   }
});
}
    

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
      
    }

    private void saveState() {
   
    }

}