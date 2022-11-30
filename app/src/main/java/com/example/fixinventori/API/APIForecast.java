package com.example.fixinventori.API;

import com.example.fixinventori.model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIForecast {

    @FormUrlEncoded
    @POST("testnested.php")
    Call<ResponseModel> getHistory(
        @Field("user") String user,
        @Field("day") int day
    );


}
