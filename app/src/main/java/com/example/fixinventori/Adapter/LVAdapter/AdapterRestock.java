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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.fixinventori.API.APIRestock;
import com.example.fixinventori.API.ServerConnection;
import com.example.fixinventori.Activity.Restock.InventRestock;
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

public class AdapterRestock extends ArrayAdapter<KomposisiModel> {
    Context context;
    List<KomposisiModel> komposisiModels;
    ArrayList<RestockModel> listBahan;
    AdapterSpinnerKomposisi adapterSpinnerBahan;
    TextView tvBahan, tvSatuan, tvIdBahan, tvJumlah, tvSatuanx, tvHarga ;
    LinearLayout layoutKomposisi;
    String user, bahan, satuan, refBahan;
    UserSession userSession;
    Dialog dialog;
    int index, jumlah, id, harga, newId;
    Spinner spinner;
    EditText etJumlah, etHarga;
    Button btnEdit, btndelete,btnDismiss;
    CardView cvKomposisi;

    public AdapterRestock(Context context, List<KomposisiModel> objects) {
        super(context, R.layout.restock_row, objects);
        this.context = context;
        komposisiModels = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        userSession = new UserSession(context);
        user = userSession.getUserDetail().get("username");

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.restock_row, parent, false);

        tvIdBahan = convertView.findViewById(R.id.tvIdBahan);
        tvBahan = convertView.findViewById(R.id.tvBahan);
        tvJumlah = convertView.findViewById(R.id.tvJumlah);
        tvSatuan = convertView.findViewById(R.id.tvSatuan);
        layoutKomposisi = convertView.findViewById(R.id.layoutKomposisi);
        cvKomposisi = convertView.findViewById(R.id.cardKomposisi);
        tvHarga = convertView.findViewById(R.id.tvHarga);

        dialog = new Dialog(context);

        if(komposisiModels.get(position).getId() != -1){
            tvIdBahan.setText(String.valueOf(komposisiModels.get(position).getId()));
        }

        tvBahan.setText(komposisiModels.get(position).getBahan());
        tvJumlah.setText(String.valueOf(komposisiModels.get(position).getJumlah()));
        tvSatuan.setText(komposisiModels.get(position).getSatuan());
        tvHarga.setText(String.format("Rp %s",komposisiModels.get(position).getHarga()));

        cvKomposisi.setOnClickListener(view -> {

            id = komposisiModels.get(position).getId();
            refBahan = komposisiModels.get(position).getBahan();
            dialog.setContentView(R.layout.restock_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            listBahan();

            TextView tvBahan = dialog.findViewById(R.id.tvBahanUtama);
            btnEdit = dialog.findViewById(R.id.btnEditKomposisi);
            spinner = dialog.findViewById(R.id.spinnerBahanUtama);
            etJumlah = dialog.findViewById(R.id.etJumlahUtama);
            etHarga = dialog.findViewById(R.id.etPrice);
            tvSatuanx = dialog.findViewById(R.id.tvSatuanUtama);
            btndelete = dialog.findViewById(R.id.btnDeleteKomposisi);
            btnDismiss = dialog.findViewById(R.id.btnDismiss);

            tvBahan.setText(refBahan);
            etJumlah.setText(String.valueOf(komposisiModels.get(position).getJumlah()));
            etHarga.setText(String.valueOf(komposisiModels.get(position).getHarga()));

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
                if(etJumlah.getText().toString().isEmpty() || etHarga.getText().toString().isEmpty()){
                    etJumlah.setError("Harus diisi");
                }else {
                    jumlah = Integer.parseInt(etJumlah.getText().toString().trim());
                    harga = Integer.parseInt(etHarga.getText().toString().trim());
                }
                InventRestock.listId.set(position, newId);
                komposisiModels.get(position).setId(newId);
                komposisiModels.get(position).setBahan(bahan);
                komposisiModels.get(position).setJumlah(jumlah);
                komposisiModels.get(position).setSatuan(satuan);
                komposisiModels.get(position).setHarga(harga);
                notifyDataSetChanged();
                InventRestock.checkItem();

                //updateKomposisi();

                new Handler().postDelayed(() -> dialog.dismiss(),200);
            });

            btndelete.setOnClickListener(view1 -> {
                komposisiModels.remove(position);
                InventRestock.listId.remove(position);

                notifyDataSetChanged();
                if(komposisiModels.size()!=0){
                   InventRestock.layoutRestock.setVisibility(View.VISIBLE);
                   InventRestock.tvTotal.setText(String.format("Total %s item",komposisiModels.size()));
                }else {
                    InventRestock.layoutRestock.setVisibility(View.GONE);
                }
//                deleteKomposisi();
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
