package com.example.fixinventori.model;

public class RestockModel {
    private int id;
    private String bahan_baku, satuan;

    public RestockModel(int id, String bahan_baku, String satuan) {
        this.id = id;
        this.bahan_baku = bahan_baku;
        this.satuan = satuan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBahan_baku() {
        return bahan_baku;
    }

    public void setBahan_baku(String bahan_baku) {
        this.bahan_baku = bahan_baku;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }
}
