package com.example.today_do;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public HashMap<Integer,String> topics = new HashMap<>();
    int highest_id = 0;

    TabLayout tabbar;
    ViewPager viewPager;
    Button settings;
    FloatingActionButton add_task;

    MyPagerAdapter myPagerAdapter;

    final int REQUESTCODE_ADDTASK = 1;
    final int REQUESTCODE_DELETETASK = 2;
    final int REQUESTCODE_SETTINGS = 3;
    final int REQUESTCODE_EDITTASK = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Connect to xml
        tabbar = findViewById(R.id.tabbar);
        viewPager = findViewById(R.id.viewpager);
        settings = findViewById(R.id.setting);
        add_task = findViewById(R.id.add_task);

        //Load setup data
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Map<String, ?> allEntries = sp.getAll();
        boolean today = false;
        for(Map.Entry<String,?> entry : allEntries.entrySet()){
            if (entry.getKey().contains("topic")){
                topics.put(Integer.parseInt(entry.getKey().charAt(entry.getKey().length()-1)+"")+1,entry.getValue()+"");
            }else{
                if (entry.getKey().contains("highest_id")) {
                    highest_id = Integer.parseInt(entry.getValue()+"");
                }
            }
            if (entry.getKey().equals("today")){
                if((""+entry.getValue()).equals("True")){
                    topics.put(0,"Today");
                    today = true;
                }else{
                    today = false;
                }
            }
        }
        //Get if Today ist checked Reorder Topics
        if(!today){
            for(int i = 0; i < topics.size();i++){
                topics.put(i,topics.get(i+1));
            }
            topics.remove(topics.size()-1);
        }

        //Setup PagerAdapter
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),topics,this);
        viewPager.setAdapter(myPagerAdapter);
        tabbar.setupWithViewPager(viewPager);

        //Style the tabbar
        update_tabbar();

        //Onclicklisteners
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                intent.putExtra("lenghtof_topics",topics.size());
                for (int i = 0; i < topics.size();i++){
                    intent.putExtra("topic_"+i,topics.get(i));
                }
                startActivityForResult(intent,REQUESTCODE_SETTINGS);
            }
        });
        add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (topics.size() == 0){
                    Toast.makeText(MainActivity.this,"You need a Topic",Toast.LENGTH_LONG).show();
                }else {
                    Intent intent = new Intent(MainActivity.this, Edit_Task.class);
                    intent.putExtra("topic", topics.get(viewPager.getCurrentItem()));
                    startActivityForResult(intent, REQUESTCODE_ADDTASK);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case REQUESTCODE_ADDTASK:
                if (resultCode == RESULT_OK){
                    String position = data.getStringExtra("topic");
                    if (position == null){ break; }
                    highest_id++;
                    Task task = new Task(highest_id,position,data.getStringExtra("Task"),data.getStringExtra("Details"),data.getDoubleExtra("creation",1000000000),data.getDoubleExtra("deadline",1000000000),data.getBooleanExtra("today",false));
                    myPagerAdapter.add_Task(task,position);
                    save_highest_id();
                }
                break;
            case REQUESTCODE_EDITTASK:
                if (resultCode == RESULT_OK){
                    String position = data.getStringExtra("topic");
                    Boolean today = data.getBooleanExtra("today",false);
                    if (position == null){ break; }
                    Task task = new Task(data.getIntExtra("id",-1),position,data.getStringExtra("Task"),data.getStringExtra("Details"),data.getDoubleExtra("creation",1000000000),data.getDoubleExtra("deadline",1000000000),today);
                    myPagerAdapter.edit_Task(task,position);
                    if (today){
                        myPagerAdapter.edit_Task(task,"Today");
                    }
                }
                break;
            case REQUESTCODE_DELETETASK:
                if (resultCode == RESULT_OK){
                    String position = data.getStringExtra("position");
                    boolean today = data.getBooleanExtra("today",false);
                    int id = data.getIntExtra("id",-1);
                    if (id == -1){break;}
                    myPagerAdapter.delete_Task(id,position,today);
                }
                break;
            case REQUESTCODE_SETTINGS:
                if (resultCode == RESULT_OK){
                    //Delete all Topics from Database and store the new ons
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sp.edit();
                    editor.clear().commit();

                    topics.clear();
                    int leghtof_topics = data.getIntExtra("lenghtof_topics",-1);
                    for(int i = 0; i < leghtof_topics;i++){
                        topics.put(i,data.getStringExtra("topic_"+i));
                        editor.putString("topic_"+i,data.getStringExtra("topic_"+i));
                    }
                    if(data.getBooleanExtra("today",false)){
                        topics.put(topics.size(),"Today");
                        editor.putString("today","True");
                    }else{
                        editor.putString("today","False");
                    }
                    editor.putString("highest_id",highest_id+"");
                    editor.commit();

                    myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),topics,this);
                    myPagerAdapter.notifyDataSetChanged();
                    viewPager.setAdapter(myPagerAdapter);
                    tabbar.setupWithViewPager(viewPager);
                    update_tabbar();

                    //Reload App to Fix Fragment Bug
                    Intent reload = new Intent(MainActivity.this,MainActivity.class);
                    startActivity(reload);
                    finish();
                }
                break;
        }
    }

    //
    private void update_tabbar(){
        int i = 0;
        for (String topic : topics.values()){
            tabbar.getTabAt(i).setText(topic);
            i++;
        }
    }

    private void save_highest_id(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("highest_id",highest_id+"");
        editor.apply();
    }

    public void changeToday(Boolean today, Task task){
        task.setToday(today);
        myPagerAdapter.changeToday(topics.get(tabbar.getSelectedTabPosition()),task);
    }
}
