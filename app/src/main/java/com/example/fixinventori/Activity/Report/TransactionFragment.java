package com.example.fixinventori.Activity.Report;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fixinventori.API.APIReport;
import com.example.fixinventori.API.ServerConnection;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.Adapter.LVAdapter.AdapterTransaction;
import com.example.fixinventori.R;
import com.example.fixinventori.model.ResponseModel;
import com.example.fixinventori.model.StatModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionFragment extends Fragment {
    ListView lvCashTransation;
    UserSession session;
    String user;
    int week, month, year;
    List<StatModel> cashIn, cashOut;
    AdapterTransaction adapterTransaction;
    ProgressBar progressBar;
    ExecutorService service;

    public TransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserSession(getActivity());
        user = session.getUserDetail().get("username");
        getWeek();
        cashIn = new ArrayList<>();
        cashOut = new ArrayList<>();
        service = Executors.newSingleThreadExecutor();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);
        lvCashTransation = view.findViewById(R.id.lvCashTransaction);
        progressBar = view.findViewById(R.id.progressRegist);

        loading(true);
        getTransaction();

        return view;
    }

    private void getTransaction(){
        APIReport data = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> getDataIn = data.statCashInAll(user);
        Call<ResponseModel> getDataOut = data.statCashOutAll(user);

        getDataIn.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if(response.body()!=null){
                    cashIn = response.body().getStatCashIn();
                    if(cashIn!=null) {
                        cashIn.sort((obj1, obj2)->
                                Objects.requireNonNull(convertToDate(obj2.getTanggal()))
                                        .compareTo(convertToDate(obj1.getTanggal())));
                        getDataOut.enqueue(new Callback<ResponseModel>() {
                            @Override
                            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                                if(response.body()!=null){
                                    cashOut = response.body().getStatCashOut();
                                    if(cashOut!=null){
                                        cashOut.sort((obj1,obj2)->
                                                Objects.requireNonNull(convertToDate(obj2.getTanggal()))
                                                        .compareTo(convertToDate(obj1.getTanggal())));
                                        if(cashIn.size() == cashOut.size()){
                                            loading(false);
                                            if (getActivity() != null) {
                                                adapterTransaction = new AdapterTransaction(getActivity(), cashIn, cashOut);
                                                lvCashTransation.setAdapter(adapterTransaction);
                                            }
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                                Toast.makeText(getActivity(), "b"+t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "a"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getWeek(){
        Calendar calendar = Calendar.getInstance();
        calendar.setMinimalDaysInFirstWeek(4);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        week = calendar.get(Calendar.WEEK_OF_YEAR);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
    }

    private Date convertToDate(String date){
        try {
            return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void loading(Boolean isLoading){
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            lvCashTransation.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            lvCashTransation.setVisibility(View.VISIBLE);
        }
    }
}