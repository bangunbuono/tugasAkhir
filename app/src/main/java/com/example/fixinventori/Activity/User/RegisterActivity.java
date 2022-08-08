package com.example.fixinventori.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText etRegisUser, etRegisPassword, etConfirmPassword;
    Button btnRegis;
    TextView tvMasuk;
    String user, password, confirmPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etRegisUser = findViewById(R.id.etRegisUser);
        etRegisPassword = findViewById(R.id.etRegisPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegis = findViewById(R.id.btnRegis);
        tvMasuk = findViewById(R.id.tvMasuk);

        btnRegis.setOnClickListener(view -> {
            user = etRegisUser.getText().toString().trim();
            password = etRegisPassword.getText().toString().trim();
            confirmPass = etConfirmPassword.getText().toString().trim();

            if(user.isEmpty() || password.isEmpty() || confirmPass.isEmpty()){
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
            }else if (!password.equals(confirmPass)){
                Toast.makeText(this, "Password tidak sesuai", Toast.LENGTH_SHORT).show();
            }else {
                register();
            }
        });

        tvMasuk.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void register(){
        APIAccounts userRegister = ServerConnection.connection().create(APIAccounts.class);
        Call<ResponseModel> register = userRegister.register(user, password);

        register.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if(response.body()!=null){
                    int code = response.body().getCode();
                    String pesan = response.body().getPesan();
                    if (code == 2){
                        Toast.makeText(RegisterActivity.this, "Gagal: "+pesan, Toast.LENGTH_SHORT).show();
                    }else if(code == 0){
                        Toast.makeText(RegisterActivity.this, "Gagal input data", Toast.LENGTH_SHORT).show();
                    }else {
                        registerFirebase();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(RegisterActivity.this, "Gagal daftar: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }

    private void registerFirebase(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String,Object> user = new HashMap<>();
        user.put(Constants.KEY_USER, this.user);
        db.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(RegisterActivity.this, "Berhasil daftar", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                })
                .addOnFailureListener(e -> {
                    deleteAccount();
                    Toast.makeText(this, "token error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void deleteAccount() {
        APIAccounts data = ServerConnection.connection().create(APIAccounts.class);
        Call<ResponseModel> delete = data.deleteUser(user);
        delete.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call,@NonNull Response<ResponseModel> response) {
                Toast.makeText(RegisterActivity.this, "Gagal registrasi: \n " +
                        "coba dengan username lain", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(RegisterActivity.this, "Gagal registrasi: \n " +
                        "coba dengan username lain", Toast.LENGTH_SHORT).show();
            }
        });
    }

}