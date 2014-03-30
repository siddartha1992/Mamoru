package com.example.mamoru;

import java.util.UUID;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.telephony.TelephonyManager;
import android.view.Menu;

public class MainActivity extends Activity {

	public final static String FRAG1_TAG = "FRAG1";
	
	public static String deviceID;
	
	private MainView f1;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deviceID = this.getId();
        setContentView(R.layout.activity_main);
        
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
    
    private String getId() {
        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice  = "" + tm.getDeviceId();
        tmSerial  = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        return deviceUuid.toString();
    }
}
