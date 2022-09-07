package com.example.fixinventori.Adapter.LVAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.fixinventori.API.APIRequestStock;
import com.example.fixinventori.API.ServerConnection;
import com.example.fixinventori.Activity.Stock.CombineStockDetail;
import com.example.fixinventori.Activity.Stock.InventorySetDetail;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.R;
import com.example.fixinventori.model.ResponseModel;
import com.example.fixinventori.model.StocksModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterStocks extends ArrayAdapter<StocksModel> {
    Context context;
    List<StocksModel> stocksModelList;
    List<StocksModel> list;
    TextView tvIdStock, tvStockName, tvStockJumlah, tvStockSatuan;
    int index, posisi;
    String bahan, user;
    UserSession session;
//            id, waktu, min_pesan,jumlah;
//    String bahan_baku, satuan;

    public AdapterStocks(Context context, List<StocksModel> objects) {
        super(context, R.layout.stock_row, objects);
        this.context = context;
        stocksModelList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.stock_row, parent, false);

        session = new UserSession(context);
        user = session.getUserDetail().get("username");

        tvIdStock = convertView.findViewById(R.id.tvIdStock);
        tvStockName = convertView.findViewById(R.id.tvStockName);
        tvStockJumlah = convertView.findViewById(R.id.tvStockJumlah);
        tvStockSatuan = convertView.findViewById(R.id.tvStockSatuan);

        if(stocksModelList.size() != 0){
            tvIdStock.setText(String.valueOf(stocksModelList.get(position).getId()));
            tvStockName.setText(stocksModelList.get(position).getBahan_baku());
            tvStockJumlah.setText(String.valueOf(stocksModelList.get(position).getJumlah()));
            tvStockSatuan.setText(stocksModelList.get(position).getSatuan());
        }

        convertView.setOnClickListener(view -> {
            index = stocksModelList.get(position).getId();
            stockDetail();

        });

        convertView.setOnLongClickListener(view -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("Hapus bahan");
            dialog.setMessage("Akan menghapus komposisi pada menu yang mengandung bahan ini");
            dialog.setPositiveButton("iya", (dialogInterface, i) -> {
               index = stocksModelList.get(position).getId();
               posisi = position;
               bahan = stocksModelList.get(position).getBahan_baku();
               deleteBahan();
            });
            dialog.setNegativeButton("batal", (dialogInterface, i) -> {

            });

            dialog.show();
            return false;
        });

        return convertView;
    }

    private void stockDetail(){
        APIRequestStock stockData = ServerConnection.connection().create(APIRequestStock.class);
        Call<ResponseModel> dataDetail = stockData.showDetail(index);

        dataDetail.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {

                if (response.body() != null) {
                    list = response.body().getStocksModels();
                    int stockId = list.get(0).getId();
                    int stockWaktu = list.get(0).getWaktu();
                    int stockMinPesan = list.get(0).getMin_pesan();
                    String bahanBaku = list.get(0).getBahan_baku();
                    String stockSatuan = list.get(0).getSatuan();
                    int stockJumlah = list.get(0).getJumlah();
                    int waktu_max = list.get(0).getWaktu_max();
                    String keterangan = list.get(0).getKeterangan();

                    if(keterangan.equals("utama")){
                        Intent intent = new Intent(context, InventorySetDetail.class);
                        intent.putExtra("id", stockId);
                        intent.putExtra("bahan", bahanBaku);
                        intent.putExtra("jumlah", stockJumlah);
                        intent.putExtra("satuan", stockSatuan);
                        intent.putExtra("min_pesan", stockMinPesan);
                        intent.putExtra("waktu", stockWaktu);
                        intent.putExtra("waktuMax", waktu_max);
                        intent.putExtra("keterangan", keterangan);
                        context.startActivity(intent);
                    }else if(keterangan.equals("kombinasi")){
                        Intent intent = new Intent(context, CombineStockDetail.class);
                        intent.putExtra("id", stockId);
                        intent.putExtra("bahan", bahanBaku);
                        intent.putExtra("jumlah", stockJumlah);
                        intent.putExtra("satuan", stockSatuan);
                        intent.putExtra("keterangan", keterangan);
                        context.startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(context, "gagal memuat: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void deleteBahan(){
        APIRequestStock deleteData = ServerConnection.connection().create(APIRequestStock.class);
        Call<ResponseModel> delete = deleteData.deleteStock(index, bahan, user);

        delete.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                assert response.body()!=null;
                String pesan = response.body().getPesan();
                stocksModelList.remove(posisi);
                notifyDataSetChanged();
                Toast.makeText(context, pesan, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call,@NonNull Throwable t) {

            }
        });
    }
}
