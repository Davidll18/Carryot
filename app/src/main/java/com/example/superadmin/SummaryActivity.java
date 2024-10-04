package com.example.superadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.example.superadmin.adapters.AdapterSummary;
import com.example.superadmin.adapters.ProductAdapter;
import com.example.superadmin.adapters.ProductCarAdapter;
import com.example.superadmin.model.Product;
import com.example.superadmin.model.ProductInCar;

import java.util.Arrays;
import java.util.List;

public class SummaryActivity extends AppCompatActivity {

    MaterialButton btnFinish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_summary);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnFinish = findViewById(R.id.btn_return_to_home);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SummaryActivity.this, HomeActivity.class));

                finish();
            }
        });
        RecyclerView recyclerView = findViewById(R.id.rv_summary_order_client);
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
        AdapterSummary adapter = new AdapterSummary(SummaryActivity.this,products);
        recyclerView.setAdapter(adapter);
    }

}