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
import com.example.superadmin.dtos.RestaurantDTO;
import com.example.superadmin.model.Restaurante;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class super_rest extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<RestaurantDTO> restaurantList;
    private RecyclerView.LayoutManager layoutManager;
    private NavigationView navigationView_menu;
    private DrawerLayout drawerLayout;
    private ImageButton buttonMenu;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_rest);

        recyclerView = findViewById(R.id.myRecyclerView); // Usa la variable miembro
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        restaurantList = new ArrayList<>();
        adapter = new RestaurantAdapter(restaurantList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        fetchRestaurants();



        buttonMenu = findViewById(R.id.buttonMenu);
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
    private void fetchRestaurants() {
        db.collection("restaurant") // Cambiar "restaurants" por "restaurant"
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Convertir los datos a RestaurantDTO
                            RestaurantDTO restaurant = document.toObject(RestaurantDTO.class);
                            restaurantList.add(restaurant);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        System.out.println("Error al cargar los datos: " + task.getException());
                    }
                });

    }

}