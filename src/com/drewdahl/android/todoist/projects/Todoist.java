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

package com.drewdahl.android.todoist.projects;

import android.net.Uri;
import android.provider.BaseColumns;

public final class Todoist {
    public static final String AUTHORITY = "com.google.provider.Todoist";

    // This class cannot be instantiated
    private Todoist() {}
    
    /**
     * Notes table
     */
    public static final class Projects implements BaseColumns {
        // This class cannot be instantiated
        private Projects() {}

        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/projects");

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of notes.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.project";

        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single note.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.project";

        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = "modified DESC";

        public static final String NAME = "name";
        public static final String COLOR = "color";
        public static final String COLLAPSED = "collapsed";
        public static final String ITEM_ORDER = "item_order";
        public static final String CACHE_COUNT = "cache_count";
        public static final String INDENT = "inent";
    }
}