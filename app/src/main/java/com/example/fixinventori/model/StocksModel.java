package com.example.fixinventori.model;

public class StocksModel {


    private int id;
    private int waktu;
    private int min_pesan;
    private int jumlah;
    private String bahan_baku;
    private String satuan;
    private String user;

    public StocksModel(int id, int waktu, int min_pesan, int jumlah, String bahan_baku, String satuan) {
        this.id = id;
        this.waktu = waktu;
        this.min_pesan = min_pesan;
        this.jumlah = jumlah;
        this.bahan_baku = bahan_baku;
        this.satuan = satuan;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public int getWaktu() {
        return waktu;
    }

    public void setWaktu(int waktu) {
        this.waktu = waktu;
    }

    public int getMin_pesan() {
        return min_pesan;
    }

    public void setMin_pesan(int min_pesan) {
        this.min_pesan = min_pesan;
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
