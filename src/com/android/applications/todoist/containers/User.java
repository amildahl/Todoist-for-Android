package com.android.applications.todoist.containers;

public class User {

	public User()
	{
		this.email = "";
		this.full_name = "";
		this.id = "";	
		this.api_token = "";
		this.start_page = "";
		this.timezone = "";
		this.tz_offset = "";
		this.time_format = 0;
		this.date_format = 0;
		this.sort_order = 0;
		this.twitter = "";
		this.jabber = "";
		this.msn = "";
		this.mobile_number = "";
		this.mobile_host = "";
	}
	
	public User(String email, String full_name, String id, String api_token, String start_page, String timezone, String tz_offset, 
			String time_format, String date_format, String sort_order, String twitter, String jabber, String msn, String mobile_number, String mobile_host)
	{
		this.setValues(email, full_name, id, api_token, start_page, timezone, tz_offset, time_format, date_format, sort_order, twitter, jabber, msn, mobile_number, mobile_host);
	}
	
	public void setValues(String email, String full_name, String id, String api_token, String start_page, String timezone, String tz_offset, 
			String time_format, String date_format, String sort_order, String twitter, String jabber, String msn, String mobile_number, String mobile_host)
	{
		this.email = email;
		this.full_name = full_name;
		this.id = id;	
		this.api_token = api_token;
		this.start_page = start_page;
		this.timezone = timezone;
		this.tz_offset = tz_offset;
		this.time_format = Byte.parseByte(time_format);
		this.date_format = Byte.parseByte(date_format);
		this.sort_order = Byte.parseByte(sort_order);
		this.twitter = twitter;
		this.jabber = jabber;
		this.msn = msn;
		this.mobile_number = mobile_number;
		this.mobile_host = mobile_host;
	}
	
	public Boolean isValid()
	{
		if( this.api_token == "")
		{
			return false;
		}
		return true;
	}
	
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param full_name the full_name to set
	 */
	public void setFullName(String full_name) {
		this.full_name = full_name;
	}
	/**
	 * @return the full_name
	 */
	public String getFullName() {
		return full_name;
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
	 * @param api_token the api_token to set
	 */
	public void setAPIToken(String api_token) {
		this.api_token = api_token;
	}
	/**
	 * @return the api_token
	 */
	public String getAPIToken() {
		return api_token;
	}
	/**
	 * @param start_page the start_page to set
	 */
	public void setStartPage(String start_page) {
		this.start_page = start_page;
	}
	/**
	 * @return the start_page
	 */
	public String getStartPage() {
		return start_page;
	}
	/**
	 * @param timezone the timezone to set
	 */
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	/**
	 * @return the timezone
	 */
	public String getTimezone() {
		return timezone;
	}
	/**
	 * @param tz_offset the tz_offset to set
	 */
	public void setTzOffset(String tz_offset) {
		this.tz_offset = tz_offset;
	}
	/**
	 * @return the tz_offset
	 */
	public String getTzOffset() {
		return tz_offset;
	}
	/**
	 * @param time_format the time_format to set
	 */
	public void setTimeFormat(Byte time_format) {
		this.time_format = time_format;
	}
	/**
	 * @return the time_format
	 */
	public Byte getTimeFormat() {
		return time_format;
	}
	/**
	 * @param date_format the date_format to set
	 */
	public void setDateFormat(Byte date_format) {
		this.date_format = date_format;
	}
	/**
	 * @return the date_format
	 */
	public Byte getDateFormat() {
		return date_format;
	}
	/**
	 * @param sort_order the sort_order to set
	 */
	public void setSortOrder(Byte sort_order) {
		this.sort_order = sort_order;
	}
	/**
	 * @return the sort_order
	 */
	public Byte getSortOrder() {
		return sort_order;
	}
	/**
	 * @param twitter the twitter to set
	 */
	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}
	/**
	 * @return the twitter
	 */
	public String getTwitter() {
		return twitter;
	}
	/**
	 * @param jabber the jabber to set
	 */
	public void setJabber(String jabber) {
		this.jabber = jabber;
	}
	/**
	 * @return the jabber
	 */
	public String getJabber() {
		return jabber;
	}
	/**
	 * @param msn the msn to set
	 */
	public void setMSN(String msn) {
		this.msn = msn;
	}
	/**
	 * @return the msn
	 */
	public String getMSN() {
		return msn;
	}
	/**
	 * @param mobile_number the mobile_number to set
	 */
	public void setMobileNumber(String mobile_number) {
		this.mobile_number = mobile_number;
	}
	/**
	 * @return the mobile_number
	 */
	public String getMobileNumber() {
		return mobile_number;
	}
	/**
	 * @param mobile_host the mobile_host to set
	 */
	public void setMobileHost(String mobile_host) {
		this.mobile_host = mobile_host;
	}
	/**
	 * @return the mobile_host
	 */
	public String getMobileHost() {
		return mobile_host;
	}

	private String email;		  // User's e-mail address	someGuy@someDomain.com
	private String full_name;	  // User's full name 		Joe Anderson
	private String id;			  // User's ID				156841
	private String api_token;	  // User's API Token		d18a76ab310947100kc60fe9b3cdc466515bb3a1 -- 40-digit hex
	private String start_page;	  // User's start_page		_info_page
	private String timezone;	  // User's Local Timezone	US/Central
	private String tz_offset;	  // User's Timezone Offset	["-5:00", -5, 0, 1] -- [GMT_STRING, HOURS, MINUTES, IS_DAYLIGHT_SAVINGS_TIME]
	private Byte time_format;	  // User's time format		0 = 13:00 else 1pm
	private Byte date_format;	  // User's date format		0 = DD-MM-YYYY else MM-DD-YYYY
	private Byte sort_order;	  // User's sort order		0 = Oldest dates first else Oldest dates last
	private String twitter;		  // User's twitter account	
	private String jabber;		  // User's jabber account	joe@doe.com
	private String msn;			  // User's msn account		amix_bin@msn.com
	private String mobile_number; // User's mobile number	
	private String mobile_host;	  // User's mobile host
}
