package com.android.applications.todoist.containers;

import java.util.HashMap;
import java.util.Map;

import org.xmlrpc.android.XMLRPCSerializable;

public class SupportCase implements XMLRPCSerializable {
	private String user_name;
	private String user_email;
	private String problem;
	private String problem_data;
	private String problem_area;
	private String product_version;
	
	public SupportCase()
	{
		initValues();
	}
	
	public SupportCase(String user_name, String user_email, String problem,
			String problem_data, String problem_area, String product_version)
	{
		this.setUserEmail(user_email);
		this.setUserName(user_name);
		this.setProblem(problem);
		this.setProblemArea(problem_area);
		this.setProblemData(problem_data);
		this.setProductVersion(product_version);
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
		map.put("user_name", user_name);
		map.put("user_email", user_email);
		map.put("problem", problem);
		map.put("problem_data", problem_data);
		map.put("problem_area", problem_area);
		map.put("product_version", product_version);
		return map;
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
	
	
}
