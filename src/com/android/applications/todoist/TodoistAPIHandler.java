package com.android.applications.todoist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TodoistAPIHandler {

	private String token;
	
	public TodoistAPIHandler( String _token)
	{
		token = _token;
	}
	
	private String navigate(String URI)
	{
		String jsonData = "";
		
		try
		{
			WebRequest browser = new WebRequest(URI);
			jsonData = ((String)browser.getContent());
		}
		catch (Exception e)
		{
			//output (e.getMessage());	
		}
		
		return jsonData;
	}
	
	/* User API - login
	 * Use HTTPS:\\
	 * Parameters: email & password
	 * Returns: JSON object w/ User Info
	 * Error: "LOGIN_ERROR"
	 */
	public User login(String email, String password)
	{
		return this.parseLogin(this.navigate(LOGIN.replace(PARAM_EMAIL, email).replace(PARAM_PASSWORD, password)));
	}
	
	public User parseLogin(String jsonData)
	{
		try
		{
			JSONObject obj = new JSONObject(jsonData);
			
			return new User(obj.getString(JSON_EMAIL), obj.getString(JSON_FULLNAME), obj.getString(JSON_ID), obj.getString(JSON_APITOKEN), 
					obj.getString(JSON_STARTPAGE), obj.getString(JSON_TIMEZONE), obj.getString(JSON_TZOFFSET), obj.getString(JSON_TIMEFORMAT), 
					obj.getString(JSON_DATEFORMAT), obj.getString(JSON_SORTORDER), obj.getString(JSON_TWITTER), obj.getString(JSON_JABBER), 
					obj.getString(JSON_MSN), obj.getString(JSON_MOBILENUMBER), obj.getString(JSON_MOBILEHOST));
			
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return new User();
	}

	/* Projects API - getProjects
	 * Use HTTP:\\
	 * Parameters: token
	 * Returns: JSON object w/ All Project Info
	 * Error: None
	 */
	public Projects getProjects()
	{		
		return this.parseProjects(this.navigate(GET_PROJECTS.replace(PARAM_TOKEN,token)));
	}
	
	// TODO: Exceptions
	private Projects parseProjects(String jsonData)
	{
		JSONObject obj;
		Projects projects = new Projects();
		Project project;
		try 
		{
			JSONArray jArray = new JSONArray(jsonData);
			for(int i=0;i<jArray.length();i++)
			{
				obj = jArray.getJSONObject(i);
				project = new Project(obj.getString(JSON_USERID), obj.getString(JSON_NAME), obj.getString(JSON_COLOR), obj.getString(JSON_COLLAPSED), 
						obj.getString(JSON_ITEMORDER), obj.getString(JSON_CACHECOUNT), obj.getString(JSON_INDENT), obj.getString(JSON_ID));
				projects.addProject(project);
			}	
			
			return projects;
		} 
		catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new Projects();
	}
	
	// TODO: Output Exceptions
	public Tasks getUncompletedTasks(String project_id)
	{
		return this.parseTasks(this.navigate(GET_UNCOMPLETED_ITEMS.replace(PARAM_PROJECTID,project_id).replace(PARAM_TOKEN,token)));
	}
	
	// TODO: Everything...
	private Tasks parseTasks(String jsonData)
	{
		return new Tasks();
	}
	
	/* URI Parameter Values */
	private static final String PARAM_TOKEN = "MyToken";		// User's API Token
	private static final String PARAM_PROJECTID = "Project_ID";	// Project's ID
	private static final String PARAM_EMAIL = "MyEMAIL";		// User's Email Address
	private static final String PARAM_PASSWORD = "MyPassword";	// User's Password
	private static final String PARAM_FULLNAME = "MyFullName";	// User's FullName
	private static final String PARAM_TIMEZONE = "MyTimeZone";	// User's Timezone
	private static final String PARAM_NAME = "MyName";			// Project Name || Label's Name
	private static final String PARAM_COLOR = "MyColor";		// Project Color
	private static final String PARAM_INDENT = "MyIndent";		// Project Indent
	private static final String PARAM_ORDER = "MyOrder";		// Project Order
	private static final String PARAM_OLDNAME = "MyOldName";	// Label's Old Name
	private static final String PARAM_NEWNAME = "MyNewName";	// Label's New Name
	private static final String PARAM_IDS = "MyIDS";			// JSON List of Item ID's (tasks)
	private static final String PARAM_CONTENT = "MyContent";	// Text of the Item (task)
	private static final String PARAM_DATESTRING = "MyDateString"; // Date String of Item (task)
	private static final String PARAM_PRIORITY = "MyPriority";	// Priority of Item (task)
	private static final String PARAM_ITEMID = "MyItemID";		// ID of an Item (task)
	private static final String PARAM_QUERIES = "MyQuery";		// Query
	
	/* Todoist URLs */
	private static final String TODOIST = "http://todoist.com/API/";
	private static final String TODOIST_SSL = "https://todoist.com/API/";
	
	/* Todoist User API */
	private static final String LOGIN = TODOIST_SSL + "login?email=" + PARAM_EMAIL + "&password=" + PARAM_PASSWORD; // .Replace(PARAM_EMAIL,email).Replace(PARAM_PASSWORD,password);
	private static final String GET_TIMEZONES = TODOIST + "getTimezones";
	private static final String REGISTER = TODOIST_SSL + "register?email=" + PARAM_EMAIL + "&full_name=" + PARAM_FULLNAME + "&password=" + PARAM_PASSWORD + "&timezone=" + PARAM_TIMEZONE; // .Replace(PARAM_EMAIL,email).Replace(PARAM_FULLNAME,full_name).Replace(PARAM_PASSWORD,password).Replace(PARAM_TIMEZONE,timezone);
	private static final String UPDATE_USER = TODOIST_SSL + "updateUser?token=" + PARAM_TOKEN; // .Replace(PARAM_TOKEN,token);
		
	/* Todoist Projects API */
	private static final String GET_PROJECTS = TODOIST + "getProjects?token=" + PARAM_TOKEN; // .Replace(PARAM_TOKEN,token);
	private static final String GET_PROJECT = TODOIST + "getProject?token=" + PARAM_TOKEN + "&project_id=" + PARAM_PROJECTID; // .Replace(PARAM_TOKEN,token).Replace(PARAM_PROJECTID,project_id);
	private static final String ADD_PROJECT = TODOIST + "addProject?name=" + PARAM_NAME + "&token=" + PARAM_TOKEN; // .Replace(PARAM_NAME,name).Replace(PARAM_TOKEN,token);
	private static final String UPDATE_PROJECT = TODOIST + "updateProject?project_id=" + PARAM_PROJECTID + "&token=" + PARAM_TOKEN; // .Replace(PARAM_PROJECTID,project_id).Replace(PARAM_TOKEN,token);
	private static final String DELETE_PROJECT = TODOIST + "deleteProject?project_id=" + PARAM_PROJECTID + "&token=" + PARAM_TOKEN; // .Replace(PARAM_PROJECTID,project_id).Replace(PARAM_TOKEN,token);
	
	/* Todoist Labels API */
	private static final String GET_LABELS = TODOIST + "getLabels?project_id=" + PARAM_PROJECTID + "&token=" + PARAM_TOKEN; // .Replace(PARAM_PROJECTID, project_id).Replace(PARAM_TOKEN,token);
	private static final String UPDATE_LABEL = TODOIST + "updateLabel?old_name=" + PARAM_OLDNAME + "&new_name=" + PARAM_NEWNAME + "&token=" + PARAM_TOKEN; // .Replace(PARAM_OLDNAME,old_name).Replace(PARAM_NEWNAME,new_name).Replace(PARAM_TOKEN,token);
	private static final String DELETE_LABEL = TODOIST + "deleteLabel?name=" + PARAM_NAME + "&token=" + PARAM_TOKEN; // .Replace(PARAM_NAME, name).Replace(PARAM_TOKEN,token);
	
	/* Todoist Items API */
	private static final String GET_UNCOMPLETED_ITEMS = TODOIST + "getUncompletedItems?project_id=" + PARAM_PROJECTID + "&token=" + PARAM_TOKEN; //.Replace(PARAM_PROJECTID,project_id).Replace(PARAM_TOKEN,token);
	private static final String GET_COMPLETED_ITEMS = TODOIST + "getCompletedItems?project_id=" + PARAM_PROJECTID + "&token=" + PARAM_TOKEN; //.Replace(PARAM_PROJECTID,project_id).Replace(PARAM_TOKEN,token);
	private static final String GET_ITEMS_BY_ID = TODOIST + "getItemsById?ids=" + PARAM_IDS + "&token=" + PARAM_TOKEN; //.Replace(PARAM_IDS,ids).Replace(PARAM_TOKEN,token);
	private static final String ADD_ITEM = TODOIST + "addItem?project_id=" + PARAM_PROJECTID + "&content=" + PARAM_CONTENT + "&token=" + PARAM_TOKEN;
	private static final String UPDATE_ITEM = TODOIST + "updateItem?id=" + PARAM_ITEMID + "&token=" + PARAM_TOKEN; // .Replace(PARAM_ITEMID,id).Replace(PARAM_TOKEN,token);
	private static final String UPDATE_ORDERS = TODOIST + "updateOrders?project_id=" + PARAM_PROJECTID + "&item_id_list=" + PARAM_IDS + "&token=" + PARAM_TOKEN; // .Replace(PARAM_PROJECTID,project_id).Replace(PARAM_IDS,item_id_list).Replace(PARAM_TOKEN,token);
	private static final String UPDATE_RECURRING_DATE = TODOIST + "UpdateRecurringDate?ids=" + PARAM_IDS + "&token=" + PARAM_TOKEN; // .Replace(PARAM_IDS,ids).Replace(PARAM_TOKEN,token);
	private static final String DELETE_ITEMS = TODOIST + "DeleteItems?ids=" + PARAM_IDS + "&token=" + PARAM_TOKEN; // .Replace(PARAM_IDS,ids).Replace(PARAM_TOKEN,token);
	private static final String COMPLETE_ITEMS = TODOIST + "CompleteItems?ids=" + PARAM_IDS + "&token=" + PARAM_TOKEN; // .Replace(PARAM_IDS,ids).Replace(PARAM_TOKEN,token);
	private static final String UNCOMPLETE_ITEMS = TODOIST + "UncompleteItems?ids=" + PARAM_IDS + "&token=" + PARAM_TOKEN; // .Replace(PARAM_IDS,ids).Replace(PARAM_TOKEN,token);
	
	/* Date Query & Search API */
	private static final String QUERY = TODOIST + "query?queries=" + PARAM_QUERIES + "&token=" + PARAM_TOKEN; // .Replace(PARAM_QUERIES,queries).Replace(PARAM_TOKEN,token);
	
	/* Todoist Optional API Parameters 
	 * Check /docs/ for Details on OPTIONAL parameters 
	 */
	private static final String OPTIONAL_EMAIL = "&email=" + PARAM_EMAIL; // .Replace(PARAM_EMAIL,email);
	private static final String OPTIONAL_FULLNAME = "&full_name=" + PARAM_FULLNAME; // .Replace(PARAM_FULLNAME, full_name);
	private static final String OPTIONAL_PASSWORD = "&password=" + PARAM_PASSWORD; // .Replace(PARAM_PASSWORD, password);
	private static final String OPTIONAL_TIMEZONE = "&timezone=" + PARAM_TIMEZONE; // .Replace(PARAM_TIMEZONE, timezone);
	private static final String OPTIONAL_NAME = "&name=" + PARAM_NAME;	// .Replace(PARAM_NAME,name);
	private static final String OPTIONAL_COLOR = "&color=" + PARAM_COLOR;	// .Replace(PARAM_COLOR,color);
	private static final String OPTIONAL_INDENT = "&indent=" + PARAM_INDENT;	// .Replace(PARAM_INDENT,indent);
	private static final String OPTIONAL_ORDER = "&order=" + PARAM_ORDER;	// .Replace(PARAM_ORDER,order);
	private static final String OPTIONAL_DATESTRING = "&date_string=" + PARAM_DATESTRING; // .Replace(PARAM_DATESTRING, date_string);
	private static final String OPTIONAL_PRIORITY = "&priority=" + PARAM_PRIORITY;	// .Replace(PARAM_PRIORITY, priority);
	private static final String OPTIONAL_CONTENT = "&content=" + PARAM_CONTENT; // .Replace(PARAM_CONTENT, content);
	
	/* Todoist JSON elements */
	private static final String JSON_USERID = "user_id";
	private static final String JSON_NAME = "name";
	private static final String JSON_COLOR = "color";
	private static final String JSON_COLLAPSED = "collapsed";
	private static final String JSON_ITEMORDER = "item_order";
	private static final String JSON_INDENT = "indent";
	private static final String JSON_CACHECOUNT = "cache_count";
	private static final String JSON_ID = "id";
	private static final String JSON_EMAIL = "email";
	private static final String JSON_FULLNAME = "full_name";
	private static final String JSON_APITOKEN = "api_token";
	private static final String JSON_STARTPAGE = "start_page";
	private static final String JSON_TIMEZONE = "timezone";
	private static final String JSON_TZOFFSET = "tz_offset";
	private static final String JSON_TIMEFORMAT = "time_format";
	private static final String JSON_DATEFORMAT = "date_format";
	private static final String JSON_SORTORDER = "sort_order";
	private static final String JSON_TWITTER = "twitter";
	private static final String JSON_JABBER = "jabber";
	private static final String JSON_MSN = "msn";
	private static final String JSON_MOBILENUMBER = "mobile_number";
	private static final String JSON_MOBILEHOST = "mobile_host";
	
}
