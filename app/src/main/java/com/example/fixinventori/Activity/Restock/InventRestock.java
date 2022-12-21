package com.example.fixinventori.Activity.Restock;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fixinventori.API.APIForecast;
import com.example.fixinventori.API.APIReport;
import com.example.fixinventori.API.APIRequestStock;
import com.example.fixinventori.API.APIRestock;
import com.example.fixinventori.API.ServerConnection;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.Adapter.LVAdapter.AdapterRestock;
import com.example.fixinventori.Adapter.SpinnerAdapter.AdapterSpinnerRestock;
import com.example.fixinventori.R;
import com.example.fixinventori.model.KomposisiModel;
import com.example.fixinventori.model.ResponseModel;
import com.example.fixinventori.model.RestockModel;
import com.example.fixinventori.model.StocksModel;
import com.example.fixinventori.model.TimeSeriesModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventRestock extends AppCompatActivity {

    Spinner spinRestock;
    ArrayList<RestockModel> restockList = new ArrayList<>();
    List<TimeSeriesModel> history;
    List<StocksModel> stocksModelList;
    public static ArrayList<KomposisiModel> listRestock;
    public static ArrayList<Integer> listId;
    ListView lvRestock;
    AdapterRestock adapterStock;
    TextView tvRestockSatuan, tvRecommendation;
    @SuppressLint("StaticFieldLeak")
    public static TextView tvTotal;
    EditText etRestockJumlah, etRestockPrice;
    AdapterSpinnerRestock adapterRestock;
    Button btnCollectRestock, btnAddRestocklist;
    String user;
    String bahan;
    String satuan;
    String bahanI;
    String satuanI;
    String formatedTime;
    UserSession userSession;
    int id, jumlah, jumlahI, idI, harga, hargaItem, dayForecast;
    public static int hargaI;
    @SuppressLint("StaticFieldLeak")
    public static LinearLayout layoutRestock;
    LocalDateTime time;
    String date, orderSeries, month;
    ArrayList<Float> ma4, ma4x4, at, tt, forecast, forecastList;
    float total, recentStock;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invent_restock);

        userSession = new UserSession(getApplicationContext());
        user = userSession.getUserDetail().get("username");

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        spinRestock = findViewById(R.id.spinRestock);
        tvRestockSatuan = findViewById(R.id.tvRestockSatuan);
        btnCollectRestock = findViewById(R.id.btnCollect);
        etRestockJumlah = findViewById(R.id.etRestockJumlah);
        btnAddRestocklist = findViewById(R.id.btnAddRestockList);
        lvRestock = findViewById(R.id.lvRestock);
        tvTotal = findViewById(R.id.tvTotalRestock);
        layoutRestock = findViewById(R.id.layoutRestock);
        etRestockPrice = findViewById(R.id.etRestockPrice);
        tvRecommendation = findViewById(R.id.tvRecomendation);
        progressBar = findViewById(R.id.progressBar);

        restockList = new ArrayList<>();
        listRestock = new ArrayList<>();
        listId = new ArrayList<>();

        adapterStock = new AdapterRestock(this, listRestock);
        checkList();

        restockList();
        loadForecasting(true);

        spinRestock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loadForecasting(true);
                forecastList = new ArrayList<>();
                total = 0;
                tvRestockSatuan.setText(restockList.get(i).getSatuan());
                id = restockList.get(i).getId();
                bahan = restockList.get(i).getBahan_baku();
                satuan = restockList.get(i).getSatuan();
                Toast.makeText(InventRestock.this, "item: "+restockList.get(i).getBahan_baku(),
                        Toast.LENGTH_SHORT).show();

                getStocks(bahan);

                for (int j = 0; j < 7; j++) {
                    dayForecast = j;
                    if(j+1==6 || j+1==7)
                    getTimeSeriesHolt(
                            getTodaySQL(j+1), bahan);
                    else getTimeSeriesDMA(getTodaySQL(j+1),bahan);
                }

                new Handler().postDelayed(()->{
                    if(forecastList!=null) {
                        if(recentStock<total) {
                            tvRecommendation.setText(String.format(
                                    "Rekomendasi jumlah pembelian %s: %s %s", bahan, total-recentStock, satuan));
                        }else tvRecommendation.setText(String.format("bahan %s masih cukup", bahan));
                        loadForecasting(false);
                    }else tvRecommendation.setText(R.string.dataKurang);
                }, 2000);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        btnAddRestocklist.setOnClickListener(view -> {
            checkList();
            if(etRestockJumlah.getText().toString().isEmpty() || etRestockPrice.getText().toString().isEmpty()){
                Toast.makeText(this, "Isi semua field dahulu", Toast.LENGTH_SHORT).show();
            }else {
                jumlah = Integer.parseInt(etRestockJumlah.getText().toString().trim());
                harga = Integer.parseInt((etRestockPrice.getText().toString().trim()));
                if (!listId.contains(id)) {
                    listRestock.add(new KomposisiModel(id, bahan, satuan, jumlah, harga));
                    listId.add(id);
                    adapterStock.notifyDataSetChanged();
                    checkItem();
                    etRestockJumlah.setText(null);
                    etRestockPrice.setText(null);
                    spinRestock.setSelection(0);
                } else {
                    Toast.makeText(this, "Bahan sudah ada di daftar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCollectRestock.setOnClickListener(view1 -> {
            time = LocalDateTime.now();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMddHHmmss");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter monthType = DateTimeFormatter.ofPattern("MM/yyyy");
            month = monthType.format(time);
            formatedTime = timeFormatter.format(time);
            date = dtf.format(time);
            orderSeries = "C."+date;

            if(listRestock!=null && listRestock.size()!=0){
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage("Konfirmasi stok masuk");
                dialog.setPositiveButton("Iya", (dialogInterface, i) -> {
                    record();
                    for (i = 0; i<listRestock.size();i++){
                        View view2 = lvRestock.getChildAt(i);
                        TextView tvIdBahan = view2.findViewById(R.id.tvIdBahan);
                        TextView tvJumlah = view2.findViewById(R.id.tvJumlah);
                        TextView tvBahan = view2.findViewById(R.id.tvBahan);
                        TextView tvSatuan = view2.findViewById(R.id.tvSatuan);

                        idI = Integer.parseInt(tvIdBahan.getText().toString().trim());
                        jumlahI = Integer.parseInt(tvJumlah.getText().toString().trim());
                        bahanI = tvBahan.getText().toString().trim();
                        satuanI = tvSatuan.getText().toString().trim();
                        hargaItem = listRestock.get(i).getHarga();
                        recordRestock();
                        restockAdd();
                    }
                    finish();
                });
                dialog.setNegativeButton("Batal", (dialogInterface, i) -> {

                });
                dialog.show();

            }
        });

    }

    private void checkList(){
        if(listRestock!=null){
            lvRestock.setAdapter(adapterStock);
        }
    }

    public static void checkItem(){
        if(listRestock!=null){
            if(listRestock.size()!=0){
                hargaI = 0;
                for(KomposisiModel model: listRestock){
                    hargaI += model.getHarga();
                }
                layoutRestock.setVisibility(View.VISIBLE);
                tvTotal.setText(String.format("Total %s item = Rp %s", listRestock.size(), hargaI));
            }else {
                layoutRestock.setVisibility(View.GONE);
            }
        }
    }

    private void restockList(){
        APIRestock restockData = ServerConnection.connection().create(APIRestock.class);
        Call<ResponseModel> getData = restockData.getStock(user);

        getData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call,@NonNull Response<ResponseModel> response) {
                if (response.body() != null) {
                    restockList = response.body().getStocks();
                    adapterRestock = new AdapterSpinnerRestock(InventRestock.this, restockList);
                    if(restockList!=null)
                        spinRestock.setAdapter(adapterRestock);
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(InventRestock.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.fillInStackTrace();
            }
        });
    }

    private void restockAdd(){
        APIRequestStock stockData = ServerConnection.connection().create(APIRequestStock.class);
        Call<ResponseModel> addStock = stockData.addStocks(idI,jumlahI);

        addStock.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull retrofit2.Response<ResponseModel> response) {
                etRestockJumlah.setText(null);
                Toast.makeText(InventRestock.this, "berhasil menambahkan stok", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(InventRestock.this, "gagal menambahkan stok "+t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void recordRestock(){
        APIReport record = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> recordData = record.recordRestock(orderSeries,bahanI,jumlahI,satuanI,user,formatedTime,hargaItem);

        recordData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call,@NonNull Response<ResponseModel> response) {

            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(InventRestock.this, "gagal record restock"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void record(){
        APIReport record = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> recordData = record.record(orderSeries,"barang masuk",
                formatedTime, user, month);

        recordData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {

            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(InventRestock.this, "gagal record"+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    void getTimeSeriesDMA(int day, String bahan){
        APIForecast data = ServerConnection.connection().create(APIForecast.class);
        Call<ResponseModel> getData = data.getHistory(user, day);

        getData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if(response.isSuccessful() && response.body()!=null){
                    history = response.body().getHistory();
                    if (history != null) {
                        for (int i = 0; i < history.size(); i++) {
                            if(history.get(i).getBahan().equals(bahan)){
                                ArrayList<Integer> demand = new ArrayList<>();
                                history.get(i).getData().forEach(data1 -> demand.add(data1.getJumlah()));
                                if(demand.size()>=8) {
                                    float y = doubleMA(demand);
                                    total = total + y;
                                    forecastList.add(y);
                                    System.out.println("DMA: "+ dayForecast+" "+y);
                                }
                                else {
                                    total = total + 0;
                                    System.out.println(0);
//                                    forecastList.add(0f);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(InventRestock.this, "Gagal mengambil data "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void getTimeSeriesHolt(int day, String bahan){
        APIForecast data = ServerConnection.connection().create(APIForecast.class);
        Call<ResponseModel> getData = data.getHistory(user, day);

        getData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if(response.isSuccessful() && response.body()!=null){
                    history = response.body().getHistory();
                    if (history != null) {
                        for (int i = 0; i < history.size(); i++) {
                            if(history.get(i).getBahan().equals(bahan)){
                                ArrayList<Integer> demand = new ArrayList<>();
                                history.get(i).getData().forEach(data1 -> demand.add(data1.getJumlah()));
                                    if(demand.size()>=4) {
                                        float y = holtForecaster(demand);
                                        total = total + y;
                                        System.out.println("Holt: "+ dayForecast+" "+y);
                                        forecastList.add(y);
                                    }
                                    else {
                                        total = total + 0;
                                        System.out.println(0);
//                                        forecastList.add(0f);
                                    }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(InventRestock.this, "Gagal mengambil data "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static int getTodaySQL(int day){
        if(day==7) return 1;
        else return day+1;
    }

    private float doubleMA(ArrayList<Integer> data){
        if(ma4!=null) ma4.clear();
        if(ma4x4!=null) ma4x4.clear();
        ma4 = new ArrayList<>();
        ma4x4 = new ArrayList<>();
//        long start = System.currentTimeMillis();
        for (int i = 0; i < data.size()-4; i++) {
            ma4.add((data.get(i)+data.get(i+1)+data.get(i+2)+data.get(i+3))/4f);
        }
        for (int i = 0; i < ma4.size()-4; i++) {
            ma4x4.add((ma4.get(i)+ma4.get(i+1)+ma4.get(i+2)+ma4.get(i+3))/4f);
//            System.out.println(2f*ma4.get(i+3)- ma4x4.get(i)+2/3f*(ma4.get(i+3)-ma4x4.get(i))*1);
        }
//        float elapsedTime = System.currentTimeMillis()-start;
        return Math.abs(2f*ma4.get(ma4.size()-2)- ma4x4.get(ma4x4.size()-1)+
                2/3f*(ma4.get(ma4.size()-2)-ma4x4.get(ma4x4.size()-1))*2);

    }

    private float holtForecaster(ArrayList<Integer> demand){
        if(at!=null) at.clear();
        if(tt!=null) tt.clear();
        if(forecast!=null) forecast.clear();
        at = new ArrayList<>();
        tt = new ArrayList<>();
        forecast = new ArrayList<>();

        float alpha = 0.1f;
        float beta = 0.01f;

        at.add(0, demand.get(0).floatValue());
        tt.add(0, demand.get(1)- demand.get(0).floatValue());
        for (int i = 1; i < demand.size(); i++) {
            at.add(alpha* demand.get(i)+(1-alpha)*(at.get(i-1)+tt.get(i-1)));
            tt.add(beta*(at.get(i)-at.get(i-1)) + (1-beta)*tt.get(i-1));
            forecast.add(i-1, Math.abs(at.get(i-1)+tt.get(i-1)*1));
        }

//        forecast.forEach(System.out::println);
        return Math.abs(at.get(at.size()-1)+ tt.get(tt.size()-1)*1);
    }

    private void loadForecasting(boolean isLoadForecast){
        if (!isLoadForecast) {
            progressBar.setVisibility(View.GONE);
            tvRecommendation.setVisibility(View.VISIBLE);
        }else {
            progressBar.setVisibility(View.VISIBLE);
            tvRecommendation.setVisibility(View.GONE);
        }
    }

    private void getStocks(String bahan){
        APIRequestStock stockData = ServerConnection.connection().create(APIRequestStock.class);
        Call<ResponseModel> showData = stockData.showStock(user);

        showData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if(response.body()!= null)
                    stocksModelList = response.body().getStocksModels();
                if(stocksModelList != null){
                    for (int i = 0; i < stocksModelList.size(); i++) {
                        if(stocksModelList.get(i).getBahan_baku().equals(bahan)){
//                            System.out.println(stocksModelList.get(i).getBahan_baku());
                            recentStock = stocksModelList.get(i).getJumlah();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call,@NonNull Throwable t) {
                Toast.makeText(InventRestock.this, "Gagal memuat stock: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}