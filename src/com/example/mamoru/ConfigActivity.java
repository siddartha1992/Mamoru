package com.example.mamoru;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ConfigActivity extends Activity {

	private Button fetchContact;
	ListView listView;

	ArrayList<String> listItems;

	ArrayAdapter<String> adapter;
	private String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config);
		
		

		fetchContact = (Button) findViewById(R.id.button_Fetch);

		listView = (ListView) findViewById(R.id.listView);
		listItems = new ArrayList<String>();

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, listItems);

		listView.setAdapter(adapter);
		fetchContact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK,
						ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(intent, 0);
			}
		});
		
//		getUserName();
		Toast.makeText(getApplicationContext(),
				username, Toast.LENGTH_SHORT).show();
	}
	
/*	public void getUserName(){
		 AccountManager manager = AccountManager.get(this); 
		    Account[] accounts = manager.getAccountsByType("com.google"); 
		    List<String> possibleEmails = new LinkedList<String>();

		    for (Account account : accounts) {
		      // TODO: Check possibleEmail against an email regex or treat
		      // account.name as an email address only for certain account.type values.
		      possibleEmails.add(account.name);
		    }

		    if(!possibleEmails.isEmpty() && possibleEmails.get(0) != null){
		        String email = possibleEmails.get(0);
		        String[] parts = email.split("@");
		        if(parts.length > 0 && parts[0] != null)
		        	username = parts[0];
		    }
	}*/

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
					listItems.add(name);
					adapter.notifyDataSetChanged();
					// TODO Whatever you want to do with the selected contact
					// name.
				}
			}
			break;
		}
	}

}
