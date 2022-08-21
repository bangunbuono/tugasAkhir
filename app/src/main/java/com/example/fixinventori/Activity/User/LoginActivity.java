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
import com.example.fixinventori.MainActivity;
import com.example.fixinventori.R;
import com.example.fixinventori.model.ResponseModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText etUsername, etPassword;
    TextView tvDaftar, tvLoginManager;
    String user, password, status;
    Button btnLogin;
    UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvDaftar = findViewById(R.id.tvDaftar);
        tvLoginManager = findViewById(R.id.tvManagerLogin);

        btnLogin.setOnClickListener(view -> {
            if(etUsername.getText().toString().trim().isEmpty() ||
                    etPassword.getText().toString().trim().isEmpty()){
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
            }
            else {
                user = etUsername.getText().toString().trim();
                password = etPassword.getText().toString().trim();

                login();
            }
        });

        tvDaftar.setOnClickListener(view ->
                startActivity(new Intent(this, RegisterActivity.class)));

        tvLoginManager.setOnClickListener(view -> {
            startActivity(new Intent(this, ManagerLoginActivity.class));
            finish();
        });
    }

    private void login() {
        APIAccounts data = ServerConnection.connection().create(APIAccounts.class);
        Call<ResponseModel> loginUser = data.login(user, password);

        loginUser.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if (response.body() != null) {
                    String pesan = response.body().getPesan();
                    status = response.body().getStatus();
                    if (status.equals("true")) {
                        userSession = new UserSession(getApplicationContext());
                        userSession.createSession(user);
                        firebaseToken();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, pesan, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(LoginActivity.this, "Gagal masuk " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void firebaseToken(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection(Constants.KEY_COLLECTION_USERS).
                whereEqualTo(Constants.KEY_USER, user).
                get()
                .addOnCompleteListener(task -> {
                    if(task.isComplete() && task.getResult()!=null &&
                            task.getResult().getDocuments().size() > 0 ){
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        userSession.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        userSession.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                    }else {
                        Toast.makeText(this, "token error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}