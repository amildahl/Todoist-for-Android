package com.drewdahl.android.todoist.service;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.drewdahl.android.todoist.apihandler.TodoistApiHandler;
import com.drewdahl.android.todoist.models.Item;
import com.drewdahl.android.todoist.models.Project;
import com.drewdahl.android.todoist.provider.TodoistProviderMetaData.Items;
import com.drewdahl.android.todoist.provider.TodoistProviderMetaData.Projects;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.util.Log;

public class TodoistService extends Service {
	@Override
	public void onCreate() {
		super.onCreate();
		initialSync();
	}
	
	class TodoistWorker implements Runnable {
		public void run() {
			while (TodoistApiHandler.getInstance().getUser() == null);
			Long now = Long.valueOf(System.currentTimeMillis());

			Cursor c = null;
				
			c = getContentResolver().query(Projects.CONTENT_URI, null, Projects.CACHE_TIME + "<=" + now.toString(), null, null);
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				Project p = new Project(c, TodoistApiHandler.getInstance().getUser());
				p.save(getContentResolver());
			}
			c.close();

			c = getContentResolver().query(Items.CONTENT_URI, null, Items.CACHE_TIME + "<=" + now, null, null);
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				Item i = new Item(c, TodoistApiHandler.getInstance().getUser());
				i.save(getContentResolver());
			}
			c.close();
		}
	}
	
	private void startScheduledSync() {
		ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(1);
		/**
		 * TODO Make this value configurable.
		 */
		scheduler.scheduleAtFixedRate(new TodoistWorker(), 0, 300, TimeUnit.SECONDS);
	}
	
	private void initialSync() {
		while (TodoistApiHandler.getInstance().getUser() == null);
		Log.d(this.toString(), "Project Count: " + TodoistApiHandler.getInstance().getProjects().size());
		for (Project p : TodoistApiHandler.getInstance().getProjects()) {
			Log.d(this.toString(), "Adding project: " + p.getName());
			p.save(TodoistService.this.getContentResolver());
			for (Item i : TodoistApiHandler.getInstance().getUncompletedItems(p.getId())) {
				Log.d(this.toString(), "Adding item: " + i.getId());
				i.save(TodoistService.this.getContentResolver());
			}
			for (Item i : TodoistApiHandler.getInstance().getCompletedItems(p.getId())) {
				Log.d(this.toString(), "Adding item: " + i.getId());
				i.save(TodoistService.this.getContentResolver());
			}
		}
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		startScheduledSync();
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}
