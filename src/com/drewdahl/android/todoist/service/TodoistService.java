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
			while (TodoistApiHandler.getInstance().getUser() == null) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					continue;
				}
			}
			Long now = Long.valueOf(System.currentTimeMillis());
			/**
			 * TODO Make this configurable?
			 */
			Long expire = now - (2 * 60 * 100000);

			Cursor c = null;
				
			c = getContentResolver().query(Projects.CONTENT_URI, null, Projects.CACHE_TIME + "<=" + expire.toString(), null, null);
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				Integer pid = c.getInt(c.getColumnIndex(Projects._ID));
				Project p = TodoistApiHandler.getInstance().getProject(pid);
				p.save(getContentResolver());
			}
			c.close();

			c = getContentResolver().query(Items.CONTENT_URI, null, Items.CACHE_TIME + "<=" + expire.toString(), null, null);
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				Integer iid = c.getInt(c.getColumnIndex(Projects._ID));
				for (Item i : TodoistApiHandler.getInstance().getItemsById(iid)) {
					i.save(getContentResolver());
				}
			}
			c.close();
			
			/**
			 * TODO Find new items.
			 */
			initialSync();
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
		while (TodoistApiHandler.getInstance().getUser() == null) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				continue;
			}
		}
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
