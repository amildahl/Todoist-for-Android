package com.drewdahl.android.todoist.provider;

import com.drewdahl.android.todoist.provider.TodoistProviderMetaData;
import com.drewdahl.android.todoist.provider.TodoistProviderMetaData.Items;
import com.drewdahl.android.todoist.provider.TodoistProviderMetaData.Projects;
import com.drewdahl.android.todoist.provider.TodoistProviderMetaData.Users;

import java.util.HashMap;

import android.text.TextUtils;
import android.util.Log;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.content.UriMatcher;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.ContentUris;
import android.database.Cursor;
import android.database.SQLException;
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
		/**
		 * TODO Make this proper?
		sUsersProjectionMap.put(Users.TZ_OFFSET, Users.TZ_OFFSET);
		 */
		sUsersProjectionMap.put(Users.MSN, Users.MSN);
		sUsersProjectionMap.put(Users.DEFAULT_REMINDER, Users.DEFAULT_REMINDER);
		sUsersProjectionMap.put(Users.EMAIL, Users.EMAIL);
	}
	
	private static final UriMatcher sUriMatcher;
	private static final int INCOMING_ITEM_COLLECTION_URI_INDICATOR = 1;
	private static final int INCOMING_SINGLE_ITEM_URI_INDICATOR = 2;
	private static final int INCOMING_PROJECT_COLLECTION_URI_INDICATOR = 3;
	private static final int INCOMING_SINGLE_PROJECT_URI_INDICATOR = 4;
	private static final int INCOMING_USER_COLLECTION_URI_INDICATOR = 5;
	private static final int INCOMING_SINGLE_USER_URI_INDICATOR = 6;
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(TodoistProviderMetaData.AUTHORITY, "items", INCOMING_ITEM_COLLECTION_URI_INDICATOR);
		sUriMatcher.addURI(TodoistProviderMetaData.AUTHORITY, "items/#", INCOMING_SINGLE_ITEM_URI_INDICATOR);
		sUriMatcher.addURI(TodoistProviderMetaData.AUTHORITY, "projects", INCOMING_PROJECT_COLLECTION_URI_INDICATOR);
		sUriMatcher.addURI(TodoistProviderMetaData.AUTHORITY, "projects/#", INCOMING_SINGLE_PROJECT_URI_INDICATOR);
		sUriMatcher.addURI(TodoistProviderMetaData.AUTHORITY, "users", INCOMING_USER_COLLECTION_URI_INDICATOR);
		sUriMatcher.addURI(TodoistProviderMetaData.AUTHORITY, "users/#", INCOMING_SINGLE_USER_URI_INDICATOR);
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
					+ Items.USER_ID + " INTEGER,"
					+ Items.PROJECT_ID + " INTEGER,"
					+ Items.DUE_DATE + " INTEGER,"
					+ Items.COLLAPSED + " INTEGER,"
					+ Items.IN_HISTORY + " INTEGER,"
					+ Items.PRIORITY + " INTEGER,"
					+ Items.ITEM_ORDER + " INTEGER,"
					+ Items.CONTENT + " TEXT,"
					+ Items.INDENT + " INTEGER,"
					+ Items.CHECKED + " INTEGER,"
					+ Items.DATE_STRING + " TEXT,"
					+ Items.CACHE_TIME + " INTEGER"
					+ ");");
			db.execSQL("CREATE TABLE " + Projects.TABLE_NAME + " ("
					+ Projects._ID + " INTEGER PRIMARY KEY,"
					+ Projects.USER_ID + " INTEGER,"
					+ Projects.NAME + " TEXT,"
					+ Projects.COLOR + " TEXT,"
					+ Projects.COLLAPSED + " INTEGER,"
					+ Projects.ITEM_ORDER + " INTEGER,"
					+ Projects.CACHE_COUNT + " INTEGER,"
					+ Projects.INDENT + " INTEGER,"
					+ Projects.CACHE_TIME + " INTEGER"
					+ ");");
			db.execSQL("CREATE TABLE " + Users.TABLE_NAME + " ("
					+ Users._ID + " INTEGER PRIMARY KEY,"
					+ Users.EMAIL + " TEXT,"
					+ Users.FULL_NAME + " TEXT,"
					+ Users.API_TOKEN + " TEXT,"
					+ Users.START_PAGE + " TEXT,"
					+ Users.TIMEZONE + " TEXT,"
					// TODO Users.TZ_OFFSET.
					+ Users.TIME_FORMAT + " INTEGER,"
					+ Users.DATE_FORMAT + " INTEGER,"
					+ Users.SORT_ORDER + " INTEGER,"
					+ Users.TWITTER + " TEXT,"
					+ Users.JABBER + " TEXT,"
					+ Users.MSN + " TEXT,"
					+ Users.MOBILE_NUMBER + " TEXT,"
					+ Users.MOBILE_HOST + " TEXT,"
					+ Users.DEFAULT_REMINDER + " TEXT,"
					+ Users.PREMIUM_UNTIL + " TEXT,"
					+ Users.CACHE_TIME + " INTEGER"
					+ ");");
			/**
			 * TODO Read http://justatheory.com/computers/databases/sqlite/foreign_key_triggers.html
			 */
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("TodoistProvider", "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all data");
			db.execSQL("DROP TABLE IF EXISTS " + Items.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + Projects.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + Users.TABLE_NAME);
			/**
			 * TODO Create migrations and manageability.
			 */
			onCreate(db);
		}
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case INCOMING_ITEM_COLLECTION_URI_INDICATOR:
			return Items.CONTENT_TYPE;
		case INCOMING_SINGLE_ITEM_URI_INDICATOR:
			return Items.CONTENT_ITEM_TYPE;
		case INCOMING_PROJECT_COLLECTION_URI_INDICATOR:
			return Projects.CONTENT_TYPE;
		case INCOMING_SINGLE_PROJECT_URI_INDICATOR:
			return Projects.CONTENT_ITEM_TYPE;
		case INCOMING_USER_COLLECTION_URI_INDICATOR:
			return Users.CONTENT_TYPE;
		case INCOMING_SINGLE_USER_URI_INDICATOR:
			return Users.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		
		String orderBy;
		
		switch (sUriMatcher.match(uri)) {
		case INCOMING_ITEM_COLLECTION_URI_INDICATOR:
			qb.setTables(Items.TABLE_NAME);
			qb.setProjectionMap(sItemsProjectionMap);
			orderBy = Items.DEFAULT_SORT_ORDER;
			break;
		case INCOMING_SINGLE_ITEM_URI_INDICATOR:
			qb.setTables(Items.TABLE_NAME);
			qb.setProjectionMap(sItemsProjectionMap);
			qb.appendWhere(Items._ID + "=" + uri.getPathSegments().get(1));
			orderBy = Items.DEFAULT_SORT_ORDER;
			break;
		case INCOMING_PROJECT_COLLECTION_URI_INDICATOR:
			qb.setTables(Projects.TABLE_NAME);
			qb.setProjectionMap(sProjectsProjectionMap);
			orderBy = Projects.DEFAULT_SORT_ORDER;
			break;
		case INCOMING_SINGLE_PROJECT_URI_INDICATOR:
			qb.setTables(Projects.TABLE_NAME);
			qb.setProjectionMap(sProjectsProjectionMap);
			qb.appendWhere(Projects._ID + "=" + uri.getPathSegments().get(1));
			orderBy = Projects.DEFAULT_SORT_ORDER;
			break;
		case INCOMING_USER_COLLECTION_URI_INDICATOR:
			qb.setTables(Users.TABLE_NAME);
			qb.setProjectionMap(sUsersProjectionMap);
			orderBy = Users.DEFAULT_SORT_ORDER;
			break;
		case INCOMING_SINGLE_USER_URI_INDICATOR:
			qb.setTables(Users.TABLE_NAME);
			qb.setProjectionMap(sUsersProjectionMap);
			qb.appendWhere(Users._ID + "=" + uri.getPathSegments().get(1));
			orderBy = Users.DEFAULT_SORT_ORDER;
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
		if (!TextUtils.isEmpty(sortOrder)) {
			orderBy = sortOrder; 
		}
		
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Long now = Long.valueOf(System.currentTimeMillis());
		
		String table;
		String column; // This column will be nulled if all values are empty.
		
		switch (sUriMatcher.match(uri)) {
		case INCOMING_ITEM_COLLECTION_URI_INDICATOR:
			table = Items.TABLE_NAME;
			column = Items.CONTENT;
			
			if (
					!values.containsKey(Items._ID) ||
					!values.containsKey(Items.USER_ID) ||
					!values.containsKey(Items.DUE_DATE) ||
					!values.containsKey(Items.DATE_STRING) ||
					!values.containsKey(Items.COLLAPSED) ||
					!values.containsKey(Items.INDENT)
					) {
				throw new IllegalArgumentException("Missing required value");
			}
			
			values.put(Items.CACHE_TIME, now);

			break;
		case INCOMING_PROJECT_COLLECTION_URI_INDICATOR:
			table = Projects.TABLE_NAME;
			column = Projects.NAME;

			if (
					!values.containsKey(Projects._ID) ||
					!values.containsKey(Projects.USER_ID) ||
					!values.containsKey(Projects.NAME) ||
					!values.containsKey(Projects.CACHE_COUNT) ||
					!values.containsKey(Projects.COLOR) ||
					!values.containsKey(Projects.INDENT) ||
					!values.containsKey(Projects.ITEM_ORDER) ||
					!values.containsKey(Projects.COLLAPSED)
					) {
				throw new IllegalArgumentException("Missing required value");
			}
			
			values.put(Projects.CACHE_TIME, now);
			
			break;
		case INCOMING_USER_COLLECTION_URI_INDICATOR:
			table = Users.TABLE_NAME;
			column = Users.FULL_NAME;

			if (
					!values.containsKey(Users.EMAIL) ||
					!values.containsKey(Users.FULL_NAME) ||
					!values.containsKey(Users._ID) ||
					!values.containsKey(Users.API_TOKEN) ||
					!values.containsKey(Users.START_PAGE) ||
					!values.containsKey(Users.TIMEZONE) ||
					//!values.containsKey(Users.TZ_OFFSET) ||
					!values.containsKey(Users.TIME_FORMAT) ||
					!values.containsKey(Users.DATE_FORMAT) ||
					!values.containsKey(Users.SORT_ORDER)
					) {
				throw new IllegalArgumentException("Missing required value");
			}
			
			values.put(Users.CACHE_TIME, now);
			
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long rowId = db.insert(table, column, values);
		if (rowId > 0) {
			Uri insertedUri = ContentUris.withAppendedId(uri, rowId);
			getContext().getContentResolver().notifyChange(insertedUri, null);
			return insertedUri;
		}
		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		String rowId;
		
		Long now = Long.valueOf(System.currentTimeMillis());

		switch (sUriMatcher.match(uri)) {
		case INCOMING_ITEM_COLLECTION_URI_INDICATOR:
			values.put(Items.CACHE_TIME, now);
			count = db.update(Items.TABLE_NAME, values, where, whereArgs);
			break;
		case INCOMING_SINGLE_ITEM_URI_INDICATOR:
			values.put(Items.CACHE_TIME, now);
			rowId = uri.getPathSegments().get(1);
			count = db.update(Items.TABLE_NAME, values, Items._ID + "=" + rowId + (!TextUtils.isEmpty(where) ? " AND (" + where + ")" : ""), whereArgs);
			break;
		case INCOMING_PROJECT_COLLECTION_URI_INDICATOR:
			values.put(Projects.CACHE_TIME, now);
			count = db.update(Projects.TABLE_NAME, values, where, whereArgs);
			break;
		case INCOMING_SINGLE_PROJECT_URI_INDICATOR:
			values.put(Projects.CACHE_TIME, now);
			rowId = uri.getPathSegments().get(1);
			count = db.update(Projects.TABLE_NAME, values, Projects._ID + "=" + rowId + (!TextUtils.isEmpty(where) ? " AND (" + where + ")" : ""), whereArgs);
			break;
		case INCOMING_USER_COLLECTION_URI_INDICATOR:
			values.put(Users.CACHE_TIME, now);
			count = db.update(Users.TABLE_NAME, values, where, whereArgs);
			break;
		case INCOMING_SINGLE_USER_URI_INDICATOR:
			values.put(Users.CACHE_TIME, now);
			rowId = uri.getPathSegments().get(1);
			count = db.update(Users.TABLE_NAME, values, Users._ID + "=" + rowId + (!TextUtils.isEmpty(where) ? " AND (" + where + ")" : ""), whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unkown URI " + uri);
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		String rowId;
		/**
		 * TODO Same stuff as update, except delete the stuff.
		 */
		switch (sUriMatcher.match(uri)) {
		case INCOMING_ITEM_COLLECTION_URI_INDICATOR:
			count = db.delete(Items.TABLE_NAME, where, whereArgs);
			break;
		case INCOMING_SINGLE_ITEM_URI_INDICATOR:
			rowId = uri.getPathSegments().get(1);
			count = db.delete(Items.TABLE_NAME, Items._ID + "=" + rowId + (!TextUtils.isEmpty(where) ? " AND (" + where + ")" : ""), whereArgs);
			break;
		case INCOMING_PROJECT_COLLECTION_URI_INDICATOR:
			count = db.delete(Projects.TABLE_NAME, where, whereArgs);
			break;
		case INCOMING_SINGLE_PROJECT_URI_INDICATOR:
			rowId = uri.getPathSegments().get(1);
			count = db.delete(Projects.TABLE_NAME, Projects._ID + "=" + rowId + (!TextUtils.isEmpty(where) ? " AND (" + where + ")" : ""), whereArgs);
			break;
		case INCOMING_USER_COLLECTION_URI_INDICATOR:
			count = db.delete(Users.TABLE_NAME, where, whereArgs);
			break;
		case INCOMING_SINGLE_USER_URI_INDICATOR:
			rowId = uri.getPathSegments().get(1);
			count = db.delete(Users.TABLE_NAME, Users._ID + "=" + rowId + (!TextUtils.isEmpty(where) ? " AND (" + where + ")" : ""), whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unkown URI " + uri);
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
}
