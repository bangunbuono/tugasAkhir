package com.example.fixinventori.Adapter.RVAdapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixinventori.Activity.Forecast.InventForecast;
import com.example.fixinventori.R;

import com.example.fixinventori.model.TimeSeriesModel;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class AdapterForecast extends RecyclerView.Adapter<AdapterForecast.Holder> {

    public List<TimeSeriesModel> data;
    ArrayList<Float> ma4, ma4x4, at, tt, forecast;
    Context context;
//    String checkDays;

    public AdapterForecast(List<TimeSeriesModel> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public class Holder extends RecyclerView.ViewHolder{
        TextView tvStockName, tvForecast;
        LinearLayout llForecast;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvStockName = itemView.findViewById(R.id.tvStockName);
            tvForecast = itemView.findViewById(R.id.tvForecast);
            llForecast = itemView.findViewById(R.id.llForecast);
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_row, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        if(data.get(position)!=null) holder.itemView.setTag(data.get(position));
        if(data.get(position).getBahan()!=null) holder.tvStockName.setText(data.get(position).getBahan());
        ArrayList<Integer> demand = new ArrayList<>();
        data.get(position).getData().forEach(data1 -> demand.add(data1.getJumlah()));

        try{
            new Handler().postDelayed(()->{
//            if(checkDays.equals(String.valueOf(R.string.doubleMA))){
                if(DayOfWeek.valueOf(InventForecast.day).getValue()==7||DayOfWeek.valueOf(InventForecast.day).getValue()==6){
                    if(demand.size()>=4) {
                        float y = holtForecaster(demand);
                        holder.tvForecast.
                                setText(String.format("Perkiraan penggunaan %s pada %s adalah %s %s",
                                        data.get(position).getBahan(),InventForecast.day.toLowerCase(),y ,
                                        data.get(position).getData().get(0).getSatuan()));
                    }
                    else holder.tvForecast.setText(R.string.dataPeramalanKurang);
                    System.out.println("HOLT");
//            }else if(checkDays.equals(String.valueOf(R.string.holt))){
                }else{
                    if(demand.size()>=8) {
                        float y = doubleMA(demand);
                        holder.tvForecast.
                                setText(String.format("Perkiraan penggunaan %s pada %s adalah %s %s",
                                        data.get(position).getBahan(),InventForecast.day.toLowerCase(),y ,
                                        data.get(position).getData().get(0).getSatuan()));
                    }
                    else holder.tvForecast.setText(R.string.dataPeramalanKurang);
                    System.out.println("DMA");
                }
            },200);
        }
        catch (Exception e){
            Toast.makeText(context, "gagal meramal", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private float doubleMA(ArrayList<Integer> data){
        ma4 = new ArrayList<>();
        ma4x4 = new ArrayList<>();
//        long start = System.currentTimeMillis();
        for (int i = 0; i < data.size()-4; i++) {
            ma4.add((data.get(i)+data.get(i+1)+data.get(i+2)+data.get(i+3))/4f);
        }
        for (int i = 0; i < ma4.size()-4; i++) {
            ma4x4.add((ma4.get(i)+ma4.get(i+1)+ma4.get(i+2)+ma4.get(i+3))/4f);
            System.out.println(2f*ma4.get(i+3)- ma4x4.get(i)+2/3f*(ma4.get(i+3)-ma4x4.get(i))*1);
        }
//        float elapsedTime = System.currentTimeMillis()-start;
        return Math.abs(2f*ma4.get(ma4.size()-2)- ma4x4.get(ma4x4.size()-1)+
                2/3f*(ma4.get(ma4.size()-2)-ma4x4.get(ma4x4.size()-1))*2);

    }

    private float holtForecaster(ArrayList<Integer> demand){
        at = new ArrayList<>();
        tt = new ArrayList<>();
        forecast = new ArrayList<>();

        float alpha = 0.1f;
        float beta = 0.01f;

        at.add(0, demand.get(0).floatValue());
        tt.add(0, demand.get(1)- demand.get(0).floatValue());
        for (int i = 1; i < demand.size(); i++) {
            at.add(alpha* demand.get(i)+(1-alpha)*(at.get(i-1)+tt.get(i-1)));
            tt.add(beta*(at.get(i)-at.get(i-1)) + (1-beta)*tt.get(i-1));
            forecast.add(i-1, Math.abs(at.get(i-1)+tt.get(i-1)*1));
        }

        forecast.forEach(System.out::println);
        return Math.abs(at.get(at.size()-1)+ tt.get(tt.size()-1)*1);
    }



}
