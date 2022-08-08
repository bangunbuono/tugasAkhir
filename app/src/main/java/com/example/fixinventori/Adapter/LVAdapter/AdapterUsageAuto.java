package com.example.fixinventori.Adapter.LVAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fixinventori.Activity.Usage.UsageAutoFrag;
import com.example.fixinventori.R;
import com.example.fixinventori.model.MenuModel;

import java.util.List;

public class AdapterUsageAuto extends ArrayAdapter<MenuModel> {
    Context context;
    List<MenuModel> list;
    int item, price;

    public AdapterUsageAuto(Context context, List<MenuModel> objects) {
        super(context, R.layout.usage_auto_row,objects);

        this.context = context;
        list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.usage_auto_row, parent, false);

        TextView tvProduct = convertView.findViewById(R.id.tvProduct);
        TextView tvQty = convertView.findViewById(R.id.tvQty);
        Button btnMin = convertView.findViewById(R.id.btnMin);
        Button btnPlus = convertView.findViewById(R.id.btnPlus);
        TextView tvPrice = convertView.findViewById(R.id.tvProductPrice);

        tvProduct.setText(list.get(position).getMenu());
        tvQty.setText(String.valueOf(list.get(position).getQty()));
        tvPrice.setText(String.format("Rp. %s", list.get(position).getHarga()));
        int qty = list.get(position).getQty();

        btnMin.setOnClickListener(view -> {
            if(qty != 0){
                list.get(position).setQty(qty-1);
                tvQty.setText(String.valueOf(list.get(position).getQty()));
            }
            notifyDataSetChanged();
            chectItem();
        });

        btnPlus.setOnClickListener(view -> {
            list.get(position).setQty(qty+1);
            tvQty.setText(String.valueOf(list.get(position).getQty()));
            notifyDataSetChanged();
            chectItem();
        });
        return convertView;
    }

    private void chectItem(){
        if(list!=null){
            item = 0;
            price = 0;
            for (int i =0; i<list.size();i++){
                item += list.get(i).getQty();
                price += list.get(i).getHarga() * list.get(i).getQty();
            }

            if (item!=0){
                UsageAutoFrag.layoutItem.setVisibility(View.VISIBLE);
                UsageAutoFrag.tvItem.setText(String.format("%s item = Rp.%s", item, price));
            }else {
                UsageAutoFrag.layoutItem.setVisibility(View.GONE);
            }
        }

    }
}
