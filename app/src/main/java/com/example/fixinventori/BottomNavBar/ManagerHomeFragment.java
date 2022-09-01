package com.example.fixinventori.BottomNavBar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fixinventori.API.APIAccounts;
import com.example.fixinventori.API.APIDashboardData;
import com.example.fixinventori.API.ServerConnection;
import com.example.fixinventori.Activity.Menu.MenuSet;
import com.example.fixinventori.Activity.Report.InventReport;
import com.example.fixinventori.Activity.Stock.InventorySet;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.Chat.Model.ManagerModel;
import com.example.fixinventori.Chat.utils.Constants;
import com.example.fixinventori.R;
import com.example.fixinventori.model.ResponseModel;
import com.example.fixinventori.model.StatModel;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerHomeFragment extends Fragment {
    UserSession session;
    String manager, selectedUser;
    Spinner spinnerUser;
    TextView tvManagerHome, tvInvSet, tvMenuSet, tvMoreReport
            , tvVisitor, tvCashflow, tvMenuSales, tvUsageMaterial;
    List<ManagerModel> userList;
    List<StatModel> maxMenu,maxStockOut,maxStockIn, cashIn, cashOut, visitors;
    ArrayList<String> user = new ArrayList<>();
    ArrayAdapter<String> adapter;
    int index, week;
    RoundedImageView rivHomeProfile;
    ExecutorService service;

    public ManagerHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new UserSession(getActivity());
        manager = session.getManagerDetail().get("manager");
        userList = new ArrayList<>();
        maxMenu = new ArrayList<>();
        maxStockIn = new ArrayList<>();
        maxStockOut = new ArrayList<>();
        cashIn = new ArrayList<>();
        cashOut = new ArrayList<>();
        visitors= new ArrayList<>();
        getWeek();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manager_home, container, false);
        tvUsageMaterial = view.findViewById(R.id.tvUsageMaterial);
        tvMenuSales = view.findViewById(R.id.tvMenuSales);
        tvVisitor = view.findViewById(R.id.tvVisitors);
        tvCashflow = view.findViewById(R.id.tvCashFlow);
        tvManagerHome = view.findViewById(R.id.tvManagerHome);
        tvInvSet = view.findViewById(R.id.tvInvSet);
        tvMenuSet = view.findViewById(R.id.tvMenuSet);
        spinnerUser = view.findViewById(R.id.spinnerUser);
        tvMoreReport = view.findViewById(R.id.tvMoreReport);
        rivHomeProfile = view.findViewById(R.id.rivHomeProfile);

        service = Executors.newSingleThreadExecutor();

        getUsers();

        tvMoreReport.setOnClickListener(view1-> startActivity(new Intent(getActivity(), InventReport.class)));

        tvManagerHome.setText(String.format("%s%s",
                manager.substring(0,1).toUpperCase(Locale.ROOT),
                manager.substring(1).toLowerCase(Locale.ROOT)));

        if(session.getString(Constants.KEY_IMAGE)!=null)
            decodeImage(session.getString(Constants.KEY_IMAGE));

        tvInvSet.setOnClickListener(view1-> startActivity(new Intent(getActivity(), InventorySet.class)));

        tvMenuSet.setOnClickListener(view1-> startActivity(new Intent(getActivity(), MenuSet.class)));

        return view;
    }

    void getUsers() {
        APIAccounts get = ServerConnection.connection().create(APIAccounts.class);
        Call<ResponseModel> getData = get.getUser(manager);

        getData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if(response.body()!=null) {
                    userList = response.body().getRecordManager();
                    if (userList!=null){
                        userList.forEach(managerModel -> user.add(managerModel.username));
                        adapter = new ArrayAdapter<>(
                                getActivity(),R.layout.tv_selected_user, user);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerUser.setAdapter(adapter);
                        spinnerUser.setSelection(0);
                        spinnerItemSelection();
                        setSpinnerSelection();
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseModel> call,@NonNull Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void spinnerItemSelection(){
        spinnerUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String user = userList.get(position).getUsername();
                session.putString("username", user);
                selectedUser = user;

                service.execute(()->{
                    getCashIn();
                    getMaxMenu();
                    getMaxStockIn();
                    getPengunjung();
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSpinnerSelection(){
        if(session.getString("username")!=null){
            String userSelected = session.getString("username");
            for (int i = 0; i < user.size(); i++) {
                if(user.get(i).equals(userSelected)) {
                    index = i;
                    break;
                }
            }
            spinnerUser.setSelection(index);
        }
    }

    private void decodeImage(String encodedImage){
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
        rivHomeProfile.setImageBitmap(bitmap);
    }

    private void getMaxMenu(){
        APIDashboardData data = ServerConnection.connection().create(APIDashboardData.class);
        Call<ResponseModel> getMaxMenu = data.maxMenu(selectedUser, week);

        getMaxMenu.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call,@NonNull Response<ResponseModel> response) {
                if(response.body()!=null) {
                    maxMenu = new ArrayList<>();
                    maxMenu = response.body().getMaxMenu();
                    requireActivity().runOnUiThread(()->{
                        if(maxMenu!=null && maxMenu.size()>0) {
                            tvMenuSales.setText(String.format("MENU\n" +
                                            "paling banyak terjual: %s\n" +
                                            "paling sedikit terjual: %s",
                                    maxMenu.get(0).getMenu() +" "+ maxMenu.get(0).getJumlah(),
                                    maxMenu.get(maxMenu.size()-1).getMenu() +" "+ maxMenu.get(maxMenu.size()-1).getJumlah()));
                        }
                    });
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseModel> call,@NonNull Throwable t) {
                Toast.makeText(getActivity(), "a"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getMaxStockIn(){
        APIDashboardData data = ServerConnection.connection().create(APIDashboardData.class);
        Call<ResponseModel> getMaxStockIn = data.maxStockIn(selectedUser, week);

        getMaxStockIn.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if(response.body()!=null) {
                    maxStockIn = new ArrayList<>();
                    maxStockIn = response.body().getMaxStockIn();
                    if(maxStockIn!=null) getMaxStockOut();
                    else System.out.println("bahan masuk kosong");
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "b"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getMaxStockOut(){
        APIDashboardData data = ServerConnection.connection().create(APIDashboardData.class);
        Call<ResponseModel> getMaxStockOut = data.maxStockOut(selectedUser, week);

        getMaxStockOut.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if(response.body()!=null) {
                    maxStockOut = new ArrayList<>();
                    maxStockOut = response.body().getMaxStockOut();
                    requireActivity().runOnUiThread(()->{
                        if(maxStockOut!=null)
                            if(maxStockOut.size()>0 && maxStockIn.size()>0)
                                tvUsageMaterial.setText(String.
                                        format("BAHAN \npaling banyak digunakan minggu ini: %s %s %s \n" +
                                                        "paling banyak dibeli minggu ini: %s %s %s",
                                                maxStockOut.get(0).getBahan(),
                                                maxStockOut.get(0).getJumlah(),
                                                maxStockOut.get(0).getSatuan(),
                                                maxStockIn.get(0).getBahan(),
                                                maxStockIn.get(0).getJumlah(),
                                                maxStockIn.get(0).getSatuan()));
                            else System.out.println("bahan keluar kosong");
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call,@NonNull Throwable t) {
                Toast.makeText(getActivity(), "c"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPengunjung(){
        APIDashboardData data = ServerConnection.connection().create(APIDashboardData.class);
        Call<ResponseModel> getPengunjung = data.pengunjung(selectedUser, week);

        getPengunjung.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if(response.body()!=null){
                    visitors = response.body().getPengunjung();
                    requireActivity().runOnUiThread(()->{
                        if(visitors!=null && visitors.size()>0){
                            tvVisitor.setText(String.
                                    format("PENGUNJUNG\nminggu ini: %s \n" +
                                                    "minggu lalu: %s",
                                            visitors.get(1).getPengunjung(),
                                            visitors.get(0).getPengunjung()));
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "d" +t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCashIn(){
        APIDashboardData data = ServerConnection.connection().create(APIDashboardData.class);
        Call<ResponseModel> getCashIn = data.cashIn(selectedUser, week);

        getCashIn.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if(response.body()!=null){
                    cashIn = response.body().getCashIn();
                    if(cashIn!=null){
                        getCashOut();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "e"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getCashOut(){
        APIDashboardData data = ServerConnection.connection().create(APIDashboardData.class);
        Call<ResponseModel> getCashOut = data.cashOut(selectedUser, week);

        getCashOut.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if (response.body()!=null) cashOut = response.body().getCashOut();
                requireActivity().runOnUiThread(()->{
                    if(cashOut!=null){
                        if(cashIn.size()>0 || cashOut.size()>0){
                            float keluar = cashOut.get(1).getHarga();
                            float masuk = cashIn.get(1).getHarga();
                            float untung = masuk-keluar;
                            float untung0 = cashIn.get(0).getHarga() - cashOut.get(0).getHarga();
                            if(untung0<untung){
                                tvCashflow.setText(String.format("%s dari minggu lalu",
                                        "pendapatan minggu ini Rp"+untung +"\n" +
                                                "naik "+((untung-untung0)/untung0)*100+"%"));
                            } else
                                tvCashflow.setText(String.format("%s dari minggu lalu",
                                        "pendapatan minggu ini Rp"+untung +"\n" +
                                                "turun "+((untung0-untung)/untung0)*100+"%"));
                        }
                    }
                });
            }
            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "f"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getWeek(){
        Calendar calendar = Calendar.getInstance();
        calendar.setMinimalDaysInFirstWeek(4);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        week = calendar.get(Calendar.WEEK_OF_YEAR);
    }
}