package com.example.fixinventori;

import android.app.Application;

import com.example.fixinventori.Chat.Model.ManagerModel;
import com.example.fixinventori.model.KomposisiModel;
import com.example.fixinventori.model.UsageMenuModel;

import java.util.ArrayList;
import java.util.List;

public class UsageAutoApplication extends Application {

    public static ArrayList<UsageMenuModel> orderList;
    public static ArrayList<KomposisiModel> komposisiList;
    public static ArrayList<KomposisiModel> opsiKomposisi;
    public static List<ManagerModel> listConnectedUser;

    @Override
    public void onCreate() {
        super.onCreate();
        orderList = new ArrayList<>();
        komposisiList = new ArrayList<>();
        opsiKomposisi = new ArrayList<>();
        listConnectedUser = new ArrayList<>();
    }
}
