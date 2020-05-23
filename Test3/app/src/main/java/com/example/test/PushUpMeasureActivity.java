package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PushUpMeasureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pushupmeasure);

        Button button = (Button) findViewById(R.id.PUMBackButton);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), PushUpActivity.class);
                startActivity(intent);
            }
        });

        button = (Button) findViewById(R.id.PUMStopButton);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), PushUpStateActivity.class);
                startActivity(intent);
            }
        });
    }
}
