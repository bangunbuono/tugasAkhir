package com.example.fixinventori.Activity.Report;

import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fixinventori.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class InventReport extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment fragment;
    FragmentManager manager;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invent_report);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        bottomNavigationView = findViewById(R.id.navReport);
        radioGroup = findViewById(R.id.radioGroupTime);

        bottomNavigationView.setItemHorizontalTranslationEnabled(true);

        manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction().replace(R.id.flFragmentReport, new HistoryFrag());
        transaction.commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            fragment = null;
            int itemId = item.getItemId();
            if (itemId == R.id.historyNav) {
                manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.flFragmentReport, new HistoryFrag()).commit();
            } else if (itemId == R.id.statNav) {
                manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.flFragmentReport, new StatFrag()).commit();
            }else if (itemId == R.id.cashNav) {
                manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.flFragmentReport, new TransactionFragment()).commit();
            }else if(itemId == R.id.bahanNav){
                manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.flFragmentReport, new MaterialFrag()).commit();
            }
            return true;
        });

    }
}