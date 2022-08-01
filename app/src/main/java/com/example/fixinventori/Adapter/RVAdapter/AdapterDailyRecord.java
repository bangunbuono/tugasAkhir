package com.example.fixinventori.Adapter.RVAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixinventori.Activity.Report.ReportDetail;
import com.example.fixinventori.R;
import com.example.fixinventori.model.RecordModel;

import java.util.List;

public class AdapterDailyRecord extends RecyclerView.Adapter<AdapterDailyRecord.HolderMonthly>{

    List<RecordModel> recordList;
    Context context;

    public AdapterDailyRecord(Context context,List<RecordModel> list){
        this.context = context;
        recordList = list;
    }

    public class HolderMonthly extends RecyclerView.ViewHolder{
        TextView tvRecordCode;

        public HolderMonthly(@NonNull View itemView) {
            super(itemView);
            tvRecordCode = itemView.findViewById(R.id.tvRecordCode);

            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, ReportDetail.class);
                intent.putExtra("kode",recordList.get(getAdapterPosition()).getKode());
                intent.putExtra("keterangan", recordList.get(getAdapterPosition()).getKeterangan());
                context.startActivity(intent);
            });

        }
    }


    @NonNull
    @Override
    public HolderMonthly onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_row, parent, false);
        return new HolderMonthly(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderMonthly holder, int position) {
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        animation.setDuration(300);
        holder.itemView.setTag(recordList.get(position));
        holder.tvRecordCode.setText(recordList.get(position).getKode());
        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }
}
