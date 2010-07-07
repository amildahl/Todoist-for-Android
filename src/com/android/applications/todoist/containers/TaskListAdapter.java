package com.android.applications.todoist.containers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.widget.SimpleAdapter;

import com.android.applications.todoist.Constants;
import com.android.applications.todoist.R;

public class TaskListAdapter {
	private SeparatedListAdapter adapter;
	private Context context;
	
	public TaskListAdapter(Context context) 
	{
		this.context = context;
		this.adapter = new SeparatedListAdapter(this.context);
	}
	
	public TaskListAdapter(Context context, Tasks tasks)
	{
		this.context = context;
		this.adapter = new SeparatedListAdapter(this.context);
		this.setTasks(tasks);
	}
	
	public TaskListAdapter(Context context, Tasks tasks, Query query)
	{
		this.context = context;
		this.adapter = new SeparatedListAdapter(this.context);
		this.setTasks(tasks, query);
	}
	
	public void setTasks(Tasks tasks, Query query)
	{
		List<Map<String,?>> list;
		SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_STRING_SHORT);
		Task tempTask;
		ArrayList<Task> taskList;
		ArrayList<Date> dates = query.getDates();
		for(int i=0; i < dates.size(); i++)
		{
			list = new LinkedList<Map<String,?>>();
			taskList = tasks.getTasksByDate(dates.get(i));
			for(int j=0; j < taskList.size(); j++)
			{
				tempTask = taskList.get(j);
				list.add(this.createItem(tempTask.getContent(), tempTask.getProjectID()));
			}
			this.adapter.addSection(format.format(dates.get(i)), new SimpleAdapter(this.context, list, R.layout.task, new String[] {Constants.ADAPTER_TITLE, Constants.ADAPTER_PROJECT}, new int[] { R.id.TextView01, R.id.TextView02} ));
		}
	}
	
	public void setTasks(Tasks tasks)
	{
		List<Map<String,?>> list = new LinkedList<Map<String,?>>();
		
		//Project listing
	}
	
	public SeparatedListAdapter getAdapter()
	{
		return this.adapter;
	}
	
	private Map<String,?> createItem(String title, String project)
	{
		Map<String,String> item = new HashMap<String,String>();
		item.put(Constants.ADAPTER_TITLE, title);
		item.put(Constants.ADAPTER_PROJECT, project);
		return item;
	}
	
	private Map<String,?> createItem(String title, Date date)
	{
		return this.createItem(title, date.toString());
	}
}
