package com.example.fixinventori.Chat.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixinventori.Chat.Model.ChatMessageModel;
import com.example.fixinventori.Chat.Model.UserModel;
import com.example.fixinventori.R;

import java.util.List;

public class ConversationList extends RecyclerView.Adapter<ConversationList.ConversationHolder> {

    List<ChatMessageModel> conversationList;

    public ConversationList(List<ChatMessageModel> list){
        conversationList = list;
    }

    public class ConversationHolder extends RecyclerView.ViewHolder{
        TextView tvUserName, tvRecentMessage;

        public ConversationHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUsername);
            tvRecentMessage = itemView.findViewById(R.id.tvRecentMessage);
        }

        void setData(ChatMessageModel mode){
            tvUserName.setText(mode.conversionName);
            if(mode.message!=null) tvRecentMessage.setText(mode.message);
        }
    }

    @NonNull
    @Override
    public ConversationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_conversation_row, parent, false);
        return new ConversationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationHolder holder, int position) {
        holder.setData(conversationList.get(position));
    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }
}
