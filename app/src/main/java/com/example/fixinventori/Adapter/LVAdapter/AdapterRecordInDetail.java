package com.example.fixinventori.Adapter.LVAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fixinventori.R;
import com.example.fixinventori.model.RecordModel;

import java.util.List;

public class AdapterRecordInDetail extends ArrayAdapter<RecordModel> {
    Context context;
    List<RecordModel> recordModelList;

    public AdapterRecordInDetail(@NonNull Context context, @NonNull List<RecordModel> objects) {
        super(context, R.layout.restock_row, objects);
        this.context = context;
        recordModelList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.restock_row, parent, false);

        TextView tvBahan, tvJumlah, tvSatuan, tvHarga;

        tvBahan = convertView.findViewById(R.id.tvBahan);
        tvJumlah = convertView.findViewById(R.id.tvJumlah);
        tvSatuan = convertView.findViewById(R.id.tvSatuan);
        tvHarga = convertView.findViewById(R.id.tvHarga);

        tvBahan.setText(recordModelList.get(position).getBahan());
        tvSatuan.setText(recordModelList.get(position).getSatuan());
        tvJumlah.setText(String.valueOf(recordModelList.get(position).getJumlah()));
        tvHarga.setText(String.valueOf(recordModelList.get(position).getHarga()));

        tvHarga.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        return convertView;
    }
}
