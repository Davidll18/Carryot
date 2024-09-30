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
import com.example.superadmin.model.Product;

import java.util.Arrays;
import java.util.List;

public class ProductsActivity extends AppCompatActivity implements ProductAdapter.OnItemClickListener{
    ConstraintLayout toolbar;
    ImageButton btnBack;
    ImageButton btnCar;
    ImageButton btnProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_products);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.toolbar_products);
        btnBack = toolbar.findViewById(R.id.btn_back);
        btnCar = toolbar.findViewById(R.id.btn_shopping_bag);
        btnProfile = toolbar.findViewById(R.id.btn_profile);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductsActivity.this, ProfileActivity.class));
            }
        });

        btnCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductsActivity.this, CarActivity.class));
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        RecyclerView recyclerView = findViewById(R.id.rv_prod);
        List<Product> products = Arrays.asList(
                new Product("Alitas BBQ", "Rustica", "$450", R.drawable.shopping_bag),
                new Product("Ceviche Mixto", "Camaron Dormido", "$650", R.drawable.shopping_bag),
                new Product("Chicharrón de Calamar", "A Que Vienes?", "$350", R.drawable.shopping_bag),
                new Product("Arroz con Mariscos", "MiscaPez", "$800", R.drawable.shopping_bag),
                new Product("Ají de Gallina", "Doña Rutilda", "$550", R.drawable.shopping_bag),
                new Product("Causa Limeña", "Antojitos", "$450", R.drawable.shopping_bag),
                new Product("Tacos de Pollo Crocante", "MexicanEAT", "$650", R.drawable.shopping_bag),
                new Product("Leche de Tigre", "Tia Veneno", "$350", R.drawable.shopping_bag),
                new Product("Mazamorra de Calabaza", "Suspiro", "$800", R.drawable.shopping_bag),
                new Product("Tallarines Rojos", "Comedor Arte", "$550", R.drawable.shopping_bag)
        );
        ProductAdapter adapter = new ProductAdapter(products, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(Product product) {
        startActivity(new Intent(ProductsActivity.this,ProductDetailActivity.class));
    }
}