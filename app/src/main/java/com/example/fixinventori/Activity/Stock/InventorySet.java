package com.example.fixinventori.Activity.Stock;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventorySet extends AppCompatActivity{

    ListView lvStocks;
    AdapterStocks adapterStocks;
    List<StocksModel> stocksModelList;
    TextView tvAddStock;
    String user;
    UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_set);

        userSession = new UserSession(getApplicationContext());
        user = userSession.getUserDetail().get("username");

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if(result.getResultCode()==RESULT_OK){
                        getStocks();
                    }
                });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvStocks = findViewById(R.id.lvStocks);
        tvAddStock = findViewById(R.id.tvAddStock);
        stocksModelList = new ArrayList<>();
        getStocks();

        tvAddStock.setOnClickListener(view -> {
            Intent intent = new Intent(InventorySet.this, AddStock.class);
            intent.putExtra("user", user);
            launcher.launch(intent);
        });

    }
    private void getStocks(){
        APIRequestStock stockData = ServerConnection.connection().create(APIRequestStock.class);
        Call<ResponseModel> showData = stockData.showStock(user);

        showData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                stocksModelList = response.body().getStocksModels();
                if(stocksModelList != null){
                    adapterStocks = new AdapterStocks(InventorySet.this, stocksModelList);
                    lvStocks.setAdapter(adapterStocks);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(InventorySet.this, "Gagal memuat stock: "+t.getMessage(), Toast.LENGTH_SHORT).show();
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
