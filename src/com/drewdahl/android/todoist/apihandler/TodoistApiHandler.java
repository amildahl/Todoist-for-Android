/**
 * Copyright (C) 2008 by Alex Brandt <alunduil@alunduil.com>
 * 
 * This program is free software; you can redistribute it and#or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package com.drewdahl.android.todoist.apihandler;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.drewdahl.android.todoist.Constants;
import com.drewdahl.android.todoist.apihandler.TodoistApiHandlerException;
import com.drewdahl.android.todoist.models.Item;
import com.drewdahl.android.todoist.models.ItemException;
import com.drewdahl.android.todoist.models.Project;
import com.drewdahl.android.todoist.models.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;

/**
 * @note This API needs to be finalized before the 1.0 release.
 * 
 * This should be implemented for the froyo release following the patterns
 * outlined in: 
 * http://feedproxy.google.com/~r/blogspot/hsDu/~3/9WEwRp2NWlY/how-to-have-your-cupcake-and-eat-it-too.html
 * 
 * TODO froyo stuffs:
 * 
 * static Bitmap downloadBitmap(String url) {
 *   final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
 *   final HttpGet getRequest = new HttpGet(url);
 *   
 *   try {
 *     HttpResponse response = client.execute(getRequest);
 *     final int statusCode = response.getStatusLine().getStatusCode();
 *     if (statusCode != HttpStatus.SC_OK) {
 *       Log.w("ImageDownloader", "Error " + statusCode + " while retrieving bitmap from " + url);
 *       return null;
 *     }
 *     
 *     final HttpEntity entity = response.getEntity();
 *     if (entity != null) {
 *       InputStream inputStream = null;
 *       try {
 *         inputStream = entity.getContent();
 *         final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
 *         return bitmap;
 *       } finally {
 *         if (inputStream != null) {
 *           inputStream.close();
 *         }
 *         entity.consumeContent();
 *       }
 *     }
 *   } catch (Exception e) {
 *     // Could provide a more explicit error message for IOException or IllegalStateException
 *     getRequest.abort();
 *     Log.w("ImageDownloader", "Error while retrieving bitmap from " + url, e.toString());
 *   } finally {
 *     if (client != null) {
 *       client.close();
 *     }
 *   }
 *   return null;
 * }
 * 
 */

public class TodoistApiHandler {
	private User user = null;
	private TodoistApiHandler() {}
	
	private static class InstanceHolder {
		private static final TodoistApiHandler INSTANCE = new TodoistApiHandler();
	}

	/**
	 * Get the single instance of the ApiHandler.
	 * @return TodoistApiHandler single instance.
	 */
	public static TodoistApiHandler getInstance()
	{
		return InstanceHolder.INSTANCE;
	}

	/**
	 * Get the instance of the ApiHandler and associate it with a different
	 * API token and user.
	 * 
	 * @deprecated 
	 * 
	 * @param token The API token of the user to associate with.
	 * @return TodoistApiHandler single instance.
	 */
	@Deprecated
	public static TodoistApiHandler getInstance(String token)
	{
		TodoistApiHandler tmp = getInstance();
		/**
		 * TODO Find the user with the ApiToken specified.
		 */
		return tmp;
	}
	
	/**
	 * Get the authentication token being used by this instance.
	 * 
	 * @deprecated
	 * @see getUser
	 * 
	 * @return String Authentication token.
	 */
	@Deprecated
	public String getToken()
	{
		return user.getApiToken();
	}
	
	private final HttpClient client = new DefaultHttpClient();
	private final HttpGet getRequest = new HttpGet();
	//private final HttpPost postRequest = new HttpPost();
	
