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
