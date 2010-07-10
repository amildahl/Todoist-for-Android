/*    
	This file is part of Todoist for Android™.

    Todoist for Android™ is free software: you can redistribute it and/or 
    modify it under the terms of the GNU General Public License as published 
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Todoist for Android™ is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Todoist for Android™.  If not, see <http://www.gnu.org/licenses/>.
    
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

package com.android.applications.todoist.handlers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.applications.todoist.Constants;
import com.android.applications.todoist.containers.Project;
import com.android.applications.todoist.containers.Projects;
import com.android.applications.todoist.containers.Tasks;
import com.android.applications.todoist.containers.User;

public class TodoistAPIHandler {

	private String token;
	
	public TodoistAPIHandler()
	{
	}
	
	public TodoistAPIHandler( String _token)
	{
		token = _token;
	}
	
	public void setToken(String _token) {
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
			
			return new User(obj.getString(Constants.JSON_EMAIL), obj.getString(Constants.JSON_FULLNAME), obj.getString(Constants.JSON_ID), obj.getString(Constants.JSON_APITOKEN), 
					obj.getString(Constants.JSON_STARTPAGE), obj.getString(Constants.JSON_TIMEZONE), obj.getString(Constants.JSON_TZOFFSET), obj.getString(Constants.JSON_TIMEFORMAT), 
					obj.getString(Constants.JSON_DATEFORMAT), obj.getString(Constants.JSON_SORTORDER), obj.getString(Constants.JSON_TWITTER), obj.getString(Constants.JSON_JABBER), 
					obj.getString(Constants.JSON_MSN), obj.getString(Constants.JSON_MOBILENUMBER), obj.getString(Constants.JSON_MOBILEHOST));
			
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
				project = new Project(obj.getString(Constants.JSON_USERID), obj.getString(Constants.JSON_NAME), obj.getString(Constants.JSON_COLOR), obj.getString(Constants.JSON_COLLAPSED), 
						obj.getString(Constants.JSON_ITEMORDER), obj.getString(Constants.JSON_CACHECOUNT), obj.getString(Constants.JSON_INDENT), obj.getString(Constants.JSON_ID));
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
		return new Tasks(this.navigate(GET_UNCOMPLETED_ITEMS.replace(PARAM_PROJECTID,project_id).replace(PARAM_TOKEN,token)));
	}
	
	public Tasks query(String query)
	{
		return new Tasks(this.navigate(QUERY.replace(PARAM_QUERIES, query).replace(PARAM_TOKEN, token)));
	}
	
	public String parseQuery(String query)
	{
		//TODO: Parse out the query
		return query;
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
	

	
}
