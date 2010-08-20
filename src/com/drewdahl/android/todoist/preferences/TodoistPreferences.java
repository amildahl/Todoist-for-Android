package com.drewdahl.android.todoist.preferences;

import com.drewdahl.android.todoist.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class TodoistPreferences extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.todoistoptions);
	}
}
