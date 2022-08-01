package com.example.fixinventori.API;

import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerConnection {
    private static final String URL = "http://10.0.2.2/notif/";

//            "https://apiinventori.000webhostapp.com/";
//
    private static Retrofit retrofit;

    public static Retrofit connection(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder().
                    baseUrl(URL).
                    addConverterFactory(GsonConverterFactory.create()).
                    build();
        }

        return retrofit;
    }

    public static Retrofit connectionLenient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(interceptor);

        Gson gson = new GsonBuilder().setLenient().create();
        GsonConverterFactory.create(gson);

        if(retrofit==null){
            retrofit = new Retrofit.Builder().
                    baseUrl(URL).
                    client(client.build()).
                    addConverterFactory(GsonConverterFactory.create(gson)).
                    build();
        }
        return retrofit;
    }
}
