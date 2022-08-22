package com.example.fixinventori;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fixinventori.API.APIAccounts;
import com.example.fixinventori.API.ServerConnection;
import com.example.fixinventori.Activity.User.ManagerLoginActivity;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.BottomNavBar.ManagerChatFragment;
import com.example.fixinventori.BottomNavBar.ManagerHomeFragment;
import com.example.fixinventori.BottomNavBar.ManagerSettingsFragment;
import com.example.fixinventori.Chat.utils.Constants;
import com.example.fixinventori.model.ResponseModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerMainActivity extends AppCompatActivity {
    UserSession userSession;
    String manager;
    FrameLayout flManagerMainLayout;
    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_main);

        Objects.requireNonNull(getSupportActionBar()).hide();

        userSession = new UserSession(this);
        if(!userSession.isLoggedIn()) moveToLogin();
        else manager=userSession.getManagerDetail().get("manager");
        System.out.println(manager);

        flManagerMainLayout = findViewById(R.id.flManagerMainLayout);
        bottomNavigationView = findViewById(R.id.navView);

        bottomNavigationView.setItemHorizontalTranslationEnabled(true);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction().
                replace(R.id.flManagerMainLayout, new ManagerHomeFragment());
        transaction.commit();

        getConnecteduser();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            fragment = null;
                if(item.getItemId()== R.id.managerHomeNav) fragmentManager.beginTransaction().
                        replace(R.id.flManagerMainLayout, new ManagerHomeFragment()).commit();
                if(item.getItemId()== R.id.managerChatNav) {
                    fragmentManager.beginTransaction().
                            replace(R.id.flManagerMainLayout, new ManagerChatFragment()).commit();
                    getToken();
                }
                if(item.getItemId()== R.id.managerSettingNav) fragmentManager.beginTransaction().
                        replace(R.id.flManagerMainLayout, new ManagerSettingsFragment()).commit();
            return true;
        });

    }

    private void moveToLogin() {
        Intent intent = new Intent(this, ManagerLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }

    private void getToken(){
        System.out.println(userSession.getString(Constants.KEY_MANAGER_ID));
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore
                .collection(Constants.KEY_COLLECTION_MANAGERS)
                .document(userSession.getString(Constants.KEY_MANAGER_ID));

        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnSuccessListener(documentReference1 ->
                        Toast.makeText(this, "updated", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(
                        this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void getConnecteduser(){
        APIAccounts data = ServerConnection.connection().create(APIAccounts.class);
        Call<ResponseModel> getData = data.getUser(manager);

        getData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                UsageAutoApplication.listConnectedUser = new ArrayList<>();
                if (response.body() != null) {
                    UsageAutoApplication.listConnectedUser = response.body().getRecordManager();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {

            }
        });
    }
}