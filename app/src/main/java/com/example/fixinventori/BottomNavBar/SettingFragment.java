package com.example.fixinventori.BottomNavBar;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fixinventori.API.APIAccounts;
import com.example.fixinventori.API.ServerConnection;
import com.example.fixinventori.Activity.User.LoginActivity;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.Chat.utils.Constants;
import com.example.fixinventori.R;
import com.example.fixinventori.model.ResponseModel;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SettingFragment extends Fragment {
    UserSession session;
    String user;
    int token;

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
        TextView tvInfo = view.findViewById(R.id.tvInfo);
        TextView tvGenerate = view.findViewById(R.id.tvGenerate);
        TextView tvBantuan = view.findViewById(R.id.tvBantuan);
        TextView tvUserName = view.findViewById(R.id.tvUserName);

        tvUserName.setText(user);

        tvGenerate.setOnClickListener(view1 -> {
            do {
                token = ThreadLocalRandom.current().nextInt();
            } while (token<0);
            generate();
            System.out.println(token);
        });

        tvBantuan.setOnClickListener(view1 -> System.out.println("bantuan"));
        tvInfo.setOnClickListener(view1 -> System.out.println("info"));


        tvLogout.setOnClickListener(view1 -> {
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            DocumentReference documentReference = firebaseFirestore.
                    collection(Constants.KEY_COLLECTION_USERS).
                    document(session.getString(Constants.KEY_USER_ID));
            HashMap<String, Object> update = new HashMap<>();
            update.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
            documentReference.update(update).addOnSuccessListener(unused -> {
                session.logOutSession();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                requireActivity().finish();
            }).addOnFailureListener(e ->
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show());

        });
        return view;
    }

    private void generate() {
        APIAccounts getToken = ServerConnection.connection().create(APIAccounts.class);
        Call<ResponseModel> tokenGenemrate = getToken.generateToken(user, this.token);

        tokenGenemrate.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("Token");
                dialog.setMessage("Gunakan ini untuk menghubungkan akun ini dengan akun manager"
                        + "\n"
                        + "token: " + token);
                dialog.setPositiveButton("selesai",
                        (dialog1, which) -> dialog1.dismiss());
                dialog.show();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call,@NonNull Throwable t) {
                Toast.makeText(getActivity(), "gagal: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}