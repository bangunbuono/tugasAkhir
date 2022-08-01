package com.example.fixinventori.BottomNavBar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.fixinventori.R;

import com.example.fixinventori.Activity.User.UserSession;

public class ManagerChatFragment extends Fragment {

    UserSession session;
    String manager;

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

        TextView tvChatManager = view.findViewById(R.id.tvChatManager);
        tvChatManager.setText(String.format("Chat %s", manager));
        return view;
    }
}