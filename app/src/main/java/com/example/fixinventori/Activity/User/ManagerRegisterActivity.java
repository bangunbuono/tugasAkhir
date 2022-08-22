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
import com.example.fixinventori.R;
import com.example.fixinventori.model.ResponseModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerRegisterActivity extends AppCompatActivity {
    EditText etRegisManager, etRegisManagerPassword, etConfirmManagerPassword;
    TextView tvMasukManager;
    Button btnRegisManager;
    String manager, password, confirmPassword;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_register);

        Objects.requireNonNull(getSupportActionBar()).hide();

        etRegisManager = findViewById(R.id.etRegisManager);
        etRegisManagerPassword = findViewById(R.id.etRegisManagerPassword);
        etConfirmManagerPassword = findViewById(R.id.etConfirmManagerPassword);
        tvMasukManager = findViewById(R.id.tvMasukManager);
        btnRegisManager = findViewById(R.id.btnRegisManager);
        progressBar = findViewById(R.id.progressRegist);

        btnRegisManager.setOnClickListener(view -> {
            manager = etRegisManager.getText().toString().trim();
            password = etRegisManagerPassword.getText().toString().trim();
            confirmPassword = etConfirmManagerPassword.getText().toString().trim();

            if(manager.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
                Toast.makeText(this, "Isi semua field", Toast.LENGTH_SHORT).show();
            }else if(!password.equals(confirmPassword)){
                Toast.makeText(this, "Password tidak sesuai", Toast.LENGTH_SHORT).show();
            }else {
                loading(true);
                register();
            }
        });

        tvMasukManager.setOnClickListener(view -> {
            startActivity(new Intent(this, ManagerLoginActivity.class));
            finish();
        });
    }

    private void register() {
        APIAccounts regist = ServerConnection.connection().create(APIAccounts.class);
        Call<ResponseModel> registManager = regist.registerManager(manager,password);

        registManager.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if(response.body()!=null){
                    String pesan = response.body().getPesan();
                    int code = response.body().getCode();
                    if(code==1){
                        registerFirebase();
                    }else {
                        loading(false);
                        Toast.makeText(ManagerRegisterActivity.this, pesan, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable throwable) {
                loading(false);
                Toast.makeText(ManagerRegisterActivity.this, "Gagal daftar: "+ throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerFirebase(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> manager = new HashMap<>();
        manager.put(Constants.KEY_MANAGER, this.manager);
        db.collection(Constants.KEY_COLLECTION_MANAGERS)
                .add(manager)
                .addOnSuccessListener(documentReference -> {
                    loading(false);
                    startActivity(new Intent(ManagerRegisterActivity.this, ManagerLoginActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> deleteManager());

    }
    private void deleteManager(){
        APIAccounts data = ServerConnection.connection().create(APIAccounts.class);
        Call<ResponseModel> delete = data.deleteManager(manager);

        delete.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call,@NonNull Response<ResponseModel> response) {
                loading(false);
                Toast.makeText(ManagerRegisterActivity.this, "Gagal registrasi: \n " +
                        "coba dengan nama lain", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                loading(false);
                Toast.makeText(ManagerRegisterActivity.this, "Gagal registrasi: \n " +
                        "coba dengan nama lain", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loading(Boolean isLoading){
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            btnRegisManager.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            btnRegisManager.setVisibility(View.VISIBLE);
        }
    }
}