package com.example.superadmin.adminrest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superadmin.LoginActivity;
import com.example.superadmin.R;
import com.example.superadmin.adminrest.Adapter.GananciaAdapter;
import com.example.superadmin.adminrest.Adapter.PedidosStatusAdapter;
import com.example.superadmin.adminrest.Adapter.PlatosEstadisticaAdapter;
import com.example.superadmin.adminrest.dto.GananciaItem;
import com.example.superadmin.adminrest.dto.PedidosStatusItem;
import com.example.superadmin.adminrest.dto.PlatosEstItem;
import com.example.superadmin.dtos.RestaurantDTO;
import com.example.superadmin.util.ToolbarUtils;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;  // Declaración de FirebaseAuth
    private FirebaseFirestore db;  // Declaración de Firestore

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private RecyclerView recyclerViewPedidos;
    private RecyclerView recyclerViewGanancia;
    private RecyclerView recyclerViewPlatosEst;

    private String restaurantUid; // UID del restaurante recuperado de la sesión

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_adminrest);

        // Inicializar FirebaseAuth y Firestore
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Configuración del DrawerLayout y NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        // Llamamos al método estático de ToolbarUtils para ajustar el padding superior
        ToolbarUtils.adjustToolbarPadding(toolbar);

        Menu menu = navigationView.getMenu();
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            // Convert androidx.core.graphics.Insets to android.graphics.Insets
            android.graphics.Insets systemBars = android.graphics.Insets.of(
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            );
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Recuperar sesión del usuario y obtener restaurante
        retrieveSessionAndFetchRestaurant();

        // Configuración de RecyclerView
        setupRecyclerViews();
    }

    private void setupRecyclerViews() {
        recyclerViewPedidos = findViewById(R.id.recyclerViewPedidosPendientes);
        recyclerViewPedidos.setLayoutManager(new LinearLayoutManager(this));
        List<PedidosStatusItem> pedidoslist = new ArrayList<>();
        pedidoslist.add(new PedidosStatusItem("PEDIDOS PENDIENTES", "08", "PEDIDOS ACTIVOS", "20"));
        PedidosStatusAdapter pedidosStatusAdapter = new PedidosStatusAdapter(this, pedidoslist);
        recyclerViewPedidos.setAdapter(pedidosStatusAdapter);

        recyclerViewGanancia = findViewById(R.id.recyclerGananciaEstadistica);
        recyclerViewGanancia.setLayoutManager(new LinearLayoutManager(this));
        List<GananciaItem> gananciaList = new ArrayList<>();
        gananciaList.add(new GananciaItem("GANANCIA TOTAL", R.drawable.statistics_image));
        GananciaAdapter gananciaAdapter = new GananciaAdapter(this, gananciaList);
        recyclerViewGanancia.setAdapter(gananciaAdapter);

        recyclerViewPlatosEst = findViewById(R.id.recyclerViewPlatosPopulares);
        recyclerViewPlatosEst.setLayoutManager(new LinearLayoutManager(this));
        List<PlatosEstItem> platosEstList = new ArrayList<>();
        platosEstList.add(new PlatosEstItem("PLATOS POPULARES", R.drawable.ceviche, "Ceviche", R.drawable.lomo, "Lomo Saltado", R.drawable.pollito, "Pollo a la brasa"));
        PlatosEstadisticaAdapter platosEstadisticaAdapter = new PlatosEstadisticaAdapter(this, platosEstList);
        recyclerViewPlatosEst.setAdapter(platosEstadisticaAdapter);
    }

    private void retrieveSessionAndFetchRestaurant() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String userUid = user.getUid();

            // Consultar Firestore para buscar el restaurante del usuario
            db.collection("restaurant")
                    .whereEqualTo("uidCreador", userUid)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                RestaurantDTO restaurant = document.toObject(RestaurantDTO.class);
                                if (restaurant != null) {
                                    // Guardar el UID del restaurante en la sesión
                                    SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("restaurant_uid", restaurant.getUidCreacion());
                                    editor.putString("restaurant_name", restaurant.getNombreRestaurante());
                                    editor.apply();

                                    // Asignar el UID del restaurante a una variable local
                                    restaurantUid = restaurant.getUidCreacion();

                                    Log.d("MainActivity", "Restaurante recuperado: " + restaurant.getNombreRestaurante());
                                }
                            }
                        } else {
                            Log.d("FirestoreError", "No se encontró restaurante para este usuario.");
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("FirestoreError", "Error al consultar el restaurante: ", e);
                    });
        } else {
            // Si no hay usuario autenticado, redirigir al login
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int menuId = menuItem.getItemId();

        if (menuId == R.id.nav_home) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (menuId == R.id.nav_pedidos) {
            startActivity(new Intent(this, PedidosActivity.class));
        } else if (menuId == R.id.nav_profile) {
            if (restaurantUid != null) {
                // Redirigir a ProfileRestActivity con el UID del restaurante
                Intent intent = new Intent(this, ProfileRestActivity.class);
                intent.putExtra("uidRestaurante", restaurantUid);
                startActivity(intent);
            } else {
                Toast.makeText(this, "No se encontró información del restaurante.", Toast.LENGTH_SHORT).show();
            }
        } else if (menuId == R.id.nav_dishes) {
            startActivity(new Intent(this, DishesActivity.class));
        } else if (menuId == R.id.nav_ganancia) {
            startActivity(new Intent(this, GananciaActivity.class));
        } else if (menuId == R.id.nav_popular) {
            startActivity(new Intent(this, StatisticsActivity.class));
        } else if (menuId == R.id.nav_users) {
            startActivity(new Intent(this, StatisticsActivity.class));
        } else if (menuId == R.id.nav_logout) {
            // Cerrar sesión
            firebaseAuth.signOut();  // Cerrar la sesión en Firebase

            // Limpiar las preferencias del usuario
            SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();  // Limpiar las preferencias de la sesión
            editor.apply();

            // Redirigir al login
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            // Finalizar la actividad actual
            finish();
        }

        // Cerrar el Drawer después de seleccionar un ítem
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
