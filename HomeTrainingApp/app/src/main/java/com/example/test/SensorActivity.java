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

import java.sql.Time;
import java.sql.Timestamp;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor mGravity;
    private Sensor mAcceleration;

    TextView textGravity;
    TextView textAcceleration;
    TextView textSpeed;
    private boolean isChanged;
    private float[] speed = new float[3];
    private boolean hasGravimeter = true;
    private float[] gravity = new float[3];
    private float[] linear_acceleration = new float[3];
    //private float[] prevaccel = new float[3];
    //private float[] prevgravity = new float[3];
    private long Timestamp;
    private long prevTimestamp;
    private float[] totalVector = new float[3];
    private double distance;
    private float alpha = 0.25f;
    protected float[] lowpass(float[] input, float[] output){
        if ( output == null ) return input;
        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + alpha * (input[i] - output[i]);
        }
        return output;
    }

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        textGravity = (TextView) findViewById(R.id.textGravity);
        textAcceleration = (TextView) findViewById(R.id.textAcceleration);
        textSpeed = (TextView) findViewById(R.id.textSpeed);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Button button = (Button) findViewById(R.id.BackButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if ((mGravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)) == null) {
            Toast.makeText(getApplicationContext(), "No Gravimeter detected!\nYour device doesn't support this app.", Toast.LENGTH_SHORT);
            finish();
            //hasGravimeter = false;
            // Failure! No Gravimeter.
        }
        if ((mAcceleration = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)) == null) {
            Toast.makeText(getApplicationContext(), "No Accelerometer detected!\nYour device doesn't support this app.", Toast.LENGTH_SHORT);
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
        if(event.sensor.getType() == Sensor.TYPE_GRAVITY){
//            gravity[0] = event.values[0];
//            gravity[1] = event.values[1];
//            gravity[2] = event.values[2];
            //using low-pass filter to get gravity values.
            gravity = lowpass(event.values.clone(), gravity);
            textGravity.setText(
                    "gravityX = " + gravity[0] + "\n" + // avg -0.0011
                    "gravityY = " + gravity[1] + "\n" + // avg  9.80665
                    "gravityZ = " + gravity[2] + "\n" + // avg  0.0011 , on AVD API 29
                    "TYPE = GRAVITY"
            );
        }
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if(Timestamp == 0L) {Timestamp = event.timestamp; prevTimestamp = Timestamp;}
            else {prevTimestamp = Timestamp; Timestamp = event.timestamp;}
            long dt = Timestamp - prevTimestamp;

            /*// Isolate the force of gravity with the low-pass filter. If when the device doesn't have a gravimeter.
            if(!hasGravimeter){
                gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
                gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
                gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];
                textGravity.setText(
                        "gravityX = " + gravity[0] + "\n" +
                        "gravityY = " + gravity[1] + "\n" +
                        "gravityZ = " + gravity[2] + "\n" +
                        "TYPE = ACCELEROMETER"
                );
            }
            // Remove the gravity contribution.
            linear_acceleration[0] = event.values[0] - gravity[0];
            linear_acceleration[1] = event.values[1] - gravity[1];
            linear_acceleration[2] = event.values[2] - gravity[2];
            */

            //use low-pass filter to get raw acceleration values.
            linear_acceleration = lowpass(event.values.clone(), linear_acceleration);
//            linear_acceleration[0] -= gravity[0];
//            linear_acceleration[1] -= gravity[1]; makes strange value! why?
//            linear_acceleration[2] -= gravity[2];
            //now linear_acceleration[] has acceleration value, with gravity contribution removed.

            textAcceleration.setText(
                    "accelX = " + linear_acceleration[0] + "\n" + // avg 0.0
                    "accelY = " + linear_acceleration[1] + "\n" + // avg 9.8099, but when displaying [accel-grav] => it comes -29.xxx. why?
                    "accelZ = " + linear_acceleration[2] + "\n" + // avg 0.0
                    "TYPE = ACCELEROMETER"  + "\n" +
                    "dt = " + dt
            );
            //use timestamp(nanoSecond) to calculate moved distance approximately.
            // speed = (m/s)
            speed[0] += linear_acceleration[0] * dt*0.000000001f;
            speed[1] += linear_acceleration[1] * dt*0.000000001f;
            speed[2] += linear_acceleration[2] * dt*0.000000001f;

            totalVector[0] += speed[0]*dt;
            totalVector[1] += speed[1]*dt;
            totalVector[2] += speed[2]*dt;
            distance = Math.sqrt( (totalVector[0]*totalVector[0])
                                + (totalVector[1]*totalVector[1])
                                + (totalVector[2]*totalVector[2]));
            distance = Math.round(distance*100)/100;
//            for(int i = 0; i<3; i++){
//                speed[i] = Math.round(speed[i]*100000)/100000;
//            }
            textSpeed.setText(
                    "speedX = " + speed[0] + "\n" +
                    "speedY = " + speed[1] + "\n" +
                    "speedZ = " + speed[2] + "\n" +
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
        // But usually said, device does more often than what developer ordered.
        // Thus, it could be said that at least 5sample/sec is guaranteed.
        if(hasGravimeter)sensorManager.registerListener(this, mGravity, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, mAcceleration, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
