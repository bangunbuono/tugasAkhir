package com.example.fixinventori.API;

import com.example.fixinventori.model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIReport {

    @FormUrlEncoded
    @POST("recordusage.php")
    Call<ResponseModel> recordUsage (
            @Field("kode") String kode,
            @Field("bahan") String bahan,
            @Field("jumlah") int jumlah,
            @Field("satuan") String satuan,
            @Field("keterangan") String keterangan,
            @Field("user") String user,
            @Field("pengunjung") int pengunjung,
            @Field("tanggal_keluar") String waktu
    );

    @FormUrlEncoded
    @POST("recordrestock.php")
    Call<ResponseModel> recordRestock (
            @Field("kode") String kode,
            @Field("bahan") String bahan,
            @Field("jumlah") int jumlah,
            @Field("satuan") String satuan,
            @Field("user") String user,
            @Field("tanggal_masuk") String waktu,
            @Field("harga") int harga
            );

    @FormUrlEncoded
    @POST("record.php")
    Call<ResponseModel> record (
            @Field("kode") String kode,
            @Field("keterangan") String keterangan,
            @Field("tanggal") String waktu,
            @Field("user") String user,
            @Field("bulan") String bulan
            );

    @FormUrlEncoded
    @POST("getmonth.php")
    Call<ResponseModel> date(
            @Field("user") String user
    );

    @FormUrlEncoded
    @POST("getdailyrecord.php")
    Call<ResponseModel> dailyRecord(
            @Field("user") String user,
            @Field("tanggal") String tanggal,
            @Field("keterangan") String keterangan
            );

    @FormUrlEncoded
    @POST("getdaterecord.php")
    Call<ResponseModel> dateList(
            @Field("user") String user,
            @Field("bulan") String bulan
    );

    @FormUrlEncoded
    @POST("getrecorddetail.php")
    Call<ResponseModel> recordDetail(
            @Field("user") String user,
            @Field("kode") String code,
            @Field("keterangan") String keterangan
    );

    @FormUrlEncoded
    @POST("getrecordmenu.php")
    Call<ResponseModel> recordMenu(
            @Field("user") String user,
            @Field("kode") String code
    );

    @FormUrlEncoded
    @POST("statset.php")
    Call<ResponseModel> satuanList(
            @Field("user") String user
    );

    @FormUrlEncoded
    @POST("statweekly.php")
    Call<ResponseModel> listSatuanWeekly(
            @Field("user") String user,
            @Field("satuan") String satuan,
            @Field("week") int week
    );

    @FormUrlEncoded
    @POST("statweeksatuan.php")
    Call<ResponseModel> statList(
            @Field("user") String user,
            @Field("satuan") String satuan,
            @Field("week") int week,
            @Field("month") int month,
            @Field("year") int year,
            @Field("keterangan") String keterangan
    );

    @FormUrlEncoded
    @POST("statweeksatuanin.php")
    Call<ResponseModel> statListIn(
            @Field("user") String user,
            @Field("satuan") String satuan,
            @Field("week") int week,
            @Field("month") int month,
            @Field("year") int year,
            @Field("keterangan") String keterangan
    );

    @FormUrlEncoded
    @POST("statweekbahan.php")
    Call<ResponseModel> statListBahan(
            @Field("user") String user,
            @Field("bahan") String bahan,
            @Field("week") int week,
            @Field("month") int month,
            @Field("year") int year,
            @Field("keterangan") String keterangan
    );

    @FormUrlEncoded
    @POST("statweekbahanin.php")
    Call<ResponseModel> statListBahanIn(
            @Field("user") String user,
            @Field("bahan") String bahan,
            @Field("week") int week,
            @Field("month") int month,
            @Field("year") int year,
            @Field("keterangan") String keterangan
    );

    @FormUrlEncoded
    @POST("recordmenu.php")
    Call<ResponseModel> recordMenu(
            @Field("kode") String kode,
            @Field("menu") String menu,
            @Field("jumlah") int jumlah,
            @Field("harga") int harga,
            @Field("user") String user,
            @Field("tanggal_keluar") String waktu
    );

    @FormUrlEncoded
    @POST("statmenu.php")
    Call<ResponseModel> statMenu(
            @Field("user") String user,
            @Field("menu") String menu,
            @Field("week") int week,
            @Field("month") int month,
            @Field("year") int year,
            @Field("keterangan") String keterangan
    );

    @FormUrlEncoded
    @POST("statpengunjung.php")
    Call<ResponseModel> statPengunjung(
            @Field("user") String user,
            @Field("week") int week,
            @Field("month") int month,
            @Field("year") int year,
            @Field("keterangan") String keterangan
    );


}
