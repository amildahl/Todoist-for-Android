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

import java.net.URI;

import org.apache.http.conn.HttpHostConnectException;
import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;
import org.xmlrpc.android.XMLRPCFault;

import com.drewdahl.android.todoist.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SupportForm extends Activity {
	private XMLRPCClient client;
	
	private URI uri;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.support);

		//URI of the XMLRPC Server-Side Script
		uri = URI.create("http://dev.drewdahl.com/server.php");
		client = new XMLRPCClient(uri);
		
		//Call rpcCall() on button click
		((Button)findViewById(R.id.ButtonSubmit)).setOnClickListener(new Button.OnClickListener() {
			public void onClick(View view) { 
				rpcCall();
			}
		});
	}
	
	private void showToast(CharSequence msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
	
	private void rpcCall() {
		if(checkValues())
		{
			Context context = getApplicationContext();
			PackageInfo info;
			
			try {
	            PackageManager manager = context.getPackageManager();
	            info = manager.getPackageInfo(context.getPackageName(), 0);
		    } catch(Exception e) {
		    	//TODO: Something better here :-D
		        Log.e("SupportCase", "Couldn't find package information in PackageManager", e);
		        return;
		    }
		    
			// TODO: Add something to XMLRPCMethod that checks the result of the page (in case the site is down) 
			XMLRPCMethod method = new XMLRPCMethod("reportproblem", new XMLRPCMethodCallback() {
				public void callFinished(Object result, String str) {
					if((Boolean)result) {
						String alert = "The problem was reported successfully.";
						if (((EditText)findViewById(R.id.EditTextEmail)).getText().length() != 0) {
							showAlert("Success!", alert + "\n\nYou should receive an automated e-mail in the next few hours.","OK",true);
						}
						showAlert("Success!","The problem was reported successfully. \n\nNotice: Since you did not enter your e-mail address, there will be no correspondance.","OK",true);
					} else {
						showAlert("Failure!","The problem failed to be reported.  Please try again shortly.  If this problem persists, please contact Admin@DrewDahl.com.","OK",false);
					}
				}
			});
			
			Object[] params = {
					/*newCase,*/				
			};
			
			method.call(params);
		}
	}
	
	private boolean checkValues() {
		if (((EditText)findViewById(R.id.EditTextDoing)).getText().toString().replaceAll(" ", "") == "") {
			showToast("Problem Text cannot be blank.");
			return false;
		}
		return true;
	}
	
	private void showAlert(String title, String message, String button_text, Boolean finish)
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(this).setTitle(title).setMessage(message);
		if (finish) {
			dialog.setNeutralButton(button_text, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();							
				}
			});
		} else {
			dialog.setNeutralButton(button_text, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//Do nothing						
				}
			});
		}
		dialog.show();
	}
	
	interface XMLRPCMethodCallback {
		void callFinished(Object result, String str);
	}
	
	class XMLRPCMethod extends Thread {
		private String method;
		private Object[] params;
		private Handler handler;
		private XMLRPCMethodCallback callBack;
		public XMLRPCMethod(String method, XMLRPCMethodCallback callBack) {
			this.method = method;
			this.callBack = callBack;
			handler = new Handler();
		}
		public void call() {
			call(null);
		}
		public void call(Object[] params) {
			Log.i("RPC Call", "Calling host " + uri.getHost());
			//TODO: Show that we're calling the host
			this.params = params;
			start();
		}
		@Override
		public void run() {
    		try {
    			final long t0 = System.currentTimeMillis();
    			final Object result = client.callEx(method, params);
    			final long t1 = System.currentTimeMillis();
    			handler.post(new Runnable() {
					public void run() {
						//Call went good, run along now...
						Log.i("RPC Call", "XML-RPC call took " + (t1-t0) + "ms");
						callBack.callFinished(result,"");
					}
    			});
    		} catch (final XMLRPCFault e) {
    			handler.post(new Runnable() {
					public void run() {
						Log.e("RPC Call", "Fault message: " + e.getFaultString() + "\nFault code: " + e.getFaultCode());

						callBack.callFinished((Object)false, "1000 - Message: " + e.getFaultString() + " -- Code: " + e.getFaultCode());
					}
    			});
    		} catch (final XMLRPCException e) {
    			handler.post(new Runnable() {
					public void run() {						
						Throwable cause = e.getCause();
						if (cause instanceof HttpHostConnectException) {
							Log.e("RPC Call", "Cannot connect to " + uri.getHost() + "\nTry again later.  If problem persists, please report this problem!");
							
							callBack.callFinished((Object)false, "1001");
						} else {
							Log.e("RPC Call", "Error " + e.getMessage());
							
							callBack.callFinished((Object)false, "1002 - Message: " + e.getMessage());
						}
					}
    			});
    		}
		}
	}
}
