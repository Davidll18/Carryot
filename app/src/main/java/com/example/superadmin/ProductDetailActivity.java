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

import com.example.superadmin.adapters.AdapterDots;
import com.example.superadmin.adapters.AdapterImageSlider;

import java.util.Arrays;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {
    ConstraintLayout toolbar;
    ImageButton btnBack;
    ImageButton btnCar;
    ImageButton btnProfile;
    ConstraintLayout btnPay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.toolbar_detail_product);
        btnBack = toolbar.findViewById(R.id.btn_back);
        btnCar = toolbar.findViewById(R.id.btn_shopping_bag);
        btnProfile = toolbar.findViewById(R.id.btn_profile);
        btnPay= findViewById(R.id.container_payment);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductDetailActivity.this, ProfileActivity.class));
            }
        });

        btnCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductDetailActivity.this, CarActivity.class));
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductDetailActivity.this, CarActivity.class));
            }
        });




        RecyclerView recyclerViewImages = findViewById(R.id.rv_slide_detail_product);
        RecyclerView recyclerViewDots = findViewById(R.id.rv_dots);

        List<String> images = Arrays.asList("","","","");
        List<String> dots = Arrays.asList("","","","");

        AdapterImageSlider adapterImage = new AdapterImageSlider(ProductDetailActivity.this,images);
        AdapterDots adapterDots = new AdapterDots(ProductDetailActivity.this,dots);

        recyclerViewImages.setAdapter(adapterImage);
        recyclerViewDots.setAdapter(adapterDots);

    }
}