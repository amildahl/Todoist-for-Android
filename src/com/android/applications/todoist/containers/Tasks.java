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
		int size = this.tasks.size();
		Date today = new Date();
		today.setSeconds(0);
		today.setMinutes(0);
		today.setHours(0);
		
		for(int i = 0; i < size; i++)
		{
			if(this.tasks.get(i).getDueDate().before(today))
			{
				list.add(this.tasks.get(i));
			}
		}
		
		return list;
	}
	
	public ArrayList<Date> getDates()
	{
		ArrayList<Date> list = new ArrayList<Date>();
		Date tempDate;
		int size = this.tasks.size();
		
		for(int i=0; i < size; i++)
		{
			tempDate = this.tasks.get(i).getDueDate();
			if(!list.contains(tempDate))
			{
				list.add(tempDate);
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