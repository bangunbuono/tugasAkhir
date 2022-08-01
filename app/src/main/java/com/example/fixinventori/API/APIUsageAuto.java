package com.example.fixinventori.API;

import com.example.fixinventori.model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIUsageAuto {
    @FormUrlEncoded
    @POST("usageauto.php")
    Call<ResponseModel> usage(
            @Field("menu") String menu,
            @Field("user") String user
    );

    @FormUrlEncoded
    @POST("usageautoopsi.php")
    Call<ResponseModel> usageOpsi(
            @Field("menu") String menu,
            @Field("user") String user,
            @Field("bahan") String bahan
    );
}
