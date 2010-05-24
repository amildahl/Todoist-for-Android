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
