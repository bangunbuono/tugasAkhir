package com.example.fixinventori.API;

import com.example.fixinventori.model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIRequestKomposisi {

    @FormUrlEncoded
    @POST("komposisiadd.php")
    Call<ResponseModel> addKomposisi(
            @Field("user") String user,
            @Field("bahan") String bahan,
            @Field("jumlah") int jumlah,
            @Field("satuan") String satuan,
            @Field("menu") String menu
    );

    @FormUrlEncoded
    @POST("komposisiset.php")
    Call<ResponseModel> getKomposisi(
            @Field("menu") String menu,
            @Field("user") String user
    );

    @FormUrlEncoded
    @POST("komposisidelete.php")
    Call<ResponseModel> deleteKomposisi(
            @Field("id") int id,
            @Field("user") String user
    );

    @FormUrlEncoded
    @POST("komposisiupdate.php")
    Call<ResponseModel> updateKomposisi(
            @Field("id") int id,
            @Field("bahan") String bahan,
            @Field("jumlah") int jumlah,
            @Field("satuan") String satuan
    );

    @FormUrlEncoded
    @POST("komposisicancel.php")
    Call<ResponseModel> cancelKomposisi(
            @Field("menu") String menu,
            @Field("user") String user
    );
}
