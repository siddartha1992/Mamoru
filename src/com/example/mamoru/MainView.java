package com.example.mamoru;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import com.example.mamoru.R.color;

import android.R.drawable;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
//import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.client.Firebase;


@SuppressLint("ResourceAsColor")
public class MainView extends Fragment {

	ImageButton setTime;
	TimePicker tPicker;
	NumberPicker minutes, seconds, mSeconds;
	RelativeLayout rLayout;
	int flag;
	CountDownTimer aCounter;
	
	Firebase settingsRef, timerRef;
	
	String deviceID;
	
	
	public MainView() {
	}
	
	public MainView(String deviceID) {
		this.deviceID = deviceID;
	}

	public void onCreate(Bundle state) {
		super.onCreate(state);
		setHasOptionsMenu(true);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.main_view, container, false);
		
		Firebase firebase = new Firebase("https://glowing-fire-3800.firebaseio.com/");
		
		settingsRef = firebase.child("userSettings").child(deviceID);
		timerRef    = firebase.child("timerRequests").child(deviceID);

		setTime = (ImageButton) view.findViewById(R.id.image);
		
		minutes = (NumberPicker) view.findViewById(R.id.minutes);
		minutes.setMinValue(0);
		minutes.setMaxValue(59);
		seconds = (NumberPicker) view.findViewById(R.id.seconds);
		seconds.setMinValue(0);
		seconds.setMaxValue(59);
		mSeconds = (NumberPicker) view.findViewById(R.id.millseconds);
		mSeconds.setEnabled(false);
		mSeconds.setMinValue(0);
		mSeconds.setMaxValue(999);
		
		rLayout = (RelativeLayout) view.findViewById(R.id.rlayout);
		
		flag = 0;
		
		
		setTime.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setTime();
			}
		});

		return view;
	}

	public void setTime() {
		
		int minutes1 = minutes.getValue();
		int seconds1 = seconds.getValue();
		int mSeconds1 = mSeconds.getValue();

		minutes.setEnabled(false);
		seconds.setEnabled(false);
		
		long timeInMSeconds = (minutes1*60*1000) + (seconds1*1000);
		
		if(aCounter== null){
			timerRef.child("length").setValue(timeInMSeconds);
			Log.i("setTime", timeInMSeconds+"");
			aCounter = new CountDownTimer(timeInMSeconds, 1000) {
	
			     public void onTick(long millisUntilFinished) {
			         long mins = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
			         long sec = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
			         minutes.setValue((int) mins);
			         seconds.setValue((int) sec);
			     }
	
			     public void onFinish() {
			         Toast.makeText(getActivity().getApplicationContext(), 
			        		 "Time Ran Out!!!", Toast.LENGTH_SHORT).show();
			         minutes.setValue(0);
			         seconds.setValue(0);
			         setEnabled2();
			     }
			  }.start();
		
		}
		if(flag == 0){
			setEnabled1();
		}else{
			setEnabled2();
		}

	}
	
	public void setEnabled1(){
		rLayout.setBackgroundColor(Color.RED);
		setTime.setImageResource(R.drawable.main_button_blue);
		flag = 1;
	}
	
	public void setEnabled2(){
		minutes.setEnabled(true);
		seconds.setEnabled(true);
		 if(aCounter != null) {
	         aCounter.cancel();
	         aCounter = null;
	     }
		Toast.makeText(getActivity().getApplicationContext(),
				"Timer Terminated", Toast.LENGTH_SHORT).show();
		rLayout.setBackgroundColor(Color.parseColor(getString(R.color.Blue)));
		setTime.setImageResource(R.drawable.main_button_red);
		flag = 0;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// checks all of the possible cases from the options menu
		switch (item.getItemId()) {
			// Select on/off
			case R.id.config:
				configFunction();
	
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void configFunction() {
		 Intent i = new Intent(getActivity(), ConfigActivity.class);
		 getActivity().startActivity(i);
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
		// main_of_list.xml needs to be created with a list of desired items
		inflater.inflate(R.menu.main_of_list, menu);

	}
}