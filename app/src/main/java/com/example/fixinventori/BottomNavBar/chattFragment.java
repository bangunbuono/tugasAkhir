package com.example.fixinventori.BottomNavBar;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.Chat.Adapter.ConversationListAdapter;
import com.example.fixinventori.Chat.Model.ChatMessageModel;
import com.example.fixinventori.Chat.Activity.UserActivity;
import com.example.fixinventori.Chat.utils.Constants;
import com.example.fixinventori.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class chattFragment extends Fragment  {

    RecyclerView rvRecentConversation;
    FloatingActionButton fabNewChat;
    UserSession session;
    List<ChatMessageModel> conversations = new ArrayList<>();
    ConversationListAdapter adapter;
    FirebaseFirestore database;
    ProgressBar progressBar;

    public chattFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserSession(getActivity());
        database = FirebaseFirestore.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chatt, container, false);

        progressBar = view.findViewById(R.id.progressRegist);
        rvRecentConversation = view.findViewById(R.id.rvRecentConversation);
        fabNewChat = view.findViewById(R.id.fabNewChat);

        fabNewChat.setOnClickListener(view1 -> startActivity(
                new Intent(getActivity(), UserActivity.class)
        ));

        listenConversations();
        return view;
    }

    private void listenConversations(){
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID, session.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);

        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, session.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = ((value, error) -> {
        loading(true);
        if(error!=null) Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
        else if(value!=null){
            for (DocumentChange documentChange: value.getDocumentChanges()){
                if(documentChange.getType() == DocumentChange.Type.ADDED){
                    String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    ChatMessageModel chatMessageModel = new ChatMessageModel();
                    chatMessageModel.senderId = senderId;
                    chatMessageModel.receiverId = receiverId;
                    if(session.getString(Constants.KEY_USER_ID).equals(senderId)){
                        chatMessageModel.conversionImage = documentChange.getDocument().getString(Constants.KEY_RECEIVER_IMAGE);
                        chatMessageModel.conversionName = documentChange.getDocument().getString(Constants.KEY_RECEIVER_NAME);
                        chatMessageModel.conversionId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    }else {
                        chatMessageModel.conversionImage = documentChange.getDocument().getString(Constants.KEY_SENDER_IMAGE);
                        chatMessageModel.conversionName = documentChange.getDocument().getString(Constants.KEY_SENDER_NAME);
                        chatMessageModel.conversionId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    }
                    chatMessageModel.message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                    chatMessageModel.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    conversations.add(chatMessageModel);
                }else if(documentChange.getType() == DocumentChange.Type.MODIFIED){
                    for (int i = 0; i<conversations.size();i++) {
                        String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                        String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                        if (conversations.get(i).senderId.equals(senderId) &&
                                conversations.get(i).receiverId.equals(receiverId)){
                            conversations.get(i).message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                            conversations.get(i).dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                            break;
                        }
                    }
                }
            }
            loading(false);
            conversations.sort((obj1, obj2) -> obj2.dateObject.compareTo(obj1.dateObject));
            adapter = new ConversationListAdapter(getActivity(), conversations);
            rvRecentConversation.setAdapter(adapter);
            rvRecentConversation.smoothScrollToPosition(0);
            rvRecentConversation.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
    });

    private void loading(Boolean isLoading){
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
        }else {
            progressBar.setVisibility(View.GONE);
        }
    }
}