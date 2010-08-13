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

package com.drewdahl.android.todoist.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.drewdahl.android.todoist.R;
import com.drewdahl.android.todoist.apihandler.TodoistApiHandler;

/**
 * An Activity that allows the user to Login.
 *
 * TODO Implement remember password.  Placing the User info in the database and reading it if the perm is set.
 *
 * @see     android.app.Activity
 */
public class Login extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.login);

        if (((CheckBox)findViewById(R.id.CheckBoxRememberPassword)).isChecked()) {
        	Log.d(this.toString(), "Remember Me Checked");
        	Log.d(this.toString(), "Setting remember me option");
        	/**
        	 * TODO Set Option to remember.
        	 */
        }
        
        findViewById(R.id.ButtonSubmit).setOnClickListener(new Button.OnClickListener() {
            	public void onClick(View view) {
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
            			Log.d(this.toString(), "Logging in");
            			TodoistApiHandler.getInstance().login(email, password);
            			/**
            			 * TODO Make this actually save the user information.
            			 * TODO Only if we want to remember information?
            			 */
            			TodoistApiHandler.getInstance().getUser().save(Login.this.getContentResolver());
                		setResult(RESULT_OK, new Intent());
                		finish(); // Kill this activity so we don't listen anymore.
            		} catch (RuntimeException e) {
            			Log.d(this.toString(), "Caught exception during login");
            			/**
            			 * TODO if the following isn't expected we should support request it.
            			 */
            			Log.e(this.toString(), e.getStackTrace().toString());
            			showToast("Incorrect Password!");
            		}
           		}
        	}
        });
    }
    
	private void showToast(CharSequence msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
}
