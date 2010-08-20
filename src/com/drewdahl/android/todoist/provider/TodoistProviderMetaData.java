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

package com.drewdahl.android.todoist.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public final class TodoistProviderMetaData {
    public static final String AUTHORITY = "com.drewdahl.android.todoist.provider.TodoistProvider";

    public static final String DATABASE_NAME = "todoist.db";
    public static final int DATABASE_VERSION = 5;
    
    public static final String ITEMS_TABLE_NAME = "items";
    public static final String PROJECTS_TABLE_NAME = "projects";
    public static final String USERS_TABLE_NAME = "users";
    
    private TodoistProviderMetaData() {}
    
    public static final class Items implements BaseColumns {
        private Items() {}
        public static final String TABLE_NAME = "items";
        
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/items");
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.drewdahl.item";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.drewdahl.item";
        public static final String DEFAULT_SORT_ORDER = "due_date DESC";

        public static final String USER_ID = "user_id";
        public static final String PROJECT_ID = "project_id";
        public static final String DUE_DATE = "due_date";
        public static final String COLLAPSED = "collapsed";
        public static final String IN_HISTORY = "in_history";
        public static final String PRIORITY = "priority";
        public static final String ITEM_ORDER = "item_order";
        public static final String CONTENT = "content";
        public static final String INDENT = "indent";
        public static final String CHECKED = "checked";
        public static final String DATE_STRING = "date_string";
        public static final String CACHE_TIME = "cache_time";
    }
    
    public static final class Projects implements BaseColumns {
        private Projects() {}
        public static final String TABLE_NAME = "projects";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/projects");
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.drewdahl.project";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.drewdahl.project";
        public static final String DEFAULT_SORT_ORDER = "name DESC";

        public static final String USER_ID = "user_id";
        public static final String NAME = "name";
        public static final String COLOR = "color";
        public static final String COLLAPSED = "collapsed";
        public static final String ITEM_ORDER = "item_order";
        public static final String CACHE_COUNT = "cache_count";
        public static final String INDENT = "inent";
        public static final String CACHE_TIME = "cache_time";
    }

    public static final class Users implements BaseColumns {
        private Users() {}
        public static final String TABLE_NAME = "users";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/users");
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.drewdahl.user";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.drewdahl.user";
        public static final String DEFAULT_SORT_ORDER = "cache_time DESC";

        public static final String START_PAGE = "start_page";
        public static final String TWITTER = "twitter";
        public static final String API_TOKEN = "api_token";
        public static final String TIME_FORMAT = "time_format";
        public static final String SORT_ORDER = "sort_order";
        public static final String FULL_NAME = "full_name";
        public static final String MOBILE_NUMBER = "mobile_number";
        public static final String MOBILE_HOST = "mobile_host";
        public static final String TIMEZONE = "timezone";
        public static final String JABBER = "jabber";
        public static final String DATE_FORMAT = "date_format";
        public static final String PREMIUM_UNTIL = "premium_until";
        /**
         * TODO Make this proper?
        public static final String TZ_OFFSET = "tz_offset";
         */
        public static final String MSN = "msn";
        public static final String DEFAULT_REMINDER = "default_reminder";
        public static final String EMAIL = "email";
        public static final String CACHE_TIME = "cache_time";
    }
}
