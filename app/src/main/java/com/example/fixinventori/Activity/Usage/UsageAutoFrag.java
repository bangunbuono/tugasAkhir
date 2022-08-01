package com.example.fixinventori.Activity.Usage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.fixinventori.API.APIRequestMenu;
import com.example.fixinventori.API.ServerConnection;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.Adapter.LVAdapter.AdapterUsageAuto;
import com.example.fixinventori.R;
import com.example.fixinventori.UsageAutoApplication;
import com.example.fixinventori.model.MenuModel;
import com.example.fixinventori.model.ResponseModel;
import com.example.fixinventori.model.UsageMenuModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsageAutoFrag extends Fragment {

    ListView lvUsageAuto;
    Button btnOrder;
    AdapterUsageAuto adapterUsageAuto;
    List<MenuModel> menuModels;
    String konfirmasi, pesanan, user;
    int jumlah;
    UserSession userSession;
    public static LinearLayout layoutItem;
    public static TextView tvItem;

    public UsageAutoFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_usage_auto, container, false);

        userSession = new UserSession(getActivity());
        user = userSession.getUserDetail().get("username");

        menuModels = new ArrayList<>();

        lvUsageAuto = view.findViewById(R.id.lvUsageAuto1);
        btnOrder = view.findViewById(R.id.btnOrder1);
        layoutItem = view.findViewById(R.id.layoutItemAuto);
        tvItem = view.findViewById(R.id.tvTotalStockAuto);

        getList();

        btnOrder.setOnClickListener(view1 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("konfirmasi pesanan");
            konfirmasi = "item: ";
            for (int i = 0; i < menuModels.size(); i++) {
                if(menuModels.get(i).getQty() != 0) {
                    pesanan = menuModels.get(i).getMenu();
                    jumlah = menuModels.get(i).getQty();
                    konfirmasi = konfirmasi + "\n" + pesanan + ": " + jumlah+"\n";
                }
            }

            builder.setMessage(konfirmasi);

            builder.setPositiveButton("Ok", (dialogInterface, i) -> {
                UsageAutoApplication.orderList = new ArrayList<>();
                for (i = 0; i < menuModels.size(); i++) {
                    if(menuModels.get(i).getQty() != 0){
                        jumlah = menuModels.get(i).getQty();
                        pesanan = menuModels.get(i).getMenu();
                        for(int repetisi = 0; repetisi<jumlah;repetisi++){
                            UsageAutoApplication.orderList.add(new UsageMenuModel(pesanan, 1));
                        }
                        menuModels.get(i).setQty(0);
                    }
                    //konfirmOrder();
                }
                startActivity(new Intent(getActivity(), UsageKomposisiDetail.class));
                getActivity().finish();
//                adapterUsageAuto.notifyDataSetChanged();

            });
            builder.setNegativeButton(
                    "Cancel", (dialogInterface, i) -> {
                    });
            builder.show();
        });
        return view;
    }

    private void getList(){
        APIRequestMenu dataMenu = ServerConnection.connection().create(APIRequestMenu.class);
        Call<ResponseModel> showData = dataMenu.showMenu(user);

        showData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                int code = response.body().getCode();
                String pesan = response.body().getPesan();

                menuModels = response.body().getData();
                if(menuModels!= null){
                    adapterUsageAuto = new AdapterUsageAuto(getActivity(), menuModels);
                    lvUsageAuto.setAdapter(adapterUsageAuto);
                    Toast.makeText(getActivity(),
                            "success: " +"(" +code+ ")" +" "+ pesan, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }

//    private void konfirmOrder(){
//        APIUsageAuto usageData = ServerConnection.connection().create(APIUsageAuto.class);
//        Call<ResponseModel> order = usageData.usage(pesanan.trim().replaceAll("\\s","_"),jumlah);
//
//        order.enqueue(new Callback<ResponseModel>() {
//            @Override
//            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
//                Toast.makeText(getActivity(), "berhasil memesan", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
//                Toast.makeText(getActivity(), "gagal: "+t.getMessage(),
//                        Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }

}