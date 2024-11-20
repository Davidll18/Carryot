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
import com.example.superadmin.adapters.UserProductHistorialAdapter;
import com.example.superadmin.model.UserProductInCar;

import java.util.Arrays;
import java.util.List;

public class UserHistorialActivity extends AppCompatActivity implements UserProductHistorialAdapter.OnItemClickListener{
    ConstraintLayout toolbar;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_activity_historial);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.toolbar_back);
        btnBack = toolbar.findViewById(R.id.btn_back);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserHistorialActivity.this, UserHomeActivity.class));

                finish();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.rv_historial);
        List<UserProductInCar> products = Arrays.asList(
                new UserProductInCar("Restaurante 1",0, 45.50,R.drawable.comida),
                new UserProductInCar("Restaurante 2", 3, 50.11,R.drawable.logo),
                new UserProductInCar("Restaurante 3", 1, 30.25,R.drawable.logo),
                new UserProductInCar("Restaurante 4", 2, 80.14,R.drawable.logo),
                new UserProductInCar("asd", 4, 55.0,R.drawable.logo),
                new UserProductInCar("ghjhgj", 1, 150.44,R.drawable.logo),
                new UserProductInCar("werwerggg", 1, 200.18,R.drawable.logo),
                new UserProductInCar("Leche de Tigre", 3, 350.54,R.drawable.logo),
                new UserProductInCar("Mazamorra de Calabaza", 6, 100.85,R.drawable.logo),
                new UserProductInCar("Tallarines Rojos", 1, 57.48,R.drawable.logo)
        );
        UserProductHistorialAdapter adapter = new UserProductHistorialAdapter(UserHistorialActivity.this, products,  this);
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