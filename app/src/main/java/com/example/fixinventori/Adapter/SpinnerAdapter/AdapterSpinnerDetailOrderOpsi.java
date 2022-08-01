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
import com.example.fixinventori.model.KomposisiModel;

import java.util.List;

public class AdapterSpinnerDetailOrderOpsi extends ArrayAdapter<KomposisiModel> {
    Context context;
    List<KomposisiModel> komposisiModelList;

    public AdapterSpinnerDetailOrderOpsi(@NonNull Context context, @NonNull List<KomposisiModel> objects) {
        super(context, R.layout.restock_spinner, objects);
        this.context = context;
        komposisiModelList = objects;
    }

    @Override
    public int getCount() {
        return komposisiModelList.size();
    }

    @Nullable
    @Override
    public KomposisiModel getItem(int position) {
        return komposisiModelList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        try {
            if(convertView == null){
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.restock_spinner, parent,false);

                KomposisiModel komposiList = komposisiModelList.get(position);
                if(komposiList != null)
                {
                    TextView tvSpinner = convertView.findViewById(R.id.tvRestockSpinner);
                    TextView tvRestockId = convertView.findViewById(R.id.tvRestockIdSpinner);
                    tvSpinner.setText(komposiList.getBahan() + " " +komposiList.getJumlah()+" "+komposiList.getSatuan());
                    tvRestockId.setText(komposiList.getId()+"");
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        try {
            if(convertView == null){
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.restock_spinner, parent,false);

                KomposisiModel komposisiModel = komposisiModelList.get(position);
                if(komposisiModel != null)
                {
                    TextView tvSpinner = convertView.findViewById(R.id.tvRestockSpinner);
                    TextView tvRestockId = convertView.findViewById(R.id.tvRestockIdSpinner);
                    tvSpinner.setText(komposisiModel.getBahan() + " " +komposisiModel.getJumlah()+" "+komposisiModel.getSatuan());
                    tvRestockId.setText(komposisiModel.getId()+"");
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
