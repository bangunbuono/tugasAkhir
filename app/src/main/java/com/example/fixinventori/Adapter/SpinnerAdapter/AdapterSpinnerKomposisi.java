package com.example.fixinventori.Adapter.SpinnerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fixinventori.R;
import com.example.fixinventori.model.RestockModel;

import java.util.List;

public class AdapterSpinnerKomposisi extends ArrayAdapter<RestockModel> {
    Context context;
    List<RestockModel> stockList;

    public AdapterSpinnerKomposisi(@NonNull Context context, @NonNull List<RestockModel> objects) {
        super(context, R.layout.restock_spinner, objects);
        this.context = context;
        stockList = objects;
    }

    @Override
    public int getCount() {
        return stockList.size();
    }

    @Nullable
    @Override
    public RestockModel getItem(int position) {
        return stockList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        try {
            if(convertView == null){
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.restock_spinner, parent,false);

                RestockModel restock = stockList.get(position);
                if(restock != null)
                {
                    TextView tvSpinner = convertView.findViewById(R.id.tvRestockSpinner);
                    TextView tvRestockId = convertView.findViewById(R.id.tvRestockIdSpinner);
                    tvSpinner.setText(restock.getBahan_baku());
                    tvRestockId.setText(restock.getId()+"");
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        assert convertView != null;
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        try {
            if(convertView == null){
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.restock_spinner, parent,false);

                RestockModel stock = stockList.get(position);
                if(stock != null)
                {
                    TextView tvSpinner = convertView.findViewById(R.id.tvRestockSpinner);
                    TextView tvRestockId = convertView.findViewById(R.id.tvRestockIdSpinner);
                    tvSpinner.setText(stock.getBahan_baku());
                    tvRestockId.setText(stock.getId()+"");
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return convertView;
    }
}
