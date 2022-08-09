package com.example.fixinventori.model;

public class UsageMenuModel {
    private String menu,deskripsi;
    private int qty;

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    private int harga;

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public UsageMenuModel(String menu, int qty, int harga) {
        this.menu = menu;
        this.qty = qty;
        this.harga = harga;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

}
