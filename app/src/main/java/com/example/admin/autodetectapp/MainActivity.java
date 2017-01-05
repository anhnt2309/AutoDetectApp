package com.example.admin.autodetectapp;

import android.content.Intent;
import android.support.test.espresso.core.deps.guava.eventbus.EventBus;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;


public class MainActivity extends AppCompatActivity {
    Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView name = (TextView) findViewById(R.id.txt_packageName);
        final TextView time = (TextView) findViewById(R.id.txt_time);



        Button btn_run = (Button) findViewById(R.id.btn_execute);
        Button btn_stop = (Button) findViewById(R.id.btn_stop);

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(serviceIntent);
            }
        });

        btn_run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                serviceIntent = new Intent(MainActivity.this, DetectService.class);
                int Time = Integer.parseInt(time.getText().toString());
                String Name = name.getText().toString();

                if(time.getText().toString().equals("") == false || Name.equals("") == false) {
                    serviceIntent.putExtra("name", Name);
                    serviceIntent.putExtra("time", Time);

                    startService(serviceIntent);
                }
                if(time.getText().toString().equals("") == true || Name.equals("") == true){
                    Toast.makeText(MainActivity.this,"Time or Package name is not inputted!!!",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


}
