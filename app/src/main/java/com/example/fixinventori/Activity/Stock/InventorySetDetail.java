package com.example.fixinventori.Activity.Stock;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fixinventori.API.APIRequestStock;
import com.example.fixinventori.API.ServerConnection;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.R;
import com.example.fixinventori.model.ResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventorySetDetail extends AppCompatActivity {

    EditText etStockName, etStockJumlah, etStockSatuan, etStockID, etStockWaktu, etStockMinPesan;
    Button btnStockSave;
    int id, waktu, min_pesan, jumlah;
    String bahan_baku,bahanBaru, satuan, user;
    UserSession userSession;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_set_detail);

        userSession = new UserSession(getApplicationContext());
        user = userSession.getUserDetail().get("username");

        etStockID = findViewById(R.id.etStockID);
        etStockJumlah = findViewById(R.id.etStockJumlah);
        etStockName = findViewById(R.id.etStockName);
        etStockSatuan = findViewById(R.id.etStockSatuan);
        etStockWaktu = findViewById(R.id.etStockWaktu);
        etStockMinPesan = findViewById(R.id.etStockMinPesan);
        btnStockSave = findViewById(R.id.btnStockSave);

        Intent intent = getIntent();
        id = intent.getIntExtra("id",-1);
        waktu = intent.getIntExtra("waktu",-1);
        jumlah = intent.getIntExtra("jumlah",-1);
        min_pesan = intent.getIntExtra("min_pesan",-1);
        bahan_baku = intent.getStringExtra("bahan");
        satuan = intent.getStringExtra("satuan");

        etStockID.setText(id+"");
        etStockWaktu.setText(waktu+"" );
        etStockSatuan.setText(satuan);
        etStockName.setText(bahan_baku);
        etStockMinPesan.setText(min_pesan+"");
        etStockJumlah.setText(jumlah+"");
        System.out.println(bahan_baku);

        btnStockSave.setOnClickListener(view -> {
            id = Integer.parseInt(etStockID.getText().toString().trim());
            bahanBaru = etStockName.getText().toString().trim();
            jumlah = Integer.parseInt(etStockJumlah.getText().toString().trim());
            waktu = Integer.parseInt(etStockWaktu.getText().toString().trim());
            min_pesan = Integer.parseInt(etStockMinPesan.getText().toString().trim());
            satuan = etStockSatuan.getText().toString().trim();

            updateStock();
        });

    }

    private void updateStock(){
        APIRequestStock stockData = ServerConnection.connection().create(APIRequestStock.class);
        Call<ResponseModel> updataData = stockData.updateData(
                id,bahan_baku,jumlah,satuan,min_pesan,waktu,user,bahanBaru);

        updataData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                finish();
                Toast.makeText(InventorySetDetail.this, "berhasil menyimpan", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(InventorySetDetail.this, "gagal: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}