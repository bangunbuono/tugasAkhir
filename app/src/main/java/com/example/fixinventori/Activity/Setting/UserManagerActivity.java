package com.example.fixinventori.Activity.Setting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.Adapter.LVAdapter.UserConnectedAdapter;
import com.example.fixinventori.Chat.Model.ManagerModel;
import com.example.fixinventori.R;
import com.example.fixinventori.UsageAutoApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserManagerActivity extends AppCompatActivity {
    List<ManagerModel> list;
    ListView lvUserList;
    UserConnectedAdapter adapter;
    TextView tvManagersUser;
    UserSession session;
    String manager;

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

        tvManagersUser.setText(String.format("Daftar user yang terhubung dengan %s", manager));

        list = UsageAutoApplication.listConnectedUser;
        if (list!=null){
            if(list.size()>0){
                adapter = new UserConnectedAdapter(this, list);
                lvUserList.setAdapter(adapter);
            }
        }
    }
}