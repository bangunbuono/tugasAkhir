package com.example.fixinventori.Activity.Usage;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fixinventori.R;

public class InventUsage extends AppCompatActivity {
    RadioGroup radiogroupUsage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invent_usage);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        radiogroupUsage = findViewById(R.id.radiogroupUsage);

        FragmentManager fmAuto = getSupportFragmentManager();
        FragmentTransaction ftAuto = fmAuto.beginTransaction().replace(R.id.flUsage, new UsageAutoFrag());
        ftAuto.commit();
    }

    public void usageSelector(View v){
        int radioId = radiogroupUsage.getCheckedRadioButtonId();

        switch (radioId){
            case R.id.radio_usage_auto:
                FragmentManager fmAuto = getSupportFragmentManager();
                FragmentTransaction ftAuto = fmAuto.beginTransaction().replace(R.id.flUsage, new UsageAutoFrag());
                ftAuto.commit();
                break;

            case R.id.radio_usage_manual:
                FragmentManager fmManual = getSupportFragmentManager();
                FragmentTransaction ftManual = fmManual.beginTransaction().replace(R.id.flUsage, new UsageManualFrag());
                ftManual.commit();
                break;
        }
    }
}