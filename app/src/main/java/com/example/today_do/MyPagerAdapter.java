package com.example.today_do;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class MyPagerAdapter extends FragmentPagerAdapter {
    Context mcontext;

    HashMap<String,MyFragment> fragments = new HashMap<>();
    HashMap<Integer, String> mtopics = new HashMap<>();

    public MyPagerAdapter(FragmentManager fm, HashMap<Integer, String> topics, Context context) {
        super(fm);
        this.mtopics = topics;
        mcontext = context;

        //Create Fragments
        fragments.clear();
        for (String topic : mtopics.values()){
            fragments.put(topic,new MyFragment(mcontext,topic));
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(mtopics.get(position));
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void add_Task(Task task, String topic){
        fragments.get(topic).add_Task(task);
    }
    public void edit_Task(Task task, String topic){
        fragments.get(topic).edit_Task(task);
    }

    public void delete_Task(int id, String topic,Boolean today){
        fragments.get(topic).del_Task(id);
        if (today){
            //Delete Task in original Fragment
            fragments.get("Today").del_Task(id);
        }
    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void changeToday(String topic, Task task) {
        //Change the today Value
        fragments.get(topic).edit_Task(task);
        //Add or Remove the Task to Today Tab and remove today Value on original Tab
        if(mtopics.get(0)=="Today"){
            if(topic == "Today"){
                fragments.get("Today").del_Task(task.getId());
                task.setToday(false);
                fragments.get(task.getTopic()).edit_Task(task);
            }else{
                if(task.isToday()){
                    fragments.get("Today").add_Task(task);
                }else {
                    fragments.get("Today").del_Task(task.getId());
                }
            }
        }
    }
}
