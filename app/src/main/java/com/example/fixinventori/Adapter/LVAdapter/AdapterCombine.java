package com.example.fixinventori.Adapter.LVAdapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fixinventori.API.APIRestock;
import com.example.fixinventori.API.ServerConnection;
import com.example.fixinventori.Activity.Stock.CombineStocksActivity;
import com.example.fixinventori.Activity.User.UserSession;
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

public class AdapterCombine extends ArrayAdapter<KomposisiModel> {
    Context context;
    List<KomposisiModel> komposisiModels;
    ArrayList<RestockModel> listBahan;
    AdapterSpinnerKomposisi adapterSpinnerBahan;
    TextView tvBahan, tvSatuan, tvIdBahan, tvJumlah, tvSatuanx, tvHarga;
    String user, bahan, satuan, refBahan;
    UserSession userSession;
    Dialog dialog;
    int index, jumlah, id, newId;
    Spinner spinner;
    EditText etJumlah, etHarga;
    Button btnEdit, btndelete,btnDismiss;

    public AdapterCombine(Context context, List<KomposisiModel> objects) {
        super(context, R.layout.stock_row, objects);
        this.context = context;
        komposisiModels = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        userSession = new UserSession(context);
        user = userSession.getUserDetail().get("username");

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.stock_row, parent, false);

        tvIdBahan = convertView.findViewById(R.id.tvIdStock);
        tvBahan = convertView.findViewById(R.id.tvStockName);
        tvJumlah = convertView.findViewById(R.id.tvStockJumlah);
        tvSatuan = convertView.findViewById(R.id.tvStockSatuan);

        dialog = new Dialog(context);

        if(komposisiModels.get(position).getId() != -1){
            tvIdBahan.setText(String.valueOf(komposisiModels.get(position).getId()));
        }

        tvBahan.setText(komposisiModels.get(position).getBahan());
        tvJumlah.setText(String.valueOf(komposisiModels.get(position).getJumlah()));
        tvSatuan.setText(komposisiModels.get(position).getSatuan());

        convertView.setOnClickListener(view -> {
            id = komposisiModels.get(position).getId();
            refBahan = komposisiModels.get(position).getBahan();
            dialog.setContentView(R.layout.restock_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            listBahan();

            TextView tvBahan = dialog.findViewById(R.id.tvBahanUtama);
            btnEdit = dialog.findViewById(R.id.btnEditKomposisi);
            spinner = dialog.findViewById(R.id.spinnerBahanUtama);
            etJumlah = dialog.findViewById(R.id.etJumlahUtama);
            tvSatuanx = dialog.findViewById(R.id.tvSatuanUtama);
            btndelete = dialog.findViewById(R.id.btnDeleteKomposisi);
            btnDismiss = dialog.findViewById(R.id.btnDismiss);
            etHarga = dialog.findViewById(R.id.etPrice);
            tvHarga = dialog.findViewById(R.id.tvHarga);

            tvHarga.setVisibility(View.GONE);
            etHarga.setVisibility(View.GONE);
            tvHarga.setEnabled(false);
            etHarga.setEnabled(false);

            tvBahan.setText(refBahan);
            etJumlah.setText(String.valueOf(komposisiModels.get(position).getJumlah()));

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    newId = listBahan.get(i).getId();
                    bahan = listBahan.get(i).getBahan_baku();
                    satuan = listBahan.get(i).getSatuan();
                    tvSatuanx.setText(satuan);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            btnEdit.setOnClickListener(view1 -> {
                if(etJumlah.getText().toString().isEmpty()){
                    etJumlah.setError("Harus diisi");
                }else {
                    jumlah = Integer.parseInt(etJumlah.getText().toString().trim());
                }
                CombineStocksActivity.listBahan.set(position, bahan);
                komposisiModels.get(position).setId(newId);
                komposisiModels.get(position).setBahan(bahan);
                komposisiModels.get(position).setJumlah(jumlah);
                komposisiModels.get(position).setSatuan(satuan);
                notifyDataSetChanged();
                System.out.println(newId+bahan+jumlah);
                new Handler().postDelayed(() -> dialog.dismiss(),200);
            });

            btndelete.setOnClickListener(view1 -> {
                komposisiModels.remove(position);
                CombineStocksActivity.listBahan.remove(position);
                notifyDataSetChanged();
                if(komposisiModels.size()!=0){
                  CombineStocksActivity.llDaftarCombine.setVisibility(View.VISIBLE);
                }else {
                    CombineStocksActivity.llDaftarCombine.setVisibility(View.GONE);
                }
                dialog.dismiss();
            });
            btnDismiss.setOnClickListener(view1 -> dialog.dismiss());

            dialog.show();

        });

        return convertView;
    }

    private void listBahan() {

        APIRestock stockData = ServerConnection.connection().create(APIRestock.class);
        Call<ResponseModel> getStock = stockData.getStock(user);

        getStock.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                listBahan = new ArrayList<>();
                if(response.body() != null) {
                    listBahan = response.body().getStocks();
                    if (listBahan != null) {
                        adapterSpinnerBahan = new AdapterSpinnerKomposisi(context, listBahan);
                        spinner.setAdapter(adapterSpinnerBahan);
                        for (int i = 0; i < listBahan.size(); i++) {
                            String data = listBahan.get(i).getBahan_baku();
                            if (data.equals(refBahan)) {
                                index = i;
                                break;
                            }
                        }
                        spinner.setSelection(index);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {

            }
        });
    }
}
