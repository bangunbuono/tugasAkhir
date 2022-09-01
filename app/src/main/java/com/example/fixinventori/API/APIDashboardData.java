package com.example.fixinventori.API;

import com.example.fixinventori.model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIDashboardData {

    @FormUrlEncoded
    @POST("getmaxmenu.php")
    Call<ResponseModel> maxMenu(
            @Field("username") String user,
            @Field("week") int week
    );

    @FormUrlEncoded
    @POST("getmaxstockin.php")
    Call<ResponseModel> maxStockIn(
            @Field("username") String user,
            @Field("week") int week
    );

    @FormUrlEncoded
    @POST("getmaxstockout.php")
    Call<ResponseModel> maxStockOut(
            @Field("username") String user,
            @Field("week") int week
    );

    @FormUrlEncoded
    @POST("getpengunjung.php")
    Call<ResponseModel> pengunjung(
            @Field("username") String user,
            @Field("week") int week
    );

    @FormUrlEncoded
    @POST("getcashin.php")
    Call<ResponseModel> cashIn(
            @Field("username") String user,
            @Field("week") int week
    );

    @FormUrlEncoded
    @POST("getcashout.php")
    Call<ResponseModel> cashOut(
            @Field("username") String user,
            @Field("week") int week
    );


}
