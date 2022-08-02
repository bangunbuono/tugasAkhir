package com.example.fixinventori.Chat;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixinventori.API.APIAccounts;
import com.example.fixinventori.API.ServerConnection;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.Chat.Adapter.UserListAdapter;
import com.example.fixinventori.Chat.Model.ManagerModel;
import com.example.fixinventori.R;
import com.example.fixinventori.model.ResponseModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends BaseActivity {

    UserSession session;
    AppCompatImageView ivBack;
    RecyclerView rvListUser;
    ProgressBar progressBar;
    String user;
    UserListAdapter adapter;
    List<ManagerModel> managerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_user);
        session = new UserSession(this);
        user = session.getString("username");

        ivBack = findViewById(R.id.ivBack);
        rvListUser = findViewById(R.id.rvListUser);
        progressBar = findViewById(R.id.progressRegist);

        getUsers();

        ivBack.setOnClickListener(view -> onBackPressed());
    }

    void getUsers() {
        loading(true);
        APIAccounts get = ServerConnection.connection().create(APIAccounts.class);
        Call<ResponseModel> getData = get.getManager(user);

        getData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if(response.body()!=null) {
                    managerList = response.body().getRecordManager();
                    if (managerList!=null){
                        adapter = new UserListAdapter(UserActivity.this,managerList);
                        rvListUser.setAdapter(adapter);
                        rvListUser.setVisibility(View.VISIBLE);
                        loading(false);
                        for (ManagerModel model: managerList) {
                            System.out.println(model.id + model.manager_name);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call,@NonNull Throwable t) {
                loading(false);
            }
        });

    }

    private void loading(Boolean isLoading){
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
        }else {
            progressBar.setVisibility(View.GONE);
        }
    }
}