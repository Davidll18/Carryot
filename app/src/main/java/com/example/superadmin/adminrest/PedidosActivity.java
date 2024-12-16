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
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superadmin.R;

import com.example.superadmin.adminrest.Adapter.PedidosAdapter;
import com.example.superadmin.adminrest.Adapter.PedidosAdapter;
import com.example.superadmin.adminrest.dto.PedidosItem;
import com.example.superadmin.dtos.Pedidos;
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

public class PedidosActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    private FirebaseStorage storage;
    private FirebaseFirestore db;
    private TabLayout tabLayoutEstados;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.pedidos_adminrest);
        tabLayoutEstados = findViewById(R.id.tabLayoutEstados);

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
                                            db.collection("pedidos")
                                                    .whereEqualTo("uidRestaurante", idRestaurante)
                                                    .get()
                                                    .addOnSuccessListener(platosQuerySnapshot -> {
                                                        if (!platosQuerySnapshot.isEmpty()) {
                                                            // Convertir documentos en objetos PlatoDTO
                                                            ArrayList<Pedidos> pedidosList = new ArrayList<>();
                                                            for (DocumentSnapshot pedidoSnapshot : platosQuerySnapshot.getDocuments()) {
                                                                Pedidos pedidos = pedidoSnapshot.toObject(Pedidos.class);
                                                                pedidosList.add(pedidos);
                                                            }
                                                            configurarRecyclerView(pedidosList);

                                                            // Configurar filtrado por categorías
                                                            tabLayoutEstados.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                                                @Override
                                                                public void onTabSelected(TabLayout.Tab tab) {
                                                                    String categoriaSeleccionada = tab.getText().toString();
                                                                    filtrarPorCategoria(categoriaSeleccionada, pedidosList);
                                                                }

                                                                @Override
                                                                public void onTabUnselected(TabLayout.Tab tab) {}

                                                                @Override
                                                                public void onTabReselected(TabLayout.Tab tab) {}
                                                            });

                                                            // Aquí puedes usar la lista de PlatoDTO
                                                            for (Pedidos pedidos : pedidosList) {
                                                                Log.d("Platos", "Plato: " + pedidos.toString());
                                                            }

                                                            Toast.makeText(this, "Pedidos encontrados: " + pedidosList.size(), Toast.LENGTH_SHORT).show();

                                                        } else {
                                                            Toast.makeText(this, "No se encontraron pedidos para este restaurante.", Toast.LENGTH_SHORT).show();
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



        RecyclerView recyclerViewPedidos = findViewById(R.id.recyclerViewPedidos);
        recyclerViewPedidos.setLayoutManager(new LinearLayoutManager(this));

        /*menu*/
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
        /*FIN DE MENU*/

    }

    private void filtrarPorCategoria(String categoriaSeleccionada, ArrayList<Pedidos> pedidosList) {
        ArrayList<Pedidos> pedidosFiltrados = new ArrayList<>();

        // Filtrar platos según la categoría seleccionada
        for (Pedidos pedidos : pedidosList) {
            if (categoriaSeleccionada.equalsIgnoreCase(pedidos.getEstado())) {
                pedidosFiltrados.add(pedidos);
            }

        }
        configurarRecyclerView(pedidosFiltrados);
    }

    private void configurarRecyclerView(ArrayList<Pedidos> pedidosList) {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewPedidos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Diseño vertical
        PedidosAdapter PedidosAdapter = new PedidosAdapter(this, pedidosList); // Instancia del adaptador
        recyclerView.setAdapter(PedidosAdapter);
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
            Intent intent = new Intent(PedidosActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (menu == R.id.nav_pedidos) {
            Intent intent = new Intent(PedidosActivity.this, PedidosActivity.class);
            startActivity(intent);
        } else if (menu == R.id.nav_profile) {
            Intent intent = new Intent(PedidosActivity.this, ProfileRestActivity.class);
            startActivity(intent);
        } else if (menu == R.id.nav_dishes) {
            Intent intent = new Intent(PedidosActivity.this, DishesActivity.class);
            startActivity(intent);
        } else if (menu == R.id.nav_ganancia) {
            Intent intent = new Intent(PedidosActivity.this, GananciaActivity.class);
            startActivity(intent);
        } else if (menu == R.id.nav_popular) {
            Intent intent = new Intent(PedidosActivity.this, StatisticsActivity.class);
            startActivity(intent);
        } else if (menu == R.id.nav_users) {
            Intent intent = new Intent(PedidosActivity.this, StatisticsActivity.class);
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}
