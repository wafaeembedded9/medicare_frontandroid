package com.ouafaa.medicare_connect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;

public class StepCounter extends AppCompatActivity implements SensorEventListener {
    private TextView stepCountTextView, distanceTextView, timeTextView, stepCountTargetTextView;
    private Button pauseButton;
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private int stepCount = 0;
    private int lastStepCount = 0;
    private float distanceInKm = 0;
    private ProgressBar progressBar;
    private boolean isPaused = false;
    private long timePaused = 0;
    private float stepLengthInMeters = 0.762f; // This is the average step length for an adult.
    private long startTime;
    private int stepCountTarget = 5000; // Set your step count target here.
    private Handler timeHandler = new Handler();
    private Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int min = seconds / 60;
            seconds = seconds % 60;
            timeTextView.setText(String.format(Locale.getDefault(), "Time : %02d:%02d", min, seconds));
            timeHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        if (stepCounterSensor != null) {
            sensorManager.unregisterListener(this);
            timeHandler.removeCallbacks(timeRunnable);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
            timeHandler.postDelayed(timeRunnable, 0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);
        stepCountTextView = findViewById(R.id.stepCountTextView);
        distanceTextView = findViewById(R.id.distance);
        timeTextView = findViewById(R.id.time);
        pauseButton = findViewById(R.id.Button990);
        stepCountTargetTextView = findViewById(R.id.textView21);
        progressBar = findViewById(R.id.progressBar);

        // Initialisation des valeurs à zéro
        stepCountTextView.setText("Step Count : 0");
        distanceTextView.setText("Distance: 0.00 km");
        timeTextView.setText("Time : 00:00");
        progressBar.setProgress(0);

        startTime = System.currentTimeMillis();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        progressBar.setMax(stepCountTarget);
        stepCountTargetTextView.setText("Step Goal:" + stepCountTarget);
        if (stepCounterSensor == null) {
            stepCount = 0;
            lastStepCount = 0;
            distanceInKm = 0;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (!isPaused) {
                stepCount = (int) sensorEvent.values[0];
                int stepIncrement = stepCount - lastStepCount;

                // Vérifier si le compteur de pas a été incrémenté
                if (stepIncrement > 0) {
                    lastStepCount = stepCount;
                    stepCountTextView.setText("Step Count :" + stepCount);
                    progressBar.incrementProgressBy(stepIncrement);
                    if (stepCount >= stepCountTarget) {
                        stepCountTargetTextView.setText("Stop");
                    }
                    distanceInKm = stepCount * stepLengthInMeters / 1000;
                    distanceTextView.setText(String.format(Locale.getDefault(), "Distance: %.2f km", distanceInKm));
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void onPauseButtonClicked(View view) {
        if (isPaused) {
            isPaused = false;
            pauseButton.setText("Pause");
            startTime = System.currentTimeMillis() - timePaused;
            timeHandler.postDelayed(timeRunnable, 0);
        } else {
            isPaused = true;
            pauseButton.setText("Resume");
            timeHandler.removeCallbacks(timeRunnable);
            timePaused = System.currentTimeMillis() - startTime;
        }
    }
}