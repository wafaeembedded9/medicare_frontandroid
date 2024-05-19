package com.ouafaa.medicare_connect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

public class Menu extends AppCompatActivity {
    private VideoView videoView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ImageView imageView11 = findViewById(R.id.imageView11);
        ImageView imageView15 = findViewById(R.id.imageView15);
        ImageView imageView16 = findViewById(R.id.imageView16);
        TextView textView26 = findViewById(R.id.textView26);
        TextView textView27 = findViewById(R.id.textView27);
        TextView textView28 = findViewById(R.id.textView28);
        videoView1 = findViewById(R.id.videoview1);

        // Initialiser le VideoView
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);
        videoView1.setVideoURI(uri);
        videoView1.start();

        // Définir une boucle pour la vidéo
        videoView1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

        imageView11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, Poids.class);
                startActivity(intent);
            }
        });

        imageView15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, StepCounter.class);
                startActivity(intent);
            }
        });

        imageView16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, QstAnswer.class);
                startActivity(intent);
            }
        });

        textView26.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, Poids.class);
                startActivity(intent);
            }
        });

        textView27.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, StepCounter.class);
                startActivity(intent);
            }
        });

        textView28.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, QstAnswer.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView1.resume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        videoView1.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView1.suspend();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView1 != null) {
            videoView1.stopPlayback();
        }
    }
}
