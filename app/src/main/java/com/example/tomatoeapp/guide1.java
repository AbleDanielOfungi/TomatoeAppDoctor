package com.example.tomatoeapp;


import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class guide1 extends AppCompatActivity {
    Button nextBtn,skip,exitBtn;
    ProgressBar splashProgress;
    int SPLASH_TIME = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide1);
        skip=findViewById(R.id.predict);


        nextBtn=findViewById(R.id.skipAll);

        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent mySuperIntent = new Intent(guide1.this, guide2.class);
                startActivity(mySuperIntent);



            }
        }, SPLASH_TIME);
    }

    private void playProgress() {
        ObjectAnimator.ofInt(splashProgress, "progress", 100)
                .setDuration(5000)
                .start();
    }


    public void openMainActivity(View view) {
        Intent intent=new Intent(guide1.this, MainActivity.class);
        startActivity(intent);
    }

    public void openguide2(View view) {
        Intent tent=new Intent(guide1.this, guide2.class);
        startActivity(tent);

    }
}


