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
import com.example.fixinventori.model.UsageMenuModel;

import java.util.List;

public class AdapterRecordMenu extends ArrayAdapter<UsageMenuModel> {
    Context context;
    List<UsageMenuModel> recordMenuList;

    public AdapterRecordMenu(@NonNull Context context, @NonNull List<UsageMenuModel> objects) {
        super(context, R.layout.record_detail_row, objects);
        this.context = context;
        recordMenuList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.record_detail_row, parent, false);

        TextView tvBahan, tvJumlah, tvSatuan;

        tvBahan = convertView.findViewById(R.id.tvBahanRecord);
        tvJumlah = convertView.findViewById(R.id.tvJumlahRecord);
        tvSatuan = convertView.findViewById(R.id.tvSatuanRecord);

        tvBahan.setText(recordMenuList.get(position).getMenu());
        tvSatuan.setText(String.valueOf(recordMenuList.get(position).getHarga()));
        tvJumlah.setText(String.valueOf(recordMenuList.get(position).getQty()));
        return convertView;
    }
}
