package com.example.superadmin.Superadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;

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
    private ArrayList<RestaurantDTO> filteredList;
    private NavigationView navigationView_menu;
    private DrawerLayout drawerLayout;
    private ImageButton buttonMenu;
    private FirebaseFirestore db;
    private Spinner tipoRestSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_gestion_rest);

        // Inicializar vistas
        // Inicializar vistas
        recyclerView = findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        restaurantList = new ArrayList<>();
        filteredList = new ArrayList<>();
        adapter = new RestaurantAdapter(this, filteredList);

        // Configurar listener en el adaptador
        adapter.setOnCardClickListener(restaurant -> {
            Intent intent = new Intent(super_gestion_rest.this, super_estadisticas_por_rest.class);
            intent.putExtra("restaurantId", restaurant.getUidCreacion()); // Pasa el ID del restaurante
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        // Configurar Firebase Firestore
        db = FirebaseFirestore.getInstance();
        fetchRestaurants(null); // Cargar todos los restaurantes al inicio

        // Configurar menú lateral
        buttonMenu = findViewById(R.id.buttonMenu);
        buttonMenu.setOnClickListener(view -> drawerLayout.open());

        getWindow().setStatusBarColor(ContextCompat.getColor(super_gestion_rest.this, R.color.red_boton));

        navigationView_menu = findViewById(R.id.navigationView_menu);
        navigationView_menu.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navGestionUsuarios) {
                startActivity(new Intent(super_gestion_rest.this, super_gestion_usuarios.class));
            } else if (id == R.id.navRegistrarAdminRest) {
                startActivity(new Intent(super_gestion_rest.this, Super_registro_admin_rest.class));
            } else if (id == R.id.navReporteVentas_por_rest) {
                startActivity(new Intent(super_gestion_rest.this, super_gestion_rest.class));
            } else if (id == R.id.navReporteVentas) {
                startActivity(new Intent(super_gestion_rest.this, super_estadisticas_general.class));
            } else if (id == R.id.navLogs) {
                startActivity(new Intent(super_gestion_rest.this, super_logs.class));
            }

            drawerLayout.closeDrawers(); // Cierra el menú después de seleccionar un ítem
            return true;
        });

        // Configurar Spinner para categorías
        tipoRestSpinner = findViewById(R.id.tipoRest);
        tipoRestSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoriaSeleccionada = parent.getItemAtPosition(position).toString();
                if (position == 0) {
                    fetchRestaurants(null); // Cargar todos si selecciona "Todas las categorías"
                } else {
                    fetchRestaurants(categoriaSeleccionada); // Filtrar por categoría seleccionada
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });
    }
    private void fetchRestaurants(String categoria) {
        db.collection("restaurant")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        restaurantList.clear();
                        filteredList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            RestaurantDTO restaurant = document.toObject(RestaurantDTO.class);
                            restaurantList.add(restaurant);

                            if (categoria == null || categoria.equals(restaurant.getCategoria())) {
                                filteredList.add(restaurant);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        System.err.println("Error al cargar los datos: " + task.getException());
                    }
                });
    }
}
