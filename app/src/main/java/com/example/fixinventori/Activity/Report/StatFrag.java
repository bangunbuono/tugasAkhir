package com.example.fixinventori.Activity.Report;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fixinventori.API.APIReport;
import com.example.fixinventori.API.APIRequestStock;
import com.example.fixinventori.API.ServerConnection;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.R;
import com.example.fixinventori.model.ResponseModel;
import com.example.fixinventori.model.StatModel;
import com.example.fixinventori.model.StocksModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatFrag extends Fragment {
    UserSession session;
    public static String user, mode;
    List<StatModel> filter = new ArrayList<>();
    List<StatModel> list = new ArrayList<>();
    List<StatModel> listBahan = new ArrayList<>();
    List<StocksModel> bahanList = new ArrayList<>();
    ArrayList<String> satuanFilter = new ArrayList<>();
    ArrayList<String> menuFilter = new ArrayList<>();
    ArrayList<BarEntry> barEntries;
    ArrayList<Entry> lineEntries;
    ArrayList<String> xValue;
    RadioGroup radioGroup, radioGroupFilter;
    RadioButton radioButton;
    Spinner spinnerStatFilter, spinnerStatFilter2;
    BarChart barChartStat;
    LineChart lineChartSat;
    int week, month, year;
    String keterangan, filterItem, initiateFilter, filterBahan;
    ArrayAdapter<String> filter2adapter;
    BarDataSet barDataSet;
    LineDataSet lineDataSet;

    public StatFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserSession(getActivity());
        user = session.getUserDetail().get("username");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stat, container, false);

        radioGroup = view.findViewById(R.id.radioGroupTime);
        radioGroupFilter = view.findViewById(R.id.radioGroupFilter);
        spinnerStatFilter = view.findViewById(R.id.spinnerStatFilter);
        spinnerStatFilter2 = view.findViewById(R.id.spinnerStatFilter2);
        barChartStat = view.findViewById(R.id.barChartStat);
        lineChartSat = view.findViewById(R.id.lineChartStat);

        Calendar calendar = Calendar.getInstance();
        calendar.setMinimalDaysInFirstWeek(4);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        week = calendar.get(Calendar.WEEK_OF_YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);
        keterangan = "weekly";
        filterBahan = "barang_keluar";

        ArrayList<String> modeFilter = new ArrayList<>();
        modeFilter.add("mode 1");
        modeFilter.add("mode 2");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, modeFilter);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatFilter.setAdapter(spinnerAdapter);
        satuanListOutInitiate();

        spinnerStatFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                radioButton = radioGroup.findViewById(i);
                if(barEntries!=null) barEntries.clear();
                if(xValue!=null) xValue.clear();
                if (lineEntries!=null) lineEntries.clear();
                barChartStat.invalidate();
                barChartStat.clear();
                mode = spinnerAdapter.getItem(i);
                if(mode.equals("mode 1") && filterBahan.equals("barang_keluar")){
                    lineChartSat.setVisibility(View.GONE);
                    satuanList();
                    check();
                    checkFilter();
                    getDataSatuanOut();

                }else if(mode.equals("mode 2") && filterBahan.equals("barang_keluar")){
                    barChartStat.setVisibility(View.GONE);
                    bahanList();
                    check();
                    checkFilter();
                    getDataBahanOut();

                }else if(mode.equals("mode 1") && filterBahan.equals("barang_masuk")){
                    lineChartSat.setVisibility(View.GONE);
                    satuanList();
                    check();
                    checkFilter();
                    getDataSatuanIn();

                }else if(mode.equals("mode 2") && filterBahan.equals("barang_masuk")){
                    barChartStat.setVisibility(View.GONE);
                    bahanList();
                    check();
                    checkFilter();
                    getDataBahanIn();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerStatFilter2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                radioButton = radioGroup.findViewById(i);
                if (barEntries!=null) barEntries.clear();
                if (xValue!=null) xValue.clear();
                if (lineEntries!=null) lineEntries.clear();
                barChartStat.invalidate();
                barChartStat.clear();
                new Handler().postDelayed(()->{
                    filterItem = filter2adapter.getItem(i);
                    check();
                    if(mode.equals("mode 1") && filterBahan.equals("barang_keluar")) getDataSatuanOut();
                    else if(mode.equals("mode 2") && filterBahan.equals("barang_keluar")) getDataBahanOut();
                    else if(mode.equals("mode 1") && filterBahan.equals("barang_masuk")) getDataSatuanIn();
                    else if(mode.equals("mode 2") && filterBahan.equals("barang_masuk")) getDataBahanIn();
                },500);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        radioGroupFilter.setOnCheckedChangeListener((radioGroup, i) -> {
            if (barEntries!=null) barEntries.clear();
            if (xValue!=null) xValue.clear();
            if (lineEntries!=null) lineEntries.clear();
            check();
            checkFilter();
            try {
                if(mode.equals("mode 1") && filterBahan.equals("barang_keluar")) getDataSatuanOut();
                else if(mode.equals("mode 2") && filterBahan.equals("barang_keluar")) getDataBahanOut();
                else if(mode.equals("mode 1") && filterBahan.equals("barang_masuk")) getDataSatuanIn();
                else if(mode.equals("mode 2") && filterBahan.equals("barang_masuk")) getDataBahanIn();
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        });

        radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            check();
            checkFilter();
            try {
                if(mode.equals("mode 1") && filterBahan.equals("barang_keluar")) getDataSatuanOut();
                else if(mode.equals("mode 2") && filterBahan.equals("barang_keluar")) getDataBahanOut();
                else if(mode.equals("mode 1") && filterBahan.equals("barang_masuk")) getDataSatuanIn();
                else if(mode.equals("mode 2") && filterBahan.equals("barang_masuk")) getDataBahanIn();
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        });

        return view;
    }

    private void checkFilter(){
        int id = radioGroupFilter.getCheckedRadioButtonId();
        if(id==R.id.radioBahanKeluar){
            filterBahan = "barang_keluar";
        }else if(id == R.id.radioBahanMasuk) {
            filterBahan = "barang_masuk";
        }
    }

    private void check() {
        int id = radioGroup.getCheckedRadioButtonId();
        if(id==R.id.radioTimeWeek){
            keterangan = "weekly";
        }
        else if(id==R.id.radioTimeMonth){
            keterangan = "monthly";
        }
        else if(id==R.id.radioTimeYear){
            keterangan = "yearly";
        }
    }

    private void satuanList(){
        APIReport satuan = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> satuanData = satuan.satuanList(user);

        satuanData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                filter = response.body() != null ? response.body().getStatmodel() : null;
                if(filter!=null){
                    for (StatModel model: filter) {
                     satuanFilter.add(model.getSatuan());
                    }
                    filter2adapter = new ArrayAdapter<>(getActivity(),
                            android.R.layout.simple_spinner_item, satuanFilter);
                    filter2adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerStatFilter2.setAdapter(filter2adapter);
                    spinnerStatFilter2.setSelection(0);
                    initiateFilter = spinnerStatFilter2.getItemAtPosition(0).toString();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call,@NonNull Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void satuanListOutInitiate(){
        APIReport satuan = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> satuanData = satuan.satuanList(user);

        satuanData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                filter = response.body() != null ? response.body().getStatmodel() : null;
                if(filter!=null){
                    for (StatModel model: filter) {
                        satuanFilter.add(model.getSatuan());
                    }
                    filter2adapter = new ArrayAdapter<>(getActivity(),
                            android.R.layout.simple_spinner_item, satuanFilter);
                    filter2adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerStatFilter2.setAdapter(filter2adapter);
                    spinnerStatFilter2.setSelection(0);
                    initiateFilter = spinnerStatFilter2.getItemAtPosition(0).toString();
                    getDataSatuanOutInitiate();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call,@NonNull Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bahanList(){
        APIRequestStock menu = ServerConnection.connection().create(APIRequestStock.class);
        Call<ResponseModel> menuData = menu.showStock(user);

        menuData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                bahanList = response.body() != null ? response.body().getStocksModels() : null;
                if(bahanList!=null){
                    for (StocksModel model: bahanList) {
                        menuFilter.add(model.getBahan_baku());
                    }
                    filter2adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, menuFilter);
                    filter2adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerStatFilter2.setAdapter(filter2adapter);
                    spinnerStatFilter2.setSelection(0);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call,@NonNull Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDataSatuanOut(){
        APIReport data = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> call = data.statList(
                user, filterItem, week, month, year, keterangan);

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if (response.body() != null) {
                    list = response.body().getStatSatuan();
                    if(list!=null) {
                        barEntries = new ArrayList<>();
                        xValue = new ArrayList<>();
                        int index = 0;
                        for (StatModel model: list) {
                            barEntries.add(new BarEntry(index++, model.getJumlah()));
                        }
                        for (StatModel a: list) {
                            xValue.add(a.getBahan());
                        }
                        barDataSet = new BarDataSet(barEntries, list.get(0).getSatuan());
                        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                        barDataSet.setValueTextSize(15f);
                        BarData barData = new BarData(barDataSet);
                        barData.setDrawValues(true);
                        Legend legend = barChartStat.getLegend();
                        legend.setTextSize(13f);
                        barData.setBarWidth(0.5f);
                        XAxis xAxis = barChartStat.getXAxis();
                        xAxis.setTextSize(15f);
                        xAxis.setGranularityEnabled(true);
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setDrawGridLines(false);
                        xAxis.setValueFormatter((value, axis) -> {
                            try {
                                int index1 = (int) value;
                                return xValue.get((index1));
                            }catch (Exception e){
                                return "";
                            }
                        });

                        barChartStat.setVisibility(View.VISIBLE);
                        barChartStat.getAxisLeft().setDrawGridLines(false);
                        barChartStat.getAxisRight().setDrawLabels(false);
                        barChartStat.setData(barData);

                        barChartStat.animateY(1000);
                        barChartStat.getDescription().setText("bahan");
                        barChartStat.getDescription().setTextColor(Color.RED);
                    }else {
                        barChartStat.clear();
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }

    private void getDataSatuanOutInitiate(){
        APIReport data = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> call = data.statList(
                user, initiateFilter, week, month, year, keterangan);

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if (response.body() != null) {
                    list = response.body().getStatSatuan();
                }
                if(list!=null) {
                    barEntries = new ArrayList<>();
                    xValue = new ArrayList<>();
                    int index = 0;
                    for (StatModel model: list) {
                        barEntries.add(new BarEntry(index++, model.getJumlah()));
                        xValue.add(model.getBahan());
                    }
                    barDataSet = new BarDataSet(barEntries, list.get(0).getSatuan());
                    barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                    barDataSet.setValueTextSize(15f);
                    BarData barData = new BarData(barDataSet);
                    barData.setDrawValues(true);
                    Legend legend = barChartStat.getLegend();
                    legend.setTextSize(13f);
                    barData.setBarWidth(0.5f);
                    XAxis xAxis = barChartStat.getXAxis();
                    xAxis.setTextSize(15f);
                    xAxis.setGranularityEnabled(true);
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setDrawGridLines(false);
                    xAxis.setValueFormatter((value, axis) -> {
                        try {
                            int index1 = (int) value;
                            return xValue.get((index1));
                        }catch (Exception e){
                            return "";
                        }
                    });

                    barChartStat.getAxisLeft().setDrawGridLines(false);
                    barChartStat.getAxisRight().setDrawLabels(false);
                    barChartStat.setData(barData);

                    barChartStat.animateY(1000);
                    barChartStat.getDescription().setText("bahan");
                    barChartStat.getDescription().setTextColor(Color.RED);


                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }

    private void getDataBahanOut(){
        APIReport data = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> getData = data.statListBahan(user, filterItem,week,month,year,keterangan);

        getData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                listBahan = response.body()!=null ? response.body().getStatBahan() : null;
                if(listBahan!=null){
                    lineEntries = new ArrayList<>();
                    xValue = new ArrayList<>();
//                    listBahan.forEach(statModel -> System.out.println(statModel.getBahan()+
//                            statModel.getJumlah()+statModel.getSatuan()+statModel.getDate()));
                    int index = 0;
                    for (StatModel model: listBahan) {
                        lineEntries.add(new Entry(index, model.getJumlah()));
                        xValue.add(model.getDate());
                        index++;
                    }
                    lineDataSet = new LineDataSet(lineEntries, listBahan.get(0).getBahan());
                    lineDataSet.setColors(Color.BLACK);
                    lineDataSet.setValueTextSize(15f);
                    LineData lineData = new LineData(lineDataSet);
                    lineData.setDrawValues(true);
                    XAxis xAxis = lineChartSat.getXAxis();
                    xAxis.setTextSize(2f);
                    xAxis.setGranularityEnabled(true);
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setDrawGridLines(false);
                    xAxis.setValueFormatter((value, axis) -> {
                        try {
                            int index1 = (int) value;
                            return xValue.get((index1));
                        }catch (Exception e){
                            return "";
                        }
                    });
                    lineChartSat.setVisibility(View.VISIBLE);
                    lineChartSat.getAxisLeft().setDrawGridLines(false);
                    lineChartSat.getAxisRight().setDrawLabels(false);
                    lineChartSat.animateY(1000);
                    lineChartSat.setData(lineData);
                }else {
                    lineChartSat.clear();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }

    private void getDataSatuanIn(){
        APIReport data = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> call = data.statListIn(
                user, filterItem, week, month, year, keterangan);

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if (response.body() != null) {
                    list = response.body().getStatSatuan();
                }
                if(list!=null) {
                    barEntries = new ArrayList<>();
                    xValue = new ArrayList<>();
                    int index = 0;
                    for (StatModel model: list) {
                        barEntries.add(new BarEntry(index++, model.getJumlah()));
                    }
                    for (StatModel a: list) {
                        xValue.add(a.getBahan());
                    }
                    barDataSet = new BarDataSet(barEntries, list.get(0).getSatuan());
                    barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                    barDataSet.setValueTextSize(15f);
                    BarData barData = new BarData(barDataSet);
                    barData.setDrawValues(true);
                    Legend legend = barChartStat.getLegend();
                    legend.setTextSize(13f);
                    barData.setBarWidth(0.5f);
                    XAxis xAxis = barChartStat.getXAxis();
                    xAxis.setTextSize(15f);
                    xAxis.setGranularityEnabled(true);
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setDrawGridLines(false);
                    xAxis.setValueFormatter((value, axis) -> {
                        try {
                            int index1 = (int) value;
                            return xValue.get((index1));
                        }catch (Exception e){
                            return "";
                        }
                    });
                    barChartStat.setVisibility(View.VISIBLE);
                    barChartStat.getAxisLeft().setDrawGridLines(false);
                    barChartStat.getAxisRight().setDrawLabels(false);
                    barChartStat.setData(barData);

                    barChartStat.animateY(1000);
                    barChartStat.getDescription().setText("bahan");
                    barChartStat.getDescription().setTextColor(Color.RED);
                }else {
                    barChartStat.clear();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }

    private void getDataBahanIn(){
        APIReport data = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> getData = data.statListBahanIn(user, filterItem,week,month,year,keterangan);

        getData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                listBahan = response.body()!=null ? response.body().getStatBahan() : null;
                if(listBahan!=null){
                    lineEntries = new ArrayList<>();
                    xValue = new ArrayList<>();
                    listBahan.forEach(statModel -> System.out.println(statModel.getBahan()+
                            statModel.getJumlah()+statModel.getSatuan()+statModel.getDate()));
                    int index = 0;
                    for (StatModel model: listBahan) {
                        lineEntries.add(new Entry(index, model.getJumlah()));
                        xValue.add(model.getDateIn());
                        index++;
                    }
                    lineDataSet = new LineDataSet(lineEntries, listBahan.get(0).getBahan());
                    lineDataSet.setColors(Color.BLACK);
                    lineDataSet.setValueTextSize(15f);
                    LineData lineData = new LineData(lineDataSet);
                    lineData.setDrawValues(true);
                    XAxis xAxis = lineChartSat.getXAxis();
                    xAxis.setTextSize(2f);
                    xAxis.setGranularityEnabled(true);
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setDrawGridLines(false);
                    xAxis.setValueFormatter((value, axis) -> {
                        try {
                            int index1 = (int) value;
                            return xValue.get((index1));
                        }catch (Exception e){
                            return "";
                        }
                    });
                    lineChartSat.setVisibility(View.VISIBLE);
                    lineChartSat.getAxisLeft().setDrawGridLines(false);
                    lineChartSat.getAxisRight().setDrawLabels(false);
                    lineChartSat.animateY(1000);
                    lineChartSat.setData(lineData);
                }else {
                    lineChartSat.clear();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }


}
