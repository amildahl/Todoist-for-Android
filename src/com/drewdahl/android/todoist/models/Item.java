/*
 * Copyright (C) 2008 by Alex Brandt <alunduil@alunduil.com>
 * 
 * This program is free software; you can redistribute it and#or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package com.drewdahl.android.todoist.models;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;

import com.drewdahl.android.todoist.apihandler.TodoistApiHandler;
import com.drewdahl.android.todoist.apihandler.TodoistApiHandlerConstants.JSON;
import com.drewdahl.android.todoist.provider.TodoistProviderMetaData.Items;

public class Item {
	public Item(JSONObject obj, User user) throws JSONException {
		due_date = obj.getInt(JSON.DUE_DATE);
		this.user = user;
		collapsed = obj.getInt(JSON.COLLAPSED);
		in_history = obj.getInt(JSON.IN_HISTORY);
		priority = obj.getInt(JSON.PRIORITY);
		item_order = obj.getInt(JSON.ITEM_ORDER);
		content = obj.getString(JSON.CONTENT);
		indent = obj.getInt(JSON.INDENT);
		/**
		 * TODO Move this object get to the Provider somehow?
		 */
		this.project = TodoistApiHandler.getInstance().getProject(obj.getInt(JSON.PROJECT_ID));
		id = obj.getInt(JSON.ID);
		checked = obj.getInt(JSON.CHECKED);
		date_string = obj.getString(JSON.DATE_STRING);
	}

	public void save(ContentResolver resolver) {
		ContentValues values = new ContentValues();

		values.put(Items.DUE_DATE, due_date);
		values.put(Items.USER_ID, user.getId());
		values.put(Items.COLLAPSED, collapsed);
		values.put(Items.IN_HISTORY, in_history);
		values.put(Items.PRIORITY, priority);
		values.put(Items.ITEM_ORDER, item_order);
		values.put(Items.CONTENT, content);
		values.put(Items.INDENT, indent);
		values.put(Items.PROJECT_ID, project.getId());
		values.put(Items._ID, id);
		values.put(Items.CHECKED, checked);
		values.put(Items.DATE_STRING, date_string);

		resolver.insert(ContentUris.withAppendedId(Items.CONTENT_URI, id), values);
	}

	/**
	 * Setters and Getters.
	 */
	public Integer getDueDate() {
		return due_date;
	}
	
	public void setDueDate(Integer due_date) {
		this.due_date = due_date;
	}
	
	public User getUser() {
		return user;
	}
	
	/**
	 * TODO Have a public setUser() method?
	 */
	
	public Integer getCollapsed() {
		return collapsed;
	}
	
	public void setCollapsed(Integer collapsed) {
		this.collapsed = collapsed;
	}
	
	public Integer getInHistory() {
		return in_history;
	}
	
	public void setInHistory(Integer in_history) {
		this.in_history = in_history;
	}
	
	public Integer getPriority() {
		return priority;
	}
	
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	public Integer getItemOrder() {
		return item_order;
	}
	
	public void setItemOrder(Integer item_order) {
		this.item_order = item_order;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public Integer getIndent() {
		return indent;
	}
	
	public void setIndent(Integer indent) {
		this.indent = indent;
	}
	
	public Project getProject() {
		return project;
	}
	
	public void setProject(Project project) {
		this.project = project;
	}
	
	public Integer getId() {
		return id;
	}
	
	public Integer getChecked() {
		return checked;
	}
	
	public void setChecked(Integer checked) {
		this.checked = checked;
	}
	
	public String getDateString() {
		return date_string;
	}
	
	public void setDateString(String date_string) {
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
