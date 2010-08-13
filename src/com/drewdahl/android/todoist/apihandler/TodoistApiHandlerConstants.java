package com.drewdahl.android.todoist.apihandler;

public final class TodoistApiHandlerConstants {
	private static final String HTTP = "http://todoist.com/API/";
	private static final String HTTPS = "https://todoist.com/API/";
	
	public static final String LOGIN = 
		HTTPS + "login" + 
		"?email=" + PARAMETERS.EMAIL + 
		"&password=" + PARAMETERS.PASSWORD;
	public static final String GET_TIMEZONES = 
		HTTP + "getTimezones";
	public static final String REGISTER = 
		HTTPS + "register" + 
		"?email=" + PARAMETERS.EMAIL + 
		"&full_name=" + PARAMETERS.FULL_NAME + 
		"&password=" + PARAMETERS.PASSWORD + 
		"&timezone=" + PARAMETERS.TIMEZONE;
	public static final String UPDATE_USER = 
		HTTPS + "updateUser" + 
		"?token=" + PARAMETERS.TOKEN;

	public static final String GET_PROJECTS = 
		HTTP + "getProjects" + 
		"?token=" + PARAMETERS.TOKEN;
	public static final String GET_PROJECT = 
		HTTP + "getProject" + 
		"?token=" + PARAMETERS.TOKEN + 
		"&project_id=" + PARAMETERS.PROJECT_ID;
	public static final String ADD_PROJECT = 
		HTTP + "addProject" + 
		"?name=" + PARAMETERS.NAME + 
		"&token=" + PARAMETERS.TOKEN;
	public static final String UPDATE_PROJECT = 
		HTTP + "updateProject" +
		"?project_id=" + PARAMETERS.PROJECT_ID + 
		"&token=" + PARAMETERS.TOKEN; 
	public static final String DELETE_PROJECT = 
		HTTP + "deleteProject" + 
		"?project_id=" + PARAMETERS.PROJECT_ID + 
		"&token=" + PARAMETERS.TOKEN; 
	
	public static final String GET_LABELS = 
		HTTP + "getLabels" +
		"?project_id=" + PARAMETERS.PROJECT_ID + 
		"&token=" + PARAMETERS.TOKEN;
	public static final String UPDATE_LABEL = 
		HTTP + "updateLabel" +
		"?old_name=" + PARAMETERS.OLD_NAME + 
		"&new_name=" + PARAMETERS.NEW_NAME + 
		"&token=" + PARAMETERS.TOKEN; 
	public static final String DELETE_LABEL = 
		HTTP + "deleteLabel" +
		"?name=" + PARAMETERS.NAME + 
		"&token=" + PARAMETERS.TOKEN;
	
	public static final String GET_UNCOMPLETED_ITEMS = 
		HTTP + "getUncompletedItems" +
		"?project_id=" + PARAMETERS.PROJECT_ID + 
		"&token=" + PARAMETERS.TOKEN;
	public static final String GET_COMPLETED_ITEMS = 
		HTTP + "getCompletedItems" +
		"?project_id=" + PARAMETERS.PROJECT_ID + 
		"&token=" + PARAMETERS.TOKEN;
	public static final String GET_ITEMS_BY_ID = 
		HTTP + "getItemsById" +
		"?ids=" + PARAMETERS.IDS + 
		"&token=" + PARAMETERS.TOKEN;
	public static final String ADD_ITEM = 
		HTTP + "addItem" +
		"?project_id=" + PARAMETERS.PROJECT_ID + 
		"&content=" + PARAMETERS.CONTENT + 
		"&token=" + PARAMETERS.TOKEN;
	public static final String UPDATE_ITEM = 
		HTTP + "updateItem" +
		"?id=" + PARAMETERS.ITEM_ID + 
		"&token=" + PARAMETERS.TOKEN;
	public static final String UPDATE_ORDERS = 
		HTTP + "updateOrders" +
		"?project_id=" + PARAMETERS.PROJECT_ID + 
		"&item_id_list=" + PARAMETERS.IDS + 
		"&token=" + PARAMETERS.TOKEN;
	public static final String UPDATE_RECURRING_DATE = 
		HTTP + "UpdateRecurringDate" +
		"?ids=" + PARAMETERS.IDS + 
		"&token=" + PARAMETERS.TOKEN;
	public static final String DELETE_ITEMS = 
		HTTP + "DeleteItems" +
		"?ids=" + PARAMETERS.IDS + 
		"&token=" + PARAMETERS.TOKEN;
	public static final String COMPLETE_ITEMS = 
		HTTP + "CompleteItems" +
		"?ids=" + PARAMETERS.IDS + 
		"&token=" + PARAMETERS.TOKEN;
	public static final String UNCOMPLETE_ITEMS = 
		HTTP + "UncompleteItems" +
		"?ids=" + PARAMETERS.IDS + 
		"&token=" + PARAMETERS.TOKEN;
	
