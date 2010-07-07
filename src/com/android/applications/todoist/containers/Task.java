package com.android.applications.todoist.containers;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.applications.todoist.Constants;

public class Task {

	public Task()
	{
		this.due_date = new Date();
		this.user_id = "";
		this.collapsed = false;
		this.in_history = false;
		this.priority = 0;
		this.item_order = 0;
		this.content = "";
		this.indent = 0;
		this.project_id = "";
		this.id = "";
		this.checked = false;
		this.date_string = "";
		this.type = "";
	}
	
	public Task(Date due_date, String user_id, Boolean collapsed, Boolean in_history, int priority, int item_order, String content, int indent, String project_id, String id, Boolean checked, String date_string, String type)
	{
		this.due_date = due_date;
		this.user_id = user_id;
		this.collapsed = collapsed;
		this.in_history = in_history;
		this.priority = priority;
		this.item_order = item_order;
		this.content = content;
		this.indent = indent;
		this.project_id = project_id;
		this.id = id;
		this.checked = checked;
		this.date_string = date_string;
		this.type = type;
	}
	
	public Task(JSONObject obj, String type)
	{
		this.type = type;
		
		try {
			this.due_date = new Date(Date.parse(obj.getString(Constants.JSON_DUEDATE)));
		} catch (JSONException e) {
			this.due_date = new Date();
			e.printStackTrace();
		}
		try {
			this.user_id = obj.getString(Constants.JSON_USERID);
		} catch (JSONException e) {
			this.user_id = "";
			e.printStackTrace();
		}
		try {
			this.collapsed = obj.getBoolean(Constants.JSON_COLLAPSED);
		} catch (JSONException e) {
			this.collapsed = false;
			e.printStackTrace();
		}
		try {
			this.in_history = obj.getBoolean(Constants.JSON_INHISTORY);
		} catch (JSONException e8) {
			this.in_history = false;
			e8.printStackTrace();
		}
		try {
			this.priority = obj.getInt(Constants.JSON_PRIORITY);
		} catch (JSONException e7) {
			this.priority = 0;
			e7.printStackTrace();
		}
		try {
			this.item_order = obj.getInt(Constants.JSON_ITEMORDER);
		} catch (JSONException e6) {
			this.item_order = 0;
			e6.printStackTrace();
		}
		try {
			this.content = obj.getString(Constants.JSON_CONTENT);
		} catch (JSONException e5) {
			this.content = "";
			e5.printStackTrace();
		}
		try {
			this.indent = obj.getInt(Constants.JSON_INDENT);
		} catch (JSONException e4) {
			this.indent = 0;
			e4.printStackTrace();
		}
		try {
			this.project_id = obj.getString(Constants.JSON_PROJECTID);
		} catch (JSONException e3) {
			this.project_id = "";
			e3.printStackTrace();
		}
		try {
			this.id = obj.getString(Constants.JSON_ID);
		} catch (JSONException e2) {
			this.id = "";
			e2.printStackTrace();
		}
		try {
			this.checked = obj.getBoolean(Constants.JSON_CHECKED);
		} catch (JSONException e1) {
			this.checked = false;
			e1.printStackTrace();
		}
		try {
			this.date_string = obj.getString(Constants.JSON_DATESTRING);
		} catch (JSONException e) {
			this.date_string = "";
			e.printStackTrace();
		}
	}
	
	/**
	 * @param due_date the due_date to set
	 */
	public void setDueDate(Date due_date) {
		this.due_date = due_date;
	}
	/**
	 * @return the due_date
	 */
	public Date getDueDate() {
		return due_date;
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
	 * @param in_history the in_history to set
	 */
	public void setInHistory(Boolean in_history) {
		this.in_history = in_history;
	}
	/**
	 * @return the in_history
	 */
	public Boolean getInHistory() {
		return in_history;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}
	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}
	/**
	 * @param item_order the item_order to set
	 */
	public void setItemOrder(int item_order) {
		this.item_order = item_order;
	}
	/**
	 * @return the item_order
	 */
	public int getItemOrder() {
		return item_order;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
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
	 * @param project_id the project_id to set
	 */
	public void setProjectID(String project_id) {
		this.project_id = project_id;
	}
	/**
	 * @return the project_id
	 */
	public String getProjectID() {
		return project_id;
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
	/**
	 * @param checked the checked to set
	 */
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	/**
	 * @return the checked
	 */
	public Boolean getChecked() {
		return checked;
	}
	/**
	 * @param date_string the date_string to set
	 */
	public void setDateString(String date_string) {
		this.date_string = date_string;
	}
	/**
	 * @return the date_string
	 */
	public String getDateString() {
		return date_string;
	}
	/**
	 * @param type - the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	private Date due_date;
	private String user_id;
	private Boolean collapsed;
	private Boolean in_history;
	private int priority;
	private int item_order;
	private String content;
	private int indent;
	private String project_id;
	private String id;
	private Boolean checked;
	private String date_string;
	private String type;
	
}
