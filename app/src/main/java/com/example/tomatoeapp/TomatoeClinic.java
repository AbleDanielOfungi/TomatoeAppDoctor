package com.example.tomatoeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class TomatoeClinic extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomatoe_clinic);
    }
    public void openMainActivity(View view) {

        Intent intent=new Intent(TomatoeClinic.this, MainActivity.class);
        startActivity(intent);
    }
}
