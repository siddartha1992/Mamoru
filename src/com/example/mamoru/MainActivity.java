package com.example.mamoru;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.Configuration;
import android.view.Menu;

public class MainActivity extends Activity {

	public final static String FRAG1_TAG = "FRAG1";
	
	private MainView f1;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
 /*       if (getResources().getConfiguration().orientation == 
        		Configuration.ORIENTATION_LANDSCAPE) {
        	
        	if (getFragmentManager().findFragmentByTag(FRAG1_TAG) == null) {
				f1 = new MainView();
				getFragmentManager().beginTransaction()
						.add(R.id.frame1, f1, FRAG1_TAG).commit();
			}
        }*/
        
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT
				&& savedInstanceState == null) {
			// Initializing AddNote fragment
			if (getFragmentManager().findFragmentByTag(FRAG1_TAG) == null) {

				f1 = new MainView();
				getFragmentManager().beginTransaction()
						.add(R.id.frame1, f1, FRAG1_TAG).commit();

			}
		}
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
