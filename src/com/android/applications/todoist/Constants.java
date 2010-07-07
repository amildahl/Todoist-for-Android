package com.android.applications.todoist;

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
	
	public static final String ADAPTER_TITLE = "title";
	public static final String ADAPTER_PROJECT = "project";
	
	public static final String DATE_STRING_SHORT = "MMM d";
	public static final String DATE_STRING_LONG = "EEE " + DATE_STRING_SHORT;
	
	private Constants()
	{
		throw new AssertionError();
	}
}
