package com.android.applications.todoist;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TasksList extends Activity {
	private ProgressDialog m_ProgressDialog = null;
    private ArrayList<Project> projectArray = null;
    private ItemAdapter adapter;
    private Runnable viewProjects;
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        /*
        projectArray = new ArrayList<Project>();
        this.adapter = new ItemAdapter(this, R.layout.row, projectArray);
        setListAdapter(this.adapter);
       
        viewProjects = new Runnable(){
            @Override
            public void run() {
                getItems();
            }
        };
        Thread thread =  new Thread(null, viewProjects, "MagentoBackground");
        thread.start();
        m_ProgressDialog = ProgressDialog.show(TasksList.this,    
              "Please wait...", "Retrieving data ...", true);*/
    }
    /*private Runnable returnRes = new Runnable() {

        @Override
        public void run() {
            if(projectArray != null && projectArray.size() > 0){
                adapter.notifyDataSetChanged();
                for(int i=0;i<projectArray.size();i++)
                adapter.add(projectArray.get(i));
            }
            m_ProgressDialog.dismiss();
            adapter.notifyDataSetChanged();
        }
    };
    private void getItems(){

    		TodoistAPIHandler handler = new TodoistAPIHandler("TOKEN");
        	//Projects projects = handler.getProjects();
        	projectArray = new ArrayList<Project>();
        	User user = handler.login("user", "pass");
        	Project project = new Project();
        	project.setName(user.getAPIToken());
        	projectArray.add(project);
        	/*for(int i = 0; i < projects.getSize(); i++)
        	{
	        	projectArray.add(projects.getProjectsAt(i));
        	}/
            runOnUiThread(returnRes);
        }*/
    private class ItemAdapter extends ArrayAdapter<Project> {

        private ArrayList<Project> projects;

        public ItemAdapter(Context context, int textViewResourceId, ArrayList<Project> items) {
                super(context, textViewResourceId, items);
                this.projects = items;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.row, null);
                }
                Project project = projects.get(position);
                if (project != null) {
                        TextView tt = (TextView) v.findViewById(R.id.TextView01);
                        TextView bt = (TextView) v.findViewById(R.id.TextView02);
                        if (tt != null) {
                              tt.setText(project.getName());                            }
                        if(bt != null){
                              bt.setText(project.getID());
                        }
                }
                return v;
        }
}
}