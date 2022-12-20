package com.example.fixinventori.Activity.Usage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fixinventori.API.APIReport;
import com.example.fixinventori.API.APIRequestStock;
import com.example.fixinventori.API.ServerConnection;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.Adapter.LVAdapter.AdapterUsageCombine;
import com.example.fixinventori.Adapter.SpinnerAdapter.AdapterSpinnerRestock;
import com.example.fixinventori.R;
import com.example.fixinventori.model.ResponseModel;
import com.example.fixinventori.model.RestockModel;
import com.example.fixinventori.model.StocksModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CombineUsageFrag extends Fragment {
    Spinner spinner;
    Button btnUsageCombine;
    EditText etJumlahCombine;
    TextView tvSatuanCombine;
    ListView lvInputCombine;
    List<StocksModel> stocksModelList, combineDetail;
    List<RestockModel> listCombine = new ArrayList<>();
    UserSession session;
    String user, satuan, bahan, bahanI, satuanI, orderSeries,orderSeriesC, date,month,formatedTime;
    int jumlahCombine, jumlahI, idx;
    AdapterSpinnerRestock adapterSpinnerRestock;
    AdapterUsageCombine adapterUsageCombine;
    LocalDateTime time;

    public CombineUsageFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserSession(getActivity());
        user = session.getUserDetail().get("username");
        stocksModelList = new ArrayList<>();
        combineDetail = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_combine_usage, container, false);
        spinner = view.findViewById(R.id.spinnerMaterialCombine);
        btnUsageCombine = view.findViewById(R.id.btnUsageCombine);
        etJumlahCombine = view.findViewById(R.id.etJumlahMaterialCombine);
        tvSatuanCombine = view.findViewById(R.id.tvSatuanCombine);
        lvInputCombine = view.findViewById(R.id.lvInputCombine);

        getStocks();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                satuan=listCombine.get(position).getSatuan();
                bahan = listCombine.get(position).getBahan_baku();
                idx = listCombine.get(position).getId();
                tvSatuanCombine.setText(satuan);

                System.out.println(idx);

                getDetailCombine();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnUsageCombine.setOnClickListener(view1->{
            if(!isCheckInput()) Toast.makeText(getActivity(), "jumlah bahan tidak boleh 0", Toast.LENGTH_SHORT).show();
            if(etJumlahCombine.getText().toString().isEmpty() || etJumlahCombine.getText().toString().equals("0"))
                Toast.makeText(getActivity(), "Isi jumlah bahan dahulu", Toast.LENGTH_SHORT).show();
            else {
                time = LocalDateTime.now();
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMddHHmmss");
                DateTimeFormatter timeStamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                DateTimeFormatter monthType = DateTimeFormatter.ofPattern("MM/yyyy");
                month = monthType.format(time);
                formatedTime = timeStamp.format(time);
                date = dtf.format(time);
                orderSeries = "B."+date;
                orderSeriesC = "C."+date;

                jumlahCombine = Integer.parseInt(etJumlahCombine.getText().toString().trim());
                record();
                recordMasuk();
                recordRestock();
                restockAdd();

                for (int i = 0; i<combineDetail.size();i++){
                    View view2 = lvInputCombine.getChildAt(i);

                    TextView tvJumlah = view2.findViewById(R.id.etStockJumlah);
                    TextView tvBahan = view2.findViewById(R.id.tvStockName);
                    TextView tvSatuan = view2.findViewById(R.id.tvStockSatuan);

                    jumlahI = Integer.parseInt(tvJumlah.getText().toString().trim());
                    bahanI = tvBahan.getText().toString().trim();
                    satuanI = tvSatuan.getText().toString().trim();
                    reportManualUsage();
                    stockOutCombine();

                    if (i==combineDetail.size()) requireActivity().finish();
                }
            }
        });

        return view;
    }

    private boolean isCheckInput(){
        boolean b = true;
        for (int i = 0; i<combineDetail.size();i++){
            View view2 = lvInputCombine.getChildAt(i);
            TextView tvJumlah = view2.findViewById(R.id.etStockJumlah);
            if(tvJumlah.getText().toString().trim().isEmpty())
                Toast.makeText(getActivity(), "Isi jumlah bahan dahulu", Toast.LENGTH_SHORT).show();
            else {
                jumlahI = Integer.parseInt(tvJumlah.getText().toString().trim());
                if (jumlahI == 0) {
                    b = false;
                    break;
                }
            }
        }
        return b;
    }

    private void getStocks(){
        APIRequestStock stockData = ServerConnection.connection().create(APIRequestStock.class);
        Call<ResponseModel> showData = stockData.showStock(user);

        showData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if(response.body()!= null)
                    stocksModelList = response.body().getStocksModels();
                if(stocksModelList != null){
                    for (int i = 0; i < stocksModelList.size(); i++) {
                        if(stocksModelList.get(i).getKeterangan().equals("kombinasi"))
                            listCombine.add(new RestockModel(stocksModelList.get(i).getId(),
                                    stocksModelList.get(i).getBahan_baku(), stocksModelList.get(i).getSatuan()));
                    }
                    if(getActivity()!=null) {
                        adapterSpinnerRestock = new AdapterSpinnerRestock(getActivity(), listCombine);
                        spinner.setAdapter(adapterSpinnerRestock);
                        spinner.setSelection(0);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call,@NonNull Throwable t) {
                if(getActivity()!=null){
                    Toast.makeText(getActivity(), "Gagal memuat stock: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getDetailCombine(){
        APIRequestStock data = ServerConnection.connection().create(APIRequestStock.class);
        Call<ResponseModel> getData = data.combineDetail(bahan, user);

        getData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if(response.body()!=null)
                    combineDetail = response.body().getCombineDetail();
                if(combineDetail!=null){
                    if(getActivity()!=null) {
                        adapterUsageCombine = new AdapterUsageCombine(getActivity(), combineDetail);
                        lvInputCombine.setAdapter(adapterUsageCombine);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                if(getActivity()!=null){
                    Toast.makeText(getActivity(), "detai gagal:" +t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void record(){
        APIReport recordData = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> record = recordData.record(orderSeries,"barang keluar", formatedTime, user, month);

        record.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {

            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                if(getActivity()!=null){
                    Toast.makeText(getActivity(), "record gagal: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void reportManualUsage(){
        APIReport report = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> reportData = report.recordUsage(
                orderSeries, bahanI, jumlahI, satuanI,"kombinasi", user,0,formatedTime );

        reportData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call,@NonNull Response<ResponseModel> response) {

            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call,@NonNull Throwable t) {
                if(getActivity()!=null){
                    Toast.makeText(getActivity(), "report gagal: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void recordRestock(){
        APIReport record = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> recordData = record.recordRestock(orderSeriesC,bahan,jumlahCombine,satuan,user,formatedTime,0);

        recordData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call,@NonNull Response<ResponseModel> response) {

            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                if(getActivity()!=null){
                    Toast.makeText(getActivity(), "gagal record restock"+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void recordMasuk(){
        APIReport record = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> recordData = record.record(orderSeriesC,"barang masuk",
                formatedTime, user, month);

        recordData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {

            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                if(getActivity()!=null){
                    Toast.makeText(getActivity(), "gagal record"+ t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void stockOutCombine(){
        APIRequestStock data = ServerConnection.connection().create(APIRequestStock.class);
        Call<ResponseModel> updateData = data.stockOutCombineData(bahanI, jumlahI, user);

        updateData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {

            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call,@NonNull Throwable t) {
                if(getActivity()!=null){
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
                System.out.println(t.getMessage());
            }
        });
    }

    private void restockAdd(){
        APIRequestStock stockData = ServerConnection.connection().create(APIRequestStock.class);
        Call<ResponseModel> addStock = stockData.addStocks(idx,jumlahCombine);

        addStock.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull retrofit2.Response<ResponseModel> response) {
                if(getActivity()!=null){
                    Toast.makeText(getActivity(), "berhasil menambahkan stok", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                if(getActivity()!=null){
                    Toast.makeText(getActivity(), "gagal menambahkan stok "+t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}