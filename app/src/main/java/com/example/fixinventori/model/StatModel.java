package com.example.fixinventori.model;

import com.google.gson.annotations.SerializedName;

public class StatModel {
    private String satuan;
    private String bahan;
    @SerializedName("tanggal_keluar")
    private String date;
    private int jumlah;
    @SerializedName("tanggal_masuk") private String dateIn;

    public String getDateIn() {
        return dateIn;
    }

    public void setDateIn(String dateIn) {
        this.dateIn = dateIn;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBahan() {
        return bahan;
    }

    public void setBahan(String bahan) {
        this.bahan = bahan;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }
}
