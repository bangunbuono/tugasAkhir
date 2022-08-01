package com.example.fixinventori;

import android.app.Application;

import com.example.fixinventori.model.KomposisiModel;
import com.example.fixinventori.model.UsageMenuModel;

import java.util.ArrayList;

public class UsageAutoApplication extends Application {

    public static ArrayList<UsageMenuModel> orderList;
    public static ArrayList<KomposisiModel> komposisiList;
    public static ArrayList<KomposisiModel> opsiKomposisi;

    @Override
    public void onCreate() {
        super.onCreate();
        orderList = new ArrayList<>();
        komposisiList = new ArrayList<>();
        opsiKomposisi = new ArrayList<>();
    }
}
