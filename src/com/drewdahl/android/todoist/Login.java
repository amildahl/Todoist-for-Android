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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.drewdahl.android.todoist.models.user.User;
import com.drewdahl.android.todoist.models.user.UserException;

/**
 * An Activity that allows the user to Login.
 *
 * TODO Implement error dialogs.
 * TODO Implement remember password.
 * TODO Make this back into a callback activity?
 *
 * @see     android.app.Activity
 */
public class Login extends Activity {
	private String email;
	private String password;
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        findViewById(R.id.ButtonSubmit).setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View view) {
            	email = ((EditText)findViewById(R.id.EditTextEmail)).getText().toString();
            	password = ((EditText)findViewById(R.id.EditTextPassword)).getText().toString();

            	if (email.length() < 1) {
            		showToast("Please, input your e-mail address.");
            	} else if (password.length() < 1) {
            		showToast("Please, input your password.");
            	} else {
            		User user = null;
            		try {
            			Log.d("com.drewdahl.android.todoist.Login", "User login being called ...");
            			user = User.login(email, password);
            			Log.d("com.drewdahl.android.todoist.Login", "User login returned.");
            			Log.d("com.drewdahl.android.todoist.Login", "User's token: " + user.getToken());
            		}
            		catch (UserException e) {
            			showToast("Exception thrown and I'm confused.  Probably, an incorrect password.");
            			return;
            		}

            		setResult(RESULT_OK, new Intent().putExtra("com.drewdahl.todoist.models.user", user));
           			finish();
           		}
        	}
        });
    }
    
    /**
     * TODO What is toast?
     * @param msg
     */
	private void showToast(CharSequence msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
}
