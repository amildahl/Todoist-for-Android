package com.android.applications.todoist;

import java.util.ArrayList;

public class Tasks {

	public Tasks()
	{
		this.setTasks(new ArrayList<Task>());
	}
	
	public Tasks(ArrayList<Task> tasks)
	{
		this.setTasks(tasks);
	}
	
	/**
	 * @param tasks the tasks to set
	 */
	public void setTasks(ArrayList<Task> tasks) {
		this.tasks = tasks;
	}

	/**
	 * @return the tasks
	 */
	public ArrayList<Task> getTasks() {
		return tasks;
	}

	private ArrayList<Task> tasks;
}