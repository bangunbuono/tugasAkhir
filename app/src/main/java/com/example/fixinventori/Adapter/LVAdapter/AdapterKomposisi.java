package com.example.fixinventori.Adapter.LVAdapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.fixinventori.API.APIRequestKomposisi;
import com.example.fixinventori.API.APIRestock;
import com.example.fixinventori.API.ServerConnection;
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

public class AdapterKomposisi extends ArrayAdapter<KomposisiModel> {
    Context context;
    List<KomposisiModel> komposisiModels;
    ArrayList<RestockModel> listBahan;
    AdapterSpinnerKomposisi adapterSpinnerBahan;
    TextView tvBahan, tvSatuan, tvIdBahan, tvJumlah, tvSatuanx ;
    LinearLayout layoutKomposisi;
    String user, bahan, satuan, refBahan;
    UserSession userSession;
    Dialog dialog;
    int index, jumlah, id;
    Spinner spinner;
    EditText etJumlah;
    Button btnEdit, btndelete,btnDismiss;
    CardView cvKomposisi;

    public AdapterKomposisi(Context context, List<KomposisiModel> objects) {
        super(context, R.layout.komposisi_row,objects);

        this.context = context;
        komposisiModels = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        userSession = new UserSession(context);
        user = userSession.getUserDetail().get("username");

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.komposisi_row, parent, false);

        tvIdBahan = convertView.findViewById(R.id.tvIdBahan);
        tvBahan = convertView.findViewById(R.id.tvBahan);
        tvJumlah = convertView.findViewById(R.id.tvJumlah);
        tvSatuan = convertView.findViewById(R.id.tvSatuan);
        layoutKomposisi = convertView.findViewById(R.id.layoutKomposisi);
        cvKomposisi = convertView.findViewById(R.id.cardKomposisi);

        dialog = new Dialog(context);

        if(komposisiModels.get(position).getId() != -1){
            tvIdBahan.setText(String.valueOf(komposisiModels.get(position).getId()));
        }
        tvBahan.setText(komposisiModels.get(position).getBahan());
        tvJumlah.setText(String.valueOf(komposisiModels.get(position).getJumlah()));
        tvSatuan.setText(komposisiModels.get(position).getSatuan());

        cvKomposisi.setOnClickListener(view -> {
            id = komposisiModels.get(position).getId();
            refBahan = komposisiModels.get(position).getBahan();
            listBahan();
            dialog.setContentView(R.layout.komposisi_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            TextView tvBahan = dialog.findViewById(R.id.tvBahanUtama);
            btnEdit = dialog.findViewById(R.id.btnEditKomposisi);
            spinner = dialog.findViewById(R.id.spinnerBahanUtama);
            etJumlah = dialog.findViewById(R.id.etJumlahUtama);
            tvSatuanx = dialog.findViewById(R.id.tvSatuanUtama);
            btndelete = dialog.findViewById(R.id.btnDeleteKomposisi);
            btnDismiss = dialog.findViewById(R.id.btnDismiss);

            tvBahan.setText(refBahan);
            etJumlah.setText(String.valueOf(komposisiModels.get(position).getJumlah()));

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
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
                komposisiModels.get(position).setBahan(bahan);
                komposisiModels.get(position).setJumlah(jumlah);
                komposisiModels.get(position).setSatuan(satuan);
                notifyDataSetChanged();
                //updateKomposisi();
                dialog.dismiss();
            });

            btndelete.setOnClickListener(view1 -> {
                komposisiModels.remove(position);
                notifyDataSetChanged();
                deleteKomposisi();
                dialog.dismiss();
            });

            btnDismiss.setOnClickListener(view1 -> dialog.dismiss());


            if(!((Activity)context).isFinishing()){
                dialog.show();
            }


        });

        return convertView;
    }

    private void deleteKomposisi() {
        APIRequestKomposisi komposisiData = ServerConnection.connection().create(APIRequestKomposisi.class);
        Call<ResponseModel> deleteKomposisi = komposisiData.deleteKomposisi(id, user);

        deleteKomposisi.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if (response.body() != null) {
                    String pesan = response.body().getPesan();
                    Toast.makeText(context, pesan, Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(context, "gagal hapus: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void listBahan() {
        APIRestock stockData = ServerConnection.connection().create(APIRestock.class);
        Call<ResponseModel> getStock = stockData.getStock(user);

        getStock.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                listBahan = new ArrayList<>();
                if(response.body() != null)
                listBahan = response.body().getStocks();
                if(listBahan != null){
                    adapterSpinnerBahan = new AdapterSpinnerKomposisi(context, listBahan);
                    spinner.setAdapter(adapterSpinnerBahan);
                    for (int i = 0; i<listBahan.size(); i++){
                        String data = listBahan.get(i).getBahan_baku();
                        if (data.equals(refBahan)){
                            index = i;
                            break;
                        }
                    }
                    spinner.setSelection(index);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
