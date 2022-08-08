package com.example.fixinventori.Activity.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fixinventori.API.APIKomposisiOpsi;
import com.example.fixinventori.API.APIRequestKomposisi;
import com.example.fixinventori.API.APIRestock;
import com.example.fixinventori.API.ServerConnection;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.Adapter.LVAdapter.AdapterKomposisi;
import com.example.fixinventori.Adapter.LVAdapter.AdapterKomposisiOpsi;
import com.example.fixinventori.Adapter.SpinnerAdapter.AdapterSpinnerKomposisi;
import com.example.fixinventori.R;
import com.example.fixinventori.model.KomposisiModel;
import com.example.fixinventori.model.ResponseModel;
import com.example.fixinventori.model.RestockModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KomposisiSet extends AppCompatActivity {

    EditText etJumlahx, etJumlahx2;
    TextView tvKomposisi, tvSatuanx, tvSatuanx2;
    Spinner spinnerBahanx, spinnerBahanx2;
    Button btnTambahKomposisi, btnTambahKomposisiOpsi, btnSimpanKomposisi, btnCancel;
    ListView lvKomposisi, lvKomposisiOpsi;
    AdapterKomposisi adapterKomposisi;
    AdapterKomposisiOpsi adapterKomposisiOpsi;
    List<KomposisiModel> listKomposisi, listKomposisiOpsi;
    List<RestockModel> listBahan, namaBahan;
    AdapterSpinnerKomposisi adapterSpinnerBahan;
    String bahan,satuan,menu, user, bahanOpsi, satuanOpsi;
    int jumlah,jumlahOpsi, i, komposisiId;
    UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_komposisi_set);

        userSession = new UserSession(getApplicationContext());
        user = userSession.getUserDetail().get("username");

        spinnerBahanx = findViewById(R.id.spinnerBahanx);
        etJumlahx = findViewById(R.id.etJumlahx);
        tvSatuanx = findViewById(R.id.tvSatuanx);
        spinnerBahanx2 = findViewById(R.id.spinnerBahanx2);
        etJumlahx2 = findViewById(R.id.etJumlahx2);
        tvSatuanx2 = findViewById(R.id.tvSatuanx2);
        lvKomposisi = findViewById(R.id.lvKomposisi);
        lvKomposisiOpsi = findViewById(R.id.lvKomposisiOpsi);
        tvKomposisi = findViewById(R.id.tvKomposisi);
        btnTambahKomposisi = findViewById(R.id.btnTambahKomposisi);
        btnTambahKomposisiOpsi = findViewById(R.id.btnTambahKomposisiOpsi);
        btnSimpanKomposisi = findViewById(R.id.btnSimpanKomposisi);
        btnCancel = findViewById(R.id.btnCancelKomposisi);

        Intent intent = getIntent();
        menu = intent.getStringExtra("menu");
        tvKomposisi.setText(intent.getStringExtra("menu"));

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
                    Toast.makeText(KomposisiSet.this, bahan + satuan, Toast.LENGTH_SHORT).show();
                    tvSatuanx.setText(satuan);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerBahanx2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (listBahan.get(i).getBahan_baku().equals("Pilih bahan")){
                    bahanOpsi = listBahan.get(0).getBahan_baku();
                    tvSatuanx2.setText(listBahan.get(0).getSatuan());
                }else {
                    bahanOpsi = listBahan.get(i).getBahan_baku();
                    satuanOpsi = listBahan.get(i).getSatuan();
                    Toast.makeText(KomposisiSet.this, bahanOpsi + satuanOpsi, Toast.LENGTH_SHORT).show();
                    tvSatuanx2.setText(satuanOpsi);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnTambahKomposisi.setOnClickListener(view -> {
//            spinnerBahanx.setVisibility(View.VISIBLE);
//            tvSatuanx.setVisibility(View.VISIBLE);
//            etJumlahx.setVisibility(View.VISIBLE);

            if(etJumlahx.getText().toString().isEmpty()){
                etJumlahx.setError("harus diisi");
            }
            else {
                jumlah = Integer.parseInt(etJumlahx.getText().toString().trim());
                addKomposisi();
            }
        });

        btnTambahKomposisiOpsi.setOnClickListener(view -> {
            if(etJumlahx2.getText().toString().isEmpty() || etJumlahx2.getText().toString().equals("")){
                etJumlahx2.setError("harus diisi");
            }
            else if(bahanOpsi.equals("Pilih bahan")){
                Toast.makeText(this, "Pilih bahan dulu", Toast.LENGTH_SHORT).show();
            }
            else {
                jumlahOpsi = Integer.parseInt(etJumlahx2.getText().toString().trim());
                addKomposisiOpsi();
                System.out.println(bahanOpsi+ jumlahOpsi+satuanOpsi);
            }
        });

        btnSimpanKomposisi.setOnClickListener(view -> {
            if(listKomposisi != null){
                for(i = 0; i<listKomposisi.size();i++){
                    View view1 = lvKomposisi.getChildAt(i);
                    TextView etIdBahan = view1.findViewById(R.id.tvIdBahan);
                    TextView etBahan = view1.findViewById(R.id.tvBahan);
                    TextView etJumlah = view1.findViewById(R.id.tvJumlah);
                    TextView etSatuan = view1.findViewById(R.id.tvSatuan);

                    komposisiId = Integer.parseInt(etIdBahan.getText().toString().trim());
                    bahan = etBahan.getText().toString().trim();
                    jumlah = Integer.parseInt(etJumlah.getText().toString().trim());
                    satuan = etSatuan.getText().toString().trim();

                    updateKomposisi();
                }
                if(etJumlahx.getText().toString().trim().isEmpty()){
                    Toast.makeText(this, "isi jumlah dulu", Toast.LENGTH_SHORT).show();
                }else if(etJumlahx.getText().toString().equals("")){
                    Toast.makeText(this, "isi jumlah dulu", Toast.LENGTH_SHORT).show();
                }
                else if(bahan.equals("Pilih bahan")){
                    Toast.makeText(this, "isi jumlah dulu", Toast.LENGTH_SHORT).show();
                } else {
                    int i = spinnerBahanx.getSelectedItemPosition();
                    bahan = listBahan.get(i).getBahan_baku();
                    satuan = tvSatuanx.getText().toString().trim();
                    jumlah = Integer.parseInt(etJumlahx.getText().toString().trim());
                    addKomposisi();
                }
                finish();
            }

        });

        btnCancel.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(KomposisiSet.this);
            builder.setTitle("Batalkan komposisi");
            builder.setMessage("Pembatalan dapat menghapus semua komposisi" + "\n" +
                    "konfimasi?");
            builder.setNegativeButton("Tidak", (dialogInterface, i) -> {

            });
            builder.setPositiveButton("Iya", (dialogInterface, i) -> {
                cancelKomposisi();
                cancelKomposisiOpsi();
                finish();
            });
            builder.show();
        });

    }

    private void addKomposisi(){
        APIRequestKomposisi dataKomposisi = ServerConnection.connection().create(APIRequestKomposisi.class);
        Call<ResponseModel> tambahKompsisi = dataKomposisi.addKomposisi(user, bahan, jumlah, satuan, menu);
        tambahKompsisi.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                assert response.body()!=null;
                int code = response.body().getCode();
                if(code == 2){
                    Toast.makeText(KomposisiSet.this, "Gagal: komposisi sudah ada", Toast.LENGTH_SHORT).show();
                }else if(code == 0){
                    Toast.makeText(KomposisiSet.this, "Gagal menambahkan komposisi", Toast.LENGTH_SHORT).show();
                }else {
                    etJumlahx.setText(null);
                    spinnerBahanx.setSelection(0);
                    tvSatuanx.setText(listBahan.get(i).getSatuan());
                    bahan = "";
                    satuan = "";
                    getKomposisi();
                    Toast.makeText(KomposisiSet.this, "Komposisi ditambahkan",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(KomposisiSet.this, "Komposisi gagal ditambahkan",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getKomposisi(){
        APIRequestKomposisi dataKomposisi = ServerConnection.connection().create(APIRequestKomposisi.class);
        Call<ResponseModel> komposisi = dataKomposisi.getKomposisi(
                menu, user);
        komposisi.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                assert response.body() != null;
                listKomposisi = response.body().getKomposisiModelList();
                if (listKomposisi != null){
                    adapterKomposisi = new AdapterKomposisi(KomposisiSet.this,listKomposisi);
                    lvKomposisi.setAdapter(adapterKomposisi);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(KomposisiSet.this, "gagal memuat: "+t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }

    private void updateKomposisi(){
        APIRequestKomposisi dataKomposisi = ServerConnection.connection().create(APIRequestKomposisi.class);
        Call<ResponseModel> ubahKomposisi = dataKomposisi.updateKomposisi(
                komposisiId, bahan, jumlah, satuan);
        ubahKomposisi.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                bahan = "";
                satuan = "";
                Toast.makeText(KomposisiSet.this, "Berhasil merubah", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(KomposisiSet.this, "Gagal merubah: "+t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }

    private void listBahan(){
        APIRestock restockData = ServerConnection.connection().create(APIRestock.class);
        Call<ResponseModel> getData = restockData.getStock(user);

        getData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                assert response.body() != null;
                namaBahan = new ArrayList<>();
                listBahan = new ArrayList<>();
                listBahan.add(0, new RestockModel(-1, "Pilih bahan", "satuan"));
                namaBahan = response.body().getStocks();
                for (int i = 0; i < namaBahan.size(); i++){
                    listBahan.add(new RestockModel(namaBahan.get(i).getId(),
                            namaBahan.get(i).getBahan_baku(),namaBahan.get(i).getSatuan()));
                }
                if(listBahan != null){
                    adapterSpinnerBahan = new AdapterSpinnerKomposisi(KomposisiSet.this,listBahan);
                    spinnerBahanx.setAdapter(adapterSpinnerBahan);
                    spinnerBahanx2.setAdapter(adapterSpinnerBahan);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                t.fillInStackTrace();
            }
        });
    }

    private void cancelKomposisi(){
        APIRequestKomposisi cancelData = ServerConnection.connection().create(APIRequestKomposisi.class);
        Call<ResponseModel> cancelKomposisi = cancelData.cancelKomposisi(menu, user);

        cancelKomposisi.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {

            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call,@NonNull Throwable t) {

            }
        });
    }

    private void addKomposisiOpsi(){
        APIKomposisiOpsi komposisiData = ServerConnection.connection().create(APIKomposisiOpsi.class);
        Call<ResponseModel> addKomposisi = komposisiData.addKomposisi(
                user, bahanOpsi,jumlahOpsi, satuanOpsi, menu);

        addKomposisi.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call,@NonNull Response<ResponseModel> response) {
                if(response.body()!=null){
                    int code = response.body().getCode();
                    String pesan = response.body().getPesan();
                    if(code == 2){
                        Toast.makeText(KomposisiSet.this, pesan, Toast.LENGTH_SHORT).show();
                    }else if(code == 0){
                        Toast.makeText(KomposisiSet.this, pesan, Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(KomposisiSet.this, pesan, Toast.LENGTH_SHORT).show();
                        etJumlahx2.setText(null);
                        spinnerBahanx2.setSelection(0);
                        getKomposisiOpsi();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                t.fillInStackTrace();
            }
        });
        etJumlahx2.setText(null);
        spinnerBahanx2.setSelection(0);
    }

    private void getKomposisiOpsi(){
        APIKomposisiOpsi komposisiData = ServerConnection.connection().create(APIKomposisiOpsi.class);
        Call<ResponseModel> getData = komposisiData.getKomposisi(menu, user);

        getData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                listKomposisiOpsi = new ArrayList<>();
                listKomposisiOpsi = response.body().getKomposisiOpsiList();
                if( listKomposisiOpsi != null){
                    adapterKomposisiOpsi = new AdapterKomposisiOpsi(KomposisiSet.this, listKomposisiOpsi);
                    lvKomposisiOpsi.setAdapter(adapterKomposisiOpsi);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {

            }
        });

    }
    private void cancelKomposisiOpsi(){
        APIKomposisiOpsi komposisiData = ServerConnection.connection().create(APIKomposisiOpsi.class);
        Call<ResponseModel> cancel = komposisiData.cancelKomposisi(menu, user);

        cancel.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {

            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {

            }
        });
    }
}