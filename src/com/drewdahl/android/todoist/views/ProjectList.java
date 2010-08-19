package com.drewdahl.android.todoist.views;

import com.drewdahl.android.todoist.R;
import com.drewdahl.android.todoist.provider.TodoistProviderMetaData.Projects;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleCursorAdapter;

public class ProjectList extends ListActivity {
    private SimpleCursorAdapter adapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        /**
         * TODO Add context menus.
         * TODO Roll this into the Item interface?
         */
        Cursor c = getContentResolver().query(Projects.CONTENT_URI, null, null, null, null);
        startManagingCursor(c);
        String[] cols = new String[]{Projects.CACHE_COUNT, Projects.NAME};
        int[] names = new int[]{R.id.TextViewProjectCacheCount, R.id.TextViewProjectName};
        adapter = new SimpleCursorAdapter(this, R.layout.project, c, cols, names);
        setListAdapter(adapter);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	super.onCreateOptionsMenu(menu);
    	
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.projectlistoptionmenu, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	switch (item.getItemId())
    	{
    	case R.id.menu_newProject:
    		return true;
    	case R.id.menu_itemList:
    		Intent intent = new Intent(this, ItemList.class);
    		startActivity(intent);
    		/**
    		 * TODO Make this a bit more robust.  I mean work ...
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
    	case R.id.menu_preferences:
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
    	inflater.inflate(R.menu.projectlistcontextmenu, menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
    	switch (item.getItemId()) {
    	case R.id.menu_deleteProject:
    		/**
    		 * TODO Create a proper query and or change User interface to handle this.
    		 * TODO Run through a picker first?  Will look into this.
    		 */
    		//this.getContentResolver().delete(Items.CONTENT_URI, where, selectionArgs);
    		return true;
    	case R.id.menu_editProject:
    		/**
    		 * TODO make this a picker and take it out of context?
    		 */
    		new Intent(this, ItemEdit.class);
    		return true;
    	case R.id.menu_addItem:
    		return true;
    	default:
    		return super.onContextItemSelected(item);
    	}
    }
}
