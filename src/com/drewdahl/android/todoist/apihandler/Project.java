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

package com.drewdahl.android.todoist.apihandler;

import org.json.JSONException;
import org.json.JSONObject;

import com.drewdahl.android.todoist.Constants;


public class Project {

	public Project(JSONObject obj, User user) throws JSONException
	{
		Integer user_id = obj.getInt(Constants.JSON_USERID);
		if (user_id == user.getId()) this.user = user;
		name = obj.getString(Constants.JSON_NAME);
		color = obj.getString(Constants.JSON_COLOR);
		collapsed = obj.getInt(Constants.JSON_COLLAPSED);
		item_order = obj.getInt(Constants.JSON_ITEMORDER);
		cache_count = obj.getInt(Constants.JSON_CACHECOUNT);
		indent = obj.getInt(Constants.JSON_INDENT);
		id = obj.getInt(Constants.JSON_ID);
	}

	/**
	 * Setters and Getters.
	 */
	public User getUser()
	{
		return user;
	}
	
	public void setUser(User user)
	{
		this.user = user;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getColor()
	{
		return color;
	}
	
	public void setColor(String color)
	{
		this.color = color;
	}
	
	public Integer getCollapsed()
	{
		return collapsed;
	}
	
	public void setCollapsed(Integer collapsed)
	{
		this.collapsed = collapsed;
	}
	
	public Integer getItemOrder()
	{
		return item_order;
	}
	
	public void setItemOrder(Integer item_order)
	{
		this.item_order = item_order;
	}
	
	public Integer getCacheCount()
	{
		return cache_count;
	}
	
	public void setCacheCount(Integer cache_count)
	{
		this.cache_count = cache_count;
	}
	
	public Integer getIndent()
	{
		return indent;
	}
	
	public void setIndent(Integer indent)
	{
		this.indent = indent;
	}
	
	public Integer getId()
	{
		return id;
	}
	
	/**
	 * TODO should this not be available?  When will the ID change?
	 * @note This is also a question in User and Item.
	 * @param id
	 */
	public void setId(Integer id)
	{
		this.id = id;
	}
	
	private User user = null;
	private String name;
	private String color;
	private int collapsed;
	private int item_order;
	private int cache_count;
	private int indent;
	private int id;
}
