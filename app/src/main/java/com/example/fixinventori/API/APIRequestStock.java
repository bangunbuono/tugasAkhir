package com.example.fixinventori.API;

import com.example.fixinventori.model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIRequestStock {
    @FormUrlEncoded
    @POST("stockset.php")
    Call<ResponseModel> showStock(
            @Field("user") String user
    );

    @FormUrlEncoded
    @POST("stockdetail.php")
    Call<ResponseModel> showDetail(
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("stockupdate.php")
    Call<ResponseModel> updateData(
            @Field("id") int id,
            @Field("bahan_baku") String bahan,
            @Field("jumlah") int jumlah,
            @Field("satuan") String satuan,
            @Field("min_pesan") int min_pesan,
            @Field("waktu") int waktu,
            @Field("user") String user,
            @Field("bahanBaru") String bahanBaru
    );

    @FormUrlEncoded
    @POST("stockadd.php")
    Call<ResponseModel> addData(
            @Field("bahan_baku") String bahan,
            @Field("jumlah") int jumlah,
            @Field("satuan") String satuan,
            @Field("min_pesan") int min_pesan,
            @Field("waktu") int waktu,
            @Field("user") String user
    );

    @FormUrlEncoded
    @POST("restockadd.php")
    Call<ResponseModel> addStocks(
            @Field("id") int id,
            @Field("jumlah") int jumlah
    );

    @FormUrlEncoded
    @POST("stockout.php")
    Call<ResponseModel> minStocks(
            @Field("id") int id,
            @Field("jumlah") int jumlah
    );

    @FormUrlEncoded
    @POST("stockdelete.php")
    Call<ResponseModel> deleteStock(
      @Field("id") int id,
      @Field("bahan") String bahan,
      @Field("user") String user
    );
}
