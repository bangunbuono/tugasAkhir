package com.example.fixinventori.API;

import com.example.fixinventori.model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIRequestMenu {
    @FormUrlEncoded
    @POST("menuset.php")
    Call<ResponseModel> showMenu(
            @Field("user") String user
    );

    @FormUrlEncoded
    @POST("menucreate.php")
    Call<ResponseModel> createMenu(
            @Field("menu") String menu,
            @Field("harga") int harga,
            @Field("deskripsi") String deskripsi,
            @Field("user") String user
    );

    @FormUrlEncoded
    @POST("menudelete.php")
    Call<ResponseModel> deleteMenu(
            @Field("id") int id,
            @Field("menu") String menu,
            @Field("user") String user
    );

    @FormUrlEncoded
    @POST("menudeleteByname.php")
    Call<ResponseModel> deleteMenuByname(
            @Field("menu") String menu
    );

    @FormUrlEncoded
    @POST("menudetail.php")
    Call<ResponseModel> detailMenu(
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("menuupdate.php")
    Call<ResponseModel> updateMenu(
            @Field("id") int id,
            @Field("menuBaru") String menuBaru,
            @Field("harga") int harga,
            @Field("deskripsi") String deskripsi,
            @Field("user") String user,
            @Field("menu") String menu
    );
}
