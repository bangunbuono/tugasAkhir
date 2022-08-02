package com.example.fixinventori.Activity.User;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashMap;

public class UserSession {
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String IS_LOGGED_IN = "isLoggedIn";
    private static final String USER = "username";
    private static final String MANAGER = "manager";

    public UserSession(Context context){
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    public void createSession(String user){
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(USER, user);
        editor.commit();
    }
    public void createManagerSession(String manager){
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(MANAGER, manager);
        editor.commit();
    }

    public HashMap<String,String> getUserDetail(){
        HashMap<String,String> user = new HashMap<>();
        user.put(USER, sharedPreferences.getString(USER, null));
        return user;
    }
    public HashMap<String,String> getManagerDetail(){
        HashMap<String,String> manager = new HashMap<>();
        manager.put(MANAGER, sharedPreferences.getString(MANAGER, null));
        return manager;
    }

    public void putString(String key, String value){
        editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key){
        return sharedPreferences.getString(key,null);
    }

    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    public void logOutSession(){
        editor.clear();
        editor.apply();
        editor.commit();

    }
}
