package com.example.fixinventori.BottomNavBar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
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
import com.example.fixinventori.Chat.utils.Constants;
import com.example.fixinventori.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Locale;

public class HomeFragment extends Fragment {

    Button btnInvSet, btnUsage, btnForecast, btnMenuSet, btnRestock, btnReport;
    TextView tvUser;
    UserSession session;
    String user;
    RoundedImageView rivHomeProfile;

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
        rivHomeProfile = view.findViewById(R.id.rivHomeProfile);

        if(user!=null){
            tvUser.setText(String.format("%s%s",user.substring(0,1).toUpperCase(Locale.ROOT)
                    ,user.substring(1).toLowerCase(Locale.ROOT)));
        }

        if(session.getString(Constants.KEY_IMAGE)!=null){
            decodeImage(session.getString(Constants.KEY_IMAGE));
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

    private void decodeImage(String encodedImage){
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
        rivHomeProfile.setImageBitmap(bitmap);
    }
}