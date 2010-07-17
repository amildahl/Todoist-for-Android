/*    
	This file is part of Todoist for Android�.

    Todoist for Android� is free software: you can redistribute it and/or 
    modify it under the terms of the GNU General Public License as published 
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Todoist for Android� is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Todoist for Android�.  If not, see <http://www.gnu.org/licenses/>.
    
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

package com.drewdahl.android.todoist.items;

import org.json.JSONException;
import org.json.JSONObject;

import com.drewdahl.android.todoist.Constants;

import com.drewdahl.android.todoist.users.User;
import com.drewdahl.android.todoist.apihandlers.TodoistApiHandler;
import com.drewdahl.android.todoist.projects.Project;

public class Item {

	/**
	 * 
	 * @param obj
	 * @param user
	 * @param project Can be null and will be filled in by constructor.
	 * @throws JSONException
	 */
	public Item(JSONObject obj, User user, Project project) throws JSONException
	{
		due_date = obj.getInt(Constants.JSON_DUEDATE);
		Integer user_id = obj.getInt(Constants.JSON_USERID);
		if (user_id == user.getId()) this.user = user;
		/**
		 * TODO Else error?
		 */
		collapsed = obj.getInt(Constants.JSON_COLLAPSED);
		in_history = obj.getInt(Constants.JSON_INHISTORY);
		priority = obj.getInt(Constants.JSON_PRIORITY);
		item_order = obj.getInt(Constants.JSON_ITEMORDER);
		content = obj.getString(Constants.JSON_CONTENT);
		indent = obj.getInt(Constants.JSON_INDENT);
		Integer project_id = obj.getInt(Constants.JSON_PROJECTID);
		if (project == null) project = TodoistApiHandler.getInstance(user.getToken()).getProject(project_id);
		if (project_id == project.getId()) this.project = project;
		/**
		 * TODO Else error?
		 */
		id = obj.getInt(Constants.JSON_ID);
		checked = obj.getInt(Constants.JSON_CHECKED);
		date_string = obj.getString(Constants.JSON_DATESTRING);
	}

	/**
	 * Setters and Getters.
	 */
	public Integer getDueDate()
	{
		return due_date;
	}
	
	public void setDueDate(Integer due_date)
	{
		this.due_date = due_date;
	}
	
	public User getUser()
	{
		return user;
	}
	
	/**
	 * TODO Have a public setUser() method?
	 */
	
	public Integer getCollapsed()
	{
		return collapsed;
	}
	
	public void setCollapsed(Integer collapsed)
	{
		this.collapsed = collapsed;
	}
	
	public Integer getInHistory()
	{
		return in_history;
	}
	
	public void setInHistory(Integer in_history)
	{
		this.in_history = in_history;
	}
	
	public Integer getPriority()
	{
		return priority;
	}
	
	public void setPriority(Integer priority)
	{
		this.priority = priority;
	}
	
	public Integer getItemOrder()
	{
		return item_order;
	}
	
	public void setItemOrder(Integer item_order)
	{
		this.item_order = item_order;
	}
	
	public String getContent()
	{
		return content;
	}
	
	public void setContent(String content)
	{
		this.content = content;
	}
	
	public Integer getIndent()
	{
		return indent;
	}
	
	public void setIndent(Integer indent)
	{
		this.indent = indent;
	}
	
	public Project getProject()
	{
		return project;
	}
	
	public void setProject(Project project)
	{
		this.project = project;
	}
	
	public Integer getId()
	{
		return id;
	}
	
	public Integer getChecked()
	{
		return checked;
	}
	
	public void setChecked(Integer checked)
	{
		this.checked = checked;
	}
	
	public String getDateString()
	{
		return date_string;
	}
	
	public void setDateString(String date_string)
	{
		this.date_string = date_string;
	}
	
	private Integer due_date;
	private User user = null;
	private Integer collapsed;
	private Integer in_history;
	private Integer priority;
	private Integer item_order;
	private String content;
	private Integer indent;
	private Project project = null;
	private Integer id;
	private Integer checked;
	private String date_string;
}
