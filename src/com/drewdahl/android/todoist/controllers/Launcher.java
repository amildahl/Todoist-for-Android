package com.drewdahl.android.todoist.controllers;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.drewdahl.android.todoist.R;
import com.drewdahl.android.todoist.apihandler.TodoistApiHandler;
import com.drewdahl.android.todoist.models.User;
import com.drewdahl.android.todoist.provider.TodoistProviderMetaData.Users;
import com.drewdahl.android.todoist.service.TodoistService;
import com.drewdahl.android.todoist.views.ItemList;
import com.drewdahl.android.todoist.views.Login;

public class Launcher extends Activity {
	private static final int LOGIN_REQUEST = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SharedPreferences prefs = getSharedPreferences("com.drewdahl.android.todoist_preferences", 0);
		/**
		 * TODO Bring default values into the resources.
		 */
		if (!prefs.getBoolean(getString(R.string.key_remember_me), false)) {
			Intent intent = new Intent(this, Login.class);
			this.startActivityForResult(intent, LOGIN_REQUEST);
		} else {
			Cursor c = getContentResolver().query(Users.CONTENT_URI, null, null, null, null);
			c.moveToFirst();
			TodoistApiHandler.getInstance().setUser(new User(c));
			startActivityUserStartPage();
			c.close();
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(resultCode, resultCode, data);
		
		switch (requestCode) {
		case LOGIN_REQUEST:
			switch (resultCode) {
			case Activity.RESULT_CANCELED:
				Log.d(this.toString(), "Login Activity Canceled");
				finish();
				break;
			case Activity.RESULT_OK:
				Log.d(this.toString(), "Login Activity Ok");
				startActivityUserStartPage();
				break;
			default:
				Log.e(this.toString(), "Unhandled requestCode");
				break;
			}
		default:
			Log.d(this.toString(), "Not our request");
			break;
		}
	}
	
	private void startActivityUserStartPage() {
		startService(new Intent(this, TodoistService.class));

		/**
		 * TODO Start user's start page.
		 */
		Log.d(this.toString(), "User's start page: " + TodoistApiHandler.getInstance().getUser().getStartPage());

		Intent intent = new Intent(this, ItemList.class);
		
		startActivity(intent);
		finish();
	}
	
	public static void startSupportIssue() {
		/**
		 * TODO Call this function when a support issue needs to be handled.
		 * TODO Determine what parameters we want from the system when an issue occurs.
		 * TODO Potentially launch the SupportForm activity here depending on the user's choice from sbumitting and seriousness of issue.
		 */
	}
}
