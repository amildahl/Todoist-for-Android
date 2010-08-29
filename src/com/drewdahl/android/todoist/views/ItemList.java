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

import com.drewdahl.android.todoist.R;
import com.drewdahl.android.todoist.apihandler.TodoistApiHandler;
import com.drewdahl.android.todoist.models.Item;
import com.drewdahl.android.todoist.preferences.TodoistPreferences;
import com.drewdahl.android.todoist.provider.TodoistProviderMetaData.Items;

import android.app.ListActivity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ItemList extends ListActivity {
    private SimpleCursorAdapter adapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        /**
         * TODO Context menu doesn't work.
         */
        registerForContextMenu(getListView());
        
        /**
         * TODO Add context menus.
         * TODO Roll this into the Item interface?
         * TODO Filter out the completed Items
         */
        Cursor c = getContentResolver().query(Items.CONTENT_URI, null, null, null, null);
        startManagingCursor(c);
        String[] cols = new String[]{Items.CONTENT};
        int[] names = new int[]{R.id.TextViewItemContent};
        adapter = new SimpleCursorAdapter(this, R.layout.item, c, cols, names);
        setListAdapter(adapter);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	super.onCreateOptionsMenu(menu);
    	
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.itemlistoptionmenu, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	Intent intent = null;
    	
    	switch (item.getItemId())
    	{
    	case R.id.menu_newItem:
    		intent = new Intent(this, ItemEdit.class);
    		startActivity(intent);
    		return true;
    	case R.id.menu_projectList:
    		intent = new Intent(this, ProjectList.class);
    		startActivity(intent);
    		/**
    		 * TODO Make this a bit more robust. I mean work ...
    		if (getParent().getParent().getComponentName() == getComponentName()) {
    			finish();
   			}
   			 */
    		return true;
    	case R.id.menu_logout:
    		/**
    		 * TODO Unset the remember me option.
    		 */
    		return true;
    	case R.id.menu_sortDate:
    		/**
    		 * TODO Recreate the adapter with the new qeury?
    		 */
    		return true;
    	case R.id.menu_preferences:
    		intent = new Intent().setClass(this, TodoistPreferences.class);
    		startActivity(intent);
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
    	super.onCreateContextMenu(menu, v, menuInfo);
    	
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.itemlistcontextmenu, menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
    	Intent intent = null;
    	
    	AdapterView.AdapterContextMenuInfo info;
  	    info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
    	long id = getListAdapter().getItemId(info.position);
    	
    	switch (item.getItemId()) {
    	case R.id.menu_deleteItem:
    		/**
    		 * TODO Create a proper query and or change User interface to handle this.
    		 * TODO Run through a picker first?  Will look into this.
    		 */
    		//this.getContentResolver().delete(Items.CONTENT_URI, where, selectionArgs);
    		return true;
    	case R.id.menu_editItem:
    		/**
    		 * TODO make this a picker and take it out of context?
    		 */
    		intent = new Intent(this, ItemEdit.class);
    		startActivity(intent);
    		return true;
    	case R.id.menu_moveItem:
    		/**
    		 * TODO Pop a response dialog to get the new project and update the item.
    		 */
    		return true;
    	default:
    		return super.onContextItemSelected(item);
    	}
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	Uri uri = ContentUris.withAppendedId(getIntent().getData(), id);
    	
    	Log.d(this.toString(), "Clicked item: " + String.valueOf(id));
    	String action = getIntent().getAction();
    	if (Intent.ACTION_PICK.equals(action) || Intent.ACTION_GET_CONTENT.equals(action)) {
    		setResult(RESULT_OK, new Intent().setData(uri));
    	} else {
    		/**
    		 * TODO Update this so it's behind a cleaner API.
    		 */
    		TodoistApiHandler.getInstance().completeItems(true, (int)id);
    		Item i = TodoistApiHandler.getInstance().getItemsById((int)id).get(0);
    		i.save(getContentResolver());
    	}
    }
}
