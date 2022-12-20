package com.example.fixinventori.Adapter.LVAdapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fixinventori.R;
import com.example.fixinventori.model.StatModel;

import java.text.DecimalFormat;
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

        tvCashIn.setGravity(Gravity.END);
        tvCashOut.setGravity(Gravity.END);
        tvCashIn.setPadding(0,0,50,0);
        tvCashOut.setPadding(0,0,50,0);

        double priceIn = cashIn.get(position).getHarga();
        double priceOut = cashOut.get(position).getHarga();

        DecimalFormat df=new DecimalFormat("#,###");

        if (cashOut.size()==0){
            tvTanggal.setText(cashIn.get(position).getTanggal().substring(0,10));
//            tvCashIn.setText(String.valueOf(cashIn.get(position).getHarga()));
            tvCashIn.setText(String.format("%s",df.format(priceIn)));
            tvCashOut.setText(String.valueOf(0));
        }else if(cashIn.size()==0) {
            tvTanggal.setText(cashOut.get(position).getTanggal().substring(0,10));
            tvCashOut.setText(String.format("%s",df.format(priceOut)));
            tvCashIn.setText(String.valueOf(0));
        }else {
            tvTanggal.setText(cashIn.get(position).getTanggal().substring(0,10));
            tvCashIn.setText(String.format("%s",df.format(priceIn)));
            tvCashOut.setText(String.format("%s",df.format(priceOut)));
        }

        return convertView;
    }
}
