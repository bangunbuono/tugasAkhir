package com.example.fixinventori.Chat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixinventori.Chat.Activity.ManagerChatActivity;
import com.example.fixinventori.Chat.Model.ChatMessageModel;
import com.example.fixinventori.Chat.utils.Constants;
import com.example.fixinventori.R;

import java.util.List;

public class ManagerConversationListAdapter extends RecyclerView.Adapter<ManagerConversationListAdapter.ConversationHolder> {

    List<ChatMessageModel> conversationList;
    Context context;

    public ManagerConversationListAdapter(Context context, List<ChatMessageModel> list){
        conversationList = list;
        this.context = context;
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
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, ManagerChatActivity.class);
                System.out.println(conversationList.get(getAdapterPosition()).getConversionName());
                intent.putExtra(Constants.KEY_USER, conversationList.get(getAdapterPosition()).getConversionName());
                context.startActivity(intent);
            });
        }
    }

    @NonNull
    @Override
    public ConversationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_conversation_row, parent, false);
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