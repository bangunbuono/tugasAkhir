package com.example.fixinventori.Activity.Stock;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fixinventori.API.APIRequestStock;
import com.example.fixinventori.API.ServerConnection;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.Adapter.LVAdapter.AdapterStocks;
import com.example.fixinventori.R;
import com.example.fixinventori.model.ResponseModel;
import com.example.fixinventori.model.StocksModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventorySet extends AppCompatActivity{

    ListView lvStocks;
    AdapterStocks adapterStocks;
    List<StocksModel> stocksModelList;
    TextView tvAddStock, tvTotalItem;
    String user;
    int totalDay;
    UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_set);

        userSession = new UserSession(this);
        user = userSession.getUserDetail().get("username");

        totalDay();

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if(result.getResultCode()==RESULT_OK){
                        getStocks();
                    }
                });

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        lvStocks = findViewById(R.id.lvStocks);
        tvAddStock = findViewById(R.id.tvAddStock);
        tvTotalItem = findViewById(R.id.tvTotalItem);

        stocksModelList = new ArrayList<>();

        new Handler().postDelayed(this::getStocks, 300);


        tvAddStock.setOnClickListener(view -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Tambah Bahan");
            dialog.setMessage("Ingin kombinasikan bahan atau tambah bahan baru");
            dialog.setPositiveButton("Tambah", (dialogInterface, i) -> {
                Intent intent = new Intent(InventorySet.this, AddStock.class);
                intent.putExtra("user", user);
                launcher.launch(intent);
            });
            dialog.setNegativeButton("Kombinasikan bahan", (dialogInterface, i) -> {
                Intent intent = new Intent(InventorySet.this, CombineStocksActivity.class);
                intent.putExtra("user", user);
                launcher.launch(intent);
            });
            dialog.show();
        });

    }
    private void getStocks(){
        APIRequestStock stockData = ServerConnection.connection().create(APIRequestStock.class);
        Call<ResponseModel> showData = stockData.showStock(user);

        showData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if(response.body()!= null)
                stocksModelList = response.body().getStocksModels();
                if(stocksModelList != null){
                    adapterStocks = new AdapterStocks(InventorySet.this, stocksModelList, totalDay);
                    new Handler().postDelayed(()->{
                        lvStocks.setAdapter(adapterStocks);
                        tvTotalItem.setText(String.format("PERSEDIAAN BAHAN (%s ITEM)", stocksModelList.size()));
                    },500);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call,@NonNull Throwable t) {
                Toast.makeText(InventorySet.this, "Gagal memuat stock: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void totalDay(){
        APIRequestStock data = ServerConnection.connection().create(APIRequestStock.class);
        Call<ResponseModel> getData = data.totalDay(user);

        getData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call,@NonNull Response<ResponseModel> response) {
                List<StocksModel> totalList;
                if(response.body()!=null){
                    totalList = response.body().getAvgDay();
                    if(totalList!=null) totalDay = totalList.get(0).getTotal();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call,@NonNull Throwable t) {
                Toast.makeText(InventorySet.this, "Gagal mendapat jumlah hari "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.inventory, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== R.id.refresh){
            getStocks();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getStocks();
    }
}
