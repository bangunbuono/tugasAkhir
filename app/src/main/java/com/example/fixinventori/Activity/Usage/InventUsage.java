package com.example.fixinventori.Activity.Usage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fixinventori.R;

import java.util.Objects;

public class InventUsage extends AppCompatActivity {
    RadioGroup radiogroupUsage;
    RadioButton radioCombine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invent_usage);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        radiogroupUsage = findViewById(R.id.radiogroupUsage);
        radioCombine = findViewById(R.id.radioCombine);

        Intent intent = getIntent();
        String fragment = intent.getStringExtra("fragment");
        if(fragment!=null && fragment.equals("combine")) {
            FragmentManager fmCombine = getSupportFragmentManager();
            FragmentTransaction ftCombine = fmCombine.beginTransaction().replace(R.id.flUsage, new CombineUsageFrag());
            ftCombine.commit();
            radioCombine.setChecked(true);
        }else {
            FragmentManager fmAuto = getSupportFragmentManager();
            FragmentTransaction ftAuto = fmAuto.beginTransaction().replace(R.id.flUsage, new UsageAutoFrag());
            ftAuto.commit();
            radioCombine.setChecked(false);
        }
    }

    public void usageSelector(View v){
        int radioId = radiogroupUsage.getCheckedRadioButtonId();

        switch (radioId){
            case R.id.radio_usage_auto:
                FragmentManager fmAuto = getSupportFragmentManager();
                FragmentTransaction ftAuto = fmAuto.beginTransaction().replace(R.id.flUsage, new UsageAutoFrag());
                ftAuto.commit();
                break;

            case R.id.radioCombine:
                FragmentManager fmCombine = getSupportFragmentManager();
                FragmentTransaction ftCombine = fmCombine.beginTransaction().replace(R.id.flUsage, new CombineUsageFrag());
                ftCombine.commit();
                break;

            case R.id.radio_usage_manual:
                FragmentManager fmManual = getSupportFragmentManager();
                FragmentTransaction ftManual = fmManual.beginTransaction().replace(R.id.flUsage, new UsageManualFrag());
                ftManual.commit();
                break;
        }
    }
}