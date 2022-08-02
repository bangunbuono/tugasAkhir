package com.example.fixinventori.Chat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixinventori.Chat.ChatActivity;
import com.example.fixinventori.Chat.ManagerChatActivity;
import com.example.fixinventori.Chat.Model.ManagerModel;
import com.example.fixinventori.Chat.utils.Constants;
import com.example.fixinventori.R;

import java.util.List;

public class ManagerListAdapter extends RecyclerView.Adapter<ManagerListAdapter.ViewHolder>{

    List<ManagerModel> list;
    Context context;


    public ManagerListAdapter(Context context, List<ManagerModel> object){
        this.context = context;
        list = object;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvUsername.setText(list.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvUsername;

        ViewHolder(@NonNull View itemView){
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);

            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, ManagerChatActivity.class);
                intent.putExtra(Constants.KEY_USER, list.get(getAdapterPosition()).getUsername());
                context.startActivity(intent);
            });
        }
    }

    private Bitmap getUserImage(String encoded){
        byte[]bytes = Base64.decode(encoded, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
