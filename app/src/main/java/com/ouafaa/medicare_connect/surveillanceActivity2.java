package com.ouafaa.medicare_connect;

import static com.ouafaa.medicare_connect.R.id.imageButton2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class surveillanceActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surveillance2);
        ImageButton imageButton = findViewById(imageButton2);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(surveillanceActivity2.this, HomeActivity2.class);
                startActivity(intent);
            }
        });
    }
}