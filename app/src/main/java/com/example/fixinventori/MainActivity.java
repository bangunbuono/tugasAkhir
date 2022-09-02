package com.example.fixinventori;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.fixinventori.Activity.User.LoginActivity;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.BottomNavBar.HomeFragment;
import com.example.fixinventori.BottomNavBar.SettingFragment;
import com.example.fixinventori.BottomNavBar.chattFragment;
import com.example.fixinventori.Chat.utils.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView navView;
    FragmentManager fragmentManager;
    Fragment fragment;
    FrameLayout frameLayout;
    String user;
    UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).hide();

        userSession = new UserSession(getApplicationContext());
        if(!userSession.isLoggedIn()) {
            moveToLogin();
            finish();
        }

        if(userSession.getManagerDetail().get("manager")!=null){
            startActivity(new Intent(this, ManagerMainActivity.class));
            finish();

        }
        else if(userSession.getUserDetail().get("username")!=null){
            user = userSession.getUserDetail().get("username");
            getToken();
        }

        frameLayout = findViewById(R.id.flMainActivity);
        navView = findViewById(R.id.navView);

        navView.setItemHorizontalTranslationEnabled(true);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.flMainActivity, new HomeFragment()).commit();

        navView.setOnItemSelectedListener(item -> {
            fragment = null;
            if(item.getItemId()==R.id.homeNav) fragmentManager.beginTransaction()
                    .replace(R.id.flMainActivity, new HomeFragment()).commit();
            if(item.getItemId()==R.id.chatNav) {
                fragmentManager.beginTransaction()
                        .replace(R.id.flMainActivity, new chattFragment()).commit();
            }
            if(item.getItemId()==R.id.settingNav) fragmentManager.beginTransaction()
                    .replace(R.id.flMainActivity, new SettingFragment()).commit();

            return true;
        });


    }

    private void moveToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }

    private void getToken(){
        System.out.println(userSession.getString(Constants.KEY_USER_ID));
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore
                .collection(Constants.KEY_COLLECTION_USERS)
                        .document(userSession.getString(Constants.KEY_USER_ID));

        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnSuccessListener(documentReference1 ->
                        Toast.makeText(this, "updated", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(
                        this, e.getMessage(), Toast.LENGTH_SHORT).show());
        userSession.putString(Constants.KEY_FCM_TOKEN, token);
        System.out.println("main " + token);
    }


}