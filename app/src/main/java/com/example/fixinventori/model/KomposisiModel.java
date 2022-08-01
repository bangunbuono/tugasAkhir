package com.example.fixinventori.model;

import com.google.gson.annotations.SerializedName;

public class KomposisiModel {


    private String bahan;
    private String satuan;
    private int jumlah, id;

    public KomposisiModel(int id, String bahan, String satuan, int jumlah) {
        this.bahan = bahan;
        this.satuan = satuan;
        this.jumlah = jumlah;
        this.id = id;
    }

    public String getBahan() {
        return bahan;
    }

    public void setBahan(String bahan) {
        this.bahan = bahan;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
