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


public class Projects {

	// Default Constructor
	public Projects()
	{
		projects = new ArrayList<Project>();
		this.size = 0;
	}
	
	// Return the project in the ArrayList at index
	public Project getProjectsAt(int index)
	{
		if(this.projects.size() > index)
			return this.projects.get(index);
		
		return new Project();
	}
	
	// Add a project to the ArrayList, provided it doesn't exist
	public void addProject(Project newProject)
	{
		if(!this.projects.contains(newProject))
		{
			this.projects.add(newProject);
			this.size++;
		}
	}

	/**
	 * @return the projects
	 */
	public ArrayList<Project> getProjects() {
		return projects;
	}

	/**
	 * @return the size
	 */
	public Integer getSize() {
		return size;
	}

	private ArrayList<Project> projects;
	private Integer size;
}
