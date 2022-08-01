package com.example.fixinventori.model;

public class UsageMenuModel {
    private String menu,deskripsi;
    private int qty;

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public UsageMenuModel(String menu, int qty) {
        this.menu = menu;
        this.qty = qty;
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
