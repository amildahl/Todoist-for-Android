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

import java.util.ArrayList;
import java.util.Date;

/**
 * Query Class that generates queries for the TODOIST API based on input.
 * The possible input includes dates, labels, and priorities.  It also
 * allows for the overdue and viewall options.
 * @author Andrew Dahl
 */
public class Query {
	private ArrayList<Date> dates;
	private ArrayList<Integer> priorities;
	private ArrayList<String> labels;
	
	private boolean include_overdue;
	private boolean include_all;
	
	/**
	 * public Query()
	 * <p>
	 * Default Constructor for Query Class
	 * @see Query
	 */
	public Query()
	{
		this.dates = new ArrayList<Date>();
		this.labels = new ArrayList<String>();
		this.priorities = new ArrayList<Integer>();
		this.include_all = false;
		this.include_overdue = false;
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
		if(this.dates.size() > 0 || this.priorities.size() > 0 || this.labels.size() > 0 || 
				this.include_all == true || this.include_overdue == true)
			return false;
		
		return true;
	}
	
	/**
	 * public void reset()
	 * <p>
	 * Resets the query list, item lists, and other values
	 */
	public void reset()
	{
		this.dates.clear();
		this.labels.clear();
		this.priorities.clear();
		
		this.include_all = false;
		this.include_overdue = false;
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
		boolean started = false;
		String query = "[";
		int i = 0;
		
		// Set Dates
		if(this.dates.size() > 0)
		{
			Date date;
			date = dates.get(0);
			query += "\"" + (date.getYear() + 1900) + "-" + (date.getMonth() + 1) + "-" + date.getDate() + "T0:0:0" + "\""; 
				
			for(i=1; i<this.dates.size(); i++)
			{
				date = dates.get(i);
				query += ",\"" + (date.getYear() + 1900) + "-" + (date.getMonth() + 1) + "-" + date.getDate() + "T0:0:0" + "\"";
			}
		}
		
		// Set Labels
		if(this.labels.size() > 0)
		{
			if(started)
			{
				i = 0;	
			}
			else
			{
				query += "\"" + labels.get(0) + "\"";
				i = 1;
				started = true;
			}
			
			for(; i < this.labels.size(); i++)
			{
				query += ",\"" + this.labels.get(i) + "\"";
			}
			
			
		}
		
		if(this.priorities.size() > 0)
		{
			if(started)
			{
				i = 0;
			}
			else
			{
				query += "\"p" + priorities.get(0) + "\"";
				i = 1;
				started = true;
			}
			
			for(; i < this.priorities.size(); i++)
			{
				query += ",\"p" + this.priorities.get(i) + "\"";
			}
		}
		
		if(started)
		{
			if(this.include_all)
			{
				query += ",\"viewall\"";
			}
			
			if(this.include_overdue)
			{
				query += ",\"overdue\"";
			}
		}
		else
		{
			if(this.include_all)
			{
				query += "\"viewall\"";
				started = true;
			}
			
			if(this.include_overdue)
			{
				if(started)
				{
					query += "\"overdue\"";
				}
				else
				{
					query += ",\"overdue\"";
				}
			}
		}
		
		query += "]";
		return query;
	}
	
	/**
	 * public ArrayList<Date> getDates()
	 * <p>
	 * Gets the ArrayList of dates that are included within the query
	 * @return
	 * <li> The list of dates included in the query
	 * @see Date
	 * @see ArrayList
	 */
	public ArrayList<Date> getDates()
	{
		return this.dates;
	}
	
	/**
	 * public ArrayList<Integer> getPriorities()
	 * <p>
	 * Gets the ArrayList of priorities included in the query 
	 * @return
	 * <li> The list of priorities included in the query
	 * @see ArrayList
	 */
	public ArrayList<Integer> getPriorities()
	{
		return this.priorities;
	}
	
	/**
	 * public ArrayList<String> getLabels()
	 * <p>
	 * Gets the ArrayList of labels included in the query
	 * @return
	 * <li> The list of labels included in the query
	 * @see ArrayList
	 */
	public ArrayList<String> getLabels()
	{
		return this.labels;
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
		this.dates.add(date);
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
			// This is required. If it's not here, all the dates in the array will be identical
			// Java is stupid.
			start = (Date)start.clone();
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
			this.priorities.add(priority);
		}
	}
	
	/**
	 * public void addOverdue()
	 * <p>
	 * Adds the overdue flag to the list of queries
	 */
	public void addOverdue()
	{
		this.include_overdue = true;
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
		
		this.labels.add(label);
	}
	
	/**
	 * public void addAll()
	 * <p>
	 * Adds the viewall option to the query list
	 */
	public void addAll()
	{
		this.include_all = true;
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
