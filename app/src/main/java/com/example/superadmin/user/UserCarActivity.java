package com.example.superadmin.user;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superadmin.R;
import com.example.superadmin.adapters.UserProductCarAdapter;
import com.example.superadmin.model.UserProductInCar;

import java.util.Arrays;
import java.util.List;

public class UserCarActivity extends AppCompatActivity implements UserProductCarAdapter.OnItemClickListener {
    ConstraintLayout toolbar;
    ImageButton btnBack;
    ConstraintLayout btnPay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_activity_car);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.toolbar_car);
        btnBack = toolbar.findViewById(R.id.btn_back);
        btnPay = findViewById(R.id.container_payment);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserCarActivity.this, UserSummaryActivity.class));
                finish();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserCarActivity.this, UserHomeActivity.class));

                finish();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.rv_car);
        List<UserProductInCar> products = Arrays.asList(
                new UserProductInCar("Alitas BBQ",2, 45.0,R.drawable.comida),
                new UserProductInCar("Ceviche Mixto", 3, 650.0,R.drawable.logo),
                new UserProductInCar("Chicharrón de Calamar", 1, 350.0,R.drawable.logo),
                new UserProductInCar("Arroz con Mariscos", 2, 800.0,R.drawable.logo),
                new UserProductInCar("Ají de Gallina", 4, 550.0,R.drawable.logo),
                new UserProductInCar("Causa Limeña", 1, 450.0,R.drawable.logo),
                new UserProductInCar("Tacos de Pollo Crocante", 1, 650.0,R.drawable.logo),
                new UserProductInCar("Leche de Tigre", 3, 350.0,R.drawable.logo),
                new UserProductInCar("Mazamorra de Calabaza", 6, 800.0,R.drawable.logo),
                new UserProductInCar("Tallarines Rojos", 1, 550.0,R.drawable.logo)
        );
        UserProductCarAdapter adapter = new UserProductCarAdapter(UserCarActivity.this, products, this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onAddClick(int position) {

    }

    @Override
    public void onMinusClick(int position) {

    }

    @Override
    public void onRemoveClick(int position) {

    }

}