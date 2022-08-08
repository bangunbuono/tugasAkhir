package com.example.fixinventori.Activity.Report;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixinventori.API.APIReport;
import com.example.fixinventori.API.ServerConnection;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.Adapter.RVAdapter.AdapterDate;
import com.example.fixinventori.Adapter.RVAdapter.AdapterMonth;
import com.example.fixinventori.R;
import com.example.fixinventori.model.MonthModel;
import com.example.fixinventori.model.RecordModel;
import com.example.fixinventori.model.ResponseModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFrag extends Fragment implements AdapterMonth.onClick{

    RecyclerView rvMonth, rvDateList;
    AdapterMonth adapter;
    AdapterDate adapterDate;
    RecyclerView.LayoutManager layoutManager, dateListLayoutManager;
    List<MonthModel> monthList;
    List<RecordModel> recordList, dateRecord;
    UserSession session;
    String user, month;
    DisplayMetrics displayMetrics;
    public static Spinner spinnerRecordFilter;
    public static ArrayList<String> recordFilter;
    ArrayAdapter<String> filter;
    public static String choosenFilter;

    public HistoryFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        rvMonth = view.findViewById(R.id.rvMonth);
        rvDateList = view.findViewById(R.id.rvRecordList);
        spinnerRecordFilter = view.findViewById(R.id.spinnerRecordFilter);
        displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        session = new UserSession(getActivity());
        user = session.getUserDetail().get("username");

        monthList = new ArrayList<>();
        recordList = new ArrayList<>();
        dateRecord = new ArrayList<>();
        recordFilter = new ArrayList<>();

        recordFilter.add("semua");
        recordFilter.add("barang keluar");
        recordFilter.add("barang masuk");

        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        rvMonth.setLayoutManager(layoutManager);

        filter = new ArrayAdapter<>(getActivity(), R.layout.simple_spinner_style, recordFilter);
        filter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerRecordFilter.setAdapter(filter);
        new Handler().postDelayed(()-> spinnerRecordFilter.setSelection(0),100);

        spinnerRecordFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                choosenFilter = recordFilter.get(i);
                dateRecord();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        getMonth();

        if(monthList!=null){
//            rvMonth.post(() -> rvMonth.smoothScrollToPosition(monthList.size()-1));
//            new Handler().post(() -> rvMonth.smoothScrollToPosition(monthList.size()-1));
            new Handler().postDelayed(()-> {
                try{
                    layoutManager.smoothScrollToPosition(
                            rvMonth, new RecyclerView.State(),monthList.size()-1);
                }catch (Exception e){
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
//                        rvMonth.smoothScrollToPosition(monthList.size()-1);
                }
                    ,800);
        }
        return view;
    }

    @Override
    public void onItemClicked(int i) {
        View view = rvMonth.findViewWithTag(monthList.get(i));
//        View view = rvMonth.getChildAt(i);
        if (view != null) {
            int x = rvMonth.getChildLayoutPosition(view);
            int y = rvMonth.getWidth()/2 - view.getWidth()/2;
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rvMonth.getLayoutManager();
            assert linearLayoutManager != null;
            linearLayoutManager.scrollToPositionWithOffset(x,y);
            month = monthList.get(i).getBulan();
            dateRecord();
        }
//        int scroll = (view.getLeft() - (displayMetrics.widthPixels/2)) + (view.getWidth()/2);
//        int a = linearLayoutManager.findFirstVisibleItemPosition()-linearLayoutManager.findLastVisibleItemPosition();
//        rvMonth.smoothScrollToPosition(i);
//        rvMonth.setScrollY(a/2);
//        rvMonth.smoothScrollBy(scroll, 0);
    }

    private void getMonth(){
        APIReport reportData = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> month = reportData.date(user);

        month.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if (response.body() != null) {
                    monthList = response.body().getBulan();
                    adapter = new AdapterMonth(getActivity(), monthList, HistoryFrag.this);
                    if(monthList!= null){
                        new Handler().postDelayed(()->{
                            rvMonth.setAdapter(adapter);
    //                        new Handler().postDelayed(()->
                        },200);
                        dateRecordInitiate();
                        //                    layoutManager = new GridLayoutManager(getActivity(),1,RecyclerView.HORIZONTAL,false);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call,@NonNull Throwable t) {

            }
        });
    }


    public void dateRecord(){
        APIReport record = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> recordData = record.dateList(user,month);

        recordData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if(response.body() != null){
                    dateRecord = response.body().getDate();
                    adapterDate = new AdapterDate(getActivity(), dateRecord);
                    if(dateRecord!=null){
                        dateListLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
                        rvDateList.setLayoutManager(dateListLayoutManager);
                        rvDateList.setAdapter(adapterDate);
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {

            }
        });
    }

    public void dateRecordInitiate(){
        APIReport record = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> recordData = record.dateList(user,monthList.get(monthList.size()-1).getBulan());

        recordData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if(response.body() != null) {
                    dateRecord = response.body().getDate();
                    adapterDate = new AdapterDate(getActivity(), dateRecord);
                    if (dateRecord != null) {
                        rvDateList.setAdapter(adapterDate);
                        dateListLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                        rvDateList.setLayoutManager(dateListLayoutManager);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {

            }
        });
    }
}