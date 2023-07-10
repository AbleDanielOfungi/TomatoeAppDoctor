package com.example.tomatoeapp;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class guide2 extends AppCompatActivity {
    ProgressBar splashProgress;
    int SPLASH_TIME = 10000;
    Button nextBtn,backBtn,skipBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide2);
        nextBtn=findViewById(R.id.next);

        backBtn=findViewById(R.id.back);

        splashProgress = findViewById(R.id.splashProgress);
        playProgress();


        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent mySuperIntent = new Intent(guide2.this, MainActivity.class);
                startActivity(mySuperIntent);



            }
        }, SPLASH_TIME);
    }

    private void playProgress() {
        ObjectAnimator.ofInt(splashProgress, "progress", 100)
                .setDuration(5000)
                .start();
    }

    public void openguide1(View view) {
        Intent able=new Intent(guide2.this, guide1.class);
        startActivity(able);
    }

    public void openMainActivity(View view) {
        Intent dan=new Intent(guide2.this, guide1.class);
        startActivity(dan);

    }
}