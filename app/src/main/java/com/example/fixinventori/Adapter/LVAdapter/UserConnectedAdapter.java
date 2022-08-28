package com.example.fixinventori.Adapter.LVAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fixinventori.API.APIAccounts;
import com.example.fixinventori.API.ServerConnection;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.Chat.Model.ManagerModel;
import com.example.fixinventori.Chat.utils.Constants;
import com.example.fixinventori.R;
import com.example.fixinventori.UsageAutoApplication;
import com.example.fixinventori.model.ResponseModel;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserConnectedAdapter extends ArrayAdapter<ManagerModel> {
    List<ManagerModel> list;
    Context context;
    String manager, user;
    UserSession session;
    int index;

    public UserConnectedAdapter(@NonNull Context context, @NonNull List<ManagerModel> objects) {
        super(context, R.layout.user_list_row, objects);
        list = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        session = new UserSession(context);
        manager = session.getManagerDetail().get("manager");
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.user_list_row, parent, false);

        RoundedImageView rivProfile = convertView.findViewById(R.id.rivUserProfile);
        TextView tvUsername = convertView.findViewById(R.id.tvUsername);

        tvUsername.setText(list.get(position).getUsername());

        convertView.setOnClickListener(view1->{
            user = list.get(position).getUsername();
            index = position;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(String.format("Hapus %s",user));
            builder.setPositiveButton("hapus", (dialog, which) -> {
                deleteUser();
                dialog.dismiss();
            });
            builder.setNegativeButton("batal", (dialog, which) -> dialog.dismiss());
            builder.show();
        });

        return convertView;
    }

    private void deleteUser() {
        APIAccounts data = ServerConnection.connection().create(APIAccounts.class);
        Call<ResponseModel> deleteData = data.userDelete(user,manager);

        deleteData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call,@NonNull Response<ResponseModel> response) {
                if(response.body()!=null){
                    String pesan = response.body().getPesan();
                    int code = response.body().getCode();
                    if(code==1){
                        for (int i = 0; i < UsageAutoApplication.listConnectedUser.size(); i++) {
                            if(UsageAutoApplication.listConnectedUser.get(i).getUsername().equals(user))
                                UsageAutoApplication.listConnectedUser.remove(i);
                        }
                        try{
                            list.remove(index);
                        }
                        catch (IndexOutOfBoundsException e){
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        notifyDataSetChanged();
                        session.deleteString("username");
                        Toast.makeText(context, pesan, Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}
