package com.example.fixinventori.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerConnection {
//    private static final String URL = "http://10.0.2.2/notif/";
    private static final String URL = "https://notifdatabase.000webhostapp.com/";
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
}
