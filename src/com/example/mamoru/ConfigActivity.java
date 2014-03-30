package com.example.mamoru;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
	ArrayList<Pair> data;
	private EditText name;
	private Firebase settingsRef;
	private Firebase valueRef;

	private class Pair {
		private String number;
		private String name;

		public Pair(String num, String name) {
			this.number = num;
			this.name = name;
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Firebase firebase = new Firebase(
				"https://glowing-fire-3800.firebaseio.com/");
		settingsRef = firebase.child("userSettings").child(
				MainActivity.deviceID);

		valueRef = settingsRef.child("contacts");

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
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!s.toString().isEmpty()) {
					fetchContact.setEnabled(true);
					settingsRef.child("firstName").setValue(s.toString());
				} else {
					fetchContact.setEnabled(false);
				}
			}
		});

		listView.setAdapter(adapter);

		valueRef.addValueEventListener(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot snap) {
				Object o = snap.getValue();
				if (o != null) {

					data = (ArrayList<Pair>) o;
					System.out.println(data.size());
					for (int i = 0; i < data.size(); i++) {
						Map map = (Map) data.get(i);
						if (!listItems.contains((String) map.get("name")))
							listItems.add((String) map.get("name"));
					}

					adapter.notifyDataSetChanged();
					// String name = (String)((Map)o).get("name");
					// name.add((String)((Map)o).get("name"));

					// for(int i = 0; i < name.size(); i++){
					// Toast.makeText(getApplicationContext(),
					// name.get(i), Toast.LENGTH_SHORT).show();
					// }
				}
			}

			@Override
			public void onCancelled(FirebaseError arg0) {
				// TODO Auto-generated method stub

			}
		});

		settingsRef.addValueEventListener(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot arg0) {
				// TODO Auto-generated method stub
				Object o = arg0.getValue();
				if (o != null) {
					String firstName = (String) ((Map) o).get("firstName");

					name.setText(firstName);

				}

			}

			@Override
			public void onCancelled(FirebaseError arg0) {
				// TODO Auto-generated method stub

			}
		});

		fetchContact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent intent = new Intent(Intent.ACTION_PICK,
				// ContactsContract.Contacts.CONTENT_URI);
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType(Phone.CONTENT_TYPE);
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
				Uri contactUri = data.getData();
				String[] projection = { Phone.DISPLAY_NAME, Phone.NUMBER };

				Cursor cursor = getContentResolver().query(contactUri,
						projection, null, null, null);

				cursor.moveToFirst();

				int namecolumn = cursor
						.getColumnIndexOrThrow(Phone.DISPLAY_NAME);
				String name = cursor.getString(namecolumn);

				int phonecolumn = cursor.getColumnIndexOrThrow(Phone.NUMBER);
				String phone = cursor.getString(phonecolumn);

				// listItems.add(name);
				Firebase contactRef = settingsRef.child("contacts").child(
						Integer.toString(listItems.size()));
				contactRef.child("name").setValue(name);
				contactRef.child("number").setValue(phone); // fixed
				// adapter.notifyDataSetChanged();
				// TODO Whatever you want to do with the selected contact
				// name.
				// }
			}
			/*
			 * if (resultCode == Activity.RESULT_OK) { Uri contactData =
			 * data.getData();
			 * 
			 * Cursor c = getContentResolver().query(contactData, null, null,
			 * null, null); startManagingCursor(c); String name = null; if
			 * (c.moveToFirst()) { name =
			 * c.getString(c.getColumnIndexOrThrow(People.NAME)); String number
			 * = c.getString(c.getColumnIndexOrThrow(People.NUMBER));
			 * 
			 * Toast.makeText(this, name + " has number " + number,
			 * Toast.LENGTH_LONG).show(); }
			 * 
			 * // if (c.moveToFirst()) { // name = c // .getString(c //
			 * .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			 * 
			 * // String number = //
			 * c.getString(c.getColumnIndex(ContactsContract
			 * .CommonDataKinds.Phone.NUMBER)); // System.out.println(number);
			 * 
			 * /* int indexName = c
			 * .getColumnIndex(ContactsContract.CommonDataKinds
			 * .Phone.DISPLAY_NAME); int indexNumber = c
			 * .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
			 * 
			 * String name = c.getString(indexName); String number =
			 * c.getString(indexNumber); System.out.println(name + ": " +
			 * number); listItems.add(name); Firebase contactRef =
			 * settingsRef.child("contacts").child("0");
			 * contactRef.child("name").setValue(name);
			 * contactRef.child("number").setValue("12403393356"); // FIXME
			 * adapter.notifyDataSetChanged(); // TODO Whatever you want to do
			 * with the selected contact // name. // } }
			 */
			break;
		}
	}
}
