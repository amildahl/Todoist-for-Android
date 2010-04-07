package com.android.applications.todoist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginPage extends Activity {
	private Button signInButton;
    private EditText emailText;
    private EditText passText;
    private TodoistAPIHandler handler;
    
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
    			Intent myIntent = new Intent(LoginPage.this, TasksList.class);
    			myIntent.putExtra("token", user.getAPIToken());
    			LoginPage.this.startActivity(myIntent);
    		}
    		else
    		{
    			//Login Failure...
    			alert.setTitle("Failure!");
    			alert.show();
    		}
    	}
    }
}
