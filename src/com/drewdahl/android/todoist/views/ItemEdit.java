package com.drewdahl.android.todoist.views;

import java.util.HashMap;
import java.util.Map;

import com.drewdahl.android.todoist.R;
import com.drewdahl.android.todoist.apihandler.TodoistApiHandler;
import com.drewdahl.android.todoist.models.Item;
import com.drewdahl.android.todoist.models.Project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ItemEdit extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.itemedit);
		
		ArrayAdapter<Project> adapter = new ArrayAdapter<Project>(this, android.R.layout.simple_spinner_item, TodoistApiHandler.getInstance().getProjects());
		((Spinner)findViewById(R.id.SpinnerProject)).setAdapter(adapter);
		
        findViewById(R.id.ButtonSubmit).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
            	String date = ((EditText)findViewById(R.id.EditTextDate)).getText().toString();
           		String content = ((EditText)findViewById(R.id.EditTextContent)).getText().toString();
           		Project project = (Project)((Spinner)findViewById(R.id.SpinnerProject)).getSelectedItem();

           		if (content.length() < 1) {
            		Log.d(this.toString(), "Content was empty!");
            		showToast("Please, input a task.");
           		} else if (project == null) {
           			Log.d(this.toString(), "No project selected!");
           			showToast("Please, select a project.");
            	} else {
            		/**
            		 * TODO Error Handling
               		 * TODO Fix this nearly fubarred mess!
               		 */
               		Item i = null;
               		if (date.length() < 1) {
               			i = TodoistApiHandler.getInstance().addItem(project.getId(), content);
               		} else {
               			setResult(RESULT_OK, new Intent());
                   		Map<String, String> e = new HashMap<String, String>();
                   		e.put("date_string", date);
                   		i = TodoistApiHandler.getInstance().addItem(project.getId(), content, e.entrySet().iterator().next());
               		}
               		i.save(ItemEdit.this.getContentResolver());
               		finish(); // Kill this activity so we don't listen anymore.
           		}
        	}
        });
    }
    
	private void showToast(CharSequence msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
}
