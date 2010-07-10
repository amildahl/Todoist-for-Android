package com.android.applications.todoist.containers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
		PriorityList priorityList = new PriorityList(Constants.ListType.DATE);
		Date tempDate;
		Task tempTask;
		ArrayList<Task> taskList;
		ArrayList<Date> dates = query.getDates();
		for(int i=0; i < dates.size(); i++)
		{
			list = new LinkedList<Map<String,?>>();
			taskList = tasks.getTasksByDate(dates.get(i), true);
			for(int j=0; j < taskList.size(); j++)
			{
				tempTask = taskList.get(j);
				list.add(this.createItem(tempTask.getContent(), tempTask.getProjectID()));
			}
			priorityList.addList(list, dates.get(i));
		}
		
		while(priorityList.getListSize() != 0)
		{
			//Important Note! priorityList.getLowestPriorityDate() removes the date from the list!
			//priorityList.getLowestPriority() removes the List AND Priority Integer from their lists!
			//SO! They need to be run in THAT order, else you'll get really wonky results.
			tempDate = priorityList.getLowestPriorityDate();
			this.adapter.addSection(this.getDateString(tempDate,priorityList.findDateDifference(tempDate)), 
					new SimpleAdapter(this.context, priorityList.getLowestPriority(), R.layout.task, 
							new String[] {Constants.ADAPTER_TITLE, Constants.ADAPTER_PROJECT}, 
							new int[] { R.id.TextView01, R.id.TextView02} ));
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
	
	private String getDateString(Date date, Integer diff)
	{
		SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_STRING_SHORT);
		String dateString = "";
			
		switch(diff)
		{
			case -1:
				dateString = "Yesterday";
				break;
			case 0:
				dateString = "Today";
				break;
			case 1:
				dateString = "Tomorrow";
				break;
			default:
				if(diff > 1)
				{
				SimpleDateFormat format2 = new SimpleDateFormat("EEEEE");
				dateString = format2.format(date);
				}
				else
				{
					dateString = (diff*(-1)) + " days ago";
				}
				break;
		}
		
		return ( dateString + " " + format.format(date) );
	}
	
	public class PriorityList
	{
		private Constants.ListType type;
		private List<List<Map<String,?>>> list;
		private List<Integer> priority;
		private List<Date> dateList;
		PriorityList(Constants.ListType type)
		{
			list = new LinkedList<List<Map<String,?>>>();
			priority = new LinkedList<Integer>();
			dateList = new LinkedList<Date>();
			this.type = type;
		}
		
		public Integer getListSize()
		{
			return this.list.size();
		}
		
		public Date getLowestPriorityDate()
		{
			int lowest = this.findLowestPriority();
			if(lowest > -1)
			{
				return this.dateList.remove(lowest);
			}
			
			return new Date();
		}
		
		public void addList(List<Map<String,?>> item, Date date)
		{
			Date today = new Date();
			this.dateList.add(date);
			this.priority.add(this.findDateDifference(date));
			this.list.add(item);
		}
		
		public List<Map<String,?>> getLowestPriority()
		{
			int lowest = this.findLowestPriority();
			if(lowest > -1)
			{	
				this.priority.remove(lowest);
				return this.list.remove(lowest);
			}
			else
			{
				return new LinkedList<Map<String,?>>();
			}
		}
		
		private Integer findLowestPriority()
		{
			if(this.priority.size() > 0)
			{
				int lowest = this.priority.get(0);
				int num = 0;
				
				if(this.priority.size() > 1)
				{
					for(int i=1; i<this.priority.size(); i++)
					{
						if(this.priority.get(i) < lowest)
						{
							lowest = this.priority.get(i);
							num = i;
						}
					}
				}
				
				return num;
			}
			else
			{
				return -1;
			}
		}
		
		public Integer findDateDifference(Date date)
		{
			return this.findDateDifference(new Date(), date);
		}
		
		public Integer findDateDifference(Date two, Date one)
		{
			int first = (one.getYear() * 12 * 30) + (one.getMonth() * 30) + one.getDate();
			int last = (two.getYear() * 12 * 30) + (two.getMonth() * 30) + two.getDate();
			
			return (first - last);
		}
	}
}

