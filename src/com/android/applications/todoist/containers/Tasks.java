package com.android.applications.todoist.containers;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.applications.todoist.Constants;

public class Tasks {

	public Tasks()
	{
		this.setTasks(new ArrayList<Task>());
	}
	
	public Tasks(ArrayList<Task> tasks)
	{
		this.setTasks(tasks);
	}
	
	public Tasks(String jsonData)
	{
		this.setTasks(new ArrayList<Task>());
		this.parseTasks(jsonData);
	}
	
	public void parseTasks(String jsonData)
	{
		JSONObject obj;
		Task task;
		try
		{
			JSONArray jArray = new JSONArray(jsonData);
			for(int i=0;i<jArray.length();i++)
			{
				obj = jArray.getJSONObject(i);
				//Example: {"data":[{"indent":1,"chains":null,"labels":[####],"children":null,"collapsed":0,"id":####,"content":"Add Travis to PS3","has_notifications":0,"item_order":1,"mm_offset":-300,"priority":1,"in_history":0,"project_id":####,"is_dst":1,"due_date":"Wed Jun  2 23:59:59 2010","date_string":"2. Jun 2010","user_id":#####,"checked":0}],"type":"date","query":"2010-6-2T10:13"}
				task = new Task(new Date(Date.parse(obj.getString(Constants.JSON_DUEDATE))),obj.getString(Constants.JSON_USERID), obj.getBoolean(Constants.JSON_COLLAPSED),obj.getBoolean(Constants.JSON_INHISTORY),
						obj.getInt(Constants.JSON_PRIORITY),obj.getInt(Constants.JSON_ITEMORDER),obj.getString(Constants.JSON_CONTENT),obj.getInt(Constants.JSON_INDENT),
						obj.getString(Constants.JSON_PROJECTID), obj.getString(Constants.JSON_ID), obj.getBoolean(Constants.JSON_CHECKED), obj.getString(Constants.JSON_DATESTRING));
				this.addTask(task);
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	public int getSize()
	{
		return this.tasks.size();
	}
	
	public void addTask(Task task)
	{
		this.tasks.add(task);
	}
	
	public Task getTaskAt(int pos)
	{
		return this.tasks.get(pos);
	}
	
	/**
	 * @param tasks the tasks to set
	 */
	public void setTasks(ArrayList<Task> tasks) {
		this.tasks = tasks;
	}

	/**
	 * @return the tasks
	 */
	public ArrayList<Task> getTasks() {
		return tasks;
	}

	private ArrayList<Task> tasks;
}