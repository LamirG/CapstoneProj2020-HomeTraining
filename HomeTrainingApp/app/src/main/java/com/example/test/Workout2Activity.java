package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Workout2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout2);

        TextView title = (TextView) findViewById(R.id.textView);
        Button buttonStart = (Button) findViewById(R.id.buttonStart);
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

        buttonStart.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), Workout3Activity.class);
                intent.putExtra("menuName",menuName.toString());
                startActivity(intent);
            }
        });
    }
}
