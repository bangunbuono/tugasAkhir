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

public class AdapterTransaction extends ArrayAdapter<StatModel> {

    Context context;
    List<StatModel> cashIn, cashOut;

    public AdapterTransaction(Context context, List<StatModel> in, List<StatModel> out){
        super(context, R.layout.transaction_row,in);
        this.context = context;
        cashIn = in;
        cashOut = out;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.transaction_row,parent,false);
        TextView tvTanggal =convertView.findViewById(R.id.tvTanggal);
        TextView tvCashIn =convertView.findViewById(R.id.tvCashIn);
        TextView tvCashOut =convertView.findViewById(R.id.tvCashOut);

        tvTanggal.setText(cashIn.get(position).getTanggal().substring(0,10));
        tvCashIn.setText(String.valueOf(cashIn.get(position).getHarga()));
        tvCashOut.setText(String.valueOf(cashOut.get(position).getHarga()));

        return convertView;
    }
}
