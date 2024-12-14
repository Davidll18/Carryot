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
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class super_gestion_rest extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RestaurantAdapter adapter;
    private ArrayList<RestaurantDTO> restaurantList;
    private NavigationView navigationView_menu;
    private DrawerLayout drawerLayout;
    private ImageButton buttonMenu;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_gestion_rest);

        // Inicializar vistas
        recyclerView = findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        restaurantList = new ArrayList<>();
        adapter = new RestaurantAdapter(this, restaurantList);

        // Configurar listener en el adaptador
        adapter.setOnCardClickListener(restaurant -> {
            Intent intent = new Intent(super_gestion_rest.this, super_estadisticas_por_rest.class);
            intent.putExtra("restaurantId", restaurant.getUidCreacion()); // Pasa el ID del restaurante
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        // Configurar Firebase Firestore
        db = FirebaseFirestore.getInstance();
        fetchRestaurants();

        // Configurar menú lateral
        buttonMenu = findViewById(R.id.buttonMenu);
        buttonMenu.setOnClickListener(view -> drawerLayout.open());

        getWindow().setStatusBarColor(ContextCompat.getColor(super_gestion_rest.this, R.color.red_boton));

        navigationView_menu = findViewById(R.id.navigationView_menu);
        navigationView_menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navGestionUsuarios) {
                    // Ir a Gestión de Usuarios
                    startActivity(new Intent(super_gestion_rest.this, super_gestion_usuarios.class));
                } else if (id == R.id.navRegistrarAdminRest) {
                    // Ir a Registrar Admin de Restaurante
                    startActivity(new Intent(super_gestion_rest.this, Super_registro_admin_rest.class));
                } else if (id == R.id.navReporteVentas_por_rest) {
                    // Ir a Registrar Admin de Restaurante
                    startActivity(new Intent(super_gestion_rest.this, super_gestion_rest.class));
                }
                else if (id == R.id.navReporteVentas) {
                    // Ir a Reporte de Ventas
                    startActivity(new Intent(super_gestion_rest.this, super_estadisticas_general.class));
                } else if (id == R.id.navLogs) {
                    // Ir a Logs
                    startActivity(new Intent(super_gestion_rest.this, super_logs.class));
                }

                drawerLayout.closeDrawers(); // Cierra el menú después de seleccionar un ítem
                return true;
            }
        });
    }

    private void fetchRestaurants() {
        db.collection("restaurant")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            RestaurantDTO restaurant = document.toObject(RestaurantDTO.class);
                            restaurantList.add(restaurant);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        System.err.println("Error al cargar los datos: " + task.getException());
                    }
                });
    }
}
