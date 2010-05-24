package com.android.applications.todoist.containers;

import java.util.ArrayList;
import java.util.Date;

import android.os.Bundle;

public class Query {
	private String query_string;
	private String project_id;
	private String search_string;
	private Integer next_days;
	private boolean is_project;
	private boolean is_today;
	private boolean is_tomorrow;
	private boolean include_overdue;
	private boolean include_all;
	private ArrayList<String> priorities;
	private ArrayList<String> labels;
	private ArrayList<Date> dates;
	
	public Query()
	{
		this.setDefaults();
	}
	
	public Query(String query)
	{
		this.setDefaults();
		this.query_string = query;
	}
	
	public Query(Bundle extras)
	{
		this.setDefaults();
		//TODO: Query Constructor (Bundle)
	}
	
	private void setDefaults()
	{
		this.query_string = "";
		this.project_id = "";
		this.search_string = "";
		this.next_days = 0;
		this.is_project = false;
		this.is_today = false;
		this.is_tomorrow = false;
		this.include_overdue = false;
		this.include_all = false;
		this.priorities = new ArrayList<String>();
		this.labels = new ArrayList<String>();
		this.dates = new ArrayList<Date>();
	}
	
	public boolean isEmpty()
	{
		if(this.query_string != "" || this.project_id != "")
			return false;
		
		return true;
	}
	
	public boolean isProjectQuery()
	{
		if(this.project_id != "")
			return true;
		
		return false;
	}
	
	public boolean isNormalQuery()
	{
		if(this.query_string != "")
			return true;
		
		return false;
	}
	
	public void clear()
	{
		this.setDefaults();
	}
	
	public String getQuery()
	{
		return this.query_string;
	}
	
	public String getProjectID()
	{
		return this.project_id;
	}
	
}
