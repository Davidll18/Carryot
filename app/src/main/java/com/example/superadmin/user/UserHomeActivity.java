package com.example.superadmin.user;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superadmin.LoginActivity;
import com.example.superadmin.ProfileActivity;
import com.example.superadmin.R;
import com.example.superadmin.Superadmin.Super_registro_admin_rest;
import com.example.superadmin.Superadmin.super_estadisticas_general;
import com.example.superadmin.Superadmin.super_estadisticas_por_rest;
import com.example.superadmin.Superadmin.super_gestion_rest;
import com.example.superadmin.Superadmin.super_gestion_usuarios;
import com.example.superadmin.Superadmin.super_logs;
import com.example.superadmin.adapters.RestaurantAdapter;
import com.example.superadmin.adapters.UserRestaurantAdapter;
import com.example.superadmin.dtos.RestaurantDTO;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.security.PrivateKey;
import java.util.ArrayList;

public class UserHomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserRestaurantAdapter adapter;
    private ArrayList<RestaurantDTO> restaurantList;
    private ArrayList<RestaurantDTO> filteredList;
    private NavigationView navigationView_menu;
    private DrawerLayout drawerLayout;
    private ImageButton buttonMenu;
    private FirebaseFirestore db;
    private Spinner tipoRestSpinner;

    private TextView saludoTextView;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_home);

        // Inicializar vistas
        // Inicializar vistas
        recyclerView = findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        saludoTextView = findViewById(R.id.textView7);

        restaurantList = new ArrayList<>();
        filteredList = new ArrayList<>();
        adapter = new UserRestaurantAdapter(this, filteredList);

        // Configuración del buscador
        searchView = findViewById(R.id.searchView_restaurants);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Filtrar cuando se presiona "Enter"
                filterRestaurants(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filtrar en tiempo real a medida que el usuario escribe
                filterRestaurants(newText);
                return true;
            }
        });

        // Configurar listener en el adaptador
        adapter.setOnCardClickListener(restaurant -> {
            Log.d("UID_INTENT", "UID del restaurante seleccionado: " + restaurant.getUidCreacion());

            // Enviar el UID a UserProductsActivity
            Intent intent = new Intent(UserHomeActivity.this, UserProductsActivity.class);
            intent.putExtra("uidRestaurante", restaurant.getUidCreacion());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        // Configurar Firebase Firestore
        db = FirebaseFirestore.getInstance();
        fetchRestaurants(null); // Cargar todos los restaurantes al inicio

        // Configurar menú lateral
        buttonMenu = findViewById(R.id.buttonMenu);
        buttonMenu.setOnClickListener(view -> drawerLayout.open());
// Inicializar DrawerLayout
        drawerLayout = findViewById(R.id.drawerLayout);
        getWindow().setStatusBarColor(ContextCompat.getColor(UserHomeActivity.this, R.color.red_boton));

        navigationView_menu = findViewById(R.id.navigationView_menu);
        navigationView_menu.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.btn_Perfil) {
                startActivity(new Intent(UserHomeActivity.this, ProfileActivity.class));
            } else if (id == R.id.btn_historial) {
                startActivity(new Intent(UserHomeActivity.this, UserHistorialActivity.class));
            } else if (id == R.id.btn_direcciones) {
                startActivity(new Intent(UserHomeActivity.this, ProfileActivity.class));
            } else if (id == R.id.btn_ayuda) {
                startActivity(new Intent(UserHomeActivity.this, ProfileActivity.class));
            }

            drawerLayout.closeDrawers(); // Cierra el menú después de seleccionar un ítem
            return true;
        });


        // Obtener el UID del usuario desde SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
        String userUid = preferences.getString("userId", null);

        if (userUid != null) {
            // Cargar el saludo personalizado para el superadmin
            cargarSaludoPersonalizado(userUid);
        } else {
            saludoTextView.setText("No hay usuario autenticado");
        }



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

    public void btnperfil(MenuItem item) {
        // Acción cuando se selecciona "Perfil"
        Toast.makeText(this, "Perfil seleccionado", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(UserHomeActivity.this, ProfileActivity.class));
    }

    public void btnhistorial(MenuItem item) {
        // Acción cuando se selecciona "Historial de Pedidos"
        Toast.makeText(this, "Historial de pedidos seleccionado", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(UserHomeActivity.this, UserHistorialActivity.class));
    }


    private void cargarSaludoPersonalizado(String userUid) {
        db.collection("users")
                .document(userUid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String nombre = documentSnapshot.getString("name");
                        saludoTextView.setText("Buenos días, " + nombre);
                    } else {
                        saludoTextView.setText("Buenos días, Usuario");
                    }
                })
                .addOnFailureListener(e -> saludoTextView.setText("Error al cargar saludo"));
    }


    private void filterRestaurants(String query) {
        filteredList.clear(); // Limpiar la lista filtrada
        for (RestaurantDTO restaurant : restaurantList) {
            // Verificar si el nombre del restaurante coincide con la búsqueda
            if (restaurant.getNombreRestaurante().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(restaurant);
            }
        }
        adapter.notifyDataSetChanged(); // Actualizar el RecyclerView
    }


}