package com.example.today_do;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class DelTask extends Activity {
    int id;
    String s_task, s_position;
    boolean today;

    TextView task;
    Button abort, delete;

    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.del_task);

        Intent intent = getIntent();
        s_position = intent.getStringExtra("position");
        today = intent.getBooleanExtra("today",false);
        s_task = intent.getStringExtra("task");
        id = intent.getIntExtra("id",-1);
        Log.e("Debug","DelTask restltcode: " + s_position +" : "+id);

        //Connect to xml
        task = findViewById(R.id.task);
        abort = findViewById(R.id.abort);
        delete = findViewById(R.id.delete);

        final Intent result = new Intent();
        //onClickListeners
        abort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED,result);
                finish();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.putExtra("position", s_position);
                result.putExtra("today", today);
                result.putExtra("id",id);
                setResult(RESULT_OK,result);
                finish();
            }
        });
    }
}
