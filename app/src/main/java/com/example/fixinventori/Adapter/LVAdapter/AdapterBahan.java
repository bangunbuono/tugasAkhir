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
import com.example.fixinventori.model.StatModel;

import java.util.List;

public class AdapterBahan extends ArrayAdapter<StatModel> {

    Context context;
    List<StatModel> bahan;

    public AdapterBahan(Context context, List<StatModel> bahan){
        super(context, R.layout.transaction_row,bahan);
        this.context = context;
        this.bahan = bahan;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.transaction_row,parent,false);
        TextView tvTanggal =convertView.findViewById(R.id.tvTanggal);
        TextView tvCashIn =convertView.findViewById(R.id.tvCashIn);
        TextView tvCashOut =convertView.findViewById(R.id.tvCashOut);

        tvTanggal.setText(bahan.get(position).getTanggal());
        tvCashIn.setText(bahan.get(position).getBahan());
        tvCashOut.setText(String.format("%s%s", bahan.get(position).getJumlah(), bahan.get(position).getSatuan()));
        return convertView;
    }
}
