package com.example.fixinventori.BottomNavBar;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.fixinventori.Activity.Forecast.InventForecast;
import com.example.fixinventori.Activity.Menu.MenuSet;
import com.example.fixinventori.Activity.Report.InventReport;
import com.example.fixinventori.Activity.Restock.InventRestock;
import com.example.fixinventori.Activity.Stock.InventorySet;
import com.example.fixinventori.Activity.Usage.InventUsage;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.R;

public class HomeFragment extends Fragment {

    Button btnInvSet, btnUsage, btnForecast, btnMenuSet, btnRestock, btnReport;
    TextView tvUser;
    UserSession session;
    String user;

    public HomeFragment() {
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
        View view =inflater.inflate(R.layout.fragment_home, container, false);
        btnInvSet = view.findViewById(R.id.btnInvSet);
        btnUsage = view.findViewById(R.id.btnUsage);
        btnForecast = view.findViewById(R.id.btnForecast);
        btnMenuSet = view.findViewById(R.id.btnMenuSet);
        btnRestock = view.findViewById(R.id.btnRestock);
        btnReport = view.findViewById(R.id.btnReport);
        tvUser = view.findViewById(R.id.tvUser);

        if(user!=null){
            tvUser.setText(user.replace(user.charAt(0), user.toUpperCase().charAt(0))+"'s Home");
        }

        btnInvSet.setOnClickListener(view1 ->{
            Intent i = new Intent(getActivity(), InventorySet.class);
            startActivity(i);
        });

        btnMenuSet.setOnClickListener(view1 ->{
            Intent i = new Intent(getActivity(), MenuSet.class);
            startActivity(i);
        });

        btnUsage.setOnClickListener(view1 ->{
            Intent i = new Intent(getActivity(), InventUsage.class);
            startActivity(i);
        });

        btnRestock.setOnClickListener(view1 ->{
            Intent i = new Intent(getActivity(), InventRestock.class);
            startActivity(i);
        });

        btnForecast.setOnClickListener(view1 ->{
            Intent i = new Intent(getActivity(), InventForecast.class);
            startActivity(i);
        });

        btnReport.setOnClickListener(view1 ->{
            Intent i = new Intent(getActivity(), InventReport.class);
            startActivity(i);
        });
        return view;
    }
}