package com.example.superadmin.adminrest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
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

    TextView saludo, rest,pedAct, pedPend, cantVent1, cantVent2;
    ImageView popularDish1,popularDish2;
    LinearLayout pp, pa, pl1,pl2;

    private String restaurantUid; // UID del restaurante recuperado de la sesión

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminrest_estadisticas_general);

        // Inicializar FirebaseAuth y Firestore
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Configuración del DrawerLayout y NavigationView
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        saludo = findViewById(R.id.saludoTextView);
        rest = findViewById(R.id.restauranteTextView);

        pp = findViewById(R.id.pendientesPedidos);
        pa = findViewById(R.id.activosPedidos);

        pedPend = findViewById(R.id.pendientesText);
        pedAct = findViewById(R.id.activosText);

        cantVent1 = findViewById(R.id.cantVent1);
        cantVent2 = findViewById(R.id.cantVent2);
        popularDish1 = findViewById(R.id.imageView5);
        popularDish2 = findViewById(R.id.imageView10);

        pl1 = findViewById(R.id.platoVendido1);
        pl2 = findViewById(R.id.platoVendido2);

        setSupportActionBar(toolbar);

        pp.setOnClickListener(v ->{
            Intent intent = new Intent(MainActivity.this, PedidosActivity.class);
            startActivity(intent);
        });
        pa.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PedidosActivity.class);
            startActivity(intent);
        });

        pl1.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
            startActivity(intent);
        });
        pl2.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
            startActivity(intent);
        });



        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_logout).setVisible(false);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_home);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Recuperar sesión del usuario y obtener restaurante
        retrieveSessionAndFetchRestaurant(saludo, rest);

    }

    private void retrieveSessionAndFetchRestaurant(TextView saludo, TextView rest) {
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
                                    saludo.setText(restaurant.getNombreCreador());
                                    rest.setText(restaurant.getNombreRestaurante());
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
            startActivity(new Intent(this, ProfileRestActivity.class));
        } else if (menuId == R.id.nav_dishes) {
            startActivity(new Intent(this, DishesActivity.class));
        } else if (menuId == R.id.nav_ganancia) {
            startActivity(new Intent(this, GananciaActivity.class));
        } else if (menuId == R.id.nav_popular) {
            startActivity(new Intent(this, StatisticsActivity.class));
        } else if (menuId == R.id.nav_users) {
            startActivity(new Intent(this, StatisticsActivity.class));
        } else if (menuId == R.id.nav_logout) {
            mostrarDialogoCerrarSesion();
        }

        // Cerrar el Drawer después de seleccionar un ítem
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private void mostrarDialogoCerrarSesion() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Estás seguro de que quieres cerrar sesión?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    // Cerrar sesión con FirebaseAuth
                    firebaseAuth.signOut();

                    // Eliminar SharedPreferences
                    SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();

                    // Redirigir al LoginActivity
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", null) // Cierra el diálogo sin hacer nada
                .show();
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
