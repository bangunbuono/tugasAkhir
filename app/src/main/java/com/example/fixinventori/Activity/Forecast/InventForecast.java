package com.example.fixinventori.Activity.Forecast;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixinventori.API.APIForecast;
import com.example.fixinventori.API.ServerConnection;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.Adapter.RVAdapter.AdapterForecast;
import com.example.fixinventori.R;
import com.example.fixinventori.model.ResponseModel;
import com.example.fixinventori.model.TimeSeriesModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventForecast extends AppCompatActivity {
    String user;
    public static String day;
    UserSession session;
    List<TimeSeriesModel> history = new ArrayList<>();
    RecyclerView rvForecast;
    RecyclerView.LayoutManager manager;
    AdapterForecast adapterForecast;
    Spinner spinnerDayForecasting;
    ArrayList<String> dayList = new ArrayList<>();
    SimpleDateFormat sdf;

//    ubah api yang di 000webhost input day
//    sql day 1 start dari minggu, java day 1 start dari senin


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invent_forecast);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        session = new UserSession(this);
        user = session.getString("username");

        rvForecast = findViewById(R.id.rvForecast);
        spinnerDayForecasting = findViewById(R.id.spinnerDayForecasting);

        sdf = new SimpleDateFormat("EEEE dd-MMM-yyyy", Locale.getDefault());

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.simple_spinner_style, getDayInWeek());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDayForecasting.setAdapter(spinnerAdapter);



        spinnerDayForecasting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                day = dayList.get(position).split(" ")[0].toUpperCase();
                System.out.println(DayOfWeek.valueOf(day).getValue());

                getTimeSeries(getTodaySQL(DayOfWeek.valueOf(day).getValue()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    void getTimeSeries(int day){
        APIForecast data = ServerConnection.connection().create(APIForecast.class);
        Call<ResponseModel> getData = data.getHistory(user, day);

        getData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if(response.isSuccessful() && response.body()!=null){
                    history = response.body().getHistory();
                    if(history!=null){
                        adapterForecast = new AdapterForecast(history, InventForecast.this);
                        manager = new LinearLayoutManager(InventForecast.this, RecyclerView.VERTICAL, false);
                        rvForecast.setLayoutManager(manager);
                        rvForecast.setAdapter(adapterForecast);
                    }
//                    if (history != null) {
//                        for (int i = 0; i < history.size(); i++) {
//                            for (int j = 0; j < history.get(i).getData().size(); j++) {
//                                System.out.println(history.get(i).getBahan() + ": "
//                                        + history.get(i).getData().get(j).getJumlah());
//                            }
//                        }
//                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(InventForecast.this, "Gagal mengambil data "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    public int getTomorrow(){
//        int day;
//        LocalDateTime date = LocalDateTime.now();
//        day = date.getDayOfWeek().getValue();
//        System.out.println(day);
//        if(day==5) return 7;
//        else if (day==6) return 1;
//        else if(day==7) return 2;
//        else return day+2;
//    }

    public static int getTodaySQL(int day){
        if(day==7) return 1;
        else return day+1;
    }

//    public static int getToday(){
//        int day;
//        LocalDateTime date = LocalDateTime.now();
//        day = date.getDayOfWeek().getValue();
//        System.out.println(day);
//        return day;
//    }

    public ArrayList<String> getDayInWeek(){
        for (int i = 0; i < 7; i++) {
            Calendar calendar = new GregorianCalendar();
            calendar.add(Calendar.DATE, i);
            String day = sdf.format(calendar.getTime());
            if(i!=0) dayList.add(day);
        }
        return dayList;
    }
}