package com.drewdahl.android.todoist.service;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.drewdahl.android.todoist.apihandler.TodoistApiHandler;
import com.drewdahl.android.todoist.models.Item;
import com.drewdahl.android.todoist.models.Project;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class TodoistService extends Service {
	@Override
	public void onCreate() {
		super.onCreate();
		startSync();
	}
	
	class TodoistWorker implements Runnable {
		public void run() {
			/**
			 * TODO Make this check the cache so it only grabs items to update that it should be.
			 */
			if (TodoistApiHandler.getInstance().getUser() != null) {
				for (Project p : TodoistApiHandler.getInstance().getProjects()) {
					p.save(TodoistService.this.getContentResolver());
					for (Item i : TodoistApiHandler.getInstance().getUncompletedItems(p.getId())) {
						i.save(TodoistService.this.getContentResolver());
					}
				}
			}
		}
	}
	
	private void startSync() {
		ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(1);
		scheduler.scheduleAtFixedRate(new TodoistWorker(), 0, 360, TimeUnit.SECONDS);
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}
