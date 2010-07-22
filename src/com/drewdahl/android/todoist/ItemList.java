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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import com.drewdahl.android.todoist.apihandler.TodoistApiHandler;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class ItemList extends ListActivity {
	protected HashMap<Integer, ResultCallbackIF> _callbackMap = new HashMap<Integer, ResultCallbackIF>();
	private ProgressDialog m_ProgressDialog = null;
    private Runnable viewTasks;
    
    public static final int REPORT_PROBLEM = Menu.FIRST + 1;
   
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras(); // TODO More at 10:00 ... back to you Andrew.
        setContentView(R.layout.itemlist);
        
        if(extras != null) {
        	TodoistApiHandler.getInstance(extras.getString("token"));
        	this.getTasks(); // @note Seems wrong ...
        }
        else if(TodoistApiHandler.getInstance().getToken() != "") {
            this.getTasks(); // @note Seems wrong ...
        }
        else {
        	this.createLogin();
        }
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
    	populateMenu(menu); // WTF?
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	/**
    	 * @note Is this ordering correct?
    	 */
    	populateMenu(menu);
    	return (super.onCreateOptionsMenu(menu));
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	return applyMenuChoice(item) || super.onContextItemSelected(item);
    }
    
    private void populateMenu(Menu menu)
    {
    	/**
    	 * TODO Put this in the layout?
    	 */
    	menu.add(Menu.NONE, REPORT_PROBLEM, Menu.NONE, "Report a Problem");
    }
    
    private boolean applyMenuChoice(MenuItem item)
    {
    	switch (item.getItemId())
    	{
    	case REPORT_PROBLEM:
    		this.launchActivity(SupportForm.class, new ItemList.ResultCallbackIF() {
    			
    			@Override
    			public void resultOk(Intent data) {
		        	Log.i("TasksList", "Returning on OK from SupportForm");
    			}
    			
    			@Override
    			public void resultCancel(Intent data) {
    				Log.i("TasksList", "Returning on Cancel from SupportForm");
    			}
    		});
    		return true;
    	}
    	
    	return false;
    }
    
    // Will probably be eventually replaced or use the SQLite3 DB
    private String getToken() 
    {
		return TodoistApiHandler.getInstance().getUser().getToken();
    }
    
    // Call the LoginPage Activity and deal with that
    private void createLogin()
	{
		this.launchActivity(LoginPage.class, new ItemList.ResultCallbackIF() {
			
			@Override
			public void resultOk(Intent data) 
			{
				//We're good! Get to token, set it to the API, and show tasks
				Bundle extras = data.getExtras();
				if(extras != null)
		        {
		        	Log.e("Login-resultOk():", TodoistApiHandler.getInstance().getToken());
		        	getTasks();
		        }
		        else
		        {
		        	//TODO: I don't think we should EVER get here, but we should
		        	// show some seriously awesome errors if we do :-)
		        	Log.e("Login-resultOk():","No Token!!!");
		        }
			}
			
			@Override
			public void resultCancel(Intent data) 
			{
				// Most likely, we got here only by the user hitting the back button, so quit.
				Log.e("Login-resultCancel:", "Login Canceled!");
				finish();
			}
		});
	}    
	
    private Runnable returnRes = new Runnable() {

        @Override
        public void run() {
            // Getting tasks is done, so dismiss the progress dialog and
        	// set the adapter to be the list adapter
            m_ProgressDialog.dismiss();
            /**
             * TODO Fix this to work again.
             */
            //setListAdapter(adapter.getAdapter());
        }
        
    };
    
    
    private void getTasks() 
    {   
    	/**
    	 * TODO Fix this to work again.
    	 */
    	//this.adapter = new TaskListAdapter(this);
    	
        viewTasks = new Runnable() {
            @Override
            public void run() 
            {
            	//Run the query, set the adapter task, return to to UI Thread
            	Date start = new Date();
            	Calendar finish = Calendar.getInstance();
            	finish.add(Calendar.DATE, 7);
            	//Query query = new Query();        	
            	//query.addDateRange(start, finish.getTime());
            	//query.addOverdue();
            	//Item[] tasks = handler.query(query.getQuery());
            	
            	//adapter.setTasks(tasks,query);

                runOnUiThread(returnRes);
            }
        };
        Thread thread =  new Thread(null, viewTasks, "MagentoBackground");
        thread.start();
        m_ProgressDialog = ProgressDialog.show(this,    
              "Please wait...", "Retrieving data ...", true);
        
    }   
    
    @SuppressWarnings("unchecked")
	public void launchActivity(Class subActivityClass, ResultCallbackIF callback, Bundle bundle) 
	{
		  int correlationId = new Random().nextInt();
		  _callbackMap.put(correlationId, callback);
		  startActivityForResult(new Intent(this, subActivityClass).putExtras(bundle), correlationId);
	}
	
	@SuppressWarnings("unchecked")
	public void launchActivity(Class subActivityClass, ResultCallbackIF resultCallbackIF) 
	{
		launchActivity(subActivityClass, resultCallbackIF, new Bundle());
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data); 
		try 
		{
			ResultCallbackIF callback = _callbackMap.get(requestCode);

			switch (resultCode) 
			{
			case Activity.RESULT_CANCELED:
				callback.resultCancel(data);
				_callbackMap.remove(requestCode);
				break;
			case Activity.RESULT_OK:
				callback.resultOk(data);
				_callbackMap.remove(requestCode);
				break;
			default:
				Log.e("Error:","requestCode not found in hash");
			}
		}
		catch (Exception e) 
		{
			Log.e("ERROR:","Issue processing Activity", e);
		}
	}

	public static interface ResultCallbackIF 
	{
	  public void resultOk(Intent data);
	  public void resultCancel(Intent data);

	}
}
