package com.example.fixinventori.Adapter.RVAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixinventori.API.APIReport;
import com.example.fixinventori.API.ServerConnection;
import com.example.fixinventori.Activity.Report.HistoryFrag;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.R;
import com.example.fixinventori.model.RecordModel;
import com.example.fixinventori.model.ResponseModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterDate extends RecyclerView.Adapter<AdapterDate.Holder> {
    List<RecordModel> dateList, recordList;
    com.example.fixinventori.Adapter.RVAdapter.AdapterDailyRecord adapterDailyRecord;
    Context context;
    UserSession userSession;
    String user,dateFormated;
    RecyclerView.RecycledViewPool  viewPool= new RecyclerView.RecycledViewPool();


    public AdapterDate(Context context, List<RecordModel> list ){
        this.context = context;
        dateList = list;
    }

    public class Holder extends RecyclerView.ViewHolder{

        TextView tvRecordDate;
        RecyclerView rvDailyRecord;
        HistoryFrag historyFrag;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvRecordDate = itemView.findViewById(R.id.tvRecordDate);
            rvDailyRecord = itemView.findViewById(R.id.rvDailyRecord);
            historyFrag = new HistoryFrag();
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_row, parent, false);
        userSession = new UserSession(context);
        user = userSession.getUserDetail().get("username");
        recordList = new ArrayList<>();

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.itemView.setTag(dateList.get(position));
        holder.tvRecordDate.setText(dateList.get(position).getTanggal());
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);

        String date = holder.tvRecordDate.getText().toString();
        try {
            Date date1 = new SimpleDateFormat("dd MMMMM yy", Locale.US).parse(date);
            dateFormated = Objects.requireNonNull(date1).toInstant().
                    atOffset(ZoneOffset.from(ZonedDateTime.now())).
                    format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        APIReport record = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> dailyRecord = record.dailyRecord(user, dateFormated, HistoryFrag.choosenFilter);

        dailyRecord.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                assert response.body()!=null;
                recordList = response.body().getRecord();
                if (recordList!=null){
                    adapterDailyRecord = new AdapterDailyRecord(context, recordList);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(
                            holder.rvDailyRecord.getContext(), RecyclerView.VERTICAL,false);
                    layoutManager.setInitialPrefetchItemCount(recordList.size());

                    holder.rvDailyRecord.setAdapter(adapterDailyRecord);
                    holder.rvDailyRecord.setLayoutManager(layoutManager);
                    holder.rvDailyRecord.setRecycledViewPool(viewPool);

                }else {
                    holder.itemView.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {

            }
        });
        animation.setDuration(200);
        holder.itemView.startAnimation(animation);
    }


    @Override
    public int getItemCount() {
        return dateList.size();
    }
}
