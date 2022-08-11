package com.example.fixinventori.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecordModel {
    private String kode;
    private String tanggal;
    private String bahan;
    private String satuan;
    private int jumlah;
    private int pengunjung;
    private int harga;

    public int getPengunjung() {
        return pengunjung;
    }

    public int getHarga() {
        return harga;
    }

    public void setBahan(String bahan) {
        this.bahan = bahan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getBahan() {
        return bahan;
    }

    public String getSatuan() {
        return satuan;
    }

    public int getJumlah() {
        return jumlah;
    }

    @SerializedName("record")
    private List<RecordModel> dailyRecord;
    public String getKeterangan() {
        return keterangan;
    }
    private String keterangan;
    public List<RecordModel> getDailyRecord() {
        return dailyRecord;
    }

    public void setDailyRecord(List<RecordModel> dailyRecord) {
        this.dailyRecord = dailyRecord;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

}
