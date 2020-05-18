package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Workout3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout3);
        TextView title = (TextView) findViewById(R.id.textView);

        Intent intent = getIntent();
        final String menuName = intent.getExtras().getString("menuName");
        title.setText(menuName);

        Button buttonBack = (Button) findViewById(R.id.BackButton);
        buttonBack.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                finish();
            }
        });

        Button buttonInstruction = (Button) findViewById(R.id.buttonInstruction);
        buttonInstruction.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), WorkoutResultActivity.class);
                intent.putExtra("menuName",menuName.toString());
                startActivity(intent);
            }
        });

        Button buttonStop = (Button) findViewById(R.id.buttonStop);
        buttonStop.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), WorkoutResultActivity.class);
                intent.putExtra("menuName",menuName.toString());
                startActivity(intent);
            }
        });
    }
}
