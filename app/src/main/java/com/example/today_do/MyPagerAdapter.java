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
        //Log.e("Debug","PageAdapter Topics: " +topics.get(0)+" length of topics: "+topics.size());
        mcontext = context;

        //Create Fragments
        fragments.clear();
        for (String topic : mtopics.values()){
            fragments.put(topic,new MyFragment(mcontext,topic));
            Log.e("Debug","PageAdapter Create Fragment: " +topic+" length of fragments: "+fragments.size());
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

    public void delete_Task(int id, String topic){
        fragments.get(topic).del_Task(id);
    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
