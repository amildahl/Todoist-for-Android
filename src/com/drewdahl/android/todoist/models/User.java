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

package com.drewdahl.android.todoist.models;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.drewdahl.android.todoist.apihandler.TodoistApiHandlerConstants.JSON;
import com.drewdahl.android.todoist.provider.TodoistProviderMetaData.Users;

/**
 * 
 * @author Andrew Dahl
 *
 * TODO Add a save method to this class that saves it in the cache and
 * updates the server.  We can then update the caching times by using this
 * class as a wrapper to everything else that manages our data stores.
 * 
 * This class should always be created from the API Handler as it should 
 * associate the user with an actual todoist account.  Without that what
 * use is there for this thing?
 */
public class User {
	public static final String KEY = "com.drewdahl.android.todoist.models.User";
	
	public User(JSONObject obj) throws JSONException {
		email = obj.getString(JSON.EMAIL);
		full_name = obj.getString(JSON.FULL_NAME);
		id = obj.getInt(JSON.ID);
		api_token = obj.getString(JSON.API_TOKEN);
		start_page = obj.getString(JSON.START_PAGE);
		timezone = obj.getString(JSON.TIMEZONE);
		/**
		 * TODO Get this right.
		tz_offset = obj.getJSONObject(Constants.JSON_TZOFFSET);
		 */
		time_format = obj.getInt(JSON.TIME_FORMAT);
		date_format = obj.getInt(JSON.DATE_FORMAT);
		sort_order = obj.getInt(JSON.SORT_ORDER);
		twitter = obj.getString(JSON.TWITTER);
		jabber = obj.getString(JSON.JABBER);
		msn = obj.getString(JSON.MSN);
		mobile_number = obj.getString(JSON.MOBILE_NUMBER);
		mobile_host = obj.getString(JSON.MOBILE_HOST);
		/**
		 * TODO Add premium stuffs ...
		 */
	}
	
	public void save(ContentResolver resolver) {
		ContentValues values = new ContentValues();

		values.put(Users.EMAIL, email);
		values.put(Users.FULL_NAME, full_name);
		values.put(Users._ID, id);
		values.put(Users.API_TOKEN, api_token);
		values.put(Users.START_PAGE, start_page);
		values.put(Users.TIMEZONE, timezone);
		/**
		 * TODO Get this right.
		values.put(Users.TZ_OFFSET, tz_offset);	  // User's Timezone Offset	["-5:00", -5, 0, 1] -- [GMT_STRING, HOURS, MINUTES, IS_DAYLIGHT_SAVINGS_TIME]
		 */
		values.put(Users.TIME_FORMAT, time_format);
		values.put(Users.DATE_FORMAT, date_format);
		values.put(Users.SORT_ORDER, sort_order);
		values.put(Users.TWITTER, twitter);
		values.put(Users.JABBER, jabber);
		values.put(Users.MSN, msn);
		values.put(Users.MOBILE_NUMBER, mobile_number);
		values.put(Users.MOBILE_HOST, mobile_host);
		
		Uri myUri = ContentUris.withAppendedId(Users.CONTENT_URI, id);
		Cursor c = resolver.query(myUri, null, null, null, null);
		if (c.getCount() < 1) {
			resolver.insert(Users.CONTENT_URI, values);
		} else {
			resolver.update(myUri, values, null, null);
		}
	}
	
	/**
	 * Getters and Setters ...
	 */
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getFullName() {
		return full_name;
	}
	
	public void setFullName(String full_name) {
		this.full_name = full_name;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getApiToken() {
		return api_token;
	}
	
	public void setApiToken(String api_token) {
		this.api_token = api_token;
	}
	
	public String getStartPage() {
		return start_page;
	}
	
	public void setStartPage(String start_page) {
		this.start_page = start_page;
	}
	
	public String getTimezone() {
		return timezone;
	}
	
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	
	/**
	 * TODO Get this right.
	public JSONObject getTzOffset() {
		return tz_offset;
	}
	
	public void setTzOffset(JSONObject tz_offset) {
		this.tz_offset = tz_offset;
	}
	 */
	
	public Integer getTimeFormat() {
		return time_format;
	}
	
	public void setTimeFormat(Integer time_format) {
		this.time_format = time_format;
	}
	
	public Integer getDateFormat() {
		return date_format;
	}
	
	public void setDateFormat(Integer date_format) {
		this.date_format = date_format;
	}
	
	public Integer getSortOrder() {
		return sort_order;
	}
	
	public void setSortOrder(Integer sort_order) {
		this.sort_order = sort_order;
	}
	
	public String getTwitter() {
		return twitter;
	}
	
	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}
	
	public String getJabber() {
		return jabber;
	}
	
	public void setJabber(String jabber) {
		this.jabber = jabber;
	}
	
	public String getMsn() {
		return msn;
	}
	
	public void setMsn(String msn) {
		this.msn = msn;
	}
	
	public String getMobileNumber() {
		return mobile_number;
	}
	
	public void setMobileNumber(String mobile_number) {
		this.mobile_number = mobile_number;
	}
	 
	public String getMobileHost() {
		return mobile_host;
	}
	
	public void setMobileHost(String mobile_host) {
		this.mobile_host = mobile_host;
	}
	
	private String email;		  // User's e-mail address	someGuy@someDomain.com
	private String full_name;	  // User's full name 		Joe Anderson
	private int id;			  // User's ID				156841
	private String api_token;	  // User's API Token		d18a76ab310947100kc60fe9b3cdc466515bb3a1 -- 40-digit hex
	private String start_page;	  // User's start_page		_info_page
	private String timezone;	  // User's Local Timezone	US/Central
	/**
	 * TODO Get this right.
	private JSONObject tz_offset;	  // User's Timezone Offset	["-5:00", -5, 0, 1] -- [GMT_STRING, HOURS, MINUTES, IS_DAYLIGHT_SAVINGS_TIME]
	 */
	private int time_format;	  // User's time format		0 = 13:00 else 1pm
	private int date_format;	  // User's date format		0 = DD-MM-YYYY else MM-DD-YYYY
	private int sort_order;	  // User's sort order		0 = Oldest dates first else Oldest dates last
	private String twitter;		  // User's twitter account	
	private String jabber;		  // User's jabber account	joe@doe.com
	private String msn;			  // User's msn account		amix_bin@msn.com
	private String mobile_number; // User's mobile number	
	private String mobile_host;	  // User's mobile host
}
