package com.example.fixinventori.model;

import com.google.gson.annotations.SerializedName;

public class StatModel implements Comparable<StatModel>{
    private String satuan;
    private String bahan;
    private String menu;
    @SerializedName("tanggal_keluar")
    private String date;
    private int jumlah;
    private int pengunjung;

    private int harga;

    @SerializedName("tanggal_masuk") private String dateIn;
    private String tanggal;
    public int getPengunjung() {
        return pengunjung;
    }

    public void setPengunjung(int pengunjung) {
        this.pengunjung = pengunjung;
    }

    public int getHarga() {
        return harga;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getDateIn() {
        return dateIn;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
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

    @Override
    public int compareTo(StatModel statModel) {
        if (this.getHarga() > statModel.getHarga()) {
            return 1;
        } else if (this.getHarga() < statModel.getHarga()) {
            return -1;
        }
        return 0;
    }
}
