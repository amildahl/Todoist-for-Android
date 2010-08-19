package com.drewdahl.android.todoist.views;

import com.drewdahl.android.todoist.R;
import com.drewdahl.android.todoist.apihandler.TodoistApiHandler;
import com.drewdahl.android.todoist.models.Project;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ItemEdit extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.itemedit);
		
		/*
		ArrayAdapter<Project> adapter = new ArrayAdapter<Project>(this, 0, 0, TodoistApiHandler.getInstance().getProjects());
		((Spinner)findViewById(R.id.SpinnerProject)).setAdapter(adapter);
		*/
		
		/**
		 * TODO Hook the project spinner into the menu edit.
		 */

        findViewById(R.id.ButtonSubmit).setOnClickListener(new Button.OnClickListener() {
            	public void onClick(View view) {
            		/*
            	String email = ((EditText)findViewById(R.id.EditTextEmail)).getText().toString();
            	String password = ((EditText)findViewById(R.id.EditTextPassword)).getText().toString();

            	if (email.length() < 1) {
            		Log.d(this.toString(), "E-Mail too short");
            		showToast("Please, input your e-mail address.");
            	} else if (password.length() < 1) {
            		Log.d(this.toString(), "Password too short");
            		showToast("Please, input your password.");
            	} else {
            		try {
                		setResult(RESULT_OK, new Intent());
                		finish(); // Kill this activity so we don't listen anymore.
            		} catch (RuntimeException e) {
            			Log.d(this.toString(), "Caught exception during login");
            			showToast("Incorrect Password!");
            		}
           		}
        */
        	}
        });
    }
    
	private void showToast(CharSequence msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
}
