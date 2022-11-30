package com.example.fixinventori.model;

import com.example.fixinventori.Chat.Model.ManagerModel;

import java.util.ArrayList;
import java.util.List;

public class ResponseModel {
    private int code;
    private List<StocksModel> stocksModels;

    public List<StocksModel> getCombineDetail() {
        return combineDetail;
    }

    private List<StocksModel> combineDetail;

    private List<StocksModel> avgDay;

    private ArrayList<RestockModel> stocks;
    private List<TimeSeriesModel> history;

    public List<TimeSeriesModel> getHistory() {
        return history;
    }

    private String pesan, status;
    private List<MenuModel> data;
    private List<KomposisiModel> komposisiModelList, komposisiOpsiList;
    private List<MonthModel> bulan;
    private List<RecordModel> record, recordDetail;
    private List<RecordModel> date;
    private List<StatModel> statmodel;
    private List<StatModel> statSatuan;
    private List<StatModel> statCashOut;
    private List<StatModel> statCashIn;
    private List<StatModel> statPengunjung;
    private List<StatModel> maxMenu;
    private List<StatModel> maxStockIn;
    private List<StatModel> maxStockOut;
    private List<StatModel> pengunjung;
    private List<StatModel> cashIn;
    private List<StatModel> cashOut;
    private List<StatModel> bahanKeluar;
    public List<StatModel> getMenu() {
        return menu;
    }

    private List<StatModel> menu;

    public List<StatModel> getBahanKeluar() {
        return bahanKeluar;
    }

    public List<StatModel> getBahanMasuk() {
        return bahanMasuk;
    }

    private List<StatModel> bahanMasuk;


    public List<StatModel> getCashIn() {
        return cashIn;
    }


    public List<StatModel> getCashOut() {
        return cashOut;
    }

    private List<StatModel> statMenu;

    private List<ManagerModel> recordManager;

    private List<UsageMenuModel> recordMenu;
    private List<UserModel> profilPicture;
    public List<StatModel> getMaxMenu() {
        return maxMenu;
    }

    public List<StatModel> getPengunjung() {
        return pengunjung;
    }

    public List<StatModel> getMaxStockIn() {
        return maxStockIn;
    }

    public List<StocksModel> getAvgDay() {
        return avgDay;
    }

    public List<StatModel> getMaxStockOut() {
        return maxStockOut;
    }

    public List<UserModel> getProfilPicture() {
        return profilPicture;
    }

    public List<StatModel> getStatCashOut() {
        return statCashOut;
    }

    public List<StatModel> getStatCashIn() {
        return statCashIn;
    }

    public List<StatModel> getStatPengunjung() {
        return statPengunjung;
    }

    public List<StatModel> getStatMenu() {
        return statMenu;
    }

    public void setStatMenu(List<StatModel> statMenu) {
        this.statMenu = statMenu;
    }

    public void setRecordManager(List<ManagerModel> recordManager) {
        this.recordManager = recordManager;
    }

    public void setRecordMenu(List<UsageMenuModel> recordMenu) {
        this.recordMenu = recordMenu;
    }

    public List<UsageMenuModel> getRecordMenu() {
        return recordMenu;
    }

    public List<ManagerModel> getRecordManager() {
        return recordManager;
    }

    public List<StatModel> getStatBahan() {
        return statBahan;
    }

    public void setStatBahan(List<StatModel> statBahan) {
        this.statBahan = statBahan;
    }

    private List<StatModel> statBahan;


    public List<StatModel> getStatSatuan() {
        return statSatuan;
    }

    public void setStatSatuan(List<StatModel> statSatuan) {
        this.statSatuan = statSatuan;
    }

    public List<StatModel> getStatmodel() {
        return statmodel;
    }

    public void setStatmodel(List<StatModel> statmodel) {
        this.statmodel = statmodel;
    }

    public void setStocks(ArrayList<RestockModel> stocks) {
        this.stocks = stocks;
    }

    public List<RecordModel> getRecordDetail() {
        return recordDetail;
    }

    public void setRecordDetail(List<RecordModel> recordDetail) {
        this.recordDetail = recordDetail;
    }

    public List<RecordModel> getDate() {
        return date;
    }

    public void setDate(List<RecordModel> date) {
        this.date = date;
    }

    public List<RecordModel> getRecord() {
        return record;
    }

    public void setRecord(List<RecordModel> record) {
        this.record = record;
    }

    public List<MonthModel> getBulan() {
        return bulan;
    }

    public void setBulan(List<MonthModel> bulan) {
        this.bulan = bulan;
    }

    public List<KomposisiModel> getKomposisiOpsiList() {
        return komposisiOpsiList;
    }

    public void setKomposisiOpsiList(List<KomposisiModel> komposisiOpsiList) {
        this.komposisiOpsiList = komposisiOpsiList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<RestockModel> getStocks() {
        return stocks;
    }

    public void setRestockModel(ArrayList<RestockModel> stocks) {
        this.stocks = stocks;
    }

    public List<StocksModel> getStocksModels() {
        return stocksModels;
    }

    public void setStocksModels(List<StocksModel> stocksModels) {
        this.stocksModels = stocksModels;
    }

    public List<KomposisiModel> getKomposisiModelList() {
        return komposisiModelList;
    }

    public void setKomposisiModelList(List<KomposisiModel> komposisiModelList) {
        this.komposisiModelList = komposisiModelList;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public List<MenuModel> getData() {
        return data;
    }

    public void setData(List<MenuModel> data) {
        this.data = data;
    }
}
