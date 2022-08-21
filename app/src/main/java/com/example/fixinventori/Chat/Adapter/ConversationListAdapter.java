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

import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.Chat.Activity.ChatActivity;
import com.example.fixinventori.Chat.Model.ChatMessageModel;
import com.example.fixinventori.Chat.Model.UserModel;
import com.example.fixinventori.Chat.utils.Constants;
import com.example.fixinventori.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class ConversationListAdapter extends RecyclerView.Adapter<ConversationListAdapter.ConversationHolder> {

    List<ChatMessageModel> conversationList;
    Context context;
    UserSession session;
    FirebaseFirestore firebaseFirestore;

    public ConversationListAdapter(Context context, List<ChatMessageModel> list){
        conversationList = list;
        this.context = context;
    }

    public class ConversationHolder extends RecyclerView.ViewHolder{
        TextView tvUserName, tvRecentMessage;
        RoundedImageView rivOtherProfile;

        public ConversationHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUsername);
            tvRecentMessage = itemView.findViewById(R.id.tvRecentMessage);
            rivOtherProfile = itemView.findViewById(R.id.rivOtherProfile);

        }

        void setData(ChatMessageModel mode){
            tvUserName.setText(mode.conversionName);
            if(mode.message!=null) tvRecentMessage.setText(mode.message);
            if(mode.conversionImage!=null) rivOtherProfile.setImageBitmap(getConversionImage(mode.conversionImage));
            itemView.setOnClickListener(v -> {

                Intent intent = new Intent(context, ChatActivity.class);
    //                System.out.println(conversationList.get(getAdapterPosition()).getConversionName());
                intent.putExtra(Constants.KEY_MANAGER, conversationList.get(getAdapterPosition()).getConversionName());
                context.startActivity(intent);
//                UserModel userModel = new UserModel();
//                userModel.id = mode.conversionId;
//                userModel.name = mode.conversionName;
//                conversationListener.onConversationClicked(userModel);
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

    private Bitmap getConversionImage(String encodedImage){
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
