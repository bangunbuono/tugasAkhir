package com.example.fixinventori.Adapter.RVAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixinventori.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class TipsAdapter extends SliderViewAdapter<TipsAdapter.ViewHolder> {

    List<String> tipsList;
    Context context;

    public TipsAdapter(Context context, List<String> tipsList) {
        this.context = context;
        this.tipsList = tipsList;
    }

    public class ViewHolder extends SliderViewAdapter.ViewHolder{

        TextView tvTips;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTips = itemView.findViewById(R.id.tvTips);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tips_row,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.itemView.setTag(tipsList.get(position));
        viewHolder.tvTips.setText(tipsList.get(position));
    }

    @Override
    public int getCount() {
        return tipsList.size();
    }

}
