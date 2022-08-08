package com.example.fixinventori.Activity.Report;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fixinventori.API.APIReport;
import com.example.fixinventori.API.ServerConnection;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.Adapter.LVAdapter.AdapterRecordDetail;
import com.example.fixinventori.R;
import com.example.fixinventori.model.RecordModel;
import com.example.fixinventori.model.ResponseModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportDetail extends AppCompatActivity {
    TextView tvReportDetail, tvRecordDate;
    String code, keterangan, user;
    List<RecordModel> recordDetail;
    ArrayList<BarEntry> barEntries = new ArrayList<>();
    ArrayList<String> xValue = new ArrayList<>();
    ListView lvRecordDetail;
    UserSession userSession;
    AdapterRecordDetail adapterRecordDetail;
    BarChart barChart;

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
        barChart = findViewById(R.id.barChart);

        Intent intent = getIntent();
        code = intent.getStringExtra("kode");
        keterangan = intent.getStringExtra("keterangan");
        recordDetail = new ArrayList<>();

        tvReportDetail.setText(String.format("Pesanan %s", code));

        getRecordDetail();
    }

    private void getRecordDetail(){
        APIReport record = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> recordData = record.recordDetail(user, code, keterangan);

        recordData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call,@NonNull Response<ResponseModel> response) {
                if(response.body() != null)
                    recordDetail = response.body().getRecordDetail();
                if(recordDetail!=null){
                    adapterRecordDetail = new AdapterRecordDetail(ReportDetail.this, recordDetail);
                    lvRecordDetail.setAdapter(adapterRecordDetail);
                    tvRecordDate.setText(String.format(("Waktu %s: %s"), keterangan, recordDetail.get(0).getTanggal()));

                    for (int i = 0, recordDetailSize = recordDetail.size(); i < recordDetailSize; i++) {
                        RecordModel model = recordDetail.get(i);
                        barEntries.add(new BarEntry(i,model.getJumlah()));
                        xValue.add(model.getBahan());
                    }

                    BarDataSet barDataSet = new BarDataSet(barEntries, "jumlah");
                    barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                    barDataSet.setValueTextSize(15f);
                    BarData barData = new BarData(barDataSet);
                    Legend legend = barChart.getLegend();
                    legend.setTextSize(13f);
                    barData.setBarWidth(0.5f);
                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setTextSize(15f);
                    xAxis.setGranularityEnabled(true);
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setDrawGridLines(false);
                    xAxis.setValueFormatter(new IAxisValueFormatter() {
                        @Override
                        public String getFormattedValue(float value, AxisBase axis) {
                            try {
                                int index = (int) value;
                                return xValue.get((index));
                            }catch (Exception e){
                                return "";
                            }
                        }
                    });
                    barChart.getAxisLeft().setDrawGridLines(false);
                    barChart.getAxisRight().setDrawLabels(false);
                    barChart.setData(barData);

                    barChart.animateY(1000);
                    barChart.getDescription().setText("report");
                    barChart.getDescription().setTextColor(Color.RED);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call,@NonNull Throwable t) {

            }
        });
    }


}