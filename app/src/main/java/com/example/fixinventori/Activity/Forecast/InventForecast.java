package com.example.fixinventori.Activity.Forecast;

import android.os.Bundle;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventForecast extends AppCompatActivity {
    String user, check;
    UserSession session;
    List<TimeSeriesModel> history = new ArrayList<>();
    RecyclerView rvForecast;
    RecyclerView.LayoutManager manager;
    AdapterForecast adapterForecast;


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

        getTimeSeries();
    }

    void getTimeSeries(){
        APIForecast data = ServerConnection.connection().create(APIForecast.class);
        Call<ResponseModel> getData = data.getHistory(user, getTomorrow());

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
                    if (history != null) {
                        for (int i = 0; i < history.size(); i++) {
                            for (int j = 0; j < history.get(i).getData().size(); j++) {
                                System.out.println(history.get(i).getBahan() + ": "
                                        + history.get(i).getData().get(j).getJumlah());
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(InventForecast.this, "Gagal mengambil data "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static int getTomorrow(){
        int day;
        LocalDateTime date = LocalDateTime.now();
        day = date.getDayOfWeek().getValue();
        System.out.println(day);
        if(day==5) return 7;
        else if (day==6) return 1;
        else if(day==7) return 2;
        else return day+2;
    }
}