package com.android.applications.todoist.containers;

import java.util.HashMap;
import java.util.Map;

import org.xmlrpc.android.XMLRPCSerializable;

import com.android.applications.todoist.SupportForm;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

public class SupportCase implements XMLRPCSerializable {
	private String user_name;
	private String user_email;
	private String problem;
	private String problem_data;
	private String problem_area;
	private String product_version;
	private String product_name;
	
	public SupportCase()
	{
		initValues();
	}
	
	public SupportCase(String user_name, String user_email, String problem,
			String problem_data, String problem_area, String product_name, String product_version)
	{
		this.setUserEmail(user_email);
		this.setUserName(user_name);
		this.setProblem(problem);
		this.setProblemArea(problem_area);
		this.setProblemData(problem_data);
		this.setProductVersion(product_version);
		this.setProductName(product_name);
	}
	
	private void initValues()
	{
		this.setUserEmail("");
		this.setUserName("");
		this.setProblem("");
		this.setProblemArea("");
		this.setProblemData("");
		this.setProductVersion("");
	}
	
	public Object getSerializable() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("user_name", cleanString(user_name));
		map.put("user_email", cleanString(user_email));
		map.put("problem", problem);
		map.put("problem_data", problem_data);
		map.put("problem_area", problem_area);
		map.put("product_version", cleanString(product_version));
		map.put("product_name", cleanString(product_name));
		return map;
	}
	
	private String cleanString(String $str)
	{
		return $str.replace("\n", "").replace("\r", "").replace("\t", "");
	}
	
	public boolean callRPC(String uri)
	{
		
		return true;
	}

	public void setUserName(String user_name) {
		this.user_name = user_name;
	}

	public String getUserName() {
		return this.user_name;
	}

	public void setUserEmail(String user_email) {
		this.user_email = user_email;
	}

	public String getUserEmail() {
		return user_email;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	public String getProblem() {
		return problem;
	}

	public void setProblemData(String problem_data) {
		this.problem_data = problem_data;
	}

	public String getProblemData() {
		return problem_data;
	}

	public void setProblemArea(String problem_area) {
		this.problem_area = problem_area;
	}

	public String getProblemArea() {
		return problem_area;
	}

	public void setProductVersion(String product_version) {
		this.product_version = product_version;
	}

	public String getProductVersion() {
		return product_version;
	}

	public void setProductName(String product_name) {
		this.product_name = product_name;
	}

	public String getProductName() {
		return product_name;
	}
	
	
}
