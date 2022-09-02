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

public class AdapterMenuRecord extends ArrayAdapter<StatModel> {

    Context context;
    List<StatModel> menu;

    public AdapterMenuRecord(Context context, List<StatModel> menu){
        super(context, R.layout.transaction_row, menu);
        this.context = context;
        this.menu = menu;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.transaction_row,parent,false);
        TextView tvTanggal =convertView.findViewById(R.id.tvTanggal);
        TextView tvCashIn =convertView.findViewById(R.id.tvCashIn);
        TextView tvCashOut =convertView.findViewById(R.id.tvCashOut);

        tvTanggal.setText(menu.get(position).getTanggal());
        tvCashIn.setText(menu.get(position).getMenu());
        tvCashOut.setText(String.valueOf(menu.get(position).getJumlah()));
        return convertView;
    }
}
