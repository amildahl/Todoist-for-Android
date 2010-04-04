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
	private Projects parseProjects(String data)
	{
		JSONObject obj;
		Projects projects = new Projects();
		Project project;
		try 
		{
			JSONArray jArray = new JSONArray(data);
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
	private Tasks parseTasks(String data)
	{
		return new Tasks();
	}
	
	private static final String PARAM_TOKEN = "MyToken";
	private static final String PARAM_PROJECTID = "Project_ID";
	
	private static final String GET_PROJECTS = "http://todoist.com/API/getProjects?token=" + PARAM_TOKEN; // .Replace(PARAM_TOKEN,token);
	private static final String GET_UNCOMPLETEDITEMS = "/API/getUncompletedItems?project_id=" + PARAM_PROJECTID + "&token=" + PARAM_TOKEN; //.Replace(PARAM_PROJECTID,project_id).Replace(PARAM_TOKEN,token);
	
	private static final String JSON_USERID = "user_id";
	private static final String JSON_NAME = "name";
	private static final String JSON_COLOR = "color";
	private static final String JSON_COLLAPSED = "collapsed";
	private static final String JSON_ITEMORDER = "item_order";
	private static final String JSON_INDENT = "indent";
	private static final String JSON_CACHECOUNT = "cache_count";
	private static final String JSON_ID = "id";
	
}
