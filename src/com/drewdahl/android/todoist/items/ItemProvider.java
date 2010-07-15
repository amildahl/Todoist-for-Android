/*
 * Copyright (C) 2010 Alex Brandt <alunduil@alunduil.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.drewdahl.android.todoist.items;

/**
 * @note This may change if Bug 13 takes.
 */
import com.drewdahl.android.todoist.items.Todoist.Items;
import com.drewdahl.android.todoist.projects.Todoist.Projects;
import com.drewdahl.android.todoist.users.Todoist.Users;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.LiveFolders;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

/**
 * Provides access to a database of notes. Each note has a title, the note
 * itself, a creation date and a modified data.
 */
public class ItemProvider extends ContentProvider {
	
	private static final String TAG = "ItemProvider";

    private static final String DATABASE_NAME = "todoist.db";
    private static final int DATABASE_VERSION = 1;
    private static final String PROJECTS_TABLE_NAME = "items";

    private static HashMap<String, String> sItemProjectionMap;
    private static HashMap<String, String> sLiveFolderProjectionMap;

    private static final int ITEMS = 1;
    private static final int ITEM_ID = 2;
    private static final int LIVE_FOLDER_PROJECTS = 3;

    private static final UriMatcher sUriMatcher;

    /**
     * This class helps open, create, and upgrade the database file.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + PROJECTS_TABLE_NAME + " ("
            		+ Items.DUE_DATE + " INTEGER,"
            		+ Users._ID + " INTEGER,"
            		+ Items.COLLAPSED + " INTGER,"
            		+ Items.IN_HISTORY + " INTEGER,"
            		+ Items.PRIORITY + " INTEGER,"
            		+ Items.ITEM_ORDER + " INTGER,"
            		+ Items.CONTENT + " TEXT,"
            		+ Items.INDENT + " INTEGER,"
            		+ Projects._ID + " INTEGER,"
            		+ Items._ID + " INTEGER PRIMARY KEY,"
            		+ Items.CHECKED + " INTEGER,"
            		+ Items.DATE_STRING + " TEXT"
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            /**
             * @note This will need to be a migration from now on!
             */
            db.execSQL("DROP TABLE IF EXISTS projects");
            onCreate(db);
        }
    }

    private DatabaseHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(PROJECTS_TABLE_NAME);

        switch (sUriMatcher.match(uri)) {
        case ITEMS:
            qb.setProjectionMap(sItemProjectionMap);
            break;

        case ITEM_ID:
            qb.setProjectionMap(sItemProjectionMap);
            qb.appendWhere(Projects._ID + "=" + uri.getPathSegments().get(1));
            break;

        case LIVE_FOLDER_PROJECTS:
            qb.setProjectionMap(sLiveFolderProjectionMap);
            break;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // If no sort order is specified use the default
        String orderBy;
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = Todoist.Items.DEFAULT_SORT_ORDER;
        } else {
            orderBy = sortOrder;
        }

        // Get the database and run the query
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);

        // Tell the cursor what uri to watch, so it knows when its source data changes
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
        case ITEMS:
        case LIVE_FOLDER_PROJECTS:
            return Projects.CONTENT_TYPE;

        case ITEM_ID:
            return Projects.CONTENT_ITEM_TYPE;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        // Validate the requested uri
        if (sUriMatcher.match(uri) != ITEMS) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long rowId = db.insert(PROJECTS_TABLE_NAME, Projects.NAME, values);
        if (rowId > 0) {
            Uri projectUri = ContentUris.withAppendedId(Projects.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(projectUri, null);
            return projectUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
        case ITEMS:
            count = db.delete(PROJECTS_TABLE_NAME, where, whereArgs);
            break;

        case ITEM_ID:
            String noteId = uri.getPathSegments().get(1);
            count = db.delete(PROJECTS_TABLE_NAME, Projects._ID + "=" + noteId
                    + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
            break;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
        case ITEMS:
            count = db.update(PROJECTS_TABLE_NAME, values, where, whereArgs);
            break;

        case ITEM_ID:
            String noteId = uri.getPathSegments().get(1);
            count = db.update(PROJECTS_TABLE_NAME, values, Projects._ID + "=" + noteId
                    + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
            break;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(Todoist.AUTHORITY, "items", ITEMS);
        sUriMatcher.addURI(Todoist.AUTHORITY, "itemss/#", ITEM_ID);
        sUriMatcher.addURI(Todoist.AUTHORITY, "live_folders/items", LIVE_FOLDER_PROJECTS);

        sItemProjectionMap = new HashMap<String, String>();
        /**
         * TODO Add all mappings.
         */
        sItemProjectionMap.put(Items._ID, Items._ID);

        // Support for Live Folders.
        sLiveFolderProjectionMap = new HashMap<String, String>();
        sLiveFolderProjectionMap.put(LiveFolders._ID, Projects._ID + " AS " +
                LiveFolders._ID);
        sLiveFolderProjectionMap.put(LiveFolders.NAME, Projects.NAME + " AS " +
                LiveFolders.NAME);
        // Add more columns here for more robust Live Folders.
    }
}
