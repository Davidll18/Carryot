package com.example.superadmin.adminrest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superadmin.R;
import com.example.superadmin.adminrest.Adapter.FoodAdapter;
import com.example.superadmin.adminrest.dto.FoodItem;
import com.example.superadmin.dtos.PlatoDTO;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class DishesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView recyclerView;
    private FoodAdapter foodAdapter;
    private List<FoodItem> foodList;
    
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    AppCompatButton addDishes;
    private FirebaseStorage storage;
    private FirebaseFirestore db;
    private TabLayout tabLayoutEstados;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.listdishes_adminrest);

        tabLayoutEstados = findViewById(R.id.tabLayoutEstados);
        addDishes = findViewById(R.id.btn_addDishes);
        addDishes.setOnClickListener(v -> {
            // Crea un Intent para ir a la actividad NewDishes
            Intent intent = new Intent(this, NewDishActivity.class);
            startActivity(intent);

        });
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {

                            // Buscar el restaurante con el uidCreador
                            db.collection("restaurant")
                                    .whereEqualTo("uidCreador", uid) // Filtrar por uidCreador
                                    .get()
                                    .addOnSuccessListener(queryDocumentSnapshots -> {
                                        if (!queryDocumentSnapshots.isEmpty()) {
                                            // Obtener el primer restaurante que coincida
                                            DocumentSnapshot restauranteSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                            String idRestaurante = restauranteSnapshot.getString("uidCreacion");

                                            // Buscar platos que correspondan al restaurante
                                            db.collection("platos")
                                                    .whereEqualTo("uidRestaurante", idRestaurante)
                                                    .get()
                                                    .addOnSuccessListener(platosQuerySnapshot -> {
                                                        if (!platosQuerySnapshot.isEmpty()) {
                                                            // Convertir documentos en objetos PlatoDTO
                                                            ArrayList<PlatoDTO> platosList = new ArrayList<>();
                                                            for (DocumentSnapshot platoSnapshot : platosQuerySnapshot.getDocuments()) {
                                                                PlatoDTO plato = platoSnapshot.toObject(PlatoDTO.class);
                                                                platosList.add(plato);
                                                            }
                                                            configurarRecyclerView(platosList);

                                                            // Configurar filtrado por categorías
                                                            tabLayoutEstados.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                                                @Override
                                                                public void onTabSelected(TabLayout.Tab tab) {
                                                                    String categoriaSeleccionada = tab.getText().toString();
                                                                    filtrarPorCategoria(categoriaSeleccionada, platosList);
                                                                }

                                                                @Override
                                                                public void onTabUnselected(TabLayout.Tab tab) {}

                                                                @Override
                                                                public void onTabReselected(TabLayout.Tab tab) {}
                                                            });

                                                            // Aquí puedes usar la lista de PlatoDTO
                                                            for (PlatoDTO plato : platosList) {
                                                                Log.d("Platos", "Plato: " + plato.toString());
                                                            }

                                                            Toast.makeText(this, "Platos encontrados: " + platosList.size(), Toast.LENGTH_SHORT).show();

                                                        } else {
                                                            Toast.makeText(this, "No se encontraron platos para este restaurante.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(e ->
                                                            Toast.makeText(this, "Error al buscar los platos: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                        } else {
                                            Toast.makeText(this, "No se encontró un restaurante para este usuario.", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(e ->
                                            Toast.makeText(this, "Error al buscar el restaurante: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                        } else {
                            Toast.makeText(this, "Usuario no encontrado en Firestore", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Error al obtener datos del usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "No hay un usuario autenticado.", Toast.LENGTH_SHORT).show();
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_logout).setVisible(false);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_home);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    private void filtrarPorCategoria(String categoriaSeleccionada, List<PlatoDTO> platosList) {
        ArrayList<PlatoDTO> platosFiltrados = new ArrayList<>();

        // Filtrar platos según la categoría seleccionada
        for (PlatoDTO plato : platosList) {
            if (categoriaSeleccionada.equals("TODO") || categoriaSeleccionada.equalsIgnoreCase(plato.getCategoriaPlato())) {
                platosFiltrados.add(plato);
            }
        }

        // Actualizar el RecyclerView con los platos filtrados
        configurarRecyclerView(platosFiltrados);
    }

    private void configurarRecyclerView(ArrayList<PlatoDTO> listaPlatos) {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Diseño vertical
        FoodAdapter foodAdapter = new FoodAdapter(this, listaPlatos); // Instancia del adaptador
        recyclerView.setAdapter(foodAdapter); // Asigna el adaptador
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else{
            super.onBackPressed();
        }

        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int menu = menuItem.getItemId();
        if (menu == R.id.nav_home){
            Intent intent = new Intent(DishesActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (menu == R.id.nav_pedidos) {
            Intent intent = new Intent(DishesActivity.this, PedidosActivity.class);
            startActivity(intent);
        } else if (menu == R.id.nav_profile) {
            Intent intent = new Intent(DishesActivity.this, ProfileRestActivity.class);
            startActivity(intent);
        } else if (menu == R.id.nav_dishes) {
            Intent intent = new Intent(DishesActivity.this, DishesActivity.class);
            startActivity(intent);
        } else if (menu == R.id.nav_ganancia) {
            Intent intent = new Intent(DishesActivity.this, GananciaActivity.class);
            startActivity(intent);
        } else if (menu == R.id.nav_popular) {
            Intent intent = new Intent(DishesActivity.this, StatisticsActivity.class);
            startActivity(intent);
        } else if (menu == R.id.nav_users) {
            Intent intent = new Intent(DishesActivity.this, StatisticsActivity.class);
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}
