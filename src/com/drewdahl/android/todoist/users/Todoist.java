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

package com.drewdahl.android.todoist.users;

import android.net.Uri;
import android.provider.BaseColumns;

public final class Todoist {
    public static final String AUTHORITY = "com.google.provider.Todoist";

    // This class cannot be instantiated
    private Todoist() {}
    
    /**
     * Notes table
     */
    public static final class Users implements BaseColumns {
        // This class cannot be instantiated
        private Users() {}

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/users");

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.user";

        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.user";

        public static final String DEFAULT_SORT_ORDER = "modified DESC";

        /**
         * @TODO All of this probably doesn't need to be cached in the sqlite.
         */
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
        public static final String TZ_OFFSET = "tz_offset";
        public static final String MSN = "msn";
        public static final String DEFAULT_REMINDER = "default_reminder";
        public static final String EMAIL = "email";
    }
}
