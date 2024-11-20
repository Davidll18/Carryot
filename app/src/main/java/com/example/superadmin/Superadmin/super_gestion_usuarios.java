package com.example.superadmin.Superadmin;

import android.content.Intent;
import android.os.Bundle;

import com.example.superadmin.R;
import com.example.superadmin.SignUpActivity;
import com.example.superadmin.adapters.AdminRestAdapter;
import com.example.superadmin.dtos.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class super_gestion_usuarios extends AppCompatActivity {

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

        // Configuración del RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adminRestAdapter = new AdminRestAdapter(userList);
        recyclerView.setAdapter(adminRestAdapter);

        // Evento del botón de menú
        buttonMenu.setOnClickListener(view -> drawerLayout.open());
        getWindow().setStatusBarColor(ContextCompat.getColor(super_gestion_usuarios.this,R.color.red_boton));

        // Llama al método para cargar los usuarios
        loadUsersFromFirestore();

    }
    private void loadUsersFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    userList.clear(); // Limpia la lista actual
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        User user = document.toObject(User.class);
                        userList.add(user);
                    }
                    adminRestAdapter.notifyDataSetChanged(); // Actualiza el RecyclerView
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar usuarios: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}



