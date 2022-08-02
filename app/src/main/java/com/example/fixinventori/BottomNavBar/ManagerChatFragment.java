package com.example.fixinventori.BottomNavBar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixinventori.Chat.Adapter.ConversationList;
import com.example.fixinventori.Chat.ManagerActivity;
import com.example.fixinventori.Chat.Model.ChatMessageModel;
import com.example.fixinventori.R;

import com.example.fixinventori.Activity.User.UserSession;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ManagerChatFragment extends Fragment {

    UserSession session;
    String manager;
    RecyclerView rvRecentConversation;
    FloatingActionButton fabNewChat;
    List<ChatMessageModel> conversations;
    ConversationList adapter;
    FirebaseFirestore database;

    public ManagerChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new UserSession(getActivity());
        manager = session.getManagerDetail().get("manager");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manager_chat, container, false);

        fabNewChat = view.findViewById(R.id.fabNewChat);
        rvRecentConversation = view.findViewById(R.id.rvRecentConversation);

        fabNewChat.setOnClickListener(view1 ->
                startActivity(new Intent(getActivity(), ManagerActivity.class)));

        return view;
    }
}