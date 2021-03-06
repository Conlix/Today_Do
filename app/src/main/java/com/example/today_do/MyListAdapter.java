package com.example.today_do;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

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
        this.mcontext = context;
        this.mrecource = resource;
        this.mobjekts = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent){
        LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
        convertView = layoutInflater.inflate(mrecource,parent,false);

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
        //String stime = "0"+time_date%10000;
        date.setText(sdate.substring(4,6)+"."+sdate.substring(2,4));
        time.setText((int)((time_date%10000-time_date%100)/100)+":"+(int)(time_date%100));

        today.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ((MainActivity)getContext()).changeToday(b,getItem(position));
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
                intent.putExtra("deadline",mobjekts.get(position).getDeadline());
                intent.putExtra("today",mobjekts.get(position).isToday());
                ((MainActivity)getContext()).startActivityForResult(intent,REQUESTCODE_EDITTASK);
            }
        });

        deadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, DelTask.class);
                intent.putExtra("position",mobjekts.get(position).getTopic());
                intent.putExtra("today",mobjekts.get(position).isToday());
                intent.putExtra("task",""+task.getText());
                intent.putExtra("id",mobjekts.get(position).getId());
                ((MainActivity)getContext()).startActivityForResult(intent,REQUESTCODE_DELETETASK);
            }
        });

        return convertView;
    }
}
