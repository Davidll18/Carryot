package com.example.superadmin;


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

import com.example.superadmin.adapters.ProductAdapter;
import com.example.superadmin.adapters.ProductCarAdapter;
import com.example.superadmin.model.Product;
import com.example.superadmin.model.ProductInCar;

import java.util.Arrays;
import java.util.List;

public class CarActivity extends AppCompatActivity implements ProductCarAdapter.OnItemClickListener {
    ConstraintLayout toolbar;
    ImageButton btnBack;
    ConstraintLayout btnPay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_car);
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
                startActivity(new Intent(CarActivity.this, SummaryActivity.class));
                finish();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.rv_car);
        List<ProductInCar> products = Arrays.asList(
                new ProductInCar("Alitas BBQ", 2, 45.0),
                new ProductInCar("Ceviche Mixto", 3, 650.0),
                new ProductInCar("Chicharrón de Calamar", 1, 350.0),
                new ProductInCar("Arroz con Mariscos", 2, 800.0),
                new ProductInCar("Ají de Gallina", 4, 550.0),
                new ProductInCar("Causa Limeña", 1, 450.0),
                new ProductInCar("Tacos de Pollo Crocante", 1, 650.0),
                new ProductInCar("Leche de Tigre", 3, 350.0),
                new ProductInCar("Mazamorra de Calabaza", 6, 800.0),
                new ProductInCar("Tallarines Rojos", 1, 550.0)
        );
        ProductCarAdapter adapter = new ProductCarAdapter(CarActivity.this, products, this);
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

    @Override
    public void onCopyClick(int position) {

    }
}