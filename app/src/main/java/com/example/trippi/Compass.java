package com.example.trippi;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class Compass extends AppCompatActivity implements SensorEventListener {
    ImageView compassImageView;
    float currentDegree, rotateDegree;
    SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        currentDegree = 0f;
        compassImageView = findViewById(R.id.compassImageView);
        compassImageView.setImageResource(R.drawable.compass);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        rotateDegree = Math.round(event.values[0]);
        rotateCompass();
    }

    private void rotateCompass() {
        RotateAnimation compassRotateAnimation = new RotateAnimation(currentDegree, -rotateDegree,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        compassRotateAnimation.setDuration(210);
        compassRotateAnimation.setFillAfter(true);
        compassImageView.startAnimation(compassRotateAnimation);
        currentDegree = -rotateDegree;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}