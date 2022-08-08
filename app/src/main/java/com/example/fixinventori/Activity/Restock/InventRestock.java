package com.example.fixinventori.Activity.Restock;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
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
import androidx.appcompat.app.AppCompatActivity;

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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventRestock extends AppCompatActivity {

    Spinner spinRestock;
    ArrayList<RestockModel> restockList = new ArrayList<>();
    public ArrayList<KomposisiModel> listRestock;
    public static ArrayList<Integer> listId;
    ListView lvRestock;
    AdapterRestock adapterStock;
    TextView tvRestockSatuan;
    @SuppressLint("StaticFieldLeak")
    public static TextView tvTotal;
    EditText etRestockJumlah;
    AdapterSpinnerRestock adapterRestock;
    Button btnCollectRestock, btnAddRestocklist;
    String user, bahan, satuan, bahanI, satuanI, formatedTime;
    UserSession userSession;
    int id, jumlah, jumlahI, idI;
    @SuppressLint("StaticFieldLeak")
    public static LinearLayout layoutRestock;
    LocalDateTime time;
    String date, orderSeries, month;

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

        restockList = new ArrayList<>();
        listRestock = new ArrayList<>();
        listId = new ArrayList<>();

        adapterStock = new AdapterRestock(this, listRestock);
        checkList();

        restockList();
        spinRestock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tvRestockSatuan.setText(restockList.get(i).getSatuan());
                id = restockList.get(i).getId();
                bahan = restockList.get(i).getBahan_baku();
                satuan = restockList.get(i).getSatuan();
                Toast.makeText(InventRestock.this, "item: "+restockList.get(i).getBahan_baku(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        btnAddRestocklist.setOnClickListener(view -> {
            checkList();
            if(etRestockJumlah.getText().toString().isEmpty()){
                Toast.makeText(this, "Isi jumlah dulu", Toast.LENGTH_SHORT).show();
            }else {
                jumlah = Integer.parseInt(etRestockJumlah.getText().toString().trim());
                if (!listId.contains(id)) {
                    listRestock.add(new KomposisiModel(id, bahan, satuan, jumlah));
                    listId.add(id);
                    adapterStock.notifyDataSetChanged();
                    checkItem();
                    etRestockJumlah.setText(null);
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
            System.out.println(time);

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

    public void checkItem(){
        if(listRestock!=null){
            if(listRestock.size()!=0){
                layoutRestock.setVisibility(View.VISIBLE);
                tvTotal.setText(String.format("Total %s item", listRestock.size()));
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
                Toast.makeText(InventRestock.this, "gaga menambahkan stok "+t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void recordRestock(){
        APIReport record = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> recordData = record.recordRestock(orderSeries,bahanI,jumlahI,satuanI,user,formatedTime);

        recordData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call,@NonNull Response<ResponseModel> response) {

            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(InventRestock.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(InventRestock.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}