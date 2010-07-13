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

package com.drewdahl.android.todoist.apihandlers;

import android.net.Uri;
import android.provider.BaseColumns;

public final class Todoist {
    public static final String AUTHORITY = "com.google.provider.Todoist";

    // This class cannot be instantiated
    private Todoist() {}
    
    /**
     * Notes table
     */
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
