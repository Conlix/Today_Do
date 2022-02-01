package com.example.today_do;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Settings extends Activity {
    ListView listView_topics;
    Button b_add_topic, b_return,b_cancel;
    Switch s_today;

    ArrayList<String> topics = new ArrayList<>();

    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        listView_topics = findViewById(R.id.listview_topics);
        b_add_topic = findViewById(R.id.b_add_topic);
        b_return = findViewById(R.id.b_return);
        b_cancel = findViewById(R.id.b_cancel);
        s_today = findViewById(R.id.today);


        arrayAdapter = new ArrayAdapter<String>(Settings.this,android.R.layout.simple_list_item_1,topics){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);
                return view;
            }
        };

        listView_topics.setAdapter(arrayAdapter);

        Intent data = getIntent();
        topics.clear();
        int leghtof_topics = data.getIntExtra("lenghtof_topics",0);
        for(int i = 0; i < leghtof_topics;i++){
            if((data.getStringExtra("topic_"+i)+"").equals("Today")){
                s_today.setChecked(true);
                continue;
            }
            topics.add(data.getStringExtra("topic_"+i)+"");
        }

        listView_topics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                topics.remove(position);
                arrayAdapter.notifyDataSetChanged();
            }
        });

        b_add_topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if Already Exists
                Intent intent = new Intent(Settings.this, Add_Topic.class);
                //intent.putExtra("topic","");
                startActivityForResult(intent,3);
            }
        });

        b_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent();
                result.putExtra("lenghtof_topics",Integer.parseInt(topics.size()+""));
                for (int i = 0; i < topics.size();i++){
                    result.putExtra("topic_"+i,topics.get(i)+"");
                }
                result.putExtra("today",s_today.isChecked());
                setResult(RESULT_OK,result);
                finish();
            }
        });
        b_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent();
                setResult(RESULT_CANCELED,result);
                finish();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 3 && resultCode == RESULT_OK){
            String new_Task = data.getStringExtra("topic");

            if(new_Task.equals("Today")){
                Toast.makeText(Settings.this,"Today is a System own Topic",Toast.LENGTH_LONG).show();
                return;
            }
            for(String topic : topics){
                if (topic.equals(new_Task)){
                    Toast.makeText(Settings.this,"You already have this Topic",Toast.LENGTH_LONG).show();
                    return;
                }
            }

            topics.add(new_Task);
            arrayAdapter.notifyDataSetChanged();
        }
    }
}
