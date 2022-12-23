package com.example.fixinventori.BottomNavBar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixinventori.Activity.Forecast.InventForecast;
import com.example.fixinventori.Activity.Menu.MenuSet;
import com.example.fixinventori.Activity.Report.InventReport;
import com.example.fixinventori.Activity.Restock.InventRestock;
import com.example.fixinventori.Activity.Stock.InventorySet;
import com.example.fixinventori.Activity.Usage.InventUsage;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.Adapter.RVAdapter.TipsAdapter;
import com.example.fixinventori.Chat.utils.Constants;
import com.example.fixinventori.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    Button btnInvSet, btnUsage, btnForecast, btnMenuSet, btnRestock, btnReport;
    TextView tvUser, tvTips;
    UserSession session;
    String user;
    RoundedImageView rivHomeProfile;
    RecyclerView rvTips;
    LinearLayoutManager layoutManager;
    List<String> tips = new ArrayList<>();
    TipsAdapter adapter;
    int item = 0;
    SliderView slider;

//    final int duration = 10;
//    final int pixelsToMove = 50;
//    private final Handler mHandler = new Handler(Looper.getMainLooper());
//
//    private final Runnable SCROLLING_RUNNABLE = new Runnable() {
//        @Override
//        public void run() {
//            rvTips.smoothScrollBy(pixelsToMove, 0);
//            mHandler.postDelayed(this, duration);
//        }
//    };

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
        tvTips = view.findViewById(R.id.tvTips);
        slider = view.findViewById(R.id.slider);
//        rvTips = view.findViewById(R.id.rvTips);

        tips = new ArrayList<>();
        tips = Arrays.asList(getResources().getStringArray(R.array.tips));

        adapter = new TipsAdapter(getActivity(), tips);
        slider.setSliderAdapter(adapter);
        slider.setIndicatorAnimation(IndicatorAnimationType.WORM);
        slider.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        slider.startAutoCycle();

//        layoutManager = new GridLayoutManager(getActivity(),1,GridLayoutManager.HORIZONTAL,false);
//        rvTips.setLayoutManager(layoutManager);

//        rvTips.setHasFixedSize(true);
//        adapter = new TipsAdapter(getActivity(), tips);
//        rvTips.setAdapter(adapter);

//        rvTips.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                int lastItem = layoutManager.findLastCompletelyVisibleItemPosition();
//                if(lastItem == layoutManager.getItemCount()-1){
//                    rvTips.setAdapter(null);
//                    rvTips.setAdapter(adapter);
//                    mHandler.removeCallbacks(SCROLLING_RUNNABLE);
//                    Handler postHandler = new Handler();
//                    postHandler.postDelayed(() -> {
//                        rvTips.setAdapter(null);
//                        rvTips.setAdapter(adapter);
//                        mHandler.postDelayed(SCROLLING_RUNNABLE, 2000);
//                    }, 2000);
//                }
//            }
//        });
//        mHandler.post(SCROLLING_RUNNABLE);
//        mHandler.postDelayed(SCROLLING_RUNNABLE,2000);

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