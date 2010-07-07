package com.android.applications.todoist.containers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;

import com.android.applications.todoist.Constants;

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
	private ArrayList<Calendar> dates;
	
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
		//Why the ffff was this a bundle again?... crap >.>
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
		this.dates = new ArrayList<Calendar>();
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
	
	public void setDate(Calendar date)
	{
		this.dates.add(date);
	}
	
	public void setDates(Calendar start, Calendar finish)
	{
		if(start.before(finish))
		{
			for(; start.before(finish); start.add(Calendar.DATE, 1))
			{
				this.dates.add(start);
			}
			
			this.dates.add(finish);
		}
	}
	
	public void clearDates()
	{
		this.dates.clear();
	}
	
	public ArrayList<Calendar> getDates()
	{
		return dates;
	}
}
