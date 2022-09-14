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
import androidx.appcompat.app.AppCompatActivity;

import com.example.fixinventori.API.APIKomposisiOpsi;
import com.example.fixinventori.API.APIRequestKomposisi;
import com.example.fixinventori.API.APIRequestMenu;
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

public class MenuSetDetail extends AppCompatActivity {

    EditText etDetailMenu, etDetailPrice, etDetailDesc,etDetailID, etJumlahy, etJumlahy2;
    TextView tvSatuany, tvSatuany2;
    Spinner spinerBahany, spinerBahany2;
    Button btnDetailSave, btnTambahKomposisiy, btnTambahKomposisiy2;
    ListView lvKomposisiy, lvKomposisiy2;
    List<KomposisiModel> komposisiModels, komposisiOpsiList;
    List<RestockModel> namaBahan, listBahan;
    AdapterSpinnerKomposisi adapterSpinnerBahan;
    AdapterKomposisi adapterKomposisi;
    AdapterKomposisiOpsi adapterKomposisiOpsi;
    int id,harga, jumlah,jumlahOpsi, komposisiId, i;
    String menu,deskripsi, bahan,bahanOpsi,satuanOpsi, satuan, user, menuBaru;
    UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_set_detail);

        userSession = new UserSession(getApplicationContext());
        user = userSession.getUserDetail().get("username");

        etDetailMenu = findViewById(R.id.etDetailMenu);
        etDetailPrice = findViewById(R.id.etDetailPrice);
        etDetailDesc = findViewById(R.id.etDetailDesc);
        etDetailID = findViewById(R.id.etDetailID);
        spinerBahany = findViewById(R.id.spinnerBahany);
        spinerBahany2 = findViewById(R.id.spinnerBahany2);
        etJumlahy = findViewById(R.id.etJumlahy);
        etJumlahy2 = findViewById(R.id.etJumlahy2);
        tvSatuany = findViewById(R.id.tvSatuany);
        tvSatuany2 = findViewById(R.id.tvSatuany2);
        lvKomposisiy = findViewById(R.id.lvKomposisiy);
        lvKomposisiy2 = findViewById(R.id.lvKomposisiy2);

        btnTambahKomposisiy = findViewById(R.id.btnTambahKomposisiy);
        btnTambahKomposisiy2 = findViewById(R.id.btnTambahKomposisiy2);
        btnDetailSave = findViewById(R.id.btnDetailSave);

        Intent intent = getIntent();

        id = intent.getIntExtra("menuId",-1);
        harga = intent.getIntExtra("menuPrice", -1);
        menu = intent.getStringExtra("menuName");
        deskripsi = intent.getStringExtra("menuDesc");

        getKomposisi();
        getKomposisiOpsi();
        listBahan();

        spinerBahany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!listBahan.get(i).getBahan_baku().equals("Pilih bahan")){
                    bahan = listBahan.get(i).getBahan_baku();
                    satuan = listBahan.get(i).getSatuan();
                    tvSatuany.setText(satuan);
                }else {
                    tvSatuany.setText(listBahan.get(0).getSatuan());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinerBahany2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!listBahan.get(i).getBahan_baku().equals("Pilih bahan")){
                    bahanOpsi = listBahan.get(i).getBahan_baku();
                    satuanOpsi = listBahan.get(i).getSatuan();
                    tvSatuany2.setText(satuanOpsi);
                }else {
                    tvSatuany2.setText(listBahan.get(0).getSatuan());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        etDetailID.setText(String.valueOf(id));
        etDetailMenu.setText(menu);
        etDetailPrice.setText(String.valueOf(harga));
        etDetailDesc.setText(deskripsi);

        btnDetailSave.setOnClickListener(view -> {
            id = Integer.parseInt(etDetailID.getText().toString().trim());
            harga = Integer.parseInt(etDetailPrice.getText().toString().trim());
            menuBaru = etDetailMenu.getText().toString().trim();
            deskripsi = etDetailDesc.getText().toString().trim();
            updateData();

            if(komposisiModels!=null) {
                for (i = 0; i < komposisiModels.size(); i++) {
                    View view1 = lvKomposisiy.getChildAt(i);
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
            }
            if(!etJumlahy.getText().toString().trim().isEmpty()){
                jumlah = Integer.parseInt(etJumlahy.getText().toString());
                addKomposisi();
            }

            finish();
        });

        btnTambahKomposisiy.setOnClickListener(view -> {
            if(spinerBahany.getSelectedItemPosition() == 0){
                Toast.makeText(this, "Pilih bahan", Toast.LENGTH_SHORT).show();
            }
            else if(etJumlahy.getText().toString().isEmpty()){
                etJumlahy.setError("harus diisi");
            }
            else {
                jumlah = Integer.parseInt(etJumlahy.getText().toString().trim());
                addKomposisi();
            }
        });

        btnTambahKomposisiy2.setOnClickListener(view -> {
            if(spinerBahany2.getSelectedItemPosition() == 0){
                Toast.makeText(this, "Pilih bahan", Toast.LENGTH_SHORT).show();
            }
            else if(etJumlahy2.getText().toString().isEmpty()){
                etJumlahy2.setError("harus diisi");
            }
            else {
                jumlahOpsi = Integer.parseInt(etJumlahy2.getText().toString().trim());
                addKomposisOpsi();
            }
        });

    }

    private void addKomposisOpsi() {
        APIKomposisiOpsi komposisiData = ServerConnection.connection().create(APIKomposisiOpsi.class);
        Call<ResponseModel> addKomposisiOpsi = komposisiData.addKomposisi(user,bahanOpsi, jumlahOpsi, satuanOpsi, menu);

        addKomposisiOpsi.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if(response.body()!=null){
                    int code = response.body().getCode();
                    String pesan = response.body().getPesan();
                    if(code == 2){
                        Toast.makeText(MenuSetDetail.this, pesan, Toast.LENGTH_SHORT).show();
                    }else if(code == 0){
                        Toast.makeText(MenuSetDetail.this, pesan, Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(MenuSetDetail.this, pesan, Toast.LENGTH_SHORT).show();
                        etJumlahy2.setText(null);
                        spinerBahany2.setSelection(0);
                        getKomposisiOpsi();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                t.fillInStackTrace();
            }
        });
    }

    public void updateData(){
        APIRequestMenu dataMenu = ServerConnection.connection().create(APIRequestMenu.class);
        Call<ResponseModel> update = dataMenu.updateMenu(id,menuBaru,harga, deskripsi,user,menu);

        update.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if(response.body()!=null){
                    String pesan = response.body().getPesan();
                    int code = response.body().getCode();
                    if(code ==2){
                        Toast.makeText(MenuSetDetail.this, pesan, Toast.LENGTH_SHORT).show();
                    }if(code ==0){
                        Toast.makeText(MenuSetDetail.this, pesan, Toast.LENGTH_SHORT).show();
                    }else
                    Toast.makeText(MenuSetDetail.this, "behasil",
                        Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(MenuSetDetail.this, "Gagal: "+t.getMessage(),
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
                if(response.body() != null) {
                    komposisiModels = response.body().getKomposisiModelList();
                    if (komposisiModels != null) {
                        adapterKomposisi = new AdapterKomposisi(MenuSetDetail.this, komposisiModels);
                        lvKomposisiy.setAdapter(adapterKomposisi);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(MenuSetDetail.this, "gagal memuat: "+t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }

    private void getKomposisiOpsi(){
        APIKomposisiOpsi dataKomposisi = ServerConnection.connection().create(APIKomposisiOpsi.class);
        Call<ResponseModel> komposisi = dataKomposisi.getKomposisi(
                menu, user);
        komposisi.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if(response.body() != null) {
                    komposisiOpsiList = new ArrayList<>();
                    komposisiOpsiList = response.body().getKomposisiOpsiList();
                    if (komposisiOpsiList != null) {
                        adapterKomposisiOpsi = new AdapterKomposisiOpsi(MenuSetDetail.this, komposisiOpsiList);
                        lvKomposisiy2.setAdapter(adapterKomposisiOpsi);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(MenuSetDetail.this, "gagal memuat: "+t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }

    private void addKomposisi(){
        APIRequestKomposisi dataKomposisi = ServerConnection.connection().create(APIRequestKomposisi.class);
        Call<ResponseModel> tambahKompsisi = dataKomposisi.addKomposisi(
                user,bahan, jumlah, satuan, menu);
        tambahKompsisi.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if(response.body()!=null) {
                    int code = response.body().getCode();
                    String pesan = response.body().getPesan();
                    if (code == 2) {
                        Toast.makeText(MenuSetDetail.this, pesan, Toast.LENGTH_SHORT).show();
                    } else if (code == 0) {
                        Toast.makeText(MenuSetDetail.this, pesan, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MenuSetDetail.this, pesan, Toast.LENGTH_SHORT).show();
                        etJumlahy.setText(null);
                        spinerBahany.setSelection(0);
                        getKomposisi();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(MenuSetDetail.this, "Komposisi gagal ditambahkan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateKomposisi(){
        APIRequestKomposisi dataKomposisi = ServerConnection.connection().create(APIRequestKomposisi.class);
        Call<ResponseModel> ubahKomposisi = dataKomposisi.updateKomposisi(
                komposisiId, bahan, jumlah, satuan);
        ubahKomposisi.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call,@NonNull Response<ResponseModel> response) {
                Toast.makeText(MenuSetDetail.this, "Berhasil merubah", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call,@NonNull Throwable t) {
                Toast.makeText(MenuSetDetail.this, "Gagal merubah: "+t.getMessage(),
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
                if(response.body() != null) {
                    namaBahan = new ArrayList<>();
                    listBahan = new ArrayList<>();
                    listBahan.add(0, new RestockModel(-1, "Pilih bahan", "satuan"));
                    namaBahan = response.body().getStocks();
                    for (int i = 0; i < namaBahan.size(); i++) {
                        listBahan.add(new RestockModel(namaBahan.get(i).getId(),
                                namaBahan.get(i).getBahan_baku(), namaBahan.get(i).getSatuan()));
                    }
                    if (listBahan != null) {
                        adapterSpinnerBahan = new AdapterSpinnerKomposisi(MenuSetDetail.this, listBahan);
                        spinerBahany.setAdapter(adapterSpinnerBahan);
                        spinerBahany2.setAdapter(adapterSpinnerBahan);
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