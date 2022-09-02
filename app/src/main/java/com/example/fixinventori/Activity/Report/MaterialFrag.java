package com.example.fixinventori.Activity.Report;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fixinventori.API.APIReport;
import com.example.fixinventori.API.ServerConnection;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.Adapter.LVAdapter.AdapterBahan;
import com.example.fixinventori.R;
import com.example.fixinventori.model.ResponseModel;
import com.example.fixinventori.model.StatModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MaterialFrag extends Fragment {
    ListView lvBahan;
    Spinner spinnerBahanFilter;
    UserSession session;
    String user;
    List<StatModel> bahanIn, bahanOut;
    ExecutorService services;
    AdapterBahan adapterBahan;
    ArrayList<String> filter;
    ArrayAdapter<String> adapter;
    ProgressBar progressBar;

    public MaterialFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserSession(getActivity());
        user = session.getUserDetail().get("username");
        bahanIn = bahanOut = new ArrayList<>();
        services = Executors.newSingleThreadExecutor();

        filter = new ArrayList<>();
        filter.add("Bahan keluar");
        filter.add("Bahan masuk");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_material, container, false);
        spinnerBahanFilter = view.findViewById(R.id.spinnerFilterBahan);
        lvBahan = view.findViewById(R.id.lvBahan);
        progressBar = view.findViewById(R.id.progressRegist);

        services.execute(this::getBahan);

        adapter = new ArrayAdapter<>(getActivity(),R.layout.material_data_spinner_item,filter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBahanFilter.setAdapter(adapter);

        spinnerBahanFilter.setSelection(0);

        spinnerBahanFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapter.getItem(i).equals("Bahan keluar")){
                    requireActivity().runOnUiThread(()->
                            new Handler().postDelayed(()->{
                                adapterBahan = new AdapterBahan(getActivity(),bahanOut);
                                lvBahan.setAdapter(adapterBahan);
                                loading(false);
                                },200));
                }else
                    requireActivity().runOnUiThread(()->
                        new Handler().postDelayed(()->{
                            adapterBahan = new AdapterBahan(getActivity(),bahanIn);
                            lvBahan.setAdapter(adapterBahan);
                            loading(false);
                        },200));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    private void getBahan(){
        loading(true);
        APIReport data = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> getBahanIn = data.bahanIn(user);
        Call<ResponseModel> getBahanOut = data.bahanOut(user);

        getBahanIn.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                bahanIn = Objects.requireNonNull(response.body()).getBahanMasuk();
            }
            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "in"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        getBahanOut.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                bahanOut = Objects.requireNonNull(response.body()).getBahanKeluar();
            }
            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "out"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loading(Boolean isLoading){
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            lvBahan.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            lvBahan.setVisibility(View.VISIBLE);
        }
    }
}