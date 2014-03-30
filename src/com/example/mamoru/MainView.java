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

@SuppressLint("ResourceAsColor")
public class MainView extends Fragment {

	ImageButton setTime;
	TimePicker tPicker;
//	EditText minutes, seconds, mSeconds;
	NumberPicker minutes, seconds, mSeconds;
	RelativeLayout rLayout;
	int flag;
	CountDownTimer aCounter;

	public void onCreate(Bundle state) {
		super.onCreate(state);
		setHasOptionsMenu(true);

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// linking together the xml and the view object
		// Note that each fragment has its own layout file
		View view = inflater.inflate(R.layout.main_view, container, false);

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
		
		
//		minutes = (EditText) view.findViewById(R.id.minutesInput);
//		seconds = (EditText) view.findViewById(R.id.secondsInput);
//		tp = (TimePicker) view.findViewById(R.id.timePicker1);
//		tp.setIs24HourView(true);

	/*	SimpleDateFormat  format = new SimpleDateFormat("mm:ss:SS");
		try {  
		     minutes.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					
				}
			});


		} catch (ParseException e) {
		    e.printStackTrace();  
		}*/
		setTime.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setTime();
				
			}
		});

		return view;
	}

	public void setTime() {
		
		int minutes1 = minutes.getValue();
		int seconds1 = seconds.getValue();
		
		int mSeconds1 = mSeconds.getValue();
	
		long timeInMSeconds = (minutes1*60*1000) + (seconds1*1000);
		if(timeInMSeconds > 0){
			minutes.setEnabled(false);
			seconds.setEnabled(false);
			
			if(aCounter== null){
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
			 System.out.println("fakjfdkaf");
	         aCounter.cancel();
	         aCounter = null;
	     }
		Toast.makeText(getActivity().getApplicationContext(),
				"Timer Terminated", Toast.LENGTH_SHORT).show();
		rLayout.setBackgroundColor(Color.parseColor(getString(R.color.Blue)));
		setTime.setImageResource(R.drawable.main_button_red);
		flag =0;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

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

//		Intent intent = new Intent(Intent.ACTION_PICK,
//				ContactsContract.Contacts.CONTENT_URI);
//		startActivityForResult(intent, 0);
	}
	
/*	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		  super.onActivityResult(reqCode, resultCode, data);

		  switch (reqCode) {
		    case (0) :
		      if (resultCode == Activity.RESULT_OK) {
		        Uri contactData = data.getData();
		        Cursor c =  getActivity().getContentResolver().query(contactData, null, null, null, null);
		        if (c.moveToFirst()) {
		          String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
		          Toast.makeText(getActivity().getApplicationContext(), name, Toast.LENGTH_SHORT).show();
		          // TODO Whatever you want to do with the selected contact name.
		        }
		      }
		      break;
		  }
		}*/

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
		// main_of_list.xml needs to be created with a list of desired items
		inflater.inflate(R.menu.main_of_list, menu);

	}

}