package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor mGravity;
    private Sensor mAcceleration;

    TextView textGravity;
    TextView textAcceleration;
    TextView textSpeed;
    //private boolean isChanged;
    private float[] speed = new float[3];
    private boolean hasGravimeter = true;
    private float[] gravity = new float[3];
    private float[] linAccel = new float[3];
    //private float[] prevlinAccel = new float[3];
    private long Timestamp;
    private float[] totalVector = new float[3];

    //    protected float[] lowpass(float[] input, float[] output){
//        if ( output == null ) return input;
//        for ( int i=0; i<input.length; i++ ) {
//            output[i] = output[i] + alpha * (input[i] - output[i]);
//        }
//        return output;
//    }
    protected float[] cut(float[] input, float threshold){
        //floor() from given threshold. Discard if absolute value < 1.5*threshold
        float t = 1/threshold;
        if ( input == null ) return null;
        for ( int i=0; i<input.length; i++ ) {
            input[i] = (float) (Math.floor(input[i]*t)/t);
            if(input[i] < 1.5f * threshold) input[i] = 0.0f;
        }
        return input;
    }

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        textGravity = findViewById(R.id.textGravity);
        textAcceleration = findViewById(R.id.textAcceleration);
        textSpeed = findViewById(R.id.textSpeed);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Button buttonBack = findViewById(R.id.BackButton);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if ((mGravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)) == null) {
            Toast.makeText(getApplicationContext(), "No Gravimeter detected!\nYour device doesn't support this app.", Toast.LENGTH_SHORT).show();
            finish();
            hasGravimeter = false;
            // Failure! No Gravimeter.
        }
        if ((mAcceleration = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)) == null) {
            Toast.makeText(getApplicationContext(), "No Accelerometer detected!\nYour device doesn't support this app.", Toast.LENGTH_SHORT).show();
            finish();
            // Failure! No Accelerometer.
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    //**references**
    //https://developer.android.com/guide/topics/sensors/sensors_motion?hl=ko#sensors-raw-data
    //https://stackoverflow.com/questions/39884057/is-it-possible-to-measure-the-how-far-the-phone-travels-vertically
    //https://www.built.io/blog/applying-low-pass-filter-to-android-sensor-s-readings (for filtering sensor values)
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_GRAVITY && hasGravimeter){
            gravity[0] = event.values[0];
            gravity[1] = event.values[1];
            gravity[2] = event.values[2];
            gravity = cut(gravity, 0.01f);
            textGravity.setText(
                    "gravityX = " + gravity[0] + "\n" +
                    "gravityY = " + gravity[1] + "\n" +
                    "gravityZ = " + gravity[2] + "\n" +
                    "TYPE = GRAVITY"
            );
        }
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long prevTimestamp;
            if(Timestamp == 0L) {
                prevTimestamp = Timestamp = event.timestamp;}
            else {
                prevTimestamp = Timestamp; Timestamp = event.timestamp;}
            //prevlinAccel = linAccel.clone();
            float dt = (Timestamp - prevTimestamp)*0.000000001f;
            dt = Math.round(dt*1000f)/1000f;

            // Isolate the force of gravity with the low-pass filter. If when the device doesn't have a gravimeter.
            if(!hasGravimeter){
                float alpha = 0.25f;
                gravity[0] = (1 - alpha) * gravity[0] + alpha * event.values[0];
                gravity[1] = (1 - alpha) * gravity[1] + alpha * event.values[1];
                gravity[2] = (1 - alpha) * gravity[2] + alpha * event.values[2];
                gravity = cut(gravity, 0.01f);
                textGravity.setText(
                        "gravityX = " + gravity[0] + "\n" +
                        "gravityY = " + gravity[1] + "\n" +
                        "gravityZ = " + gravity[2] + "\n" +
                        "TYPE = ACCELEROMETER"
                );
            }
            // Remove the gravity contribution.
            linAccel[0] = event.values[0] - gravity[0];
            linAccel[1] = event.values[1] - gravity[1];
            linAccel[2] = event.values[2] - gravity[2];
            //now linear_acceleration[] has acceleration value with gravity contribution removed.
            linAccel = cut(linAccel, 0.01f);

            textAcceleration.setText(
                    "accelX = " + linAccel[0] + "\n" +
                    "accelY = " + linAccel[1] + "\n" +
                    "accelZ = " + linAccel[2] + "\n" +
                    "TYPE = ACCELEROMETER"  + "\n" +
                    "dt = " + dt
            );
            //use timestamp(nanoSecond) to calculate moved distance approximately.
            // speed = (m/s)

            // method 1
//            speed[0] += linAccel[0]* dt;
//            speed[1] += linAccel[1]* dt;
//            speed[2] += linAccel[2]* dt;

            //method 2
//            speed[0] = (linAccel[0] + prevlinAccel[0])/2 * dt;
//            speed[1] = (linAccel[1] + prevlinAccel[1])/2 * dt;
//            speed[2] = (linAccel[2] + prevlinAccel[2])/2 * dt;

            //method 3
            speed[0] = linAccel[0] * dt;
            speed[1] = linAccel[1] * dt;
            speed[2] = linAccel[2] * dt;

            speed = cut(speed, 0.01f);
            double velocity = Math.sqrt((speed[0] * speed[0])
                    + (speed[1] * speed[1])
                    + (speed[2] * speed[2]));
            velocity = Math.round(velocity *1000f)/1000f;


            totalVector[0] += speed[0]*dt;
            totalVector[1] += speed[1]*dt;
            totalVector[2] += speed[2]*dt;
            double distance = Math.sqrt((totalVector[0] * totalVector[0])
                    + (totalVector[1] * totalVector[1])
                    + (totalVector[2] * totalVector[2]));
            distance = Math.round(distance *1000f)/1000f;
            textSpeed.setText(
                    "speedX = " + speed[0] + "\n" +
                    "speedY = " + speed[1] + "\n" +
                    "speedZ = " + speed[2] + "\n" +
                    "velocity : " + velocity + "\n" +
                    "distance : " + distance
            );

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    protected void onResume() {
        super.onResume();
        // SENSOR_DELAY_NORMAL == 200,000 micro-sec(200 msec, 0.2sec) -> 5 samples per 1 second.
        // SENSOR_DELAY_GAME == 20,000 micro-sec(20 msec, 0.02sec) -> 50 samples per 1 second.
        // But usually said, device does more often than what developer ordered.
        // Thus, it could be said that at least 5(or 50)sample/sec is guaranteed.
        if(hasGravimeter)sensorManager.registerListener(this, mGravity, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, mAcceleration, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
