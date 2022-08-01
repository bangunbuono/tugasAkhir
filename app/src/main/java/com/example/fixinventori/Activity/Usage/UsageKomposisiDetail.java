package com.example.fixinventori.Activity.Usage;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.Adapter.LVAdapter.AdapterOrderDetail;
import com.example.fixinventori.R;
import com.example.fixinventori.UsageAutoApplication;
import com.example.fixinventori.model.UsageMenuModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class UsageKomposisiDetail extends AppCompatActivity {

    ListView lvOrderDetail;
    TextView tvOrder;
    List<UsageMenuModel> orderList;
    AdapterOrderDetail adapterOrderDetail;
    Button btnCancel;
    public static Button btnConfirm;
    String user, date;
    public static String orderSeries, formatedTime, month;
    public static LocalDateTime time;
    UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_komposisi_detail);

        userSession = new UserSession(getApplicationContext());
        user = userSession.getUserDetail().get("username");

        lvOrderDetail = findViewById(R.id.lvOrderDetail);
        tvOrder = findViewById(R.id.tvOrderDetail);
        btnCancel = findViewById(R.id.btnCancelOrder);
        btnConfirm = findViewById(R.id.btnConfirmOrder);

        orderList = new ArrayList<>();
        orderList = UsageAutoApplication.orderList;

        adapterOrderDetail = new AdapterOrderDetail(UsageKomposisiDetail.this, orderList);
        lvOrderDetail.setAdapter(adapterOrderDetail);

        time = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        DateTimeFormatter timeStamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter monthType = DateTimeFormatter.ofPattern("MM/yyyy");
        month = monthType.format(time);
        formatedTime = timeStamp.format(time);
        date = dtf.format(time);
        orderSeries = "B."+date;

        tvOrder.setText(orderSeries);
    }
}