package com.example.superadmin.Superadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superadmin.R;
import com.example.superadmin.adapters.RestaurantAdapter;
import com.example.superadmin.model.Restaurante;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class super_rest extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private NavigationView navigationView_menu;
    private DrawerLayout drawerLayout;
    private ImageButton buttonMenu;

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

        buttonMenu = findViewById(R.id.buttonMenu);
        drawerLayout = findViewById(R.id.drawerLayout_super_getion_usuarios);
        buttonMenu.setOnClickListener(view -> drawerLayout.open());

        getWindow().setStatusBarColor(ContextCompat.getColor(super_rest.this,R.color.red_boton));
        navigationView_menu = findViewById(R.id.navigationView_menu);
        navigationView_menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navGestionUsuarios) {
                    // Ir a Gestión de Usuarios
                    startActivity(new Intent(super_rest.this, super_gestion_usuarios.class));
                } else if (id == R.id.navRegistrarAdminRest) {
                    // Ir a Registrar Admin de Restaurante
                    startActivity(new Intent(super_rest.this, Super_registro_admin_rest.class));
                } else if (id == R.id.navReporteVentas_por_rest) {
                    // Ir a Registrar Admin de Restaurante
                    startActivity(new Intent(super_rest.this, super_rest.class));
                }
                else if (id == R.id.navReporteVentas) {
                    // Ir a Reporte de Ventas
                    startActivity(new Intent(super_rest.this, super_estadisticas_general.class));
                } else if (id == R.id.navLogs) {
                    // Ir a Logs
                    startActivity(new Intent(super_rest.this, super_logs.class));
                }

                drawerLayout.closeDrawers(); // Cierra el menú después de seleccionar un ítem
                return true;
            }
        });


    }

}