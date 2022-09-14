package com.example.fixinventori.Activity.Stock;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fixinventori.API.APIReport;
import com.example.fixinventori.API.APIRequestStock;
import com.example.fixinventori.API.APIRestock;
import com.example.fixinventori.API.ServerConnection;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.Adapter.LVAdapter.AdapterCombine;
import com.example.fixinventori.Adapter.SpinnerAdapter.AdapterSpinnerRestock;
import com.example.fixinventori.R;
import com.example.fixinventori.model.KomposisiModel;
import com.example.fixinventori.model.ResponseModel;
import com.example.fixinventori.model.RestockModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CombineStocksActivity extends AppCompatActivity {
    Spinner spinnerCombine;
    EditText etCombine, etBahanCombine, etJumlahCombine;
    AutoCompleteTextView etSatuanCombine;
    TextView tvCombine;
    public static LinearLayout llDaftarCombine;
    ListView lvCombine;
    Button btnAddCombineList, btnConfirmCombine;
    UserSession session;
    String user, bahan, satuan, bahanAkhir, satuanAkhir, bahanI, satuanI, month, orderSeries, date, formatedTime;
    int jumlah, jumlahAkhir, jumlahI;
    List<RestockModel> restockList = new ArrayList<>();
    public static List<String> listBahan = new ArrayList<>();
    public static List<KomposisiModel> listCombine = new ArrayList<>();
    AdapterSpinnerRestock adapterSpinnerRestock;
    AdapterCombine adapterCombineList;
    LocalDateTime time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combine_stocks);

        session = new UserSession(this);
        user = session.getUserDetail().get("username");

        spinnerCombine = findViewById(R.id.spinnerCombine);
        etCombine = findViewById(R.id.etCombine);
        etBahanCombine = findViewById(R.id.etBahanCombine);
        etJumlahCombine = findViewById(R.id.etJumlahCombine);
        etSatuanCombine = findViewById(R.id.etSatuanCombine);
        tvCombine = findViewById(R.id.tvCombine);
        btnAddCombineList = findViewById(R.id.btnAddCombineList);
        btnConfirmCombine = findViewById(R.id.btnConfirmCombine);
        llDaftarCombine = findViewById(R.id.llDaftarCombine);
        lvCombine = findViewById(R.id.lvCombine);

        String[] units = {"gram", "ml", "kilogram", "liter","buah"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, units);
        etSatuanCombine.setAdapter(adapter);

        adapterCombineList = new AdapterCombine(this, listCombine);
        restockList();

        spinnerCombine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bahan = restockList.get(i).getBahan_baku();
                satuan = restockList.get(i).getSatuan();
                tvCombine.setText(satuan);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        btnAddCombineList.setOnClickListener(view -> {
            checkList();
            if(etCombine.getText().toString().isEmpty())
                Toast.makeText(this, "Isi Field Jumlah Dahulu", Toast.LENGTH_SHORT).show();
            else {
                jumlah = Integer.parseInt(etCombine.getText().toString().trim());
                if(!listBahan.contains(bahan)){
                    listCombine.add(new KomposisiModel(bahan,satuan,jumlah));
                    listBahan.add(bahan);
                    adapterCombineList.notifyDataSetChanged();
                    checkItem();
                    etCombine.setText(null);
                    spinnerCombine.setSelection(0);
                }else Toast.makeText(this, "Bahan Sudah Ada", Toast.LENGTH_SHORT).show();
            }
        });

        btnConfirmCombine.setOnClickListener(view->{
            if(etBahanCombine.getText().toString().isEmpty() || etJumlahCombine.getText().toString().isEmpty()
                    || etSatuanCombine.getText().toString().isEmpty())
                Toast.makeText(this, "Isi Semua Field Dahulu", Toast.LENGTH_SHORT).show();
            else {
                bahanAkhir = etBahanCombine.getText().toString();
                jumlahAkhir = Integer.parseInt(etJumlahCombine.getText().toString().trim());
                satuanAkhir = etSatuanCombine.getText().toString();

                if(satuanAkhir.equals("kg") || satuanAkhir.equals("kilogram")) {
                    satuanAkhir = "gram";
                    jumlahAkhir = jumlah*1000;
                }else if(satuanAkhir.equals("liter") || satuanAkhir.equals("l")){
                    satuanAkhir = "ml";
                    jumlahAkhir = jumlah*1000;
                }

                time = LocalDateTime.now();
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMddHHmmss");
                DateTimeFormatter timeStamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                DateTimeFormatter monthType = DateTimeFormatter.ofPattern("MM/yyyy");
                month = monthType.format(time);
                formatedTime = timeStamp.format(time);
                date = dtf.format(time);
                orderSeries = "B."+date;

                if(listCombine.size()>=2) addDataBahan();
                else Toast.makeText(CombineStocksActivity.this, "Bahan yang dicampur " +
                            "hanya 1", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void restockList(){
        APIRestock restockData = ServerConnection.connection().create(APIRestock.class);
        Call<ResponseModel> getData = restockData.getStock(user);

        getData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if (response.body() != null) {
                    restockList = response.body().getStocks();
                    adapterSpinnerRestock = new AdapterSpinnerRestock(CombineStocksActivity.this, restockList);
                    spinnerCombine.setAdapter(adapterSpinnerRestock);
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(CombineStocksActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.fillInStackTrace();
            }
        });
    }

    private void checkList(){
        if(restockList!=null){
            lvCombine.setAdapter(adapterCombineList);
        }
    }

   public static void checkItem(){
       if(listCombine!=null){
           if(listCombine.size()!=0){
               llDaftarCombine.setVisibility(View.VISIBLE);
           }else {
               llDaftarCombine.setVisibility(View.GONE);
           }
       }
   }

   private void addDataBahan(){
       APIRequestStock data = ServerConnection.connection().create(APIRequestStock.class);
       Call<ResponseModel> addData = data.addCombineData(bahanAkhir,jumlahAkhir,satuanAkhir,"kombinasi",user);

       addData.enqueue(new Callback<ResponseModel>() {
           @Override
           public void onResponse(@NonNull Call<ResponseModel> call,@NonNull Response<ResponseModel> response) {
               if (response.body() != null) {
                   int code = response.body().getCode();
                   if(code == 1) {
                       record();
                       for (int i = 0; i < listCombine.size(); i++) {
                           View view1 = lvCombine.getChildAt(i);

                           TextView tvBahan = view1.findViewById(R.id.tvStockName);
                           TextView tvJumlah = view1.findViewById(R.id.tvStockJumlah);
                           TextView tvSatuan = view1.findViewById(R.id.tvStockSatuan);

                           bahanI = tvBahan.getText().toString().trim();
                           jumlahI = Integer.parseInt(tvJumlah.getText().toString().trim());
                           satuanI = tvSatuan.getText().toString().trim();
                           addCombineStock();
                           stockOutCombine();
                           reportManualUsage();

                           if(i== listCombine.size()) {
                               listBahan.clear();
                               listCombine.clear();
                               finish();
                           }
                       }
                       finish();
                   }else
                       Toast.makeText(CombineStocksActivity.this, "Nama" +
                               " bahan hasil kombinasi sudah digunakan \n" +
                               "Gunakan nama bahan lain", Toast.LENGTH_SHORT).show();
               }
           }

           @Override
           public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
               Toast.makeText(CombineStocksActivity.this, "Gagal menambah bahan"+t.getMessage(), Toast.LENGTH_SHORT).show();
           }
       });
   }

   private void addCombineStock(){
        APIRequestStock data = ServerConnection.connection().create(APIRequestStock.class);
        Call<ResponseModel> addData = data.addCombineStock(bahanAkhir, bahanI, satuanI, user);

        addData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call,@NonNull Response<ResponseModel> response) {

            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call,@NonNull Throwable t) {
                System.out.println("gagal"+t.getMessage());
                Toast.makeText(CombineStocksActivity.this, "Gagal"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
   }

   private void stockOutCombine(){
        APIRequestStock data = ServerConnection.connection().create(APIRequestStock.class);
        Call<ResponseModel> updateData = data.stockOutCombineData(bahanI, jumlahI, user);

        updateData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {

            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call,@NonNull Throwable t) {
                Toast.makeText(CombineStocksActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
   }

    private void reportManualUsage(){
        APIReport report = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> reportData = report.recordUsage(
                orderSeries, bahanI, jumlahI, satuanI,"kombinasi", user,1,formatedTime );

        reportData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call,@NonNull Response<ResponseModel> response) {

            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call,@NonNull Throwable t) {
                Toast.makeText(CombineStocksActivity.this, "report gagal: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void record(){
        APIReport recordData = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> record = recordData.record(orderSeries,"barang keluar", formatedTime, user, month);

        record.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {

            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(CombineStocksActivity.this, "record gagal: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}