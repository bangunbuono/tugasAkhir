package com.example.fixinventori.Adapter.LVAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fixinventori.R;
import com.example.fixinventori.model.StocksModel;

import java.util.List;

public class AdapterUsageCombine extends ArrayAdapter<StocksModel> {
    Context context;
    List<StocksModel> list;

    public AdapterUsageCombine(@NonNull Context context, @NonNull List<StocksModel> objects) {
        super(context, R.layout.combine_input_row, objects);
        this.context = context;
        list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.combine_input_row, parent, false);

        TextView tvStockName = convertView.findViewById(R.id.tvStockName);
        TextView tvStockSatuan = convertView.findViewById(R.id.tvStockSatuan);
        EditText etStockJumlah = convertView.findViewById(R.id.etStockJumlah);

        tvStockName.setText(list.get(position).getBahan_baku());
        tvStockSatuan.setText(list.get(position).getSatuan());
        etStockJumlah.setHint("jumlah");

        return convertView;
    }
}
