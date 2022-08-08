package com.example.fixinventori.Activity.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fixinventori.API.APIRequestMenu;
import com.example.fixinventori.API.ServerConnection;
import com.example.fixinventori.Activity.User.UserSession;
import com.example.fixinventori.R;
import com.example.fixinventori.model.ResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMenu extends AppCompatActivity {
    EditText etMenuName, etMenuPrice, etMenuDesc;
    Button btnSaveMenu;
    String menu,price,desc,user;
    UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        userSession = new UserSession(getApplicationContext());
        user = userSession.getUserDetail().get("username");

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.setTitle("Tambah menu");

        etMenuDesc = findViewById(R.id.etMenuDesc);
        etMenuName = findViewById(R.id.etMenuName);
        etMenuPrice = findViewById(R.id.etMenuPrice);
        btnSaveMenu = findViewById(R.id.btnSaveMenu);

        btnSaveMenu.setOnClickListener(view -> {
            menu = etMenuName.getText().toString().trim();
            price = etMenuPrice.getText().toString().trim();
            desc = etMenuDesc.getText().toString().trim();

            if (menu.isEmpty()){
                etMenuName.setError("harus diisi");
            }
            else if (price.isEmpty()){
                etMenuPrice.setError("harus diisi");
            }
            else {
               addMenu();
            }
        });
    }

    private void addMenu(){
        APIRequestMenu dataMenu = ServerConnection.connection().create(APIRequestMenu.class);
        Call<ResponseModel> addData = dataMenu.createMenu(menu, Integer.parseInt(price), desc, user);

        addData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call,@NonNull Response<ResponseModel> response) {
                if(response.body() != null){
                    int code = response.body().getCode();
                    String pesan = response.body().getPesan();

                    if(code == 2){
                        Toast.makeText(AddMenu.this, "Gagal: " + pesan,
                                Toast.LENGTH_SHORT).show();
                    }else if(code == 0){
                        Toast.makeText(AddMenu.this, "Gagal "+ pesan,
                                Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(AddMenu.this, "berhasil menyimpan menu",
                                Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddMenu.this);
                        builder.setTitle(menu);
                        builder.setMessage("Atur komposisi?");
                        builder.setNegativeButton("Nanti saja", (dialogInterface, i) -> finish());
                        builder.setPositiveButton("Atur Komposisi", (dialogInterface, i) -> {
                            Intent intent = new Intent(AddMenu.this, KomposisiSet.class);
                            intent.putExtra("menu", menu);
                            startActivity(intent);
                            finish();
                        });
                        builder.show();
                    }
                }



            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call,@NonNull Throwable t) {
                Toast.makeText(AddMenu.this, "Gagal: "+t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}