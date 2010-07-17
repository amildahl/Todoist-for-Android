/*
 * Copyright (C) 2008 by Alex Brandt <alunduil@alunduil.com>
 * 
 * This program is free software; you can redistribute it and#or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package com.drewdahl.android.todoist.apihandlers;

import android.net.Uri;
import android.provider.BaseColumns;

public final class Todoist {
    public static final String AUTHORITY = "com.google.provider.Todoist";

    // This class cannot be instantiated
    private Todoist() {}
    
    public static final class Caches implements BaseColumns {
        // This class cannot be instantiated
        private Caches() {}

        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/caches");

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of notes.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.cache";

        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single note.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.cache";

        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = "modified DESC";

        public static final String USER_ID = "user_id";
        public static final String PROJECT_ID = "project_id";
        public static final String ITEM_ID = "item_id";
        public static final String CACHE_TIME = "inserted_at";
    }
}