	public static final String QUERY = 
		HTTP + "query" + 
		"?queries=" + PARAMETERS.QUERIES + 
		"&token=" + PARAMETERS.TOKEN;
	
	public final class JSON {
		public static final String API_TOKEN = "api_token";
		public static final String CACHE_COUNT = "cache_count";
		public static final String CHAINS = "chains";
		public static final String CHECKED = "checked";
		public static final String CHILDREN = "children";
		public static final String COLLAPSED = "collapsed";
		public static final String COLOR = "color";
		public static final String CONTENT = "content";
		public static final String DATE_FORMAT = "date_format";
		public static final String DATE_STRING = "date_string";
		public static final String DUE_DATE = "due_date";
		public static final String EMAIL = "email";
		public static final String FULL_NAME = "full_name";
		public static final String HAS_NOTIFICATIONS = "has_notifications";
		public static final String ID = "id";
		public static final String INDENT = "indent";
		public static final String IN_HISTORY = "in_history";
		public static final String IS_DST = "is_dst";
		public static final String ITEM_ID = "item_id";
		public static final String ITEM_LABEL_ID = "item_label.id";
		public static final String ITEM_ORDER = "item_order";
		public static final String JABBER = "jabber";
		public static final String LABEL_ID = "label_id";
		public static final String L_ORDER = "l_order";
		public static final String MM_OFFSET = "mm_offset";
		public static final String MOBILE_HOST = "mobile_host";
		public static final String MOBILE_NUMBER = "mobile_number";
		public static final String MSN = "msn";
		public static final String NAME = "name";
		public static final String PRIORITY = "priority";
		public static final String PROJECT_ID = "project_id";
		public static final String SORT_ORDER = "sort_order";
		public static final String START_PAGE = "start_page";
		public static final String TIME_FORMAT = "time_format";
		public static final String TIMEZONE = "timezone";
		public static final String TWITTER = "twitter";
		public static final String TYPE = "type";
		public static final String TZ_OFFSET = "tz_offset";
		public static final String UID = "uid";
		public static final String USER_ID = "user_id";
		public static final String TOKEN = "token";
	}
	
	public final class PARAMETERS {
		public static final String TOKEN = "My Token";
		public static final String PROJECT_ID = "Project ID";
		public static final String EMAIL = "My E-Mail";
		public static final String PASSWORD = "My Password";
		public static final String FULL_NAME = "My Full Name";
		public static final String TIMEZONE = "My Timezone";
		public static final String NAME = "My Name";
		public static final String COLOR = "My Color";
		public static final String INDENT = "My Indent";
		public static final String ORDER = "My Order";
		public static final String OLD_NAME = "My Old Name";
		public static final String NEW_NAME = "My New Name";
		public static final String IDS = "My IDS";
		public static final String CONTENT = "My Content";
		public static final String DATE_STRING = "My Date String";
		public static final String PRIORITY = "My Priority";
		public static final String ITEM_ID = "My Item ID";
		public static final String QUERIES = "My Queries";
		public static final String IN_HISTORY = "In History";
	}
	
	public final class OPTIONAL_PARAMETERS {
		public static final String EMAIL = "&email=" + PARAMETERS.EMAIL;
		public static final String FULL_NAME = "&full_name=" + PARAMETERS.FULL_NAME;
		public static final String PASSWORD = "&password=" + PARAMETERS.PASSWORD;
		public static final String TIMEZONE = "&timezone=" + PARAMETERS.TIMEZONE;
		public static final String NAME = "&name=" + PARAMETERS.NAME;
		public static final String COLOR = "&color=" + PARAMETERS.COLOR;
		public static final String INDENT = "&indent=" + PARAMETERS.INDENT;
		public static final String ORDER = "&order=" + PARAMETERS.ORDER;
		public static final String DATE_STRING = "&date_string=" + PARAMETERS.DATE_STRING;
		public static final String PRIORITY = "&priority=" + PARAMETERS.PRIORITY;
		public static final String CONTENT = "&content=" + PARAMETERS.CONTENT;
		public static final String IN_HISTORY = "&in_history=" + PARAMETERS.IN_HISTORY;
	}
}
