package com.example.fixinventori.BottomNavBar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fixinventori.API.APIAccounts;
import com.example.fixinventori.API.ServerConnection;
import com.example.fixinventori.Activity.Menu.MenuSet;
import com.example.fixinventori.Activity.Report.InventReport;
import com.example.fixinventori.Activity.Stock.InventorySet;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.Chat.Model.ManagerModel;
import com.example.fixinventori.Chat.utils.Constants;
import com.example.fixinventori.R;
import com.example.fixinventori.model.ResponseModel;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerHomeFragment extends Fragment {
    UserSession session;
    String manager, selectedUser;
    Spinner spinnerUser;
    TextView tvManagerHome, tvInvSet, tvMenuSet, tvMoreReport;
    List<ManagerModel> userList;
    ArrayList<String> user = new ArrayList<>();
    ArrayAdapter<String> adapter;
    int index;
    RoundedImageView rivHomeProfile;

    public ManagerHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new UserSession(getActivity());
        manager = session.getManagerDetail().get("manager");
        userList = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manager_home, container, false);
        tvManagerHome = view.findViewById(R.id.tvManagerHome);
        tvInvSet = view.findViewById(R.id.tvInvSet);
        tvMenuSet = view.findViewById(R.id.tvMenuSet);
        spinnerUser = view.findViewById(R.id.spinnerUser);
        tvMoreReport = view.findViewById(R.id.tvMoreReport);
        rivHomeProfile = view.findViewById(R.id.rivHomeProfile);

        getUsers();

        tvMoreReport.setOnClickListener(view1-> startActivity(new Intent(getActivity(), InventReport.class)));

        tvManagerHome.setText(String.format("%s%s",
                manager.substring(0,1).toUpperCase(Locale.ROOT),
                manager.substring(1).toLowerCase(Locale.ROOT)));

        if(session.getString(Constants.KEY_IMAGE)!=null)
            decodeImage(session.getString(Constants.KEY_IMAGE));

        tvInvSet.setOnClickListener(view1-> startActivity(new Intent(getActivity(), InventorySet.class)));

        tvMenuSet.setOnClickListener(view1-> startActivity(new Intent(getActivity(), MenuSet.class)));

        return view;
    }

    void getUsers() {
        APIAccounts get = ServerConnection.connection().create(APIAccounts.class);
        Call<ResponseModel> getData = get.getUser(manager);

        getData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if(response.body()!=null) {
                    userList = response.body().getRecordManager();
                    if (userList!=null){
                        userList.forEach(managerModel -> user.add(managerModel.username));
                        adapter = new ArrayAdapter<>(
                                getActivity(),R.layout.tv_selected_user, user);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerUser.setAdapter(adapter);
                        spinnerUser.setSelection(0);
                        spinnerItemSelection();
                        setSpinnerSelection();
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseModel> call,@NonNull Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void spinnerItemSelection(){
        spinnerUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String user = userList.get(position).getUsername();
                session.putString("username", user);
                selectedUser = user;
                System.out.println(user);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSpinnerSelection(){
        if(session.getString("username")!=null){
            String userSelected = session.getString("username");
            for (int i = 0; i < user.size(); i++) {
                if(user.get(i).equals(userSelected)) {
                    index = i;
                    break;
                }
            }
            spinnerUser.setSelection(index);
        }
    }

    private void decodeImage(String encodedImage){
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
        rivHomeProfile.setImageBitmap(bitmap);
    }
}