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
import android.widget.TextView;
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
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
    List<StatModel> listMenu = new ArrayList<>();
    List<StatModel> listPengunjung = new ArrayList<>();
    List<StatModel> listCashOut = new ArrayList<>();
    List<StatModel> listCashIn = new ArrayList<>();
    List<StocksModel> bahanList = new ArrayList<>();
    ArrayList<String> satuanFilter = new ArrayList<>();
    ArrayList<String> bahanFilter = new ArrayList<>();
    ArrayList<BarEntry> barEntries;
    ArrayList<Entry> lineEntries, lineEntries2;
    ArrayList<String> xValue;
    ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
    RadioGroup radioGroup, radioGroupFilter;
    TextView tvStat;
    RadioButton radioButton, radioBahanMasuk, radioBahanKeluar, radioAllCashFlow;
    Spinner spinnerStatFilter, spinnerStatFilter2;
    HorizontalBarChart barChartStat;
    LineChart lineChartSat;
    int week, month, year;
    String keterangan, filterItem, initiateFilter, filterBahan;
    ArrayAdapter<String> filter2adapter;
    BarDataSet barDataSet;
    LineDataSet lineDataSet, lineDataSet2;

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
        radioBahanKeluar = view.findViewById(R.id.radioBahanKeluar);
        radioBahanMasuk = view.findViewById(R.id.radioBahanMasuk);
        radioAllCashFlow = view.findViewById(R.id.radioAllCashFlow);
        tvStat = view.findViewById(R.id.tvStat);

        Calendar calendar = Calendar.getInstance();
        calendar.setMinimalDaysInFirstWeek(4);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        week = calendar.get(Calendar.WEEK_OF_YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);
        keterangan = "weekly";
        filterBahan = "barang_keluar";

        IMarker iMarker = new LineChartMarkerView(getActivity(), R.layout.tv_content_view, lineChartSat);
        lineChartSat.setMarker(iMarker);

        ArrayList<String> modeFilter = new ArrayList<>();
        modeFilter.add("mode 1");
        modeFilter.add("mode 2");
        modeFilter.add("menu");
        modeFilter.add("cash flow");
        modeFilter.add("pengunjung");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.custom_simple_spinner_item, modeFilter);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatFilter.setAdapter(spinnerAdapter);
        satuanListOutInitiate();

        spinnerStatFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                radioButton = radioGroup.findViewById(i);
                initStat();
                mode = spinnerAdapter.getItem(i);
                if(mode.equals("cash flow")) {
                    radioAllCashFlow.setVisibility(View.VISIBLE);
                }else {
                    radioAllCashFlow.setVisibility(View.GONE);
                }
                switch (mode) {
                    case "mode 1":
                        radioGroupFilter.setVisibility(View.VISIBLE);
                        radioGroupFilter.setActivated(true);
                        spinnerStatFilter2.setVisibility(View.VISIBLE);
                        spinnerStatFilter2.setActivated(true);
                        radioBahanMasuk.setText(R.string.bahan_masuk);
                        radioBahanKeluar.setText(R.string.bahan_keluar);
                        initStat();
                        lineChartSat.setVisibility(View.GONE);
                        satuanList();
                        check();
                        checkFilter();
                        break;
                    case "mode 2":
                        radioGroupFilter.setVisibility(View.VISIBLE);
                        radioGroupFilter.setActivated(true);
                        spinnerStatFilter2.setVisibility(View.VISIBLE);
                        spinnerStatFilter2.setActivated(true);
                        radioBahanMasuk.setText(R.string.bahan_masuk);
                        radioBahanKeluar.setText(R.string.bahan_keluar);
                        initStat();
                        barChartStat.setVisibility(View.GONE);
                        bahanList();
                        check();
                        checkFilter();
                        break;
                    case "menu":
                        radioGroupFilter.setVisibility(View.GONE);
                        radioGroupFilter.setActivated(false);
                        spinnerStatFilter2.setVisibility(View.VISIBLE);
                        spinnerStatFilter2.setActivated(true);
                        lineChartSat.setVisibility(View.GONE);
                        barChartStat.setVisibility(View.VISIBLE);
                        spinnerStatFilter2.setActivated(false);
                        spinnerStatFilter2.setVisibility(View.GONE);
                        initStat();
                        clearArrayList();
                        getMenu();
                        break;
                    case "pengunjung":
                        radioGroupFilter.setVisibility(View.GONE);
                        radioGroupFilter.setActivated(false);
                        spinnerStatFilter2.setVisibility(View.GONE);
                        spinnerStatFilter2.setActivated(false);
                        initStat();
                        barChartStat.setVisibility(View.GONE);
                        check();
                        pengunjungList();
                        break;
                    case "cash flow":
                        spinnerStatFilter2.setVisibility(View.GONE);
                        spinnerStatFilter2.setActivated(false);
                        radioGroupFilter.setVisibility(View.VISIBLE);
                        radioGroupFilter.setActivated(true);
                        radioBahanMasuk.setText(R.string.kas_masuk);
                        radioBahanKeluar.setText(R.string.kas_keluar);
                        initStat();
                        barChartStat.setVisibility(View.GONE);
                        check();
                        getStatCash();
                        break;
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
                initStat();
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
            clearArrayList();
            check();
            checkFilter();
            try {
                if(mode.equals("mode 1") && filterBahan.equals("barang_keluar")) getDataSatuanOut();
                else if(mode.equals("mode 2") && filterBahan.equals("barang_keluar")) getDataBahanOut();
                else if(mode.equals("mode 1") && filterBahan.equals("barang_masuk")) getDataSatuanIn();
                else if(mode.equals("mode 2") && filterBahan.equals("barang_masuk")) getDataBahanIn();
                else if(mode.equals("menu")) getMenu();
                else if(mode.equals("cash flow")) getStatCash();

            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }); //radio keluar/masuk

        radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            clearArrayList();
            check();
            checkFilter();
            try {
                if(mode.equals("mode 1") && filterBahan.equals("barang_keluar")) getDataSatuanOut();
                else if(mode.equals("mode 2") && filterBahan.equals("barang_keluar")) getDataBahanOut();
                else if(mode.equals("mode 1") && filterBahan.equals("barang_masuk")) getDataSatuanIn();
                else if(mode.equals("mode 2") && filterBahan.equals("barang_masuk")) getDataBahanIn();
                else if(mode.equals("menu")) getMenu();
                else if(mode.equals("pengunjung")) pengunjungList();
                else if(mode.equals("cash flow")) {
                    if(lineDataSets!=null) {
                        lineDataSets.clear();
                        lineDataSet.clear();
                        lineDataSet2.clear();
                        lineChartSat.clear();
                        lineChartSat.invalidate();
                        new Handler().postDelayed(this::getStatCash,200);
                    }
                }

            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }); //radio waktu

        return view;
    }

    private void checkFilter(){
        int id = radioGroupFilter.getCheckedRadioButtonId();
        if(id==R.id.radioBahanKeluar){
            filterBahan = "barang_keluar";
        }else if(id == R.id.radioBahanMasuk) {
            filterBahan = "barang_masuk";
        }else if(id == R.id.radioAllCashFlow)
            filterBahan = "semua";
    } // check bahan keluar/masuk

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
    } // check waktu(tahun, bulan, minggu)

    private void initStat(){
        clearArrayList();
        barChartStat.invalidate();
        barChartStat.clear();
        lineChartSat.invalidate();
        lineChartSat.clear();
    } //clear dan invalidate data chart

    private void clearArrayList(){
        if (barEntries!=null) barEntries.clear();
        if (xValue!=null) xValue.clear();
        if (lineEntries!=null) lineEntries.clear();
        if (lineEntries2!=null) lineEntries2.clear();
    }

    private void satuanList(){
        APIReport satuan = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> satuanData = satuan.satuanList(user);

        satuanData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if(satuanFilter!=null) satuanFilter.clear();
                filter = response.body() != null ? response.body().getStatmodel() : null;
                if(filter!=null){
                    for (StatModel model: filter) {
                        satuanFilter.add(model.getSatuan());
                    }
                    filter2adapter = new ArrayAdapter<>(getActivity(),
                            R.layout.custom_simple_spinner_item, satuanFilter);
                    filter2adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerStatFilter2.setAdapter(filter2adapter);
                    spinnerStatFilter2.setSelection(0);
                    initiateFilter = spinnerStatFilter2.getItemAtPosition(0).toString();

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call,@NonNull Throwable t) {
                Toast.makeText(getActivity(),"i"+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bahanList(){
        APIRequestStock menu = ServerConnection.connection().create(APIRequestStock.class);
        Call<ResponseModel> menuData = menu.showStock(user);

        menuData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if(bahanFilter!=null) bahanFilter.clear();
                bahanList = response.body() != null ? response.body().getStocksModels() : null;
                if(bahanList!=null){
                    for (StocksModel model: bahanList) {
                        bahanFilter.add(model.getBahan_baku());
                    }
                    filter2adapter = new ArrayAdapter<>(getActivity(), R.layout.custom_simple_spinner_item, bahanFilter);
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
                            R.layout.custom_simple_spinner_item, satuanFilter);
                    filter2adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerStatFilter2.setAdapter(filter2adapter);
                    spinnerStatFilter2.setSelection(0);
                    initiateFilter = spinnerStatFilter2.getItemAtPosition(0).toString();
                    filterItem = satuanFilter.get(0);
                    getDataSatuanOutInitiate();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call,@NonNull Throwable t) {
                Toast.makeText(getActivity(), "b"+t.getMessage(), Toast.LENGTH_SHORT).show();
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
                    renderBarChart(barEntries, xValue, list);
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
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
                        StringBuilder bahan = new StringBuilder();
                        int index = 0;
                        for (StatModel model: list) {
                            barEntries.add(new BarEntry(index++, model.getJumlah()));
                            xValue.add(model.getBahan());
                            bahan.append(model.getBahan()).append(" ")
                                    .append(model.getJumlah()).append(" ").append(filterItem).append("\n");
                        }
                        renderBarChart(barEntries,xValue,list);
                        tvStat.setText(bahan);
                        tvStat.setVisibility(View.VISIBLE);

                    }else {
                        barChartStat.clear();
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
//                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println("c"+t.getMessage());
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
                    int jumlah = 0;
                    int index = 0;
                    for (StatModel model: listBahan) {
                        lineEntries.add(new Entry(index, model.getJumlah()));
                        xValue.add(model.getDate());
                        index++;
                        jumlah += model.getJumlah();
                    }
                    renderLineChart(lineEntries, xValue, listBahan);
                    tvStat.setText(String.format("Total %s yang keluar adalah %s %s"
                            , filterItem, jumlah, listBahan.get(0).getSatuan()));
                    tvStat.setVisibility(View.VISIBLE);

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
                    StringBuilder bahan = new StringBuilder();
                    for (StatModel model: list) {
                        barEntries.add(new BarEntry(index++, model.getJumlah()));
                        xValue.add(model.getBahan());
                        bahan.append(model.getBahan()).append(" ")
                                .append(model.getJumlah()).append(" ").append(filterItem).append("\n");
                    }
                    tvStat.setText(bahan);
                    tvStat.setVisibility(View.VISIBLE);
                    renderBarChart(barEntries,xValue,list);
//
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
                            statModel.getJumlah()+statModel.getSatuan()+statModel.getDateIn()));
                    int index = 0;
                    int jumlah = 0;
                    for (StatModel model: listBahan) {
                        lineEntries.add(new Entry(index, model.getJumlah()));
                        xValue.add(model.getDateIn());
                        index++;
                        jumlah += model.getJumlah();
                    }
                    tvStat.setText(String.format("Total %s yang masuk adalah %s %s"
                            , filterItem, jumlah, listBahan.get(0).getSatuan()));
                    tvStat.setVisibility(View.VISIBLE);
                    renderLineChart(lineEntries, xValue, listBahan);

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

    private void getMenu(){
        APIReport data = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> getData = data.statMenu(user, filterItem, week, month, year, keterangan);

        getData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if(response.body()!=null) listMenu = response.body().getStatMenu();
                if(listMenu!=null){
//                    lineEntries = new ArrayList<>();
                    barEntries = new ArrayList<>();
                    xValue = new ArrayList<>();
                    int index = 0;
                    StringBuilder menu = new StringBuilder();
                    for (StatModel model: listMenu) {
                        barEntries.add(new BarEntry(index, model.getJumlah()));
                        xValue.add(model.getMenu());
                        index++;
                        menu.append(model.getMenu()).append(" dipesan sebanyak ").append(model.getJumlah()).append("\n");
                    }
                    renderBarChart(barEntries,xValue,listMenu);
                    tvStat.setText(menu);
                    tvStat.setVisibility(View.VISIBLE);
//                    renderLineChart(lineEntries, xValue, listMenu);

                }else {
                    lineChartSat.clear();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Gagal memuat chart menu" +t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pengunjungList() {
        APIReport data = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> getData = data.statPengunjung(user,week,month,year,keterangan);

        getData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call,@NonNull Response<ResponseModel> response) {
                if(response.body()!=null) {
                    listPengunjung = response.body().getStatPengunjung();
                    if (listPengunjung != null) {
                        lineEntries = new ArrayList<>();
                        xValue = new ArrayList<>();
                        int index = 0;
                        int jumlah = 0;
                        for (StatModel model : listPengunjung) {
                            lineEntries.add(new Entry(index, model.getPengunjung()));
                            xValue.add(model.getTanggal());
                            index++;
                            jumlah += model.getPengunjung();
                        }
                        renderLineChart(lineEntries, xValue, listPengunjung);
                        tvStat.setText(String.format("Jumlah pengunjung periode ini : %s orang", jumlah));
                        tvStat.setVisibility(View.GONE);
                    } else {
                        lineChartSat.clear();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Gagal memuat chart pengunjung "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getStatCash(){
        xValue = new ArrayList<>();
        lineDataSets = new ArrayList<>();

        APIReport data = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> getData = data.statCashOut(user, week, month, year, keterangan);

        getData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if(response.body()!=null){
                    listCashOut = response.body().getStatCashOut();
                    int index2 = 0;
                    lineEntries2 = new ArrayList<>();
                    if(listCashOut!=null) {
                        for (StatModel statModel:listCashOut){
                            lineEntries2.add(new Entry(index2, statModel.getHarga()));
                            index2++;
                        }
                        lineDataSet2 = new LineDataSet(lineEntries2, "Out");
                        lineDataSet2.setDrawValues(false);
                        lineDataSet2.setColors(Color.RED);
                        lineDataSet2.setCircleColor(Color.RED);
                        lineDataSet2.setLineWidth(5f);
                        lineDataSet2.setDrawCircleHole(false);
                        lineDataSet2.setValueTextSize(15f);
                        lineDataSet2.setDrawFilled(true);
                        lineDataSet2.setFillColor(Color.RED);

                        XAxis xAxis = lineChartSat.getXAxis();
                        xAxis.setTextSize(2f);
                        xAxis.setGranularityEnabled(true);
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setDrawGridLines(false);
                        xAxis.setValueFormatter((value, axis) -> {
                            try {
                                int index1 = (int) value;
                                return xValue.get(index1);
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                                return "";
                            }
                        });

                        lineDataSets.add(lineDataSet2);
                    }
                }
                APIReport data2 = ServerConnection.connection().create(APIReport.class);
                Call<ResponseModel> getData2 = data2.statCashIn(user, week, month, year, keterangan);

                getData2.enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                        if(response.body()!=null){
                            listCashIn = response.body().getStatCashIn();
                            lineEntries = new ArrayList<>();
                            xValue = new ArrayList<>();

                            if(listCashIn!=null) {
                                int index = 0;
                                xValue = new ArrayList<>();
                                for (StatModel statModel : listCashIn) {
                                    lineEntries.add(new Entry(index, statModel.getHarga()));
                                    xValue.add(statModel.getTanggal().substring(0,10));
                                    index++;
                                }
                                lineDataSet = new LineDataSet(lineEntries, "In");
                                lineDataSet.setColors(Color.BLUE);
                                lineDataSet.setDrawValues(false);
                                lineDataSet.setCircleColor(Color.BLUE);
                                lineDataSet.setLineWidth(5f);
                                lineDataSet.setDrawCircleHole(false);
                                lineDataSet.setValueTextSize(15f);
                                lineDataSet.setDrawFilled(true);
                                lineDataSet.setFillColor(Color.BLUE);
                                lineDataSets.add(lineDataSet);

                                switch (filterBahan) {
                                    case "semua": {
                                        LineData lineData = new LineData(lineDataSets);
                                        lineChartSat.setData(lineData);
                                        lineChartSat.invalidate();
                                        lineChartSat.setVisibility(View.VISIBLE);
                                        lineChartSat.getAxisLeft().setDrawGridLines(false);
                                        lineChartSat.getAxisRight().setDrawLabels(false);
                                        lineChartSat.getDescription().setTextSize(10f);
                                        lineChartSat.animateX(1000);
                                        break;
                                    }
                                    case "barang_masuk": {
                                        LineData lineData = new LineData(lineDataSet);
                                        lineChartSat.setData(lineData);
                                        lineChartSat.invalidate();
                                        lineChartSat.setVisibility(View.VISIBLE);
                                        lineChartSat.getAxisLeft().setDrawGridLines(false);
                                        lineChartSat.getAxisRight().setDrawLabels(false);
                                        lineChartSat.getDescription().setTextSize(10f);
                                        lineChartSat.animateX(1000);
                                        break;
                                    }
                                    case "barang_keluar": {
                                        LineData lineData = new LineData(lineDataSet2);
                                        lineChartSat.setVisibility(View.VISIBLE);
                                        lineChartSat.getAxisLeft().setDrawGridLines(false);
                                        lineChartSat.getAxisRight().setDrawLabels(false);
                                        lineChartSat.getDescription().setTextSize(10f);
                                        lineChartSat.animateX(1000);
                                        lineChartSat.setTouchEnabled(true);
                                        lineChartSat.setData(lineData);
                                        lineChartSat.invalidate();
                                        break;
                                    }
                                }
                                StatModel maxCashIn = Collections.max(listCashIn);
                                StatModel maxCashOut = Collections.max(listCashOut);
                                StatModel minCashIn = Collections.min(listCashIn);
                                StatModel minCashOut = Collections.min(listCashOut);

                                tvStat.setText(String.format("Pemasukan terbesar Rp %s saat %s \n" +
                                        "Pemasukan terkecil Rp %s saat %s \n" +
                                        "Pengeluaran terbesar Rp %s saat %s \n" +
                                        "Pengeluaran terkecil Rp %s saat %s",
                                        maxCashIn.getHarga(), maxCashIn.getTanggal(),
                                        minCashIn.getHarga(), minCashIn.getTanggal(),
                                        maxCashOut.getHarga(), maxCashOut.getTanggal(),
                                        minCashOut.getHarga(), minCashOut.getTanggal()));
                                tvStat.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                        Toast.makeText(getActivity(), "Gagal memuat data: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Gagal memuat data: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void renderLineChart(ArrayList<Entry> entries, ArrayList<String> xValues, List<StatModel> list){
        if(list.get(0).getBahan()!=null)lineDataSet = new LineDataSet(entries, list.get(0).getBahan());
        else if(list.get(0).getMenu()!=null)lineDataSet = new LineDataSet(entries, list.get(0).getMenu());
        else lineDataSet = new LineDataSet(entries, "pengunjung");
        lineDataSet.setColors(Color.BLACK);
        lineDataSet.setDrawValues(false);
        lineDataSet.setValueTextSize(15f);
        LineData lineData = new LineData(lineDataSet);

        XAxis xAxis = lineChartSat.getXAxis();
        xAxis.setTextSize(2f);
        xAxis.setGranularityEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter((value, axis) -> {
            try {
                int index1 = (int) value;
                return xValues.get((index1));
            }catch (Exception e){
                return "";
            }
        });
        lineChartSat.setVisibility(View.VISIBLE);
        lineChartSat.getAxisLeft().setDrawGridLines(false);
        lineChartSat.getAxisRight().setDrawLabels(false);
        lineChartSat.animateX(1000);
        lineChartSat.setData(lineData);
//
    }

    private void renderBarChart(ArrayList<BarEntry> entries, ArrayList<String> xValues, List<StatModel> list){
        if(list.get(0).getSatuan()!=null) barDataSet = new BarDataSet(entries, list.get(0).getSatuan());
        else barDataSet = new BarDataSet(entries, "menu");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        if(!barDataSet.isDrawValuesEnabled()) barDataSet.setDrawValues(true);
        barDataSet.setValueTextSize(2f);
        barDataSet.setValueTextColor(Color.BLACK);
        BarData barData = new BarData(barDataSet);
        barData.setDrawValues(true);
        Legend legend = barChartStat.getLegend();
        legend.setTextSize(15f);
        barData.setBarWidth(0.5f);
        XAxis xAxis = barChartStat.getXAxis();
        xAxis.setTextSize(15f);
        xAxis.setGranularityEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter((value, axis) -> {
            try {
                int index1 = (int) value;
                return xValues.get((index1));
            }catch (Exception e){
                return "";
            }
        });
        barChartStat.setVisibility(View.VISIBLE);
        barChartStat.getAxisRight().setDrawGridLines(false);
        barChartStat.getAxisLeft().setDrawLabels(false);
        barChartStat.setData(barData);
        barChartStat.setDrawValueAboveBar(true);
        barChartStat.setFitBars(true);
        barChartStat.animateY(1000);
        barChartStat.getDescription().setEnabled(false);
    }
}
