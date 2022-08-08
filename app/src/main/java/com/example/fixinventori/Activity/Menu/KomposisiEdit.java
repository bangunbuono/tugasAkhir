package com.example.fixinventori.Activity.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fixinventori.API.APIRestock;
import com.example.fixinventori.API.ServerConnection;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.Adapter.SpinnerAdapter.AdapterSpinnerKomposisi;
import com.example.fixinventori.R;
import com.example.fixinventori.model.ResponseModel;
import com.example.fixinventori.model.RestockModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KomposisiEdit extends AppCompatActivity {

    EditText etJumlahx;
    Spinner spinnerBahanx;
    TextView tvSatuanx, tvEditBahan, tvEditJumlah, tvEditSatuan;
    List<RestockModel> listBahan, namaBahan;
    AdapterSpinnerKomposisi adapterSpinnerBahan;
    String bahan, satuan, user, dataBahan, dataSatuan;
    int dataId, dataJumlah;
    UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_komposisi_edit);

        userSession = new UserSession(getApplicationContext());
        user = userSession.getUserDetail().get("username");

        spinnerBahanx = findViewById(R.id.spinnerBahanx);
        etJumlahx = findViewById(R.id.etJumlahx);
        tvSatuanx = findViewById(R.id.tvSatuanx);
        tvEditBahan = findViewById(R.id.tvEditBahan);
        tvEditSatuan = findViewById(R.id.tvEditSatuan);
        tvEditJumlah = findViewById(R.id.tvEditJumlah);

        Intent intent = getIntent();
        dataBahan = intent.getStringExtra("bahan");
        dataSatuan = intent.getStringExtra("satuan");
        dataId = intent.getIntExtra("id", -1);
        dataJumlah = intent.getIntExtra("jumlah", -1);

        tvEditBahan.setText(dataBahan);
        tvEditSatuan.setText(dataSatuan);
        tvEditJumlah.setText(String.valueOf(dataJumlah));

        listBahan();

        spinnerBahanx.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (listBahan.get(i).getBahan_baku().equals("Pilih bahan")){
                    bahan = listBahan.get(0).getBahan_baku();
                    tvSatuanx.setText(listBahan.get(0).getSatuan());
                }else {
                    bahan = listBahan.get(i).getBahan_baku();
                    satuan = listBahan.get(i).getSatuan();
                    Toast.makeText(KomposisiEdit.this, bahan + satuan, Toast.LENGTH_SHORT).show();
                    tvSatuanx.setText(satuan);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void listBahan(){
        APIRestock restockData = ServerConnection.connection().create(APIRestock.class);
        Call<ResponseModel> getData = restockData.getStock(user);

        getData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if(response.body() != null){
                    namaBahan = new ArrayList<>();
                    listBahan = new ArrayList<>();
                    listBahan.add(0, new RestockModel(-1, "Pilih bahan", "satuan"));
                    namaBahan = response.body().getStocks();
                    for (int i = 0; i < namaBahan.size(); i++){
                        listBahan.add(new RestockModel(namaBahan.get(i).getId(),
                                namaBahan.get(i).getBahan_baku(),namaBahan.get(i).getSatuan()));
                    }
                    if(listBahan != null){
                        adapterSpinnerBahan = new AdapterSpinnerKomposisi(KomposisiEdit.this,listBahan);
                        spinnerBahanx.setAdapter(adapterSpinnerBahan);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                t.fillInStackTrace();
            }
        });
    }
}