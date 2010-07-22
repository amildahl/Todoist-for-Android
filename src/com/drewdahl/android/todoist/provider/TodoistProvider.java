package com.drewdahl.android.todoist.provider;

import com.drewdahl.android.todoist.provider.TodoistProviderMetaData.Projects;
import com.drewdahl.android.todoist.provider.TodoistProviderMetaData.Items;
import com.drewdahl.android.todoist.provider.TodoistProviderMetaData.Users;
import com.drewdahl.android.todoist.provider.TodoistProviderMetaData.CacheTimes;

import java.util.HashMap;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.UriMatcher;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class TodoistProvider extends ContentProvider {
	private static HashMap<String, String> sItemsProjectionMap;
	static {
		sItemsProjectionMap = new HashMap<String, String>();
		sItemsProjectionMap.put(Items._ID, Items._ID);
		sItemsProjectionMap.put(Items.DUE_DATE, Items.DUE_DATE);
		sItemsProjectionMap.put(Items.COLLAPSED, Items.COLLAPSED);
		sItemsProjectionMap.put(Items.IN_HISTORY, Items.IN_HISTORY);
		sItemsProjectionMap.put(Items.PRIORITY, Items.PRIORITY);
		sItemsProjectionMap.put(Items.ITEM_ORDER, Items.ITEM_ORDER);
		sItemsProjectionMap.put(Items.CONTENT, Items.CONTENT);
		sItemsProjectionMap.put(Items.INDENT, Items.INDENT);
		sItemsProjectionMap.put(Items.CHECKED, Items.CHECKED);
		sItemsProjectionMap.put(Items.DATE_STRING, Items.DATE_STRING);		
	}
	
	private static HashMap<String, String> sProjectsProjectionMap;
	static {
		sProjectsProjectionMap = new HashMap<String, String>();
		sProjectsProjectionMap.put(Projects._ID, Projects._ID);
		sProjectsProjectionMap.put(Projects.NAME, Projects.NAME);
		sProjectsProjectionMap.put(Projects.COLOR, Projects.COLOR);
		sProjectsProjectionMap.put(Projects.COLLAPSED, Projects.COLLAPSED);
		sProjectsProjectionMap.put(Projects.ITEM_ORDER, Projects.ITEM_ORDER);
		sProjectsProjectionMap.put(Projects.CACHE_COUNT, Projects.CACHE_COUNT);
		sProjectsProjectionMap.put(Projects.INDENT, Projects.INDENT);
	}
	
	private static HashMap<String, String> sUsersProjectionMap;
	static {
		sUsersProjectionMap = new HashMap<String, String>();
		sUsersProjectionMap.put(Users._ID, Users._ID);
		sUsersProjectionMap.put(Users.START_PAGE, Users.START_PAGE);
		sUsersProjectionMap.put(Users.TWITTER, Users.TWITTER);
		sUsersProjectionMap.put(Users.API_TOKEN, Users.API_TOKEN);
		sUsersProjectionMap.put(Users.TIME_FORMAT, Users.TIME_FORMAT);
		sUsersProjectionMap.put(Users.SORT_ORDER, Users.SORT_ORDER);
		sUsersProjectionMap.put(Users.FULL_NAME, Users.FULL_NAME);
		sUsersProjectionMap.put(Users.MOBILE_NUMBER, Users.MOBILE_NUMBER);
		sUsersProjectionMap.put(Users.MOBILE_HOST, Users.MOBILE_HOST);
		sUsersProjectionMap.put(Users.TIMEZONE, Users.TIMEZONE);
		sUsersProjectionMap.put(Users.JABBER, Users.JABBER);
		sUsersProjectionMap.put(Users.DATE_FORMAT, Users.DATE_FORMAT);
		sUsersProjectionMap.put(Users.PREMIUM_UNTIL, Users.PREMIUM_UNTIL);
		sUsersProjectionMap.put(Users.TZ_OFFSET, Users.TZ_OFFSET);
		sUsersProjectionMap.put(Users.MSN, Users.MSN);
		sUsersProjectionMap.put(Users.DEFAULT_REMINDER, Users.DEFAULT_REMINDER);
		sUsersProjectionMap.put(Users.EMAIL, Users.EMAIL);
	}
	
	private static HashMap<String, String> sCacheTimesProjectionMap;
	static {
		sCacheTimesProjectionMap = new HashMap<String, String>();
		sCacheTimesProjectionMap.put(CacheTimes._ID, CacheTimes._ID);
		sCacheTimesProjectionMap.put(CacheTimes.USER_ID, CacheTimes.USER_ID);
		sCacheTimesProjectionMap.put(CacheTimes.PROJECT_ID, CacheTimes.PROJECT_ID);
		sCacheTimesProjectionMap.put(CacheTimes.ITEM_ID, CacheTimes.ITEM_ID);
		sCacheTimesProjectionMap.put(CacheTimes.INSERTED_AT, CacheTimes.INSERTED_AT);		
	}
	
	private static final UriMatcher sUriMatcher;
	private static final int INCOMING_ITEM_COLLECTION_URI_INDICATOR = 1;
	private static final int INCOMING_SINGLE_ITEM_URI_INDICATOR = 2;
	private static final int INCOMING_PROJECT_COLLECTION_URI_INDICATOR = 3;
	private static final int INCOMING_SINGLE_PROJECT_URI_INDICATOR = 4;
	private static final int INCOMING_USER_COLLECTION_URI_INDICATOR = 5;
	private static final int INCOMING_SINGLE_USER_URI_INDICATOR = 6;
	private static final int INCOMING_CACHETIME_COLLECTION_URI_INDICATOR = 7;
	private static final int INCOMING_SINGLE_CACHETIME_URI_INDICATOR = 8;
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(TodoistProviderMetaData.AUTHORITY, "items", INCOMING_ITEM_COLLECTION_URI_INDICATOR);
		sUriMatcher.addURI(TodoistProviderMetaData.AUTHORITY, "items/#", INCOMING_SINGLE_ITEM_URI_INDICATOR);
		sUriMatcher.addURI(TodoistProviderMetaData.AUTHORITY, "projects", INCOMING_PROJECT_COLLECTION_URI_INDICATOR);
		sUriMatcher.addURI(TodoistProviderMetaData.AUTHORITY, "projects/#", INCOMING_SINGLE_PROJECT_URI_INDICATOR);
		sUriMatcher.addURI(TodoistProviderMetaData.AUTHORITY, "users", INCOMING_USER_COLLECTION_URI_INDICATOR);
		sUriMatcher.addURI(TodoistProviderMetaData.AUTHORITY, "users/#", INCOMING_SINGLE_USER_URI_INDICATOR);
		sUriMatcher.addURI(TodoistProviderMetaData.AUTHORITY, "cacheitems", INCOMING_CACHETIME_COLLECTION_URI_INDICATOR);
		sUriMatcher.addURI(TodoistProviderMetaData.AUTHORITY, "cacheitems/#", INCOMING_SINGLE_CACHETIME_URI_INDICATOR);
	}

	private DatabaseHelper mOpenHelper;
	
	@Override
	public boolean onCreate() {
		mOpenHelper = new DatabaseHelper(getContext());
		return true;
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, TodoistProviderMetaData.DATABASE_NAME, null, TodoistProviderMetaData.DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + Items.TABLE_NAME + " ("
				+ Items._ID + " INTEGER PRIMARY KEY,"
				+ Items.DUE_DATE + " INTEGER,"
				+ Items.COLLAPSED + " INTEGER,"
			sItemsProjectionMap.put(Items.COLLAPSED, Items.COLLAPSED);
			sItemsProjectionMap.put(Items.IN_HISTORY, Items.IN_HISTORY);
			sItemsProjectionMap.put(Items.PRIORITY, Items.PRIORITY);
			sItemsProjectionMap.put(Items.ITEM_ORDER, Items.ITEM_ORDER);
			sItemsProjectionMap.put(Items.CONTENT, Items.CONTENT);
			sItemsProjectionMap.put(Items.INDENT, Items.INDENT);
			sItemsProjectionMap.put(Items.CHECKED, Items.CHECKED);
			sItemsProjectionMap.put(Items.DATE_STRING, Items.DATE_STRING);
				");");
		}
	}

	@Override
	public int delete(Uri uri, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
