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

import java.util.HashMap;
import java.util.Map;

import org.xmlrpc.android.XMLRPCSerializable;

import com.android.applications.todoist.views.SupportForm;

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
