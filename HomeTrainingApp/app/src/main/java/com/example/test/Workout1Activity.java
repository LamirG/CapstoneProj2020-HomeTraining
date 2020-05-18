package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Workout1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout1);

        TextView title = (TextView) findViewById(R.id.textView);
        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);

        Intent intent = getIntent();
        final String menuName = intent.getExtras().getString("menuName");
        title.setText(menuName);

        switch (menuName){
            case "상체":
                button1.setText("푸시업");//대흉근, 삼두
                button2.setText("덤벨컬");
                button3.setText("벤치 딥스");
                button4.setText("사이드 레터럴레이즈");
                break;
            case "하체":
                button1.setText("스쿼트");
                button2.setText("런지");
                button3.setText("브릿지");
                button4.setText("사이드 레그레이즈");
                break;
            case "코어":
                button1.setText("플랭크");
                button2.setText("슈퍼맨 랫풀다운");
                button3.setText("바이시클");
                button4.setText("버드독");
                break;
            case "전신":
                button1.setText("팔 벌려 뛰기");
                button2.setText("버피");
                button3.setText("암 워킹");
                button4.setText("마운틴 클라이머");
                break;
        }

        Button buttonBack = (Button) findViewById(R.id.BackButton);
        buttonBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
        button1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), Workout2Activity.class);
                switch (menuName){
                    case "상체":
                        intent.putExtra("menuName","푸시업");
                        break;
                    case "하체":
                        intent.putExtra("menuName","스쿼트");
                        break;
                    case "코어":
                        intent.putExtra("menuName","플랭크");
                        break;
                    case "전신":
                        intent.putExtra("menuName","팔벌려뛰기");
                        break;
                }
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), Workout2Activity.class);
                switch (menuName){
                    case "상체":
                        intent.putExtra("menuName","덤벨컬");
                        break;
                    case "하체":
                        intent.putExtra("menuName","런지");
                        break;
                    case "코어":
                        intent.putExtra("menuName","슈퍼맨 랫풀다운");
                        break;
                    case "전신":
                        intent.putExtra("menuName","버피");
                        break;
                }
                startActivity(intent);
            }
        });
        button3.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), Workout2Activity.class);
                switch (menuName){
                    case "상체":
                        intent.putExtra("menuName","벤치 딥스");
                        break;
                    case "하체":
                        intent.putExtra("menuName","브릿지");
                        break;
                    case "코어":
                        intent.putExtra("menuName","바이시클");
                        break;
                    case "전신":
                        intent.putExtra("menuName","암 워킹");
                        break;
                }
                startActivity(intent);
            }
        });
        button4.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), Workout2Activity.class);
                switch (menuName){
                    case "상체":
                        intent.putExtra("menuName","사이드 레터럴레이즈");
                        break;
                    case "하체":
                        intent.putExtra("menuName","사이드 레그레이즈");
                        break;
                    case "코어":
                        intent.putExtra("menuName","버드독");
                        break;
                    case "전신":
                        intent.putExtra("menuName","마운틴 클라이머");
                        break;
                }
                startActivity(intent);
            }
        });
    }
}
