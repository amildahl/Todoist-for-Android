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
		PriorityDisplayList priorityList = new PriorityDisplayList(Constants.ListType.DATE);
		Date tempDate;
		Task tempTask;
		ArrayList<Task> taskList;
		//ArrayList<Date> dates = query.getDates();
		ArrayList<Date> dates = this.combineDates(tasks.getDates(),query.getDates());
		int size = dates.size();
		
		for(int i=0; i < size; i++)
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
			SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_STRING_SHORT);
			//Important Note! priorityList.getLowestPriorityDate() removes the date from the list!
			//priorityList.getLowestPriority() removes the List AND Priority Integer from their lists!
			//SO! They need to be run in THAT order, else you'll get really wonky results.
			tempDate = priorityList.getHighestPriorityDate();
			this.adapter.addSection(this.getDateString(tempDate,priorityList.findDateDifference(tempDate)), " " + format.format(tempDate),
					new SimpleAdapter(this.context, priorityList.getHighestPriority(), R.layout.task, 
							new String[] {Constants.ADAPTER_TITLE, Constants.ADAPTER_PROJECT}, 
							new int[] { R.id.TextView01, R.id.TextView02} ));
		}
	}
	
	private ArrayList<Date> combineDates(ArrayList<Date> dateSet1, ArrayList<Date> dateSet2)
	{
		int size = dateSet2.size();
		Date tempDate;
		
		for(int i=0; i < size; i++)
		{
			tempDate = dateSet2.get(i);
			tempDate.setHours(23);
			tempDate.setMinutes(59);
			tempDate.setSeconds(59);
			
			if(!dateSet1.contains(tempDate))
			{
				dateSet1.add(tempDate);
			}
		}
		
		return dateSet1;
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
		
		return ( dateString );
	}
	
	public class PriorityDisplayList
	{
		private Constants.ListType type;
		private List<List<Map<String,?>>> list;
		private List<Integer> priority;
		private List<Date> dateList;
		PriorityDisplayList(Constants.ListType type)
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
		
		public Date getHighestPriorityDate()
		{
			int lowest = this.findHighestPriority();
			if(lowest > -1)
			{
				return this.dateList.remove(lowest);
			}
			
			return new Date();
		}
		
		public void addList(List<Map<String,?>> item, Date date)
		{
			this.dateList.add(date);
			this.priority.add(this.findDateDifference(date));
			this.list.add(item);
		}
		
		public List<Map<String,?>> getHighestPriority()
		{
			int lowest = this.findHighestPriority();
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
		
		private Integer findHighestPriority()
		{			
			if(this.priority.size() > 0)
			{
				int lowest = this.priority.get(0);
				int num = 0;
				int challenge = 0;
				
				if(this.priority.size() > 1)
				{
					for(int i=1; i<this.priority.size(); i++)
					{
						challenge = this.priority.get(i);
						
						if(challenge < 0 && challenge > -6)
						{
							//Priority #1
							if(challenge < lowest || lowest < -5 || lowest >= 0)
							{
								lowest = challenge;
								num = i;
							}
						}
						else if(challenge >= 0 && (lowest >= 0 || lowest < -5))
						{
							//Priority #2
							if(challenge < lowest || lowest < -5)
							{
								lowest = challenge;
								num = i;
							}
						}
						else if(lowest < -5)
						{
							//Priority #3
							if(challenge > lowest)
							{
								lowest = challenge;
								num = i;
							}
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

