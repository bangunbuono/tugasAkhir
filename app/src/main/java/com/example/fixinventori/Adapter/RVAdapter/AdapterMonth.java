package com.example.fixinventori.Adapter.RVAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixinventori.R;
import com.example.fixinventori.model.MonthModel;

import java.util.List;

public class AdapterMonth extends RecyclerView.Adapter<AdapterMonth.Holder> {

    Context context;
    List<MonthModel> monthList;
    onClick onClick;
    LinearLayoutManager layoutManager;
    public static int rowIndex = -1;

    public AdapterMonth(Context context, List<MonthModel> monthList, onClick onClick) {
        this.monthList = monthList;
        this.onClick = onClick;
        this.context = context;
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvMonth;
        CardView cvMonth;
        onClick onClickListener;

        public Holder(@NonNull View itemView, onClick onClick) {
            super(itemView);
            tvMonth = itemView.findViewById(R.id.tvMonth);
            cvMonth = itemView.findViewById(R.id.cvMonth);
            itemView.setOnClickListener(this);
            layoutManager = new LinearLayoutManager(context);
            onClickListener = onClick;
        }

        @Override
        public void onClick(View view) {
            onClickListener.onItemClicked(getAdapterPosition());
            rowIndex = getAdapterPosition();
            notifyDataSetChanged();
        }
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.month_row, parent, false);

        return new Holder(view, onClick);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.fade_in);
        animation.setDuration(100);
        holder.itemView.setTag(monthList.get(position));
        holder.tvMonth.setText(monthList.get(position).getBulan());
        holder.itemView.startAnimation(animation);

        if(rowIndex == position){
            holder.cvMonth.setBackgroundColor(Color.rgb(150,150,150));
            holder.itemView.setTag(monthList.get(position));
            holder.tvMonth.setText(monthList.get(position).getBulan());
            holder.itemView.startAnimation(animation);
        }else {
            holder.cvMonth.setBackgroundColor(Color.TRANSPARENT);
            holder.itemView.startAnimation(animation);
        }
    }

    @Override
    public int getItemCount() {
        return monthList.size();
    }

    public interface onClick{
        void onItemClicked(int i);
    }

}
