package com.android.applications.todoist.containers;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
		String type;
		try
		{
			JSONArray jArray = new JSONArray(jsonData);
			for(int i=0;i<jArray.length();i++)
			{
				obj = jArray.getJSONObject(i);

				if(!((JSONArray)obj.get("data")).equals(new JSONArray()))
				{
					type = obj.getString("type");
					JSONArray temp = (JSONArray)obj.get("data");
					for(int j=0; j < temp.length(); j++)
					{
						obj = temp.getJSONObject(j);
						//Example: {"data":[{"indent":1,"chains":null,"labels":[####],"children":null,"collapsed":0,"id":####,"content":"Add Travis to PS3","has_notifications":0,"item_order":1,"mm_offset":-300,"priority":1,"in_history":0,"project_id":####,"is_dst":1,"due_date":"Wed Jun  2 23:59:59 2010","date_string":"2. Jun 2010","user_id":#####,"checked":0}],"type":"date","query":"2010-6-2T10:13"}
						
						this.addTask(new Task(obj,type));
					}
				}
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	public ArrayList<Task> getTasksByDate(Date date, boolean remove)
	{
		ArrayList<Task> list = new ArrayList<Task>();
		Date dueDate;
		for(int i = 0; i < this.tasks.size(); i++)
		{
			dueDate = this.tasks.get(i).getDueDate();
			if(dueDate.compareTo(date) == 0)
			{
				list.add(this.tasks.get(i));
				if(remove)
				{
					this.tasks.remove(i);
					i--;
				}
			}
		}
		
		return list;
	}
	
	public ArrayList<Task> getOverdueTasks()
	{
		ArrayList<Task> list = new ArrayList<Task>();
		Date dueDate;
		Date today = new Date();
		today.setSeconds(0);
		today.setMinutes(0);
		today.setHours(0);
		
		for(int i = 0; i < this.tasks.size(); i++)
		{
			dueDate = this.tasks.get(i).getDueDate();
			if(dueDate.before(today))
			{
				list.add(this.tasks.get(i));
			}
		}
		
		return list;
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