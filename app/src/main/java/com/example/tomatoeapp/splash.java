package com.example.tomatoeapp;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class splash extends AppCompatActivity {
    ProgressBar splashProgress;
    int SPLASH_TIME = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashProgress = findViewById(R.id.splashProgress);
        playProgress();


        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent mySuperIntent = new Intent(splash.this, guide1.class);
                startActivity(mySuperIntent);



            }
        }, SPLASH_TIME);
    }

    private void playProgress() {
        ObjectAnimator.ofInt(splashProgress, "progress", 100)
                .setDuration(5000)
                .start();
    }
}