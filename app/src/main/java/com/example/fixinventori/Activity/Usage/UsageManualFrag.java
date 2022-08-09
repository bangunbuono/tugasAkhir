package com.example.fixinventori.Activity.Usage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.fixinventori.API.APIReport;
import com.example.fixinventori.API.APIRequestStock;
import com.example.fixinventori.API.APIRestock;
import com.example.fixinventori.API.ServerConnection;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.Adapter.LVAdapter.AdapterUsageManualFrag;
import com.example.fixinventori.Adapter.SpinnerAdapter.AdapterSpinnerRestock;
import com.example.fixinventori.R;
import com.example.fixinventori.model.KomposisiModel;
import com.example.fixinventori.model.ResponseModel;
import com.example.fixinventori.model.RestockModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsageManualFrag extends Fragment {

    Spinner spinner;
    ArrayList<RestockModel> stockList = new ArrayList<>();
    ArrayList<KomposisiModel> manualUsageList;
    public static ArrayList<Integer> listId;
    ListView lvManualUsage;
    AdapterUsageManualFrag adapterList;
    TextView tvSatuanStock;
    @SuppressLint("StaticFieldLeak")
    public static TextView totalItem;
    EditText etJumlahStock;
    @SuppressLint("StaticFieldLeak")
    public static LinearLayout layoutItem;
    AdapterSpinnerRestock adapter;
    Button btnConfirm, btnAddManualList;
    String user, bahan, satuan, date, orderSeries, bahanx, satuanx, formatedTime;
    UserSession userSession;
    int id, jumlah, idx, jumlahx;
    LocalDateTime time;
    String month;

    public UsageManualFrag() {
        checkItem();
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        userSession = new UserSession(getActivity());
        user = userSession.getUserDetail().get("username");

        View view = inflater.inflate(R.layout.fragment_usage_manual, container, false);

        layoutItem = view.findViewById(R.id.layoutItemManual);
        spinner = view.findViewById(R.id.spinnerManual);
        tvSatuanStock = view.findViewById(R.id.tvManual);
        btnConfirm = view.findViewById(R.id.btnManual);
        etJumlahStock = view.findViewById(R.id.etManual);
        btnAddManualList = view.findViewById(R.id.btnAddManualList);
        lvManualUsage = view.findViewById(R.id.lvManualUsage);
        totalItem = view.findViewById(R.id.tvTotalStockManual);

        manualUsageList = new ArrayList<>();
        listId = new ArrayList<>();
        adapterList = new AdapterUsageManualFrag(getActivity(), manualUsageList);

        if(manualUsageList!=null){
            lvManualUsage.setAdapter(adapterList);
        }

        restockList();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tvSatuanStock.setText(stockList.get(i).getSatuan());
                id = stockList.get(i).getId();
                bahan = stockList.get(i).getBahan_baku();
                satuan = stockList.get(i).getSatuan();
//                Toast.makeText(getActivity(), "item: "+restockList.get(i).getBahan_baku(),
//                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        btnAddManualList.setOnClickListener(view1 -> {
            if(etJumlahStock.getText().toString().isEmpty()){
                Toast.makeText(getActivity(), "Isi jumlah dulu", Toast.LENGTH_SHORT).show();
            }else {
                if (!listId.contains(id)){
                    jumlah = Integer.parseInt(etJumlahStock.getText().toString().trim());
                    manualUsageList.add(new KomposisiModel(id, bahan, satuan, jumlah));
                    listId.add(id);
                    adapterList.notifyDataSetChanged();
                    etJumlahStock.setText(null);
                    spinner.setSelection(0);
                    checkItem();
                }
                else {
                    Toast.makeText(getActivity(), "Bahan sudah ada", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnConfirm.setOnClickListener(view1 -> {
            time = LocalDateTime.now();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMddHHmmss");
            DateTimeFormatter timeStamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter monthType = DateTimeFormatter.ofPattern("MM/yyyy");
            month = monthType.format(time);
            formatedTime = timeStamp.format(time);
            date = dtf.format(time);
            orderSeries = "B."+date;

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Konfirmasi pesanan?");
            builder.setPositiveButton("iya", (dialogInterface, i) -> {
                if(manualUsageList!= null){
                    record();
                    for(i =0; i<manualUsageList.size();i++){
                        idx = manualUsageList.get(i).getId();
                        bahanx = manualUsageList.get(i).getBahan();
                        jumlahx = manualUsageList.get(i).getJumlah();
                        satuanx = manualUsageList.get(i).getSatuan();
                        reportManualUsage();
                        stockUsage();
                    }
                }
                getActivity().finish();
            });
            builder.setNegativeButton("batal", (dialogInterface, i) -> {

            });
            builder.show();

        });
        return view;
    }

    public void checkItem(){
        if(manualUsageList!=null){
            if(manualUsageList.size()!=0){
                layoutItem.setVisibility(View.VISIBLE);
                totalItem.setText(String.format("Total %s item", manualUsageList.size()));
            }
            else {
                layoutItem.setVisibility(View.GONE);
            }
        }
    }

    private void restockList(){
        APIRestock restockData = ServerConnection.connection().create(APIRestock.class);
        Call<ResponseModel> getData = restockData.getStock(user);

        getData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call,@NonNull Response<ResponseModel> response) {
                assert response.body() != null;
                stockList = response.body().getStocks();
                adapter = new AdapterSpinnerRestock(requireActivity(), stockList);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                t.fillInStackTrace();
            }
        });
    }

    private void stockUsage(){
        APIRequestStock stockData = ServerConnection.connection().create(APIRequestStock.class);
        Call<ResponseModel> minusStock = stockData.minStocks(idx,jumlahx);

        minusStock.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull retrofit2.Response<ResponseModel> response) {
                etJumlahStock.setText(null);
                Toast.makeText(getActivity(), "berhasil", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "stock update gagal "+t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void reportManualUsage(){
        APIReport report = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> reportData = report.recordUsage(
                orderSeries, bahanx, jumlahx, satuanx,"manual", user,1,formatedTime );

        reportData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call,@NonNull Response<ResponseModel> response) {

            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call,@NonNull Throwable t) {
                Toast.makeText(getActivity(), "report gagal: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void record(){
        APIReport recordData = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> record = recordData.record(orderSeries,"barang keluar", formatedTime, user, month);

        record.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {

            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "record gagal: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}