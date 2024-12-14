package com.example.superadmin.Superadmin;

import android.content.Intent;
import android.os.Bundle;

import com.example.superadmin.R;
import com.example.superadmin.adapters.AdminRestAdapter;
import com.example.superadmin.dtos.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class super_gestion_usuarios extends AppCompatActivity {

    private NavigationView navigationView_menu;
    private DrawerLayout drawerLayout;
    private ImageButton buttonMenu;
    private RecyclerView recyclerView;
    private AdminRestAdapter adminRestAdapter;
    private List<User> userList = new ArrayList<>(); // Inicializamos la lista vacía

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_gestion_usuarios);

        drawerLayout = findViewById(R.id.drawerLayout_super_getion_usuarios);
        buttonMenu = findViewById(R.id.buttonMenu);
        recyclerView = findViewById(R.id.recyclerViewAdmins);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adminRestAdapter = new AdminRestAdapter(userList, this::onUserClicked);
        recyclerView.setAdapter(adminRestAdapter);

        buttonMenu.setOnClickListener(view -> drawerLayout.open());
        getWindow().setStatusBarColor(ContextCompat.getColor(super_gestion_usuarios.this, R.color.red_boton));

        navigationView_menu = findViewById(R.id.navigationView_menu);
        navigationView_menu.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navGestionUsuarios) {
                startActivity(new Intent(super_gestion_usuarios.this, super_gestion_usuarios.class));
            } else if (id == R.id.navRegistrarAdminRest) {
                startActivity(new Intent(super_gestion_usuarios.this, Super_registro_admin_rest.class));
            } else if (id == R.id.navReporteVentas_por_rest) {
                startActivity(new Intent(super_gestion_usuarios.this, super_gestion_rest.class));
            } else if (id == R.id.navReporteVentas) {
                startActivity(new Intent(super_gestion_usuarios.this, super_estadisticas_general.class));
            } else if (id == R.id.navLogs) {
                startActivity(new Intent(super_gestion_usuarios.this, super_logs.class));
            }
            drawerLayout.closeDrawers();
            return true;
        });

        loadUsersFromFirestore();
    }

    private void loadUsersFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    userList.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        User user = document.toObject(User.class);
                        if (user != null) {
                            user.setUid(document.getId());
                            userList.add(user);
                        } else {
                            Log.e("Firestore", "Error: Documento vacío o nulo");
                        }
                    }
                    adminRestAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error al cargar usuarios: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }


    private void onUserClicked(User user) {
        Log.d("Intent", "User ID: " + user.getUid());  // Verifica si UID no es null
        Log.d("Intent", "Name: " + user.getName());  // Verifica si Name no es null
        Log.d("Intent", "Surname: " + user.getSurname());  // Verifica si Surname no es null
        Log.d("Intent", "DNI: " + user.getDni());  // Verifica si DNI no es null
        Log.d("Intent", "Phone: " + user.getPhone());  // Verifica si Phone no es null
        Log.d("Intent", "Email: " + user.getEmail());  // Verifica si Email no es null
        Log.d("Intent", "Status: " + user.getStatus());  // Verifica si Status no es null

        Intent intent = new Intent(this, super_habilitar_usuarios.class);
        intent.putExtra("userId", user.getUid());
        startActivity(intent);

    }



}



