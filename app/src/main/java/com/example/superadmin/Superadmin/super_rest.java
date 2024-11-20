package com.example.superadmin.Superadmin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superadmin.R;
import com.example.superadmin.adapters.RestaurantAdapter;
import com.example.superadmin.model.Restaurante;

import java.util.ArrayList;
import java.util.List;

public class super_rest extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_rest);

        recyclerView = findViewById(R.id.recyclerViewRestaurants);
        recyclerView.setHasFixedSize(true);

        // Usar LinearLayout para el RecyclerView
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Lista de restaurantes de ejemplo
        List<Restaurante> restaurants = new ArrayList<>();
        restaurants.add(new Restaurante("KFC", R.drawable.kfc_logo));
        restaurants.add(new Restaurante("Bembos", R.drawable.bembos_logo));
        restaurants.add(new Restaurante("Pizza Hut", R.drawable.pizzahut_logo));
        restaurants.add(new Restaurante("Roky's", R.drawable.rokys_logo));
        restaurants.add(new Restaurante("Toku", R.drawable.toku__logo));
        restaurants.add(new Restaurante("Burger King", R.drawable.burgerking_logo));
        restaurants.add(new Restaurante("Popeyes", R.drawable.popeyes_logo));

        // Crear y configurar el adapter
        adapter = new RestaurantAdapter(restaurants);
        recyclerView.setAdapter(adapter);


    }

}