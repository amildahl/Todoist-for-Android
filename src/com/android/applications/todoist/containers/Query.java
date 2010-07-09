package com.android.applications.todoist.containers;

import java.util.ArrayList;
import java.util.Date;

/**
 * Query Class that generates queries for the TODOIST API based on input.
 * The possible input includes dates, labels, and priorities.  It also
 * allows for the overdue and viewall options.
 * @author Andrew Dahl
 */
public class Query {
	private ArrayList<String> queries;
	
	/**
	 * public Query()
	 * <p>
	 * Default Constructor for Query Class
	 * @see Query
	 */
	public Query()
	{
		this.queries = new ArrayList<String>();
	}
	
	/**
	 * public boolean isEmpty()
	 * <p>
	 * Checks if the query list is empty
	 * @return 
	 * <li> True if list is empty
	 * <li> False if it's not empty
	 */
	public boolean isEmpty()
	{	
		if(this.queries.size() > 0)
			return false;
		
		return true;
	}
	
	/**
	 * public void clear()
	 * <p>
	 * Clears the current query list
	 */
	public void clear()
	{
		this.queries.clear();
	}
	
	/**
	 * public String getQuery()
	 * <p>
	 * Returns a String that can be used as the query parameters in the query API method
	 * @return
	 * <li>Example String: ["2007-4-29T0:0:0","overdue","p1","p2"]
	 */
	public String getQuery()
	{
		String query = "[";
		
		if(this.queries.size() > 0)
		{
			query += "\"queries[0]\""; 
				
			for(int i=1; i<this.queries.size(); i++)
			{
				query += ",\"" + queries.get(i) + "\"";
			}
		}
		
		query += "]";
		return query;
	}
	
	/**
	 * public void addDate(Date date)
	 * <p>
	 * Adds a date to the list of queries
	 * @param date - The date to be queried for
	 * @see Date
	 */
	public void addDate(Date date)
	{
		this.queries.add(date.getYear() + "-" + date.getMonth() + "-" + date.getDate() + "T0:0:0");
	}
	
	/**
	 * public void addDateRange(Date start, Date finish)
	 * <p>
	 * Adds all dates from start to finish to the list of queries, including start and finish
	 * @param start - Starting date of the range to be queried for
	 * @param finish - Finishing date of the range to be queried for
	 * @see Date
	 */
	public void addDateRange(Date start, Date finish)
	{
		start.setHours(0);
		start.setMinutes(0);
		start.setSeconds(0);
		finish.setHours(1);
		finish.setMinutes(0);
		finish.setSeconds(0);
		
		for(; start.before(finish); advanceDate(start))
		{
			this.addDate(start);
		}
	}
	
	/**
	 * public void addPriority(int priority)
	 * <p>
	 * Adds a priority to the list of queries
	 * @param priority - The integer of the priority to be queried for. Possible integers are 1-4
	 */
	public void addPriority(int priority)
	{
		if(priority > 0 && priority < 5)
		{
			this.queries.add("p" + priority);
		}
	}
	
	/**
	 * public void addOverdue()
	 * <p>
	 * Adds the overdue flag to the list of queries
	 */
	public void addOverdue()
	{
		this.queries.add("overdue");
	}
	
	/**
	 * public void addLable(String label)
	 * <p>
	 * Adds a label to the list of queries
	 * @param label - Label to be queried
	 */
	public void addLabel(String label)
	{
		if(!(label.contains("@")))
			label = "@" + label;
		
		this.queries.add(label);
	}
	
	/**
	 * public void addAll()
	 * <p>
	 * Adds the viewall option to the query list
	 */
	public void addAll()
	{
		this.queries.add("viewall");
	}
	
	/**
	 * private void advanceDate(Date date)
	 * <p>
	 * Advances the date by one day
	 * @param date - The date to be advanced
	 * @see Date
	 */
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

	/**
	 * private boolean isLeapYear(int year)
	 * <p>
	 * Checks to see if year is a leap year
	 * @param year - The year to be checked
	 * @return
	 * <li>True if year is divisible by (4, but not 100) OR by 400
	 * <li>False the above is not true
	 */
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
}
