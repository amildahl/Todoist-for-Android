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
	
	/* User API
	 * Use HTTPS:\\
	 * Parameters: email & password
	 * Returns: JSON object w/ User Info
	 * Error: "LOGIN_ERROR"
	 */
	public User login(String email, String password)
	{
		String jsonData = "";
		
		try
		{
			WebRequest browser = new WebRequest(LOGIN.replace(PARAM_EMAIL, email).replace(PARAM_PASSWORD, password));
			jsonData = ((String)browser.getContent());
		}
		catch (Exception e)
		{
			//output (e.getMessage());	
		}
		
		return this.parseLogin(jsonData);
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

	// TODO: Output Exceptions
	public Projects getProjects()
	{		
		String data = "";
		
		try
		{
			WebRequest browser = new WebRequest(GET_PROJECTS.replace(PARAM_TOKEN,token));
			data = ((String)browser.getContent());
		}
		catch (Exception e)
		{
			//output (e.getMessage());	
		}
		
		return this.parseProjects(data);
	}
	
	// TODO: EVERYTHING
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
		try
		{
			WebRequest browser = new WebRequest(GET_UNCOMPLETEDITEMS.replace(PARAM_PROJECTID,project_id).replace(PARAM_TOKEN,token));
			return this.parseTasks(((String)browser.getContent()));
		}
		catch (Exception e)
		{
			//output (e.getMessage());
		}
		
		return new Tasks();
	}
	
	// TODO: Everything...
	private Tasks parseTasks(String jsonData)
	{
		return new Tasks();
	}
	
	private static final String PARAM_TOKEN = "MyToken";
	private static final String PARAM_PROJECTID = "Project_ID";
	private static final String PARAM_EMAIL = "MyEMAIL";
	private static final String PARAM_PASSWORD = "MyPassword";
	
	private static final String TODOIST = "http://todoist.com/API/";
	private static final String TODOIST_SSL = "https://todoist.com/API/";
	private static final String LOGIN = TODOIST_SSL + "login?email=" + PARAM_EMAIL + "&password=" + PARAM_PASSWORD; // .Replace(PARAM_EMAIL,email).Replace(PARAM_PASSWORD,password);
	private static final String GET_PROJECTS = TODOIST + "getProjects?token=" + PARAM_TOKEN; // .Replace(PARAM_TOKEN,token);
	private static final String GET_UNCOMPLETEDITEMS = TODOIST + "getUncompletedItems?project_id=" + PARAM_PROJECTID + "&token=" + PARAM_TOKEN; //.Replace(PARAM_PROJECTID,project_id).Replace(PARAM_TOKEN,token);
	
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
