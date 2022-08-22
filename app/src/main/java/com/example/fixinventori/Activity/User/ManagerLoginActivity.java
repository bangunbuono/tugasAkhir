package com.example.fixinventori.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fixinventori.API.APIAccounts;
import com.example.fixinventori.API.ServerConnection;
import com.example.fixinventori.Chat.utils.Constants;
import com.example.fixinventori.ManagerMainActivity;
import com.example.fixinventori.R;
import com.example.fixinventori.model.ResponseModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerLoginActivity extends AppCompatActivity {
    EditText etManagerName, etManagerPassword;
    Button btnLoginManager;
    TextView tvManagerDaftar, tvUserLogin;
    String manager,password;
    UserSession session;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_login);

        Objects.requireNonNull(getSupportActionBar()).hide();

        etManagerName = findViewById(R.id.etManagerName);
        etManagerPassword = findViewById(R.id.etManagerPassword);
        btnLoginManager = findViewById(R.id.btnLoginManager);
        tvManagerDaftar = findViewById(R.id.tvManagerDaftar);
        tvUserLogin = findViewById(R.id.tvUserLogin);
        progressBar = findViewById(R.id.progressRegist);

        tvUserLogin.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        tvManagerDaftar.setOnClickListener(view -> {
            startActivity(new Intent(this, ManagerRegisterActivity.class));
            finish();
        });

        btnLoginManager.setOnClickListener(view -> {
            manager = etManagerName.getText().toString().trim();
            password = etManagerPassword.getText().toString().trim();
            if(manager.isEmpty()||password.isEmpty()){
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
            }else {
                loading(true);
                login();
            }
        });
    }

    private void login() {
        APIAccounts login = ServerConnection.connection().create(APIAccounts.class);
        Call<ResponseModel> loginManager = login.loginManager(manager, password);

        loginManager.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if(response.body()!=null) {
                    String pesan = response.body().getPesan();
                    String status = response.body().getStatus();
                    if(status.equals("true") ){
                        session = new UserSession(ManagerLoginActivity.this);
                        session.createManagerSession(manager);
                        firebaseToken();
                        loading(false);
                        startActivity(new Intent(ManagerLoginActivity.this, ManagerMainActivity.class));
                        finish();
                    }
                    else {
                        loading(false);
                        Toast.makeText(ManagerLoginActivity.this, pesan, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable throwable) {
                Toast.makeText(ManagerLoginActivity.this, "Gagal masuk:" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void firebaseToken(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection(Constants.KEY_COLLECTION_MANAGERS).
                whereEqualTo(Constants.KEY_MANAGER, manager).
                get()
                .addOnCompleteListener(task -> {
                    if(task.isComplete() && task.getResult()!=null &&
                            task.getResult().getDocuments().size() > 0 ){
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        session.putString(Constants.KEY_MANAGER_ID, documentSnapshot.getId());
                        session.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                    }else {
                        loading(false);
                        Toast.makeText(this, "token error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loading(Boolean isLoading){
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }
}