package com.example.fixinventori.Activity.Report;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fixinventori.API.APIReport;
import com.example.fixinventori.API.ServerConnection;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.Adapter.LVAdapter.AdapterRecordDetail;
import com.example.fixinventori.Adapter.LVAdapter.AdapterRecordInDetail;
import com.example.fixinventori.Adapter.LVAdapter.AdapterRecordMenu;
import com.example.fixinventori.R;
import com.example.fixinventori.model.RecordModel;
import com.example.fixinventori.model.ResponseModel;
import com.example.fixinventori.model.UsageMenuModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportDetail extends AppCompatActivity {
    TextView tvReportDetail, tvRecordDate, tvJumlahPengunjung, tvDataPemesaanan, tvTotalPrice, tvTotalPrice2,
            tvTitleHarga;
    String code, keterangan, user;
    List<RecordModel> recordDetail;
    List<UsageMenuModel> listMenu;
    ListView lvRecordDetail, lvRecordMenu;
    UserSession userSession;
    AdapterRecordDetail adapterRecordDetail;
    AdapterRecordMenu adapterRecordMenu;
    AdapterRecordInDetail adapterRecordInDetail;
//    BarChart barChart;
    LinearLayout llRecordMenu;
    int totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        userSession = new UserSession(this);
        user = userSession.getUserDetail().get("username");

        tvReportDetail = findViewById(R.id.tvReportDetail);
        tvRecordDate = findViewById(R.id.tvRecordDateDetail);
        lvRecordDetail = findViewById(R.id.lvRecordDetail);
//        barChart = findViewById(R.id.barChart);
        tvJumlahPengunjung = findViewById(R.id.tvJumlahPengunjung);
        lvRecordMenu = findViewById(R.id.lvRecordMenu);
        llRecordMenu = findViewById(R.id.llMenuRecord);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvTotalPrice2 = findViewById(R.id.tvTotalPrice2);
        tvDataPemesaanan = findViewById(R.id.tvDataPemesanan);
        tvTitleHarga = findViewById(R.id.tvTitleHarga);

        Intent intent = getIntent();
        code = intent.getStringExtra("kode");
        keterangan = intent.getStringExtra("keterangan");
        recordDetail = new ArrayList<>();
        listMenu = new ArrayList<>();

        tvReportDetail.setText(String.format("Pesanan %s", code));

        getRecordDetail();
    }

    private void getRecordDetail(){
        System.out.println(user+code+keterangan);
        APIReport record = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> recordData = record.recordDetail(user, code, keterangan);

        recordData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call,@NonNull Response<ResponseModel> response) {
                if(response.body() != null)
                    recordDetail = response.body().getRecordDetail();
                if(recordDetail!=null){
                    if(code.contains("B")){
                        if(recordDetail.get(0).getKeterangan().equals("kombinasi")
                                || recordDetail.get(0).getKeterangan().equals("manual"))
                            tvJumlahPengunjung.setVisibility(View.GONE);
                        else tvJumlahPengunjung.setVisibility(View.VISIBLE);
                        getMenuRecord();
                        if(recordDetail.get(0).getPengunjung()==0) tvJumlahPengunjung.setText(R.string.ojek_online);
                        else tvJumlahPengunjung.setText(String.format("Jumlah pengunjung: %s", recordDetail.get(0).getPengunjung()));
                        adapterRecordDetail = new AdapterRecordDetail(ReportDetail.this, recordDetail);
                        lvRecordDetail.setAdapter(adapterRecordDetail);
                    }else {
                        tvJumlahPengunjung.setVisibility(View.GONE);
                        totalPrice = 0;
                        for (RecordModel model: recordDetail) {
                            totalPrice += model.getHarga();
                        }
                        tvTitleHarga.setVisibility(View.VISIBLE);
                        tvTotalPrice2.setVisibility(View.VISIBLE);
                        tvTotalPrice2.setText(String.format("Total = Rp %s", totalPrice));

                        adapterRecordInDetail = new AdapterRecordInDetail(ReportDetail.this, recordDetail);
                        lvRecordDetail.setAdapter(adapterRecordInDetail);
                    }

                    tvRecordDate.setText(String.format(("Waktu %s: %s"), keterangan, recordDetail.get(0).getTanggal()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call,@NonNull Throwable t) {
                Toast.makeText(ReportDetail.this, "Gagal mendapat record: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getMenuRecord(){
        APIReport data = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> getData = data.recordMenu(user, code);

        getData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if(response.body()!=null) {
                    listMenu = response.body().getRecordMenu();
                    if(listMenu!=null){
                        adapterRecordMenu = new AdapterRecordMenu(ReportDetail.this, listMenu);
                        lvRecordMenu.setAdapter(adapterRecordMenu);
                        llRecordMenu.setVisibility(View.VISIBLE);
                        tvDataPemesaanan.setVisibility(View.VISIBLE);
                        totalPrice = 0;
                        for (UsageMenuModel model: listMenu) {
                            totalPrice += model.getHarga();
                        }
                        tvTotalPrice.setText(String.format("Total = Rp %s", totalPrice));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call,@NonNull Throwable t) {
                Toast.makeText(ReportDetail.this, "Gagal mendapat record menu: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



}