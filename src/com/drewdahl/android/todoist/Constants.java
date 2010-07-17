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

package com.drewdahl.android.todoist;

public final class Constants {
	/* Todoist JSON elements */
	
	public static final String JSON_APITOKEN = "api_token";
	public static final String JSON_CACHECOUNT = "cache_count";
	public static final String JSON_CHAINS = "chains";
	public static final String JSON_CHECKED = "checked";
	public static final String JSON_CHILDREN = "children";
	public static final String JSON_COLLAPSED = "collapsed";
	public static final String JSON_COLOR = "color";
	public static final String JSON_CONTENT = "content";
	
	public static final String JSON_DATEFORMAT = "date_format";
	public static final String JSON_DATESTRING = "date_string";
	public static final String JSON_DUEDATE = "due_date";
	public static final String JSON_EMAIL = "email";
	public static final String JSON_FULLNAME = "full_name";
	public static final String JSON_HASNOTIFICATIONS = "has_notifications";
	public static final String JSON_ID = "id";
	public static final String JSON_INDENT = "indent";
	public static final String JSON_INHISTORY = "in_history";
	public static final String JSON_ISDST = "is_dst";
	public static final String JSON_ITEMID = "item_id";
	public static final String JSON_ITEMLABELID = "item_label.id";
	public static final String JSON_ITEMORDER = "item_order";
	public static final String JSON_JABBER = "jabber";
	public static final String JSON_LABELID = "label_id";
	public static final String JSON_LORDER = "l_order";
	public static final String JSON_MMOFFSET = "mm_offset";
	public static final String JSON_MOBILEHOST = "mobile_host";
	public static final String JSON_MOBILENUMBER = "mobile_number";
	public static final String JSON_MSN = "msn";
	public static final String JSON_NAME = "name";
	public static final String JSON_PRIORITY = "priority";
	public static final String JSON_PROJECTID = "project_id";
	public static final String JSON_SORTORDER = "sort_order";
	public static final String JSON_STARTPAGE = "start_page";
	public static final String JSON_TIMEFORMAT = "time_format";
	public static final String JSON_TIMEZONE = "timezone";
	public static final String JSON_TWITTER = "twitter";
	public static final String JSON_TYPE = "type";
	public static final String JSON_TZOFFSET = "tz_offset";
	public static final String JSON_UID = "uid";
	public static final String JSON_USERID = "user_id";
	public static final String JSON_TOKEN = "token";
	
	public static final String ADAPTER_TITLE = "title";
	public static final String ADAPTER_PROJECT = "project";
	
	public static final String DATE_STRING_SHORT = "MMM d";
	public static final String DATE_STRING_LONG = "EEE " + DATE_STRING_SHORT;
	
	public enum ListType { DATE, PRIORITY, LABEL, PROJECT }
	
	private Constants()
	{
		throw new AssertionError();
	}
}
