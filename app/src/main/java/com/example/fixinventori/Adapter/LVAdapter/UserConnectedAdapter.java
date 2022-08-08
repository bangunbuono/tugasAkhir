package com.example.fixinventori.Adapter.LVAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fixinventori.Chat.Model.ManagerModel;
import com.example.fixinventori.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class UserConnectedAdapter extends ArrayAdapter<ManagerModel> {
    List<ManagerModel> list;
    Context context;

    public UserConnectedAdapter(@NonNull Context context, @NonNull List<ManagerModel> objects) {
        super(context, R.layout.user_list_row, objects);
        list = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.user_list_row, parent, false);

        RoundedImageView rivProfile = convertView.findViewById(R.id.rivUserProfile);
        TextView tvUsername = convertView.findViewById(R.id.tvUsername);

        tvUsername.setText(list.get(position).getUsername());

        return convertView;
    }
}
