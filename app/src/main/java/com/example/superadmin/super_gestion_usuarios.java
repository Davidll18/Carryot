package com.example.superadmin;

import android.content.Intent;
import android.os.Bundle;

import com.example.superadmin.adapters.AdminRestAdapter;
import com.example.superadmin.model.user;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;


import java.util.ArrayList;
import java.util.List;

public class super_gestion_usuarios extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageButton buttonMenu;
    private RecyclerView recyclerView;
    private AdminRestAdapter adminRestAdapter;
    private List<user> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_gestion_usuarios);

        drawerLayout = findViewById(R.id.drawerLayout_super_getion_usuarios); // Este ID debe coincidir
        buttonMenu = findViewById(R.id.buttonMenu);
        recyclerView = findViewById(R.id.recyclerViewAdmins); // Asegúrate de que este RecyclerView exista en tu layout


        // Configuración del RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = generateDummyData(); // Generar datos estáticos
        adminRestAdapter = new AdminRestAdapter(userList);
        recyclerView.setAdapter(adminRestAdapter);

        buttonMenu = findViewById(R.id.buttonMenu);

        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
            }
        });
    }
    // Método para generar datos de prueba
    private List<user> generateDummyData() {
        List<user> users = new ArrayList<>();
        users.add(new user("Juan", "Pérez", "12345678", "juan.perez@example.com", "987654321", "Av. Principal 123", "Admin", true));
        users.add(new user("Ana", "García", "87654321", "ana.garcia@example.com", "123456789", "Calle Secundaria 456", "Admin", false));
        users.add(new user("Luis", "Martínez", "12345679", "luis.martinez@example.com", "456789123", "Calle Terciaria 789", "Admin", true));
        // Agrega más usuarios según sea necesario
        return users;
    }

    public void register_btn(View view){
        Intent m7intent = new Intent(this, SignUpActivity.class);
        startActivity(m7intent);
    }
    public void Edit_rest(View view){
        Intent m10intent = new Intent(this, super_habilitar_usuarios.class);
        startActivity(m10intent);
    }

}