package com.example.fixinventori.Activity.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fixinventori.API.APIRequestMenu;
import com.example.fixinventori.API.ServerConnection;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.Adapter.LVAdapter.AdapterMenuSet;
import com.example.fixinventori.R;
import com.example.fixinventori.model.MenuModel;
import com.example.fixinventori.model.ResponseModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuSet extends AppCompatActivity {

    AdapterMenuSet adapter;
    ListView lvMenu;
    List<MenuModel> listMenu;
    TextView tvAddMenu;
    UserSession userSession;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_set);

        userSession = new UserSession(getApplicationContext());
        user = userSession.getUserDetail().get("username");

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode()== RESULT_OK){
                        retrieveData();
                    }
                });

        tvAddMenu = findViewById(R.id.tvAddMenu);
        lvMenu = findViewById(R.id.lvMenu);
        listMenu = new ArrayList<>();
        retrieveData();

        tvAddMenu.setOnClickListener(view ->
                launcher.launch(new Intent(this, AddMenu.class)));

    }

    public void retrieveData(){
        APIRequestMenu dataMenu = ServerConnection.connection().create(APIRequestMenu.class);
        Call<ResponseModel> showData = dataMenu.showMenu(user);

        showData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if(response.body()!=null){
                    int code = response.body().getCode();
                    String pesan = response.body().getPesan();
                    listMenu = response.body().getData();
                    if (listMenu != null){
                        adapter = new AdapterMenuSet(MenuSet.this, listMenu);
                        lvMenu.setAdapter(adapter);
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call,@NonNull Throwable t) {
                Toast.makeText(MenuSet.this, "error: " +t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveData();
    }
}