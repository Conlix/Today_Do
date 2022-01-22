package com.example.today_do;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyListAdapter extends ArrayAdapter<Task> {
    Context mcontext;
    int mrecource;
    ArrayList<Task> mobjekts;

    final int REQUESTCODE_DELETETASK = 2;
    final int REQUESTCODE_EDITTASK = 4;

    TextView task, details, date, time;
    LinearLayout deadline, preview;
    Switch today;

    public MyListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Task> objects) {
        super(context, resource, objects);
        //Log.e("Debug","ListAdapter Createt length of objekts: "+objects.size());
        this.mcontext = context;
        this.mrecource = resource;
        this.mobjekts = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent){
        LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
        convertView = layoutInflater.inflate(mrecource,parent,false);

        //Log.e("Debug","ListAdapter Task: "+getItem(position).getTask());

        task = convertView.findViewById(R.id.task);
        details = convertView.findViewById(R.id.details);
        deadline = convertView.findViewById(R.id.deadline);
        date = convertView.findViewById(R.id.date);
        time = convertView.findViewById(R.id.time);
        preview = convertView.findViewById(R.id.preview);
        today = convertView.findViewById(R.id.today);

        //Fill View
        task.setText(getItem(position).getTask());
        today.setChecked(getItem(position).isToday());
        //  Cep details at 25 characters
        if (getItem(position).getDetails().length() >= 25){
            details.setText(getItem(position).getDetails().substring(0,25)+"...");
        }else {
            details.setText(getItem(position).getDetails());
        }
        //  Split deadline int to time and date
        double time_date = getItem(position).deadline;
        String sdate = ""+Math.round(time_date/10000);
        String stime = "0"+time_date%10000;
        Log.e("ListAdapter","TimeDate: "+time_date+"stime: "+stime);
        date.setText(sdate.substring(2,4)+"."+sdate.substring(4,6));
        time.setText((int)((time_date%10000-time_date%100)/100)+":"+(int)(time_date%100));

        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("MyListAdapter",today.isChecked()+"");
                ((MainActivity)getContext()).changeToday(today.isChecked(),getItem(position).getId());
            }
        });
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext,Edit_Task.class);
                intent.putExtra("id",mobjekts.get(position).getId());
                intent.putExtra("topic",mobjekts.get(position).getTopic());
                intent.putExtra("task",mobjekts.get(position).getTask());
                intent.putExtra("detalis",""+getItem(position).getDetails());
                //intent.putExtra("creation",mobjekts.get(position).getCreations_time());
                intent.putExtra("deadline",mobjekts.get(position).getDeadline());
                Log.e("Debug","ListAdapter Current Task: "+mobjekts.get(position).getTask()+"Current deadline: "+mobjekts.get(position).getDeadline());

                ((MainActivity)getContext()).startActivityForResult(intent,REQUESTCODE_EDITTASK);
            }
        });
        deadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, DelTask.class);
                intent.putExtra("position",mobjekts.get(position).getTopic());
                intent.putExtra("task",""+task.getText());
                intent.putExtra("id",mobjekts.get(position).getId());
                //Log.e("LisAdapter", mobjekts.get(position).getId()+"");
                ((MainActivity)getContext()).startActivityForResult(intent,REQUESTCODE_DELETETASK);
            }
        });

        return convertView;
    }
}
