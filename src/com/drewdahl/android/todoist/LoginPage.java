/*    
	This file is part of Todoist for Android�.

    Todoist for Android� is free software: you can redistribute it and/or 
    modify it under the terms of the GNU General Public License as published 
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Todoist for Android� is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Todoist for Android�.  If not, see <http://www.gnu.org/licenses/>.
    
    This file incorporates work covered by the following copyright and  
 	permission notice:
 	
 	Copyright [2010] pskink <pskink@gmail.com>
 	Copyright [2010] ys1382 <ys1382@gmail.com>
 	Copyright [2010] JonTheNiceGuy <JonTheNiceGuy@gmail.com>

   	Licensed under the Apache License, Version 2.0 (the "License");
   	you may not use this file except in compliance with the License.
   	You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   	Unless required by applicable law or agreed to in writing, software
   	distributed under the License is distributed on an "AS IS" BASIS,
   	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   	See the License for the specific language governing permissions and
   	limitations under the License.
*/

package com.drewdahl.android.todoist;

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
import android.widget.Toast;

import com.drewdahl.android.todoist.users.User;
import com.drewdahl.android.todoist.apihandlers.TodoistApiHandler;

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
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        // Initiate Controls
        initControls();
    }
    
    // Initialize the controls of the page and set event handlers
    private void initControls()
    { 
    	rememberPass = (CheckBox)findViewById(R.id.check_rememberPass);
    	emailText = (EditText)findViewById(R.id.edit_email);
    	passText = (EditText)findViewById(R.id.edit_pass);
    	signInButton = (Button)findViewById(R.id.btn_signIn);
    	
    	// Call signIn() when button is clicked
    	signInButton.setOnClickListener(new Button.OnClickListener() { public void onClick (View view){ signIn(); } } ); 
    }
    
    // Perform user sign-in
    private void signIn()
    {
    	//Get E-mail and Password, then check their length...
    	String email = emailText.getText().toString();
    	String password = passText.getText().toString();

    	if(email.length() == 0)
    	{
    		//email is empty.. problem
    		this.showToast("Your E-Mail address cannot be empty.");
    	}
    	else if(password.length() == 0)
    	{
    		//password is empty.. problem
    		this.showToast("Your Password cannot be empty.");
    	}
    	else
    	{
    		//Attempt Login...
    		User user = null;
    		try
    		{
    			user = TodoistApiHandler.getInstance().login(email, password);
    		}
    		catch (Exception e)
    		{
    			/**
    			 * TODO Catch and handle ...
    			 */
    		}
    		
   			// Login was successful, set user API Token in result bundle and call finish()
   			setResult(RESULT_OK, new Intent().putExtra("token", user.getApiToken()));
   			finish();
   		}
   	}
    
    // Displays error message
    private void displayError(String msg)
    {
    	// TODO: We need an OK Button on this Dialog, or some way to exit out w/o having to hit back
    	AlertDialog alert = new AlertDialog.Builder(this).create();
		alert.setTitle(msg);
		alert.show();
    }
    
    // Displays Toast... YUMMY!
	private void showToast(CharSequence msg)
	{
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
}
