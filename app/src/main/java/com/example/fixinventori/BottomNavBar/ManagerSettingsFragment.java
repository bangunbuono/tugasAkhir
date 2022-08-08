package com.example.fixinventori.BottomNavBar;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fixinventori.Activity.Setting.AddUserConnect;
import com.example.fixinventori.Activity.Setting.UserManagerActivity;
import com.example.fixinventori.Activity.User.ManagerLoginActivity;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.Chat.utils.Constants;
import com.example.fixinventori.R;
import com.example.fixinventori.UsageAutoApplication;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class ManagerSettingsFragment extends Fragment {
    UserSession session;
    String manager;

    public ManagerSettingsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_manager_settings, container, false);
        TextView tvLogout = view.findViewById(R.id.tvLogOut);
        TextView tvInfo = view.findViewById(R.id.tvInfo);
        TextView tvConnect = view.findViewById(R.id.tvUserConnect);
        TextView tvBantuan = view.findViewById(R.id.tvBantuan);
        TextView tvManagerName = view.findViewById(R.id.tvUserName);
        TextView tvUserManager = view.findViewById(R.id.tvUserManager);

        tvManagerName.setText(manager);

        if(UsageAutoApplication.listConnectedUser.size()>0){
            tvConnect.setVisibility(View.GONE);
        }else tvUserManager.setVisibility(View.GONE);

        tvConnect.setOnClickListener(view1 ->
                startActivity(new Intent(getActivity(), AddUserConnect.class)));

        tvUserManager.setOnClickListener(view1 ->
                startActivity(new Intent(getActivity(), UserManagerActivity.class)));

        tvBantuan.setOnClickListener(view1 -> System.out.println("bantuan"));

        tvInfo.setOnClickListener(view1 -> System.out.println("info"));

        tvLogout.setOnClickListener(view1 -> {
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            DocumentReference documentReference = firebaseFirestore.
                    collection(Constants.KEY_COLLECTION_MANAGERS).
                    document(session.getString(Constants.KEY_MANAGER_ID));
            HashMap<String, Object> update = new HashMap<>();
            update.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
            documentReference.update(update).addOnSuccessListener(unused -> {
                session.logOutSession();
                Intent intent = new Intent(getActivity(), ManagerLoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                requireActivity().finish();
            }).addOnFailureListener(e ->
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show());

        });
        return view;
    }
}