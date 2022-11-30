package com.example.fixinventori.Activity.Stock;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class AddStock extends AppCompatActivity {

    EditText etNameStock, etJumlah, etStockPesan, etStockWaktuTgu, etStockWaktuTguMax;
    AutoCompleteTextView etSatuan;
    TextView tvStockUser;
    Button btnStockAdd;
    int waktu, min_pesan, jumlah, waktuMax;
    String bahan_baku, satuan, user;
    UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);

        userSession = new UserSession(getApplicationContext());
        user = userSession.getUserDetail().get("username");

        tvStockUser = findViewById(R.id.tvStockUser);
        etJumlah = findViewById(R.id.etJumlahStock);
        etNameStock = findViewById(R.id.etNameStock);
        etSatuan = findViewById(R.id.etSatuanStock);
        etStockPesan = findViewById(R.id.etPesanStock);
        etStockWaktuTgu = findViewById(R.id.etStockWaktuTgu);
        etStockWaktuTguMax = findViewById(R.id.etStockWaktuTguMax);
        btnStockAdd = findViewById(R.id.btnStockAdd);
        tvStockUser.setText(user);

        String[] units = {"gram", "ml", "kilogram", "liter","buah"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, units);
        etSatuan.setAdapter(adapter);

        btnStockAdd.setOnClickListener(view -> {
            if(etJumlah.getText().toString().isEmpty() || etNameStock.getText().toString().isEmpty() ||
                    etSatuan.getText().toString().isEmpty() || etStockPesan.getText().toString().isEmpty() ||
                    etStockWaktuTgu.getText().toString().isEmpty() ||
                    etStockWaktuTguMax.getText().toString().isEmpty() ){
                Toast.makeText(AddStock.this, "Harus diisi", Toast.LENGTH_SHORT).show();
            }
            else {
                waktuMax = Integer.parseInt(etStockWaktuTguMax.getText().toString().trim());
                bahan_baku = etNameStock.getText().toString().trim();
                jumlah = Integer.parseInt(etJumlah.getText().toString().trim());
                waktu = Integer.parseInt(etStockWaktuTgu.getText().toString().trim());
                min_pesan = Integer.parseInt(etStockPesan.getText().toString().trim());
                satuan = etSatuan.getText().toString().trim();
                if(satuan.equals("kg") || satuan.equals("kilogram")
                        || satuan.equals("Kg") || satuan.equals("KG") || satuan.equals("Kilogram")) {
                    satuan = "gram";
                    jumlah = jumlah*1000;
                    min_pesan = min_pesan*100;
                }else if(satuan.equals("liter") || satuan.equals("l") || satuan.equals("L")
                        || satuan.equals("Liter")){
                    satuan = "ml";
                    jumlah = jumlah*1000;
                    min_pesan = min_pesan*1000;
                }
                addStock();
            }
        });
    }

    private void addStock(){
        APIRequestStock stockData = ServerConnection.connection().create(APIRequestStock.class);
        Call<ResponseModel> addData = stockData.addData(bahan_baku, jumlah, satuan, min_pesan, waktu, waktuMax, user);

        addData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if (response.body() != null) {
                    int code = response.body().getCode();
                    String pesan = response.body().getPesan();
                    if (code == 2) {
                        Toast.makeText(AddStock.this, "Gagal: " + pesan, Toast.LENGTH_SHORT).show();
                    } else if (code == 0) {
                        Toast.makeText(AddStock.this, "Gagal input data", Toast.LENGTH_SHORT).show();
                    } else {
                        setResult(RESULT_OK);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call,@NonNull Throwable t) {
                Toast.makeText(AddStock.this, "gagal: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}