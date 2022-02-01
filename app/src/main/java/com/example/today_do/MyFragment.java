package com.example.today_do;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class MyFragment extends Fragment {
    Context mcontext;
    String topic;
    ArrayList<Task> tasks = new ArrayList<>();
    ArrayList<String> test = new ArrayList<>();

    TextView debug;
    ListView listView;

    MyListAdapter myListAdapter;
    DatabaseHelper databaseHelper;

    ArrayAdapter listAdapter;

    public MyFragment(Context context, String topic){
        mcontext = context;
        this.topic = topic;
        databaseHelper = new DatabaseHelper(mcontext,topic);
        //Load data from SQLite
        tasks.clear();
        tasks = databaseHelper.getAllTasks();

        myListAdapter = new MyListAdapter(mcontext,R.layout.tasks_row,tasks);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment,container,false);
        super.onCreate(savedInstanceState);

        //Connect to xml
        debug = view.findViewById(R.id.debug);
        debug.setText(topic);
        listView = view.findViewById(R.id.tasks);
        listView.setAdapter(myListAdapter);
        myListAdapter.notifyDataSetChanged();

        return view;
    }

    //Add Task
    public void add_Task(Task task){
        tasks.add(task);
        databaseHelper.addTask(task);
        myListAdapter.notifyDataSetChanged();
    }

    public void edit_Task(Task task){
        for (Task c_task : tasks){
            if (c_task.getId() == task.getId()){
                c_task.setTask(task.getTask());
                c_task.setDetails(task.getDetails());
                c_task.setDeadline(task.getDeadline());
                c_task.setToday(task.isToday());
                break;
            }
        }
        databaseHelper.editTask(task);
        myListAdapter.notifyDataSetChanged();
    }

    //Delete Task
    public void del_Task(int id){
        for (int i = 0 ; i < tasks.size(); i++){
            if(tasks.get(i).getId() == id){
                tasks.remove(i);
                databaseHelper.deliteOne(id);
                myListAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    //For removing the Database
    //public void deleteDatabese(){ databaseHelper.DeleteDatabase(); }

    public String getTopic(){
        return topic;
    }

    public void changeToday(Boolean today, int id) {
        for (Task task : tasks){
            if(task.getId() == id){
                task.setToday(today);
                databaseHelper.editTask(task);
            }
        }
    }
}
