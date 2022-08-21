package com.example.fixinventori.API;

import com.example.fixinventori.model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIAccounts {

    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseModel> login(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("register.php")
    Call<ResponseModel> register(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("deleteUser.php")
    Call<ResponseModel> deleteUser(
            @Field("username") String username
    );

    @FormUrlEncoded
    @POST("generateToken.php")
    Call<ResponseModel> generateToken(
        @Field("username") String user,
        @Field("token") int token
    );


    //manager
    @FormUrlEncoded
    @POST("registermanager.php")
    Call<ResponseModel> registerManager(
            @Field("manager_name") String managerName,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("deleteManager.php")
    Call<ResponseModel> deleteManager(
            @Field("manager_name") String managerName
    );

    @FormUrlEncoded
    @POST("loginmanager.php")
    Call<ResponseModel> loginManager(
            @Field("manager_name") String username,
            @Field("password") String password
    );

    //connection

    @FormUrlEncoded
    @POST("connectUser.php")
    Call<ResponseModel> connectUser(
            @Field("username") String user,
            @Field("token") int token,
            @Field("manager_name") String username
    );

    @FormUrlEncoded
    @POST("getManager.php")
    Call<ResponseModel> getManager(
            @Field("username") String user
    );

    @FormUrlEncoded
    @POST("getUser.php")
    Call<ResponseModel> getUser(
            @Field("manager_name") String manager
    );





}
