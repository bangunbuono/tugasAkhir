package com.example.fixinventori.BottomNavBar;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fixinventori.Activity.User.LoginActivity;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.R;


public class SettingFragment extends Fragment {
    UserSession session;
    String user;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new UserSession(getActivity());
        user = session.getUserDetail().get("username");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        TextView tvLogout = view.findViewById(R.id.tvLogOut);
        tvLogout.setOnClickListener(view1 -> {
            session.logOutSession();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            requireActivity().finish();
        });
        return view;
    }
}