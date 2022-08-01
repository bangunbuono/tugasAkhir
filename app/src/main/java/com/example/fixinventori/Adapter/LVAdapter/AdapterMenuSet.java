package com.example.fixinventori.Adapter.LVAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.fixinventori.API.APIRequestMenu;
import com.example.fixinventori.API.ServerConnection;
import com.example.fixinventori.Activity.Menu.MenuSet;
import com.example.fixinventori.Activity.Menu.MenuSetDetail;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.R;
import com.example.fixinventori.model.MenuModel;
import com.example.fixinventori.model.ResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterMenuSet extends ArrayAdapter<MenuModel> {
    private Context context;
    private List<MenuModel> listMenu;
    private List<MenuModel> menu;
    public TextView tvIdMenu, tvMenu;
    int index;
    String menuName, user;
    UserSession userSession;

    public AdapterMenuSet(Context context, List<MenuModel> list) {
        super(context, R.layout.menu_row,list);
        this.context = context;
        listMenu = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        userSession = new UserSession(context);
        user = userSession.getUserDetail().get("username");

        LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.menu_row, parent, false);

        tvMenu =convertView.findViewById(R.id.tvMenu);
        tvIdMenu = convertView.findViewById(R.id.tvIdMenu);

        tvMenu.setText(listMenu.get(position).getMenu());
        tvIdMenu.setText(listMenu.get(position).getId()+"");


        convertView.setOnClickListener(view -> {
            index = listMenu.get(position).getId();
            detailMenu();
        });

        convertView.setOnLongClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            index = listMenu.get(position).getId();
            menuName = listMenu.get(position).getMenu();
            builder.setMessage(menuName);
            builder.setPositiveButton("Hapus", (dialogInterface, i) -> {
                System.out.println(menuName +"\n"+user);
                deleteMenu();
                listMenu.remove(position);
                notifyDataSetChanged();
                Handler handler = new Handler();
                handler.postDelayed(() -> (
                        (MenuSet)context).retrieveData(), 500);
            });

            builder.setNegativeButton("ubah", (dialogInterface, i) -> detailMenu());
            builder.show();
            return false;
        });

        return convertView;

    }
    private void deleteMenu(){
        APIRequestMenu dataMenu = ServerConnection.connection().create(APIRequestMenu.class);
        Call<ResponseModel> deleteData = dataMenu.deleteMenu(index, menuName, user);
        
        deleteData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                Toast.makeText(context, "Berhasil menghapus menu", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call,@NonNull Throwable t) {
                Toast.makeText(context, "Gagal menghapus menu: "+t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void detailMenu(){
        APIRequestMenu dataMenu = ServerConnection.connection().create(APIRequestMenu.class);
        Call<ResponseModel> detailData = dataMenu.detailMenu(index);

        detailData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                assert response.body() != null;
                menu = response.body().getData();

                int menuId = menu.get(0).getId();
                String menuName = menu.get(0).getMenu();
                int menuPrice = menu.get(0).getHarga();
                String menuDesc = menu.get(0).getDeskripsi();

                Intent intent = new Intent(context, MenuSetDetail.class);
                intent.putExtra("menuId", menuId);
                intent.putExtra("menuName", menuName);
                intent.putExtra("menuPrice", menuPrice);
                intent.putExtra("menuDesc", menuDesc);
                context.startActivity(intent);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(context, "Gagal menghapus data "+t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}
