package com.example.today_do;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;

import java.util.Calendar;

public class Edit_Task extends Activity {
    String topic, s_task, s_details;
    double deadline, creration;
    boolean today = false;
    int id;

    TextView task, details, date, time;
    Button abort, add;

    Intent result;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task);

        Intent intent = getIntent();
        topic = intent.getStringExtra("topic");

        //Connect to xml
        task = findViewById(R.id.task);
        details = findViewById(R.id.details);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        abort = findViewById(R.id.abort);
        add = findViewById(R.id.add);

        //If Task already exists
        id = intent.getIntExtra("id",-1);
        if (id != -1){
            s_task = intent.getStringExtra("task");
            s_details = intent.getStringExtra("detalis");
            deadline = intent.getDoubleExtra("deadline",-1);
            today = intent.getBooleanExtra("today",false);
            //Fill xml
            task.setText(s_task);
            details.setText(s_details);
        }else{
            //Create current time & day
            final Calendar cal = Calendar.getInstance();
            double year = cal.get(Calendar.YEAR)-2000;
            double month = cal.get(Calendar.MONTH)+1;
            //if (month == 13){ month = 1;}
            final int day = cal.get(Calendar.DAY_OF_MONTH);

            final Calendar c = Calendar.getInstance();
            final int hour = c.get(Calendar.HOUR_OF_DAY);
            final int minute = c.get(Calendar.MINUTE);

            //Create string of current date & time
            creration = year*100000000 + month*1000000 + day*10000 + hour*100 + minute;
            Log.e("EditTask","Hour: "+hour+" Creation: "+creration);
            if (hour < 19){
                deadline = year*100000000 + month*1000000 + day*10000 + 2000;
            }else{
                deadline = year*100000000 + month*1000000 + (day+1)*10000 + 2000;
            }
        }
        date.setText((int)(deadline / 10000-(int)(deadline / 1000000)*100)+"."+(int)(deadline / 1000000-(int)(deadline / 100000000)*100)+".20"+(int)(deadline / 100000000));
        time.setText((int)(deadline / 100-(int)(deadline / 10000)*100)+":"+(int)(deadline%100));

        final int year = (int)(2000+(int)(deadline / 100000000));
        final int month = (int)(deadline/1000000)-(int)(deadline / 100000000)*100;
        final int day = (int)(deadline/10000)-(int)(deadline/1000000)*100;

        result = new Intent();
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Edit_Task.this,android.R.style.Theme_Holo_Dialog_MinWidth,
                        new DatePickerDialog.OnDateSetListener(){
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                                date.setText(dayOfMonth + "." + (monthOfYear+1) + "." + year);
                                deadline = (double)(year%100)* 100000000 + (double)(monthOfYear+1)*1000000 +(dayOfMonth)*10000 + deadline%10000;
                            }
                        }, year, month-1, day);
                datePickerDialog.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(Edit_Task.this, android.R.style.Theme_Holo_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time.setText(hourOfDay+":"+minute);
                        deadline = (deadline-deadline%10000)+hourOfDay*100+minute;
                    }
                }, (int)(deadline / 100-(int)(deadline / 10000)*100),(int)(deadline%100),false);
                timePickerDialog.show();
            }
        });
        abort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED,result);
                finish();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.putExtra("id",id);
                result.putExtra("Task",""+task.getText());
                result.putExtra("Details",""+details.getText());
                result.putExtra("creation",creration);
                result.putExtra("deadline",deadline);
                result.putExtra("topic",""+topic);
                result.putExtra("today",today);

                setResult(RESULT_OK,result);
                finish();
            }
        });
    }
}
