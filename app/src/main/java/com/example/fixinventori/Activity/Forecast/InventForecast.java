package com.example.fixinventori.Activity.Forecast;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fixinventori.R;

public class InventForecast extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invent_forecast);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}