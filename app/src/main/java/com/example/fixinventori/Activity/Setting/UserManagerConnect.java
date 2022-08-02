package com.example.fixinventori.Activity.Setting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fixinventori.API.APIAccounts;
import com.example.fixinventori.API.ServerConnection;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.R;
import com.example.fixinventori.model.ResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserManagerConnect extends AppCompatActivity {
    EditText etInputUsername, etInputUserToken;
    Button btnConnect;
    String usename, managerName;
    int token;
    UserSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manager_connect);

        session = new UserSession(this);
        managerName = session.getString("manager");
        
        etInputUsername = findViewById(R.id.etInputUsername);
        etInputUserToken = findViewById(R.id.etInputUserToken);
        btnConnect = findViewById(R.id.btnConnect);
        
        btnConnect.setOnClickListener(view -> {
            usename = etInputUsername.getText().toString().trim();
            token = Integer.parseInt(etInputUserToken.getText().toString().trim());
            if(!usename.isEmpty() && !etInputUserToken.getText().toString().isEmpty())
                connectUser();
        });
    }

    private void connectUser() {
        APIAccounts connect = ServerConnection.connection().create(APIAccounts.class);
        Call<ResponseModel> connectUser = connect.connectUser(usename, token, managerName);

        connectUser.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call,@NonNull Response<ResponseModel> response) {
                Toast.makeText(UserManagerConnect.this, "berhasil", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(UserManagerConnect.this, "gagal "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}