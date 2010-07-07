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
	
	public void setDate(Date date)
	{
		this.dates.add(date);
	}
	
	public void setDates(Date start, Date finish)
	{
		start.setHours(0);
		start.setMinutes(0);
		start.setSeconds(0);
		finish.setHours(0);
		finish.setMinutes(0);
		finish.setSeconds(0);
		
		for(; start.before(finish); advanceDate(start))
		{
			this.dates.add((Date)start.clone());
		}
		
		this.dates.add(finish);
	}
	
	private void advanceDate(Date date) 
	{
		int[] daysMonth = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

		 if(isLeapYear(date.getYear()))
			daysMonth[2] = 29;

		 if(date.getDate() < daysMonth[date.getMonth()]) 
		 {
			 date.setDate(date.getDate() + 1);
		 }
		 else 
		 {
			  date.setDate(1);

			  if(date.getMonth() == 12) 
			  {
				 date.setMonth(1);
				 date.setYear(date.getYear() + 1);
			  }
			  else
			  {
				  date.setMonth(date.getMonth() +1);
			  }
		  }
	}

	private boolean isLeapYear(int year)
	{
		if( ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0) )
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void clearDates()
	{
		this.dates.clear();
	}
	
	public ArrayList<Date> getDates()
	{
		return dates;
	}
}
