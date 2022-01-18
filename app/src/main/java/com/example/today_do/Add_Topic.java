package com.example.today_do;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class Add_Topic extends Activity {
    EditText topic;
    Button abort, add;

    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_fragment);

        //Connect to xml
        topic = findViewById(R.id.topic);
        abort = findViewById(R.id.abort);
        add = findViewById(R.id.add);


        //Set onClickListeners
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((topic.getText()+"").length() <= 0){
                    Toast.makeText(Add_Topic.this,"You can not add an empty Topic",Toast.LENGTH_LONG).show();
                    return;
                }
                final Intent result = new Intent();
                result.putExtra("topic",topic.getText()+"");
                setResult(RESULT_OK,result);
                finish();

            }
        });
        abort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

    }
}