	/**
	 * Call a Todoist RESTful function.
	 * 
	 * @param Uri The resource locater.
	 * @return String The raw JSON response string.
	 */
	protected String call(String Uri)
	{
		/**
		 * TODO 2048 Character limit on Uri good enough?
		 * TODO Think about moving to calling a URI with a parameter map for post and get.
		 * TODO Read pg. 292 in Pro Android 2 for more information.
		 * TODO Make this thread safe pg. 299 in Pro Android 2.
		 */
		BufferedReader in = null;
		
		String ret = "";
		
		try {
			getRequest.setURI(new URI(Uri));
			HttpResponse response = client.execute(getRequest);
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			in.close();
			
			String page = sb.toString();
			ret = page;
		} catch (Exception e) {
			/**
			 * TODO Raise support issue or throw up?
			 */
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					/**
					 * TODO Raise a support issue or throw up?
					 */
				}
			}
		}
		Log.d(this.toString(), "Web Result: " + ret);
		return ret;
	}
	
	/**
	 * Login as the user and return that user.
	 * 
	 * /API/login should be HTTPS
	 * Login user into Todoist to get a token. Required to do any communication.
	 * 
	 * Required parameters:
	 *   email: User's email
	 *   password: User's password
	 * 
	 * Successful return:
	 *   HTTP 200 OK with a JSON object with user info:
	 *     {"email": "...", "token": ..., ...}
	 * 
	 * Error returns:
	 *   "LOGIN_ERROR"
	 *
	 * @param email The email address of the user.
	 * @param password The password for the user.
	 * @return The user that was successfully logged in.
	 */
	public User login(String email, String password) throws TodoistApiHandlerException
	{
		try {
			user = new User(new JSONObject(call(LOGIN.replace(PARAM_EMAIL, email).replace(PARAM_PASSWORD, password))));
		} catch (JSONException e) {
			/**
			 * TODO Better handling of failed logins.
			 */
			throw new TodoistApiHandlerException("Login failure!");
		}
		return user;
	}
	
	/**
	 * Get the list of timezones that Todoist supports.
	 * @return An array of Strings (each is a timezone string).
	 * 
	 * /API/getTimezones
	 * Returns the timezones Todoist supports.
	 * 
	 * Successful return:
	 *   HTTP 200 OK with a JSON object with timezone names:
	 *     ["US/Alaska", "US/Arizona", "US/Central", "US/Eastern", ...]
	 */
	public ArrayList<String> getTimezones()
	{
		ArrayList<String> ret = new ArrayList<String>();
		try {
			JSONArray jArray = new JSONArray(call(GET_TIMEZONES));
			for (int i = 0; i < jArray.length(); ++i) {
				ret.add(jArray.getJSONObject(i).getString(Constants.JSON_TIMEZONE));
			}
		} catch (JSONException e) {
			/**
			 * TODO Error handling.
			 */
		}
		return ret;
	}
	
	/**
	 * 
	 * @param email
	 * @param full_name
	 * @param password
	 * @param timezone
	 * @return
	 * 
	 * TODO Proper error handling.
	 */
	public User register(String email, String full_name, String password, String timezone)
	{
		try {
			user = new User(new JSONObject(call(REGISTER.replace(PARAM_EMAIL, email).replace(PARAM_FULLNAME, full_name).replace(PARAM_PASSWORD, password).replace(PARAM_TIMEZONE, timezone))));
		} catch (JSONException e) {
			/**
			 * TODO Error handling.
			 */
		}
		return user;
	}
	
	public User updateUser(Map.Entry<String, String>...entries)
	{
		String Uri = UPDATE_USER.replace(PARAM_TOKEN, user.getApiToken());
		for (Map.Entry<String, String> n : entries) {
			if (n.getKey().toLowerCase() == "email") {
				Uri += OPTIONAL_EMAIL.replace(PARAM_EMAIL, n.getValue());
			}
			else if (n.getKey().toLowerCase() == "full_name") {
				Uri += OPTIONAL_FULLNAME.replace(PARAM_FULLNAME, n.getValue());
			}
			else if (n.getKey().toLowerCase() == "password") {
				Uri += OPTIONAL_PASSWORD.replace(PARAM_PASSWORD, n.getValue());
			}
			else if (n.getKey().toLowerCase() == "timezone") {
				Uri += OPTIONAL_TIMEZONE.replace(PARAM_TIMEZONE, n.getValue());
			}
		}
		try {
			user = new User(new JSONObject(call(Uri)));
		} catch (JSONException e) {
			/**
			 * TODO Error handling.
			 */
		}
		return user;
	}

	public ArrayList<Project> getProjects()
	{
		ArrayList<Project> ret = new ArrayList<Project>();
		try {
			JSONArray jArray = new JSONArray(call(GET_PROJECTS.replace(PARAM_TOKEN, user.getApiToken()))); 
			for(int i = 0; i < jArray.length(); ++i) {
				ret.add(new Project(jArray.getJSONObject(i), user));
			}
		} catch (JSONException e) {
			/**
			 * TODO Error Handling.
			 */
		}
		return ret;
	}
	
	public Project getProject(Integer project_id)
	{
		Project ret = null;
		try {
			ret = new Project(new JSONObject(call(GET_PROJECT.replace(PARAM_TOKEN, user.getApiToken()).replace(PARAM_PROJECTID, project_id.toString()))), user);
		} catch (JSONException e) {
			/**
			 * TODO Error handling.
			 */
		}
		return ret;
	}

	public Project addProject(String name)
	{
		Project ret = null;
		try {
			ret = new Project(new JSONObject(call(ADD_PROJECT.replace(PARAM_TOKEN, user.getApiToken()).replace(PARAM_NAME, name))), user);
		} catch (JSONException e) {
			/**
			 * TODO Error Handling.
			 */
		}
		return ret;
	}
	
	public Project updateProject(Map.Entry<String, String>...entries)
	{
		String Uri = UPDATE_PROJECT.replace(PARAM_TOKEN, user.getApiToken());
		boolean id_found = false;
		
		for (Map.Entry<String, String> n : entries) {
			if (n.getKey().toLowerCase() == "name") {
				Uri += OPTIONAL_NAME.replace(PARAM_NAME, n.getValue());
			}
			else if (n.getKey().toLowerCase() == "color") {
				Uri += OPTIONAL_COLOR.replace(PARAM_COLOR, n.getValue());
			}
			else if (n.getKey().toLowerCase() == "indent") {
				Uri += OPTIONAL_INDENT.replace(PARAM_INDENT, n.getValue());
			}
			else if (n.getKey().toLowerCase() == "projectid") { // REQUIRED!
				Uri += PARAM_PROJECTID.replace(PARAM_PROJECTID, n.getValue());
				id_found = true;
			}
		}
		
		if (!id_found) {
			throw new IllegalArgumentException();
		}
		
		Project ret = null;
		try {
			ret = new Project(new JSONObject(call(Uri)), user);
		} catch (JSONException e) {
			/**
			 * TODO ErrorHandling
			 */
		}
		return ret;
	}
	
	public void deleteProject(Integer project_id)
	{
		call(DELETE_PROJECT.replace(PARAM_TOKEN, user.getApiToken()).replace(PARAM_PROJECTID, project_id.toString()));
	}
	
	/**
	 * @param project_id
	 * @return
	 */
	public ArrayList<String> getLabels(Integer project_id)
	{
		ArrayList<String> ret = new ArrayList<String>();
		try {
			JSONArray jArray = new JSONArray(call(GET_LABELS.replace(PARAM_TOKEN, user.getApiToken()).replace(PARAM_PROJECTID, project_id.toString()))); 
			for (int i = 0; i < jArray.length(); ++i) {
				ret.add(jArray.getJSONObject(i).getString(Constants.JSON_LABELID));
			}
		} catch (JSONException e) {
			/**
			 * TODO ErrorHandling
			 */
		}
		return ret;
	}
	
	public void updateLabel(String old_name, String new_name) {
		call(UPDATE_LABEL.replace(PARAM_TOKEN, user.getApiToken()).replace(PARAM_OLDNAME, old_name).replace(PARAM_NEWNAME, new_name));
	}
	
	public void deleteLavel(String name) {
		call(DELETE_LABEL.replace(PARAM_TOKEN, user.getApiToken()).replace(PARAM_NAME, name));
	}
	
	public ArrayList<Item> getUncompletedItems(Integer project_id)
	{
		ArrayList<Item> ret = new ArrayList<Item>();
		try {
			JSONArray jArray = new JSONArray(call(GET_UNCOMPLETED_ITEMS.replace(PARAM_TOKEN, user.getApiToken()).replace(PARAM_PROJECTID, project_id.toString()))); 
			for(int i = 0; i < jArray.length(); ++i) {
				ret.add(new Item(jArray.getJSONObject(i), user));
			}
		} catch (JSONException e) {
			/**
			 * TODO Error Handling.
			 */
		} catch (ItemException e) {
			//Log("ApiHandler", "User and item mismatch!");
		}
		return ret;
	}
	
	public ArrayList<Item> getCompletedItems(Integer project_id)
	{
		ArrayList<Item> ret = new ArrayList<Item>();
		try {
			JSONArray jArray = new JSONArray(call(GET_COMPLETED_ITEMS.replace(PARAM_TOKEN, user.getApiToken()).replace(PARAM_PROJECTID, project_id.toString()))); 
			for(int i = 0; i < jArray.length(); ++i) {
				ret.add(new Item(jArray.getJSONObject(i), user));
			}
		} catch (JSONException e) {
			/**
			 * Error Handling.
			 */
		} catch (ItemException e) {
			
		}
		return ret;
	}
	
	public ArrayList<Item> getItemsById(Integer...ids)
	{
		boolean first = true;
		String idstring = "[";
		for (Integer n : ids) {
			if (first) {
				idstring += n.toString();
				first = false;
			}
			idstring += "," + n.toString(); 
		}
		idstring += "]";
		
		String response = call(GET_ITEMS_BY_ID.replace(PARAM_TOKEN, user.getApiToken()).replace(PARAM_IDS, idstring));
		ArrayList<Item> ret = new ArrayList<Item>();
		try
		{
			JSONArray jArray = new JSONArray(response); 
			for(int i = 0; i < jArray.length(); ++i) {
				ret.add(new Item(jArray.getJSONObject(i), user));
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		catch (ItemException e)
		{
			
		}
		return ret;
	}
	
	public Item addItem(Integer project_id, String content, Map.Entry<String, String>...entries)
	{
		String Uri = ADD_ITEM.replace(PARAM_TOKEN, user.getApiToken()).replace(PARAM_PROJECTID, project_id.toString()).replace(PARAM_CONTENT, content);

		for (Map.Entry<String, String> n : entries) {
			if (n.getKey().toLowerCase() == "date_string") {
				Uri += OPTIONAL_DATESTRING.replace(PARAM_DATESTRING, n.getValue());
			}
			else if (n.getKey().toLowerCase() == "priority") {
				Uri += OPTIONAL_PRIORITY.replace(PARAM_PRIORITY, n.getValue());
			}
		}
		
		Item ret = null;
		try
		{
			ret = new Item(new JSONObject(call(Uri)), user);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		catch (ItemException e)
		{
			
		}
		return ret;
	}
	
	public Item updateItem(Integer item_id, Map.Entry<String, String>...entries)
	{
		String Uri = UPDATE_ITEM.replace(PARAM_TOKEN, user.getApiToken()).replace(PARAM_ITEMID, item_id.toString());
		
		for (Map.Entry<String, String> n : entries) {
			if (n.getKey().toLowerCase() == "content") {
				Uri += OPTIONAL_CONTENT.replace(PARAM_CONTENT, n.getValue());
			}
			else if (n.getKey().toLowerCase() == "date_string") {
				Uri += OPTIONAL_DATESTRING.replace(PARAM_DATESTRING, n.getValue());
			}
			else if (n.getKey().toLowerCase() == "priority") {
				Uri += OPTIONAL_PRIORITY.replace(PARAM_PRIORITY, n.getValue());
			}
			else if (n.getKey().toLowerCase() == "indent") {
				Uri += OPTIONAL_INDENT.replace(PARAM_INDENT, n.getValue());
			}
			else if (n.getKey().toLowerCase() == "item_order") {
				Uri += OPTIONAL_ORDER.replace(PARAM_ORDER, n.getValue());
			}
		}
		
		Item ret = null;
		try
		{
			ret = new Item(new JSONObject(call(Uri)), user);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		catch (ItemException e)
		{
			
		}
		return ret;
	}
	
	public void updateOrders(Integer project_id, Integer...item_ids)
	{
		boolean first = true;
		String idstring = "[";
		for (Integer n : item_ids) {
			if (first) {
				idstring += n.toString();
				first = false;
			}
			else { // TODO Crap, where else did I forget this?
				idstring += "," + n.toString();
			}
		}
		idstring += "]";
		
		call(UPDATE_ORDERS.replace(PARAM_TOKEN, user.getApiToken()).replace(PARAM_PROJECTID, project_id.toString()).replace(PARAM_IDS, idstring));
	}
	
	/**
	 * @param item_ids
	 * @return
	 */
	public Item[] updateRecurringDate(Integer...item_ids)
	{
		boolean first = true;
		String idstring = "[";
		for (Integer n : item_ids) {
			if (first) {
				idstring += n.toString();
				first = false;
			}
			else {
				idstring += "," + n.toString();
			}
		}
		idstring += "]";
		
		String response = call(UPDATE_RECURRING_DATE.replace(PARAM_TOKEN, user.getApiToken()).replace(PARAM_IDS, idstring));
		ArrayList<Item> ret = new ArrayList<Item>();
		try
		{
			JSONArray jArray = new JSONArray(response); 
			for(int i = 0; i < jArray.length(); ++i) {
				ret.add(new Item(jArray.getJSONObject(i), user));
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		catch (ItemException e)
		{
			
		}
		return (Item[])ret.toArray();
	}
	
	public void deleteItems(Integer...item_ids)
	{
		boolean first = true;
		String idstring = "[";
		for (Integer n : item_ids) {
			if (first) {
				idstring += n.toString();
				first = false;
			}
			else {
				idstring += "," + n.toString();
			}
		}
		idstring += "]";
		
		call(DELETE_ITEMS.replace(PARAM_TOKEN, user.getApiToken()).replace(PARAM_IDS, idstring));
	}
	
	public void completeItems(boolean in_history, Integer...item_ids)
	{
		/**
		 * TODO Make this a private method, listToString()?
		 */
		boolean first = true;
		String idstring = "[";
		for (Integer n : item_ids) {
			if (first) {
				idstring += n.toString();
				first = false;
			}
			else {
				idstring += "," + n.toString();
			}
		}
		idstring += "]";
		
		String Uri = COMPLETE_ITEMS.replace(PARAM_TOKEN, user.getApiToken()).replace(PARAM_IDS, idstring);
		if (!in_history) Uri += OPTIONAL_INHISTORY.replace(PARAM_INHISTORY, "0");
		call(Uri);
	}
	
	public void uncompleteItems(Integer...item_ids)
	{
		boolean first = true;
		String idstring = "[";
		for (Integer n : item_ids) {
			if (first) {
				idstring += n.toString();
				first = false;
			}
			else {
				idstring += "," + n.toString();
			}
		}
		idstring += "]";
		
		call(UNCOMPLETE_ITEMS.replace(PARAM_TOKEN, user.getApiToken()).replace(PARAM_IDS, idstring));
	}
	
	/*
	public ArrayList<Item> query(String...queries)
	{
		// TODO Double check API.
		boolean first = true;
		String idstring = "[";
		for (String n : queries) {
			if (first) {
				idstring += n.toString();
				first = false;
			} else {
				idstring += "," + n.toString();
			}
		}
		idstring += "]";
		
		call(QUERY.replace(PARAM_TOKEN, user.getApiToken()).replace(PARAM_QUERIES, idstring));
	}
	*/
	
	public User getUser()
	{
		return user;
	}
	
	/**
	 * TODO Implement the query call.
	 */
	
	/* Todoist URLs */
	private static final String TODOIST = "http://todoist.com/API/";
	private static final String TODOIST_SSL = "https://todoist.com/API/";
	
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
	private static final String PARAM_INHISTORY = "InHistory";  // In History
	
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
	private static final String OPTIONAL_INHISTORY = "&in_history=" + PARAM_INHISTORY; // .Replace(PARAM_INHISTORY, inhistory);
}
