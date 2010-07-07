package com.android.applications.todoist.views;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.applications.todoist.R;
import com.android.applications.todoist.containers.User;
import com.android.applications.todoist.handlers.DBHelper;
import com.android.applications.todoist.handlers.TodoistAPIHandler;

/**
 * An Activity that allows the user to Login.
 *
 * @see     android.app.Activity
 */
public class LoginPage extends Activity {
	private CheckBox rememberPass;
	private Button signInButton;
    private EditText emailText;
    private EditText passText;
    private TodoistAPIHandler handler;
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initControls();
    }
    
    private void initControls()
    {
    	//TextView
    	//EditText 
    	handler = new TodoistAPIHandler("TOKEN");
    	rememberPass = (CheckBox)findViewById(R.id.check_rememberPass);
    	emailText = (EditText)findViewById(R.id.edit_email);
    	passText = (EditText)findViewById(R.id.edit_pass);
    	signInButton = (Button)findViewById(R.id.btn_signIn);
    	signInButton.setOnClickListener(new Button.OnClickListener() { public void onClick (View view){ signIn(); } } ); 
    }
    
    private void signIn()
    {
    	String email = emailText.getText().toString();
    	String password = passText.getText().toString();
    	if(email.length() == 0)
    	{
    		//email is empty.. problem
    	}
    	else if(password.length() == 0)
    	{
    		//password is empty.. problem
    	}
    	else
    	{
    		//Attempt Login...
    		User user = handler.login(email, password);
    		AlertDialog alert = new AlertDialog.Builder(this).create();
    		if(user.isValid())
    		{
    			/*Intent myIntent = new Intent(LoginPage.this, TasksList.class);
    			myIntent.putExtra("token", user.getAPIToken());
    			LoginPage.this.startActivity(myIntent);*/
    			if(rememberPass.isChecked())
    			{
	    			DBHelper help = new DBHelper(this);
	    			try
	    			{
	    				help.createDB();
	    			}
	    			catch (IOException e)
	    			{
	    				throw new Error("Unable to create database");
	    			}
	    			
	    			try 
	    			{
	    				help.openDB();
	    			}
	    			catch (SQLException sqle)
	    			{
	    				throw sqle;
	    			}
	    			help.storeUser(user);
    			}
    			
    			setResult(RESULT_OK, new Intent().putExtra("token", user.getAPIToken()));
    			finish();
    		}
    		else
    		{
    			//Login Failure...
    			alert.setTitle("Failure!");
    			alert.show();
    			//setResult(RESULT_CANCELED, new Intent());
    		}
    	}
    }
}
