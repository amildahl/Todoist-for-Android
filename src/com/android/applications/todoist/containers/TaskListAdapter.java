package com.android.applications.todoist.containers;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.android.applications.todoist.Constants;

public class TaskListAdapter {
	private SeparatedListAdapter adapter;
	
	public TaskListAdapter(Context context) 
	{
		this.adapter = new SeparatedListAdapter(context);
	}
	
	public TaskListAdapter(Context context, Tasks tasks, Boolean projectList)
	{
		this.adapter = new SeparatedListAdapter(context);
		this.setTasks(tasks, projectList);
	}
	
	public void setTasks(Tasks tasks, Boolean projectList)
	{
		List<Map<String,?>> list = new LinkedList<Map<String,?>>();
		list.add(this.createItem("title","date"));
		if(projectList)
		{
			
		}
		else
		{
			
		}
		//this.ad.addSection("Header", new SimpleAdapter(this, list , R.layout.task, new String[] {"title","date"},new int[] { R.id.TextView01, R.id.TextView02} ));
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
