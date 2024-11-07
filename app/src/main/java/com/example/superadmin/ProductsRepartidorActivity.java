package com.example.superadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.superadmin.adapters.ProductRepartidorAdapter;
import com.example.superadmin.model.Product;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Arrays;
import java.util.List;

public class ProductsRepartidorActivity extends AppCompatActivity implements ProductRepartidorAdapter.OnItemClickListener {
    ConstraintLayout toolbar;
    ImageButton btnBack;

    ImageButton btnProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_products_repartidor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.toolbar_products_repartidor);
        btnBack = toolbar.findViewById(R.id.btn_back);

        btnProfile = toolbar.findViewById(R.id.btn_profile);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductsRepartidorActivity.this, ProfileActivity.class));
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        RecyclerView recyclerView = findViewById(R.id.rv_prod_repartidor);
        List<Product> products = Arrays.asList(
                new Product("N° 1214", "Rustica", "$450", R.drawable.shopping_bag),
                new Product("N°0221", "Camaron Dormido", "$650", R.drawable.shopping_bag),
                new Product("N° 5415 ", "Rustica", "$450", R.drawable.shopping_bag),
                new Product("N° 1112", "Rustica", "$50", R.drawable.shopping_bag),
                new Product("N° 12185", "Rustica", "$150", R.drawable.shopping_bag),
                new Product("N° 8112", "Rustica", "$850", R.drawable.shopping_bag)
        );
        ProductRepartidorAdapter adapter = new ProductRepartidorAdapter(products, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(Product product) {
        startActivity(new Intent(ProductsRepartidorActivity.this,ProductDetailActivity.class));
    }

    @Override
    public void onButton3Click(Product product) {
        // Mostrar un diálogo de confirmación al presionar el botón 3
        new MaterialAlertDialogBuilder(this)
                .setTitle("Confirmación")
                .setMessage("¿Deseas tomar el pedido N°: " + product.getName() + "?")
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    // Acción al presionar aceptar
                  //  Toast.makeText(this, "Acción confirmada para " + product.getName(), Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }
}