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

import com.drewdahl.android.todoist.apihandler.TodoistApiHandlerConstants.JSON;
import com.drewdahl.android.todoist.apihandler.TodoistApiHandlerConstants.OPTIONAL_PARAMETERS;
import com.drewdahl.android.todoist.apihandler.TodoistApiHandlerConstants.PARAMETERS;
import com.drewdahl.android.todoist.models.Item;
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
	public static TodoistApiHandler getInstance() {
		return InstanceHolder.INSTANCE;
	}
	
	public User getUser()
	{
		return user;
	}
	
	public void setUser(User user)
	{
		this.user = user;
	}

	/*
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
	
	private final HttpClient client = new DefaultHttpClient();
	private final HttpGet getRequest = new HttpGet();
	//private final HttpPost postRequest = new HttpPost();
	
	/**
	 * Call a Todoist RESTful function.
	 * 
	 * @param Uri The resource locater.
	 * @return String The raw JSON response string.
	 */
	protected String call(String Uri) {
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
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()), 8);
			
			/**
			 * TODO Return status code to requester?
			 */
			
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
		Log.d(this.toString(), "Web Result: " + ret.toString());
		return ret;
	}
	
	/**
	 * <pre>
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
	 * </pre>
	 *
	 * @param email The email address of the user.
	 * @param password The password for the user.
	 * @return The user that was successfully logged in.
	 */
	public User login(String email, String password) {
		String query = TodoistApiHandlerConstants.LOGIN
			.replace(PARAMETERS.EMAIL, email)
			.replace(PARAMETERS.PASSWORD, password);
		String response = call(query); 
		try {
			user = new User(new JSONObject(response));
		} catch (JSONException e) {
			Log.e(this.toString(), "Received the following response from Todoist: " + response);
			/**
			 * TODO Error Handling.
			 */
			throw new RuntimeException("Failed to create the user object!");
		}
		return user;
	}
	
	/**
	 * <pre> 
	 * /API/getTimezones
	 * Returns the timezones Todoist supports.
	 * 
	 * Successful return:
	 *   HTTP 200 OK with a JSON object with timezone names:
	 *     ["US/Alaska", "US/Arizona", "US/Central", "US/Eastern", ...]
	 * </pre>
	 * 
	 * @return An array of Strings (each is a timezone string).
	 */
	public ArrayList<String> getTimezones() {
		String query = TodoistApiHandlerConstants.GET_TIMEZONES;
		String response = call(query); 
		ArrayList<String> ret = new ArrayList<String>();
		try {
			JSONArray jArray = new JSONArray(response);
			for (int i = 0; i < jArray.length(); ++i) {
				ret.add(jArray.getJSONObject(i).getString(JSON.TIMEZONE));
			}
		} catch (JSONException e) {
			Log.e(this.toString(), "Received the following response from Todoist: " + response);
			/**
			 * TODO Error handling.
			 */
		}
		return ret;
	}
	
	/**
	 * <pre>
	 *  /API/register should be HTTPS
	 *  Required parameters:
	 *    email: User's email
	 *    full_name: User's full name
	 *    password: User's password, should be at least 5 characters long
	 *    timezone: User's timezone (check /API/getTimezones)
	 *    
	 *    Successful return:
	 *      HTTP 200 OK with a JSON object with user info:
	 *        {"email": "...", "token": ..., ...}
	 *    
	 *    Error returns:
	 *      "ALREADY_REGISTRED"
	 *      "TOO_SHORT_PASSWORD"
	 *      "INVALID_EMAIL"
	 *      "INVALID_TIMEZONE"
	 *      "INVALID_FULL_NAME"
	 *      "UNKNOWN_ERROR"
	 * </pre>
	 *      
	 * @param email User's email
	 * @param full_name User's full name
	 * @param password User's password, should be at least 5 characters long
	 * @param timezone User's timezone (check /API/getTimezones)
	 * 
	 * @return The user that was successfully registered. 
	 */
	public User register(String email, String full_name, String password, String timezone)
	{
		String query = TodoistApiHandlerConstants.REGISTER
			.replace(PARAMETERS.EMAIL, email)
			.replace(PARAMETERS.FULL_NAME, full_name)
			.replace(PARAMETERS.PASSWORD, password)
			.replace(PARAMETERS.TIMEZONE, timezone);
		String response = call(query);
		try {
			user = new User(new JSONObject(response));
		} catch (JSONException e) {
			Log.e(this.toString(), "Received the following response from Todoist: " + response);
			/**
			 * TODO Error handling.
			 */
		}
		return user;
	}
	
	/**
	 * <pre>
	 * /API/updateUser should be HTTPS
	 * 
	 * You can just update full_name if you like, don't send parameters that 
	 * you don't want to change.
	 * 
	 * Required parameters:
	 *   token: The user's token (received on login)
	 *   Optional parameters:
	 *     email: User's email
	 *     full_name: User's full name
	 *     password: User's password, should be at least 5 characters long
	 *     timezone: User's timezone (check /API/getTimezones)
	 * 
	 * Successful return:
	 *   HTTP 200 OK with a JSON object with user info:
	 *     {"email": "...", "token": ..., ...}
	 *     
	 * Error returns:
	 *   "ERROR_PASSWORD_TOO_SHORT"
	 *   "ERROR_EMAIL_FOUND"
	 * </pre>
	 * 
	 * @param entries The optional parameters to change.
	 * @return The updated user.
	 */
	public User updateUser(Map.Entry<String, String>...entries)
	{
		String query = TodoistApiHandlerConstants.UPDATE_USER
			.replace(PARAMETERS.TOKEN, user.getApiToken());
		
		for (Map.Entry<String, String> n : entries) {
			if (n.getKey().toLowerCase() == "email") {
				query += OPTIONAL_PARAMETERS.EMAIL.replace(PARAMETERS.EMAIL, n.getValue());
			} else if (n.getKey().toLowerCase() == "full_name") {
				query += OPTIONAL_PARAMETERS.FULL_NAME.replace(PARAMETERS.FULL_NAME, n.getValue());
			} else if (n.getKey().toLowerCase() == "password") {
				query += OPTIONAL_PARAMETERS.PASSWORD.replace(PARAMETERS.PASSWORD, n.getValue());
			} else if (n.getKey().toLowerCase() == "timezone") {
				query += OPTIONAL_PARAMETERS.TIMEZONE.replace(PARAMETERS.TIMEZONE, n.getValue());
			}
		}
		
		String response = call(query);
		try {
			user = new User(new JSONObject(response));
		} catch (JSONException e) {
			Log.e(this.toString(), "Received the following response from Todoist: " + response);
			/**
			 * TODO Error handling.
			 */
		}
		return user;
	}

	/**
	 * <pre>
	 * /API/getProjects
	 * 
	 * Returns all os user's projects.
	 * 
	 * Required parameters:
	 *   token: The user's token (received on login)
	 *   
	 * Successful return:
	 *   HTTP 200 OK with a JSON list of all of user's projects:
	 *     [
	 *       {"user_id": 1, "name": "Test project", "color": 1, "collapsed": 0, "item_order": 1, "indent": 1, "cache_count": 4, "id": 22073},
	 *       {"user_id": 1, "name": "Another test project", "color": 2, "collapsed": 0, "item_order": 2, "indent": 1, "cache_count": 0, "id": 22074},
	 *       ...
	 *     ]
	 * </pre>
	 * 
	 * @return A list of projects for the current user.
	 */
	public ArrayList<Project> getProjects()
	{
		String query = TodoistApiHandlerConstants.GET_PROJECTS
			.replace(PARAMETERS.TOKEN, user.getApiToken());
		String response = call(query);
		ArrayList<Project> ret = new ArrayList<Project>();
		try {
			JSONArray jArray = new JSONArray(response); 
			for(int i = 0; i < jArray.length(); ++i) {
				ret.add(new Project(jArray.getJSONObject(i), user));
			}
		} catch (JSONException e) {
			Log.e(this.toString(), "Received the following response from Todoist: " + response);
			/**
			 * TODO Error Handling.
			 */
		}
		return ret;
	}
	
	/**
	 * <pre>
	 * /API/getProject
	 * 
	 * Return's information about a project.
	 * 
	 * Required parameters:
	 *   token: The user's token (received on login)
	 *   project_id: The id of the project to fetch
	 *   
	 * Successful return:
	 *   HTTP 200 OK with a JSON object with project info
	 *     {"user_id": 1L, "name": "Test project", "color": 1L, "collapsed": 0L, "item_order": 1L, "indent": 1L, "cache_count": 4L, "id": 22073L}
	 * </pre>
	 * 
	 * @param project_id the id of the project to fetch.
	 * @return Project
	 */
	public Project getProject(Integer project_id)
	{
		String query = TodoistApiHandlerConstants.GET_PROJECT
			.replace(PARAMETERS.TOKEN, user.getApiToken())
			.replace(PARAMETERS.PROJECT_ID, project_id.toString());
		String response = call(query);
		Project ret = null;
		try {
			ret = new Project(new JSONObject(response), user);
		} catch (JSONException e) {
			Log.e(this.toString(), "Received the following response from Todoist: " + response);
			/**
			 * TODO Error handling.
			 */
		}
		return ret;
	}

	/**
	 * <pre>
	 * /API/addProject
	 * 
	 * Add a new project.
	 * 
	 * Required parameters:
	 *   name: The name of the new project
	 *   token: The user's token (received on login)
	 *   
	 * Optional parameters:
	 *   color: The color of the new project
	 *   indent: The indent of the new project
	 *   order: The order of the new project
	 *   
	 * Successful return:
	 *   HTTP 200 OK with a JSON object with project info:
	 *     {"name": "...", "user_id": ..., ..., "collapsed": 0L, "id": 22000}
	 *     
	 * Error returns:
	 *   "ERROR_NAME_IS_EMPTY"
	 * </pre>
	 * 
	 * @param name The name of the new project.
	 * @param entries The optional parameters.
	 * @return The new project.
	 */
	public Project addProject(String name, Map.Entry<String, String>...entries)
	{
		String query = TodoistApiHandlerConstants.ADD_PROJECT
			.replace(PARAMETERS.TOKEN, user.getApiToken())
			.replace(PARAMETERS.NAME, name);
		
		for (Map.Entry<String, String> n : entries) {
			if (n.getKey().toLowerCase() == "color") {
				query += OPTIONAL_PARAMETERS.COLOR.replace(PARAMETERS.COLOR, n.getValue());
			} else if (n.getKey().toLowerCase() == "indent") {
				query += OPTIONAL_PARAMETERS.INDENT.replace(PARAMETERS.INDENT, n.getValue());
			} else if (n.getKey().toLowerCase() == "order") {
				query += OPTIONAL_PARAMETERS.ORDER.replace(PARAMETERS.ORDER, n.getValue());
			}
		}
		String response = call(query);
		
		Project ret = null;
		try {
			ret = new Project(new JSONObject(response), user);
		} catch (JSONException e) {
			Log.e(this.toString(), "Received the following response from Todoist: " + response);
			/**
			 * TODO Error Handling.
			 */
		}
		return ret;
	}
	
	/**
	 * <pre>
	 * /API/updateProject
	 * 
	 * Update an existing project.
	 * 
	 * Required parameters:
	 *   project_id: The id of the project to update
	 *   token: The user's token (received on login)
	 *   
	 * Optional parameters:
	 *   name: New name of the project
	 *   color: New color of the project
	 *   indent: New indent of the project
	 *   
	 * Successful return:
	 *   HTTP 200 OK with a JSON object with project info:
	 *     {"name": "...", "user_id": ..., ..., "collapsed": 0L, "id": 22000}
	 *     
	 * Error returns:
	 *   "ERROR_PROJECT_NOT_FOUND"
	 * </pre>
	 * 
	 * @param project_id The id of the project to update.
	 * @param entries Optional parameters.
	 * @return The updated project.
	 */
	public Project updateProject(Integer project_id, Map.Entry<String, String>...entries)
	{
		String query = TodoistApiHandlerConstants.UPDATE_PROJECT
			.replace(PARAMETERS.TOKEN, user.getApiToken())
			.replace(PARAMETERS.PROJECT_ID, project_id.toString());
		
		for (Map.Entry<String, String> n : entries) {
			if (n.getKey().toLowerCase() == "color") {
				query += OPTIONAL_PARAMETERS.COLOR.replace(PARAMETERS.COLOR, n.getValue());
			} else if (n.getKey().toLowerCase() == "indent") {
				query += OPTIONAL_PARAMETERS.INDENT.replace(PARAMETERS.INDENT, n.getValue());
			} else if (n.getKey().toLowerCase() == "name") {
				query += OPTIONAL_PARAMETERS.NAME.replace(PARAMETERS.NAME, n.getValue());
			}
		}
		String response = call(query);
		Project ret = null;
		try {
			ret = new Project(new JSONObject(response), user);
		} catch (JSONException e) {
			Log.e(this.toString(), "Received the following response from Todoist: " + response);
			/**
			 * TODO ErrorHandling
			 */
		}
		return ret;
	}
	
	/**
	 * <pre>
	 * /API/deleteProject
	 * 
	 * Delete an existing project.
	 * 
	 * Required parameters:
	 *   proejct_id: The id of the project to update
	 *   token: The user's token (received on login)
	 *   
	 * Successful return:
	 *   HTTP 200 OK with text:
	 *     "ok"
	 * </pre>
	 * 
	 * @param project_id The ID of the project to delete.
	 */
	public void deleteProject(Integer project_id)
	{
		String query = TodoistApiHandlerConstants.DELETE_PROJECT
			.replace(PARAMETERS.TOKEN, user.getApiToken())
			.replace(PARAMETERS.PROJECT_ID, project_id.toString());
		String response = call(query);
		if (response != "ok") {
			Log.e(this.toString(), "Received the following response from Todoist: " + response);
			/**
			 * TODO Error Handling.
			 */
		}
	}
	
	/**
	 * <pre>
	 * /API/getLabels
	 * 
	 * Returns all of user's labels.
	 * 
	 * Required parameters:
	 *   proejct_id: The id of the project to update
	 *   token: The user's token (received on login)
	 * </pre>
	 * 
	 * @param project_id The project to get the labels for.
	 * @return A list of labels.
	 */
	public ArrayList<String> getLabels(Integer project_id)
	{
		String query = TodoistApiHandlerConstants.GET_LABELS
			.replace(PARAMETERS.TOKEN, user.getApiToken())
			.replace(PARAMETERS.PROJECT_ID, project_id.toString());
		String response = call(query);
		ArrayList<String> ret = new ArrayList<String>();
		try {
			JSONArray jArray = new JSONArray(response); 
			for (int i = 0; i < jArray.length(); ++i) {
				ret.add(jArray.getJSONObject(i).getString(JSON.LABEL_ID));
			}
		} catch (JSONException e) {
			Log.e(this.toString(), "Received the following response from Todoist: " + response);
			/**
			 * TODO ErrorHandling
			 */
		}
		return ret;
	}
	
	/**
	 * <pre>
	 * /API/updateLabel
	 * 
	 * Changes the name of an existing label.
	 * 
	 * Required parameters:
	 *   old_name: The name of the old label
	 *   new_name: The name of the new label
	 *   token: The user's token (received on login)
	 *   
	 * Successful return:
	 *   HTTP 200 OK with text:
	 *     "ok"
	 * </pre>
	 * 
	 * @param old_name The old label name.
	 * @param new_name The new label name.
	 */
	public void updateLabel(String old_name, String new_name) {
		String query = TodoistApiHandlerConstants.UPDATE_LABEL
			.replace(PARAMETERS.TOKEN, user.getApiToken())
			.replace(PARAMETERS.OLD_NAME, old_name)
			.replace(PARAMETERS.NEW_NAME, new_name);
		String response = call(query);
		if (response != "ok") {
			Log.e(this.toString(), "Received the following response from Todoist: " + response);
			/**
			 * TODO Error Handling.
			 */
		}
	}
	
	/**
	 * <pre>
	 * /API/deleteLabel
	 * 
	 * Deletes an existing label.
	 * 
	 * Required parameters:
	 *   name: The name of the label to delete
	 *   token: The user's token (received on login)
	 *   
	 * Successful return:
	 *   HTTP 200 OK with text:
	 *     "ok"
	 * </pre>
	 * 
	 * @param name The label name to delete.
	 */
	public void deleteLabel(String name) {
		String query = TodoistApiHandlerConstants.DELETE_LABEL
			.replace(PARAMETERS.TOKEN, user.getApiToken())
			.replace(PARAMETERS.NAME, name);
		String response = call(query);
		if (response != "ok") {
			Log.e(this.toString(), "Received the following response from Todoist: " + response);
			/**
			 * TODO Error Handling.
			 */
		}
	}
	
	/**
	 * <pre>
	 * /API/getUncompletedItems
	 * 
	 * Returns a project's uncompleted items (tasks).
	 * 
	 * Required parameters:
	 *   proejct_id: The id of the project to update
	 *   token: The user's token (received on login)
	 *   
	 * Successful return:
	 *   HTTP 200 OK with a JSON object with user info:
	 *     [
	 *       {"due_date": new Date("Sun Apr 29 23:59:59 2007"), "user_id": 1, "collapsed": 0, "in_history": 0, "priority": 1, "item_order": 1, "content": "By these things", "indent": 1, "project_id": 22073, "id": 210870, "checked": 0, "date_string": "29. Apr 2007"},
	 *       {"due_date": null, "user_id": 1, "collapsed": 0, "in_history": 0, "priority": 1, "item_order": 2, "content": "Milk", "indent": 2, "project_id": 22073, "id": 210867, "checked": 0, "date_string": ""},
	 *       ...
	 *     ]
	 * </pre>
	 * 
	 * @param project_id The project to get the uncompleted items for.
	 * @return List of items.
	 */
	public ArrayList<Item> getUncompletedItems(Integer project_id) {
		Log.d(this.toString(), "Calling getUncompletedItems");
		String query = TodoistApiHandlerConstants.GET_UNCOMPLETED_ITEMS
			.replace(PARAMETERS.TOKEN, user.getApiToken())
			.replace(PARAMETERS.PROJECT_ID, project_id.toString());
		String response = call(query);
		ArrayList<Item> ret = new ArrayList<Item>();
		try {
			JSONArray jArray = new JSONArray(response); 
			for(int i = 0; i < jArray.length(); ++i) {
				ret.add(new Item(jArray.getJSONObject(i), user));
			}
		} catch (JSONException e) {
			Log.e(this.toString(), "Received the following response from Todoist: " + response);
			/**
			 * TODO Error Handling.
			 */
		}
		return ret;
	}
	
	/**
	 * <pre>
	 * /API/getCompletedItems
	 * 
	 * Returns a project's completed items (tasks) - the tasks that are in history.
	 * 
	 * Required parameters:
	 *   proejct_id: The id of the project to update
	 *   token: The user's token (received on login)
	 *   
	 * Successful return:
	 *   HTTP 200 OK with a JSON object with user info:
	 *     [
	 *       {"due_date": null, "user_id": 1, "collapsed": 0, "in_history": 1, "priority": 1, "item_order": 2, "content": "Fluffy ferret", "indent": 1, "project_id": 22073, "id": 210872, "checked": 1, "date_string": ""},
	 *       {"due_date": null, "user_id": 1, "collapsed": 0, "in_history": 1, "priority": 1, "item_order": 1, "content": "Test", "indent": 1, "project_id": 22073, "id": 210871, "checked": 1, "date_string": ""}
	 *       ...
	 *     ]
	 * </pre>
	 * 
	 * @param project_id The id of the project to get the completed items for.
	 * @return The list of items.
	 */
	public ArrayList<Item> getCompletedItems(Integer project_id) {
		Log.d(this.toString(), "Calling getCompletedItems");
		String query = TodoistApiHandlerConstants.GET_COMPLETED_ITEMS
			.replace(PARAMETERS.TOKEN, user.getApiToken())
			.replace(PARAMETERS.PROJECT_ID, project_id.toString());
		String response = call(query);
		ArrayList<Item> ret = new ArrayList<Item>();
		try {
			JSONArray jArray = new JSONArray(response); 
			for(int i = 0; i < jArray.length(); ++i) {
				ret.add(new Item(jArray.getJSONObject(i), user));
			}
		} catch (JSONException e) {
			Log.e(this.toString(), "Received the following response from Todoist: " + response);
			/**
			 * Error Handling.
			 */
		}
		return ret;
	}

	/**
	 * <pre>
	 * /API/getItemsById
	 * 
	 * Required parameters:
	 *   ids: A JSON list of ids to fetch
	 *   token: The user's token (received on login)
	 *   
	 * Successful return:
	 *   HTTP 200 OK with a JSON list with item objects. Example:
	 *     http://todoist.com/API/getItemsById?ids=[210873,210874]&token=fb5f22601ec566e48083213f7573e908a7a272e5
	 *     [
	 *       {"due_date": null, "user_id": 1, "collapsed": 0, "in_history": 1, "priority": 1, "item_order": 2, "content": "Fluffy ferret", "indent": 1, "project_id": 22073, "id": 210873, "checked": 1, "date_string": ""},
	 *       {"due_date": null, "user_id": 1, "collapsed": 0, "in_history": 1, "priority": 1, "item_order": 1, "content": "Test", "indent": 1, "project_id": 22073, "id": 210874, "checked": 1, "date_string": ""}
	 *       ...
	 *     ]
	 * </pre>
	 * 
	 * @param ids The list of ids to return.
	 * @return The list of items.
	 */
	public ArrayList<Item> getItemsById(Integer...ids) {
		/**
		 * TODO Make this method take an actual list?
		 */
		String query = TodoistApiHandlerConstants.GET_ITEMS_BY_ID
			.replace(PARAMETERS.TOKEN, user.getApiToken());
	
		boolean first = true;
		String idstring = "[";
		for (Integer n : ids) {
			if (first) {
				idstring += n.toString();
				first = false;
			} else {
				idstring += "," + n.toString();
			}
		}
		idstring += "]";
		
		query.replace(PARAMETERS.IDS, idstring);
		String response = call(query);
		ArrayList<Item> ret = new ArrayList<Item>();
		try {
			JSONArray jArray = new JSONArray(response); 
			for(int i = 0; i < jArray.length(); ++i) {
				ret.add(new Item(jArray.getJSONObject(i), user));
			}
		} catch (JSONException e) {
			Log.e(this.toString(), "Received the following response from Todoist: " + response);
			/**
			 * TODO Error Handling.
			 */
		}
		return ret;
	}
	
	/**
	 * <pre>
	 * /API/addItem
	 * 
	 * Adds an item to a project.
	 * 
	 * Required parameters:
	 *   proejct_id: The id of the project to add to
	 *   content: The text of the task
	 *   token: The user's token (received on login)
	 *   
	 * Optional parameters:
	 *   date_string: The date of the task, added in free form text. Examples of how date_string could look like.
	 *   priority: The priority of the task (a number between 1 and 4 - 1 for very urgent and 4 for natural).
	 *   
	 * Successful return:
	 *   HTTP 200 OK with a JSON object with task info. Example:
	 *     http://todoist.com/API/addItem?content=Test&project_id=22073&priority=1&token=fb5f22601ec566e48083213f7573e908a7a272e5
	 *     {"due_date": null, "user_id": 1, "collapsed": 0, "in_history": 0, "priority": 1, "item_order": 5, "content": "Test", "indent": 1, "project_id": 22073, "id": 210873, "checked": 0, "date_string": null}
	 * </pre>
	 * 
	 * @param project_id The id of the project to add the item to.
	 * @param content The content of the item.
	 * @param entries The optional parameters.
	 * @return
	 */
	public Item addItem(Integer project_id, String content, Map.Entry<String, String>...entries) {
		String query = TodoistApiHandlerConstants.ADD_ITEM
			.replace(PARAMETERS.TOKEN, user.getApiToken())
			.replace(PARAMETERS.PROJECT_ID, project_id.toString())
			.replace(PARAMETERS.CONTENT, content);

		for (Map.Entry<String, String> n : entries) {
			if (n.getKey().toLowerCase() == "date_string") {
				query += OPTIONAL_PARAMETERS.DATE_STRING.replace(PARAMETERS.DATE_STRING, n.getValue());
			} else if (n.getKey().toLowerCase() == "priority") {
				query += OPTIONAL_PARAMETERS.PRIORITY.replace(PARAMETERS.PRIORITY, n.getValue());
			}
		}
		
		String response = call(query);
		
		Item ret = null;
		try {
			ret = new Item(new JSONObject(response), user);
		} catch (JSONException e) {
			Log.e(this.toString(), "Received the following response from Todoist: " + response);
			/**
			 * TODO Error Handling.
			 */
		}
		return ret;
	}
	
	/**
	 * <pre>
	 * /API/updateItem
	 * 
	 * Update an existing item.
	 * 
	 * Required parameters:
	 *   id: The id of the item to update
	 *   token: The user's token (received on login)
	 *   
	 * Optional parameters:
	 *   content: The text of the task
	 *   date_string: The date of the task, added in free form text. Examples of how date_string could look like.
	 *   priority: The priority of the task (a number between 1 and 4 - 1 for very urgent and 4 for natural).
	 *   indent: The indent of the task
	 *   item_order: The order of the task
	 *   
	 * Successful return:
	 *   HTTP 200 OK with a JSON object with the updated task info. Example:
	 *     http://todoist.com/API/updateItem?id=210873&content=TestHello&token=fb5f22601ec566e48083213f7573e908a7a272e5
	 *     {"due_date": null, "user_id": 1, "collapsed": 0, "in_history": 0, "priority": 1, "item_order": 5, "content": "TestHello", "indent": 1, "project_id": 22073, "id": 210873, "checked": 0, "date_string": null}
	 * </pre>
	 * 
	 * @param item_id The id of the item to update.
	 * @param entries The optional parameters.
	 * @return The updated item.
	 */
	public Item updateItem(Integer item_id, Map.Entry<String, String>...entries) {
		String query = TodoistApiHandlerConstants.UPDATE_ITEM
			.replace(PARAMETERS.TOKEN, user.getApiToken())
			.replace(PARAMETERS.ITEM_ID, item_id.toString());
		
		for (Map.Entry<String, String> n : entries) {
			if (n.getKey().toLowerCase() == "content") {
				query += OPTIONAL_PARAMETERS.CONTENT.replace(PARAMETERS.CONTENT, n.getValue());
			}
			else if (n.getKey().toLowerCase() == "date_string") {
				query += OPTIONAL_PARAMETERS.DATE_STRING.replace(PARAMETERS.DATE_STRING, n.getValue());
			}
			else if (n.getKey().toLowerCase() == "priority") {
				query += OPTIONAL_PARAMETERS.PRIORITY.replace(PARAMETERS.PRIORITY, n.getValue());
			}
			else if (n.getKey().toLowerCase() == "indent") {
				query += OPTIONAL_PARAMETERS.INDENT.replace(PARAMETERS.INDENT, n.getValue());
			}
			else if (n.getKey().toLowerCase() == "item_order") {
				query += OPTIONAL_PARAMETERS.ORDER.replace(PARAMETERS.ORDER, n.getValue());
			}
		}
		
		String response = call(query);
		
		Item ret = null;
		try {
			ret = new Item(new JSONObject(response), user);
		} catch (JSONException e) {
			Log.e(this.toString(), "Received the following response from Todoist: " + response);
			/**
			 * TODO Error Handling.
			 */
		}
		return ret;
	}
	
	/**
	 * <pre>
	 * /API/updateOrders
	 * 
	 * Update the order of a project's tasks.
	 * 
	 * Required parameters:
	 *   project_id: The project of the tasks
	 *   item_id_list: A JSON list of the tasks's order, could be [3,2,9,7]
	 *   token: The user's token (received on login)
	 *   
	 * Successful return:
	 *   HTTP 200 OK with:
	 *     "ok"
	 * </pre>
	 * 
	 * @param project_id The id of the project to update.
	 * @param item_ids The order of the ids to update to.
	 */
	public void updateOrders(Integer project_id, Integer...item_ids) {
		/**
		 * TODO Make the item_ids an actual list?
		 */
		String query = TodoistApiHandlerConstants.UPDATE_ORDERS
			.replace(PARAMETERS.TOKEN, user.getApiToken())
			.replace(PARAMETERS.PROJECT_ID, project_id.toString());
		
		boolean first = true;
		String idstring = "[";
		for (Integer n : item_ids) {
			if (first) {
				idstring += n.toString();
				first = false;
			} else {
				idstring += "," + n.toString();
			}
		}
		idstring += "]";
		
		query.replace(PARAMETERS.IDS, idstring);
		
		String response = call(query);
		
		if (response != "ok") {
			Log.e(this.toString(), "Received the following response from Todoist: " + response);
			/**
			 * TODO Error Handling.
			 */
		}
	}
	
	/**
	 * <pre>
	 * /API/updateRecurringDate
	 * 
	 * Update recurring dates and set them to next date regarding an item's date_string.
	 * 
	 * Required parameters:
	 *   ids: A JSON list of items that are recurring.
	 *   token: The user's token (received on login)
	 *   
	 * Successful return:
	 *   HTTP 200 OK with a JSON list with the updated tasks. Example:
	 *     [
	 *       {"due_date": "Wed Mar 3 23:59:59 2010", "collapsed": 0, "labels": [], "is_dst": 0, "has_notifications": 0, "checked": 0, "indent": 1, "children": null, "content": "Test", "user_id": 1, "mm_offset": 60, "in_history": 0, "id": 3457537, "priority": 3, "item_order": 99, "project_id": 455832, "chains": null, "date_string": "every day"}
	 *     ]
	 * </pre>
	 * 
	 * @param item_ids Items to mark as recurring.
	 * @return List of updated items.
	 */
	public ArrayList<Item> updateRecurringDate(Integer...item_ids) {
		/**
		 * TODO Make item_ids an actual list.
		 */
		String query = TodoistApiHandlerConstants.UPDATE_RECURRING_DATE
			.replace(PARAMETERS.TOKEN, user.getApiToken());
		
		boolean first = true;
		String idstring = "[";
		for (Integer n : item_ids) {
			if (first) {
				idstring += n.toString();
				first = false;
			} else {
				idstring += "," + n.toString();
			}
		}
		idstring += "]";
		
		query.replace(PARAMETERS.IDS, idstring);
		
		String response = call(query);
		
		ArrayList<Item> ret = new ArrayList<Item>();
		try {
			JSONArray jArray = new JSONArray(response); 
			for(int i = 0; i < jArray.length(); ++i) {
				ret.add(new Item(jArray.getJSONObject(i), user));
			}
		} catch (JSONException e) {
			Log.e(this.toString(), "Received the following response from Todoist: " + response);
			/**
			 * TODO Error Handling.
			 */
		}
		return ret;
	}
	
	/**
	 * <pre>
	 * /API/deleteItems
	 * 
	 * Delete existing items.
	 * 
	 * Required parameters:
	 *   ids: A JSON list of ids to delete
	 *   token: The user's token (received on login)
	 *   
	 * Successful return:
	 *   HTTP 200 OK with:
	 *     "ok"
	 * </pre>
	 * 
	 * @param item_ids The items to delete.
	 */
	public void deleteItems(Integer...item_ids) {
		/**
		 * TODO Make item_ids an actual list?
		 */
		String query = TodoistApiHandlerConstants.DELETE_ITEMS
			.replace(PARAMETERS.TOKEN, user.getApiToken());
		
		boolean first = true;
		String idstring = "[";
		for (Integer n : item_ids) {
			if (first) {
				idstring += n.toString();
				first = false;
			} else {
				idstring += "," + n.toString();
			}
		}
		idstring += "]";
		
		query.replace(PARAMETERS.IDS, idstring);
		
		String response = call(query);
		
		if (response != "ok") {
			Log.e(this.toString(), "Received the following response from Todoist: " + response);
			/**
			 * TODO Error Handling.
			 */
		}
	}
	
	/**
	 * <pre>
	 * /API/completeItems
	 * 
	 * Complete items and move them to history.
	 * 
	 * Required parameters:
	 *   ids: A JSON list of ids to delete
	 *   token: The user's token (received on login)
	 *   
	 * Optional parameters:
	 *   in_history: If these tasks should be moved to history, default is 1. Setting it to 0 will not move it to history. Useful when checking off sub tasks.
	 * 
	 * Successful return:
	 *   HTTP 200 OK with:
	 *     "ok"
	 * </pre>
	 * 
	 * @param in_history Move these tasks to history?
	 * @param item_ids The items to complete.
	 */
	public void completeItems(boolean in_history, Integer...item_ids) {
		/**
		 * TODO Make item_ids an actual list?
		 * TODO Make in_history truly optional?
		 */
		String query = TodoistApiHandlerConstants.COMPLETE_ITEMS
			.replace(PARAMETERS.TOKEN, user.getApiToken());
		
		if (!in_history) query += OPTIONAL_PARAMETERS.IN_HISTORY.replace(PARAMETERS.IN_HISTORY, "0");
		
		boolean first = true;
		String idstring = "[";
		for (Integer n : item_ids) {
			if (first) {
				idstring += n.toString();
				first = false;
			} else {
				idstring += "," + n.toString();
			}
		}
		idstring += "]";
		
		query.replace(PARAMETERS.IDS, idstring);
		String response = call(query);
		
		if (response != "ok") {
			Log.e(this.toString(), "Received the following response from Todoist: " + response);
			/**
			 * TODO Error Handling.
			 */
		}
	}
	
	/**
	 * <pre>
	 * /API/uncompleteItems
	 * 
	 * Uncomplete items and move them to the active projects.
	 * 
	 * Required parameters:
	 *   ids: A JSON list of ids to delete
	 *   token: The user's token (received on login)
	 *   
	 * Successful return:
	 *   HTTP 200 OK with:
	 *     "ok"
	 * </pre>
	 * 
	 * @param item_ids
	 */
	public void uncompleteItems(Integer...item_ids) {
		/**
		 * TODO Make item_ids an actual list.
		 */
		String query = TodoistApiHandlerConstants.UNCOMPLETE_ITEMS
			.replace(PARAMETERS.TOKEN, user.getApiToken());
		
		boolean first = true;
		String idstring = "[";
		for (Integer n : item_ids) {
			if (first) {
				idstring += n.toString();
				first = false;
			} else {
				idstring += "," + n.toString();
			}
		}
		idstring += "]";
		
		query.replace(PARAMETERS.IDS, idstring);
		
		String response = call(query);
		
		if (response != "ok") {
			Log.e(this.toString(), "Received the following response from Todoist: " + response);
			/**
			 * TODO Error Handling.
			 */
		}
	}
	
	/**
	 * <pre>
	 * /API/query
	 * 
	 * Required parameters:
	 *   queries: A JSON list of queries to search. Examples of searches can be found on Todoist help
	 *   token: The user's token (received on login)
	 * 
	 * Optional parameters:
	 *   as_count: If set to 1 then no data will be returned, instead count of tasks will be returned.
	 *    
	 * Successful return:
	 *   HTTP 200 OK with JSON data of tasks found:
	 *     http://todoist.com/API/query?queries=["2007-4-29T10:13","overdue","p1","p2"]&token=fb5f22601ec566e48083213f7573e908a7a272e5
	 *     
	 *   JSON data is returned:
	 *     [
	 *       {"type": "date", "query": "2007-4-29T10:13", "data": [[...]]},
	 *       {"type": "overdue", "data": [...]}, 
	 *       ...
	 *     ]
	 * </pre>
	 */
	public ArrayList<Item> query(String...queries) {
		/**
		 * TODO Make queries an actual list?
		 */
		String query = TodoistApiHandlerConstants.QUERY
			.replace(PARAMETERS.TOKEN, user.getApiToken());
		
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
		
		query.replace(PARAMETERS.QUERIES, idstring);
		
		String response = call(query);
		
		ArrayList<Item> ret = new ArrayList<Item>();
		try {
			JSONArray jArray = new JSONArray(response); 
			for(int i = 0; i < jArray.length(); ++i) {
				ret.add(new Item(jArray.getJSONObject(i), user));
			}
		} catch (JSONException e) {
			Log.e(this.toString(), "Received the following response from Todoist: " + response);
			/**
			 * TODO Error Handling.
			 */
		}
		return ret;
	}
}
