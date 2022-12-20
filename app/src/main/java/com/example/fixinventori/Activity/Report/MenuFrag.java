package com.example.fixinventori.Activity.Report;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fixinventori.API.APIReport;
import com.example.fixinventori.API.ServerConnection;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.Adapter.LVAdapter.AdapterMenuRecord;
import com.example.fixinventori.R;
import com.example.fixinventori.model.ResponseModel;
import com.example.fixinventori.model.StatModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuFrag extends Fragment {
    UserSession session;
    String user;
    List<StatModel> menuRecord;
    ListView lvMenuRecord;
    AdapterMenuRecord adapterMenuRecord;
    ProgressBar progressBar;

    public MenuFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserSession(getActivity());
        user = session.getUserDetail().get("username");
        menuRecord = new ArrayList<>();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        lvMenuRecord = view.findViewById(R.id.lvMenuRecord);
        progressBar = view.findViewById(R.id.progressRegist);
        getMenuRecord();
        return view;
    }

    private void getMenuRecord(){
        loading(true);
        APIReport data = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> getData = data.menurecord(user);

        getData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                menuRecord = Objects.requireNonNull(response.body()).getMenu();
                if(getActivity()!=null)
                {
                    adapterMenuRecord = new AdapterMenuRecord(getActivity(), menuRecord);
                    if(menuRecord!=null)
                    lvMenuRecord.setAdapter(adapterMenuRecord);
                    loading(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loading(Boolean isLoading){
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            lvMenuRecord.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            lvMenuRecord.setVisibility(View.VISIBLE);
        }
    }
}