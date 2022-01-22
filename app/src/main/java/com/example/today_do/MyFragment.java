package com.example.today_do;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Comparator;

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
        for(Task task : tasks){
            Log.e("Debug","MyFragment id: "+task.getId());
            Log.e("Debug","Fragment Current deadline: "+task.getDeadline());
        }

        myListAdapter = new MyListAdapter(mcontext,R.layout.tasks_row,tasks);
        listAdapter = new ArrayAdapter<String>(mcontext,android.R.layout.simple_list_item_1,test);
        test.add("tesdfsd");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment,container,false);
        super.onCreate(savedInstanceState);

        //databaseHelper = new DatabaseHelper(mcontext,topic);
        //myListAdapter = new MyListAdapter(mcontext,R.layout.tasks_row,tasks);

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
        Log.e("Debug","Fragment AddTask Task: "+task.getTask()+" tasks.sice: "+tasks.size());
        myListAdapter.notifyDataSetChanged();
    }
    public void edit_Task(Task task){
        Log.e("Fragment","editTask id: "+task.getId()+" length of Tasks: "+tasks.size());
        for (Task c_task : tasks){
            Log.e("Fragment","curser id: "+c_task.getId());
            if (c_task.getId() == task.getId()){
                //Task d_task = c_task;
                /*
                tasks.remove(c_task);
                tasks.add(task);

                 */
                c_task.setTask(task.getTask());
                c_task.setDetails(task.getDetails());
                c_task.setDeadline(task.getDeadline());
                c_task.setToday(task.isToday());
                break;
            }
        }
        databaseHelper.editTask(task);
        //tasks.sort(Comparator.comparing(Task::getDeadline));
        Log.e("Debug","Fragment AddTask Task: "+task.getTask()+" tasks.sice: "+tasks.size());
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
        Log.e("Error","MyFragment del_Task Task with id: "+id+" not found");
    }

    public void deleteDatabese(){
        databaseHelper.DeleteDatabase();
    }

    public String getTopic(){
        return topic;}

    public void changeToday(Boolean today, int id) {
        for (Task task : tasks){
            if(task.getId() == id){
                task.setToday(today);
                databaseHelper.editTask(task);
            }
        }
    }
}
