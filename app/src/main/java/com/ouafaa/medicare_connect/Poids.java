package com.ouafaa.medicare_connect;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;


import com.ouafaa.medicare_connect.databinding.ActivityPoidsBinding;

public class Poids extends AppCompatActivity {

    private ActivityPoidsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPoidsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.weightPicker.setMinValue(30);
        binding.weightPicker.setMaxValue(150);

        binding.heightPicker.setMinValue(100);
        binding.heightPicker.setMaxValue(250);

        binding.weightPicker.setOnValueChangedListener((picker, oldVal, newVal) -> calculateBMI());
        binding.heightPicker.setOnValueChangedListener((picker, oldVal, newVal) -> calculateBMI());
    }

    private void calculateBMI() {
        int height = binding.heightPicker.getValue();
        double doubleHeight = (double) height / 100;

        int weight = binding.weightPicker.getValue();

        double bmi = weight / (doubleHeight * doubleHeight);

        binding.resultsTV.setText(String.format("Votre BMI est: %.2f", bmi));
        binding.healthyTV.setText(String.format("Considéré: %s", healthyMessage(bmi)));
    }

    private String healthyMessage(double bmi) {
        if (bmi < 18.5)
            return "Maigreur";
        if (bmi < 25.0)
            return "Normal";
        if (bmi < 30.0)
            return "Surpoids";
        return "Obésité";
    }
}