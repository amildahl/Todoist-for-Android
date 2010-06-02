package com.android.applications.todoist.views;

import java.util.HashMap;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class Main extends Activity {

	protected HashMap<Integer, ResultCallbackIF> _callbackMap = new HashMap<Integer, ResultCallbackIF>();
	private String token;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		token="";
		createLogin();
	}
	
	private void createLogin()
	{
		this.launchActivity(LoginPage.class, new Main.ResultCallbackIF() {
			
			@Override
			public void resultOk(Intent data) 
			{
				Bundle extras = data.getExtras();
				if(extras != null)
		        {
					storeToken(extras.getString("token"));
		        	Log.e("Login-resultOk():",token);
		        	createTasksList();
		        }
		        else
		        {
		        	Log.e("Login-resultOk():","No Token!!!");
		        }
			}
			
			@Override
			public void resultCancel(Intent data) 
			{
				// TODO Auto-generated method stub
				Log.e("Login-resultCancel:", "Login Canceled!");
			}
		});
	}

	private void createTasksList()
	{
		createTasksList("");
	}
	
	private void createTasksList(String query)
	{
		Bundle extras = new Bundle();
		extras.putString("query", query);
		extras.putString("token", token);
		this.launchActivity(TasksList.class, new Main.ResultCallbackIF() 
		{
			
			@Override
			public void resultOk(Intent data) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void resultCancel(Intent data) {
				// TODO Auto-generated method stub
				
			}
		}, extras);
	}
	
	private void createProjectsList(String project_id)
	{
		if(project_id != "")
		{
			
		}
		else
		{
			
		}
	}
	
	private void storeToken(String token)
	{
		// TODO Store Token
		this.token = token; 
	}
	
	@SuppressWarnings("unchecked")
	public void launchActivity(Class subActivityClass, ResultCallbackIF callback, Bundle bundle) 
	{
		  int correlationId = new Random().nextInt();
		  _callbackMap.put(correlationId, callback);
		  startActivityForResult(new Intent(this, subActivityClass).putExtras(bundle), correlationId);
	}
	
	@SuppressWarnings("unchecked")
	public void launchActivity(Class subActivityClass, ResultCallbackIF callback) 
	{
		launchActivity(subActivityClass, callback, new Bundle());
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data); 
		try 
		{
			ResultCallbackIF callback = _callbackMap.get(requestCode);

			switch (resultCode) 
			{
			case Activity.RESULT_CANCELED:
				callback.resultCancel(data);
				_callbackMap.remove(requestCode);
				break;
			case Activity.RESULT_OK:
				callback.resultOk(data);
				_callbackMap.remove(requestCode);
				break;
			default:
				Log.e("Error:","requestCode not found in hash");
			}
		}
		catch (Exception e) 
		{
			Log.e("ERROR:","Issue processing Activity", e);
		}
	}

	public static interface ResultCallbackIF 
	{
	  public void resultOk(Intent data);
	  public void resultCancel(Intent data);

	}

}
