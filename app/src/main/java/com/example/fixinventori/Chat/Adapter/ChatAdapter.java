package com.example.fixinventori.Chat.Adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixinventori.Chat.Model.ChatMessageModel;
import com.example.fixinventori.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<ChatMessageModel> chatMessageModels;
    Bitmap receiverProfilImage;
    final String senderId;

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    public void setReceiverProfilImage(Bitmap bitmap){
        receiverProfilImage = bitmap;
    }

    public ChatAdapter(List<ChatMessageModel> chatMessageModels, String senderId) {
        this.chatMessageModels = chatMessageModels;
        this.receiverProfilImage = receiverProfilImage;
        this.senderId = senderId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT){
            return new MyMessageViewHolder(
              LayoutInflater.from(parent.getContext()).inflate(R.layout.my_message_row, parent, false));

        }else
            return new OtherMessageVIewHolder(
               LayoutInflater.from(parent.getContext()).inflate(R.layout.other_message_row,parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_SENT)
            ((MyMessageViewHolder)holder).setBinding(chatMessageModels.get(position));
        else
            ((OtherMessageVIewHolder)holder).setBinding(chatMessageModels.get(position), receiverProfilImage);

    }

    @Override
    public int getItemCount() {
        return chatMessageModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(chatMessageModels.get(position).senderId.equals(senderId)) return VIEW_TYPE_SENT;
        else return VIEW_TYPE_RECEIVED;
    }

    static class MyMessageViewHolder extends RecyclerView.ViewHolder{

        TextView tvMyMessage, tvMyDateTime;
        MyMessageViewHolder (@NonNull View itemView){
            super(itemView);
            tvMyMessage = itemView.findViewById(R.id.tvMyMessage);
            tvMyDateTime = itemView.findViewById(R.id.tvMyDateTime);
        }

        void setBinding(ChatMessageModel chatMessageModel){
            tvMyMessage.setText(chatMessageModel.message);
            tvMyDateTime.setText(chatMessageModel.dateTime);

        }

    }

    static class OtherMessageVIewHolder extends RecyclerView.ViewHolder{

        TextView tvOtherMessage, tvDateTime;
        RoundedImageView rivProfileChat;

        OtherMessageVIewHolder (@NonNull View itemView){
            super(itemView);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvOtherMessage = itemView.findViewById(R.id.tvOtherMessage);
        }

        void setBinding(ChatMessageModel messageModel, Bitmap receivedProfilImage){
            tvOtherMessage.setText(messageModel.message);
            tvDateTime.setText(messageModel.dateTime);
            if(receivedProfilImage!=null)
            rivProfileChat.setImageBitmap(receivedProfilImage);
        }
    }
}
