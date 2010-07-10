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

public class Project {
	
	public Project() {
		this.user_id = "";
		this.name = "";
		this.color = "";
		this.collapsed = false;
		this.item_order = 0;
		this.cache_count = 0;
		this.indent = 0;
		this.id = "";
	}
	
	public Project(String user_id, String name, String color, String collapsed, String item_order, String cache_count, String indent, String id) {
		this.user_id = user_id;
		this.name = name;
		this.color = color;
		if(collapsed == "1")
			this.collapsed = Boolean.TRUE;
		else
			this.collapsed = Boolean.FALSE;
		this.item_order = Integer.parseInt(item_order);
		this.cache_count = Integer.parseInt(cache_count);
		this.indent = Integer.parseInt(indent);
		this.id = id;
	}
	
	/**
	 * @param user_id the user_id to set
	 */
	public void setUserID(String user_id) {
		this.user_id = user_id;
	}
	/**
	 * @return the user_id
	 */
	public String getUserID() {
		return user_id;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}
	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}
	/**
	 * @param collapsed the collapsed to set
	 */
	public void setCollapsed(Boolean collapsed) {
		this.collapsed = collapsed;
	}
	/**
	 * @return the collapsed
	 */
	public Boolean getCollapsed() {
		return collapsed;
	}
	/**
	 * @param item_order the item_order to set
	 */
	public void setItem_order(int item_order) {
		this.item_order = item_order;
	}
	/**
	 * @return the item_order
	 */
	public int getItem_order() {
		return item_order;
	}
	/**
	 * @param cache_count the cache_count to set
	 */
	public void setCache_count(int cache_count) {
		this.cache_count = cache_count;
	}
	/**
	 * @return the cache_count
	 */
	public int getCache_count() {
		return cache_count;
	}
	/**
	 * @param indent the indent to set
	 */
	public void setIndent(int indent) {
		this.indent = indent;
	}
	/**
	 * @return the indent
	 */
	public int getIndent() {
		return indent;
	}
	/**
	 * @param id the id to set
	 */
	public void setID(String id) {
		this.id = id;
	}
	/**
	 * @return the id
	 */
	public String getID() {
		return id;
	}
	
	private String user_id;
	private String name;
	private String color;
	private Boolean collapsed;
	private int item_order;
	private int cache_count;
	private int indent;
	private String id;
}
