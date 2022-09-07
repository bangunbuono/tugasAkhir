package com.example.fixinventori.Activity.Stock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fixinventori.API.APIRequestStock;
import com.example.fixinventori.API.ServerConnection;
import com.example.fixinventori.Activity.Usage.InventUsage;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.R;
import com.example.fixinventori.model.ResponseModel;
import com.example.fixinventori.model.StocksModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CombineStockDetail extends AppCompatActivity {

    TextView tvMaterialCombine;
    EditText etStockName, etStockJumlah, etStockSatuan, etStockID;
    Button btnStockSave, btnAddCombineQty;
    UserSession userSession;
    String user, bahan_baku, satuan, bahanBaru;
    int id, jumlah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combine_stock_detail);

        etStockID = findViewById(R.id.etStockCombineID);
        etStockJumlah = findViewById(R.id.etStockCombineJumlah);
        etStockName = findViewById(R.id.etStockCombineName);
        etStockSatuan = findViewById(R.id.etStockCombineSatuan);
        tvMaterialCombine = findViewById(R.id.tvMaterialCombine);
        btnStockSave = findViewById(R.id.btnStockCombineSave);
        btnAddCombineQty = findViewById(R.id.btnAddCombineQty);

        userSession = new UserSession(getApplicationContext());
        user = userSession.getUserDetail().get("username");

        Intent intent = getIntent();
        id = intent.getIntExtra("id",-1);
        jumlah = intent.getIntExtra("jumlah",-1);
        bahan_baku = intent.getStringExtra("bahan");
        satuan = intent.getStringExtra("satuan");

        etStockID.setText(String.valueOf(id));
        etStockSatuan.setText(satuan);
        etStockName.setText(bahan_baku);
        etStockJumlah.setText(String.valueOf(jumlah));

        getCombine();

        btnAddCombineQty.setOnClickListener(view ->{
            Intent intent1 = new Intent(this, InventUsage.class);
            intent1.putExtra("fragment", "combine");
            startActivity(intent1);
        });

        btnStockSave.setOnClickListener(view -> {
            if(etStockJumlah.getText().toString().isEmpty()||etStockSatuan.getText().toString().isEmpty()||
                    etStockName.getText().toString().isEmpty())
                Toast.makeText(this, "Field kosong \nIsi terlebih dahulu", Toast.LENGTH_SHORT).show();
            else {
                bahanBaru = etStockName.getText().toString().trim();
                jumlah = Integer.parseInt(etStockJumlah.getText().toString().trim());
                satuan = etStockSatuan.getText().toString().trim();

                updateStock();
            }
        });
    }

    private void getCombine(){
        APIRequestStock data = ServerConnection.connection().create(APIRequestStock.class);
        Call<ResponseModel> getData = data.combineDetail(bahan_baku,user);

        getData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call,@NonNull Response<ResponseModel> response) {
                if (response.body() != null) {
                    List<StocksModel> combineList = response.body().getCombineDetail();
                    StringBuilder bahan = new StringBuilder("Dari campuran bahan\n");
                    if(combineList!=null) {
                        for (int i = 0; i < combineList.size(); i++) {
                            bahan.append(combineList.get(i).getBahan_baku()).append("\n");
                            System.out.println(combineList.get(i).getBahan_baku());
                        }
                        tvMaterialCombine.setText(bahan.toString());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(CombineStockDetail.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStock(){
        APIRequestStock stockData = ServerConnection.connection().create(APIRequestStock.class);
        Call<ResponseModel> updataData = stockData.updateData(
                id,bahan_baku,jumlah,satuan,0,0,0,user,bahanBaru);

        updataData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                int code;
                if (response.body() != null) {
                    code = response.body().getCode();
                    if(code==2) Toast.makeText(CombineStockDetail.this, "Nama bahan sudah digunakan", Toast.LENGTH_SHORT).show();
                    else updateCombine();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(CombineStockDetail.this, "gagal: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCombine(){
        APIRequestStock stockData = ServerConnection.connection().create(APIRequestStock.class);
        Call<ResponseModel> updataData = stockData.updateCombineData(bahan_baku,user,bahanBaru);

        updataData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                finish();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(CombineStockDetail.this, "gagal2: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}