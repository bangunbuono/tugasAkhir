package com.example.fixinventori.Activity.Setting;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fixinventori.API.APIAccounts;
import com.example.fixinventori.API.ServerConnection;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.Adapter.LVAdapter.UserConnectedAdapter;
import com.example.fixinventori.Chat.Model.ManagerModel;
import com.example.fixinventori.R;
import com.example.fixinventori.UsageAutoApplication;
import com.example.fixinventori.model.ResponseModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserManagerActivity extends AppCompatActivity {
    List<ManagerModel> list;
    ListView lvUserList;
    UserConnectedAdapter adapter;
    TextView tvManagersUser;
    UserSession session;
    String manager;
    Button btnAddUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manager);

        Objects.requireNonNull(getSupportActionBar()).hide();

        session = new UserSession(this);
        manager = session.getManagerDetail().get("manager");

        list = new ArrayList<>();
        lvUserList = findViewById(R.id.lvConnectedUser);
        tvManagersUser = findViewById(R.id.tvManagersUser);
        btnAddUser = findViewById(R.id.btnAddUser);

        tvManagersUser.setText(String.format("Daftar user terhubung dengan %s", manager));

        btnAddUser.setOnClickListener(view-> startActivity(new Intent(this, AddUserConnect.class)));

        list = UsageAutoApplication.listConnectedUser;
        if (list!=null){
            if(list.size()>0){
                adapter = new UserConnectedAdapter(this, list);
                lvUserList.setAdapter(adapter);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getConnecteduser();

    }

    public void getConnecteduser(){
        APIAccounts data = ServerConnection.connection().create(APIAccounts.class);
        Call<ResponseModel> getData = data.getUser(manager);

        getData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                UsageAutoApplication.listConnectedUser = new ArrayList<>();
                if (response.body() != null) {
                    UsageAutoApplication.listConnectedUser = list = response.body().getRecordManager();
                    adapter = new UserConnectedAdapter(UserManagerActivity.this, list);
                    lvUserList.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(UserManagerActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}