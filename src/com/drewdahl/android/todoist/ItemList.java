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

import java.util.HashMap;
import java.util.Random;

import com.drewdahl.android.todoist.models.user.User;
import com.drewdahl.android.todoist.provider.TodoistProviderMetaData.Items;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleCursorAdapter;

public class ItemList extends ListActivity {
	private HashMap<Integer, ResultCallbackIF> callbackMap = new HashMap<Integer, ResultCallbackIF>();
    private SimpleCursorAdapter adapter;
    private User user = null;
    
    private static final int REPORT_PROBLEM = Menu.FIRST;
   
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras == null || !extras.containsKey("com.drewdahl.android.todoist.models.user")) {
    		Intent intent = new Intent("com.drewdahl.android.todoist.Login");
    		startActivityWithCallback(intent, new ResultCallbackIF() {
    			@Override
    			public void resultOk(Intent data) {
    				Bundle extras = data.getExtras();
  		        	user = extras.getParcelable("com.drewdahl.android.todoist.models.user");
  		        	connectAdapter();
    			}
    			
    			@Override
    			public void resultCancel(Intent data) {
    				finish();
    			}
    		});
        } else {
            user = extras.getParcelable("com.drewdahl.android.todoist.models.user");
            connectAdapter();
        }
    }
    
    private void connectAdapter() {
        Cursor c = getContentResolver().query(Items.CONTENT_URI, null, null, null, null);
        startManagingCursor(c);
        String[] cols = new String[]{Items.CONTENT,Items.PROJECT_ID};
        int[] names = new int[]{R.id.TextViewItemContent,R.id.TextViewItemCategory};
        adapter = new SimpleCursorAdapter(this, R.layout.item, c, cols, names);
        setListAdapter(adapter);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	super.onCreateOptionsMenu(menu);
    	// TODO Make report a problem occur when something happens.
    	//menu.add(Menu.NONE, REPORT_PROBLEM, Menu.NONE, "Report a Problem");
    	return true;
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	switch (item.getItemId())
    	{
    	case REPORT_PROBLEM:
    		startActivityWithCallback(new Intent("com.drewdahl.android.todoist.SupportForm"), new ItemList.ResultCallbackIF() {
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
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
    
	public void startActivityWithCallback(Intent intent, ResultCallbackIF callback) 
	{
		int correlationId = new Random().nextInt();
		callbackMap.put(correlationId, callback);
		/**
		 * TODO Force Close here, but why?
		 */
		startActivityForResult(intent, correlationId);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data); 
		ResultCallbackIF callback = callbackMap.get(requestCode);

		switch (resultCode) 
		{
		case Activity.RESULT_CANCELED:
			callback.resultCancel(data);
			callbackMap.remove(requestCode);
			break;
		case Activity.RESULT_OK:
			callback.resultOk(data);
			callbackMap.remove(requestCode);
			break;
		default:
			Log.e("Error:","requestCode not found in hash");
		}
	}

	public static interface ResultCallbackIF 
	{
	  public void resultOk(Intent data);
	  public void resultCancel(Intent data);
	}
}
