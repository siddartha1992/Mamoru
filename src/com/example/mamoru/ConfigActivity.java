package com.example.mamoru;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.firebase.client.Firebase;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ConfigActivity extends Activity {

	private Button fetchContact;
	ListView listView;

	ArrayList<String> listItems;

	ArrayAdapter<String> adapter;
	private EditText name;
	private Firebase settingsRef;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        Firebase firebase = new Firebase("https://glowing-fire-3800.firebaseio.com/");
		settingsRef = firebase.child("userSettings").child(MainActivity.deviceID);
		
		setContentView(R.layout.activity_config);

		name = (EditText) findViewById(R.id.nameValue);

		fetchContact = (Button) findViewById(R.id.button_Fetch);
		fetchContact.setEnabled(false);
		listView = (ListView) findViewById(R.id.listView);
		listItems = new ArrayList<String>();

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, listItems);

		name.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

			@Override
			public void afterTextChanged(Editable s) {
				if(!s.toString().isEmpty()){
					fetchContact.setEnabled(true);
					settingsRef.child("firstName").setValue(s.toString());
				}else{
					fetchContact.setEnabled(false);
				}
			}
		});

		listView.setAdapter(adapter);
		
		fetchContact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK,
						ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(intent, 0);
			}
		});

		// getUserName();

	}

	/*
	 * public void getUserName(){ AccountManager manager =
	 * AccountManager.get(this); Account[] accounts =
	 * manager.getAccountsByType("com.google"); List<String> possibleEmails =
	 * new LinkedList<String>();
	 * 
	 * for (Account account : accounts) { // TODO: Check possibleEmail against
	 * an email regex or treat // account.name as an email address only for
	 * certain account.type values. possibleEmails.add(account.name); }
	 * 
	 * if(!possibleEmails.isEmpty() && possibleEmails.get(0) != null){ String
	 * email = possibleEmails.get(0); String[] parts = email.split("@");
	 * if(parts.length > 0 && parts[0] != null) username = parts[0]; } }
	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.config, menu);
		return true;
	}

	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);

		switch (reqCode) {
		case (0):
			if (resultCode == Activity.RESULT_OK) {
				Uri contactData = data.getData();
				Cursor c = getContentResolver().query(contactData, null, null,
						null, null);
				if (c.moveToFirst()) {
					String name = c
							.getString(c
									.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					//String number = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					//System.out.println(number);
					listItems.add(name);
					Firebase contactRef = settingsRef.child("contacts").child("0");
					contactRef.child("name").setValue(name);
					contactRef.child("number").setValue("12403393356"); // FIXME
					adapter.notifyDataSetChanged();
					// TODO Whatever you want to do with the selected contact
					// name.
				}
			}
			break;
		}
	}
}
