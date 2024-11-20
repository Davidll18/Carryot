package com.example.superadmin.Superadmin;

import android.content.Intent;
import android.os.Bundle;

import com.example.superadmin.R;
import com.example.superadmin.SignUpActivity;
import com.example.superadmin.adapters.AdminRestAdapter;
import com.example.superadmin.dtos.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageButton;


import java.util.ArrayList;
import java.util.List;

public class super_gestion_usuarios extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageButton buttonMenu;
    private RecyclerView recyclerView;
    private AdminRestAdapter adminRestAdapter;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_gestion_usuarios);

        drawerLayout = findViewById(R.id.drawerLayout_super_getion_usuarios); // Este ID debe coincidir
        buttonMenu = findViewById(R.id.buttonMenu);
        recyclerView = findViewById(R.id.recyclerViewAdmins); // Asegúrate de que este RecyclerView exista en tu layout


        // Configuración del RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
}

//    public void register_btn(View view){
//        Intent m7intent = new Intent(this, SignUpActivity.class);
//        startActivity(m7intent);
//    }
//    public void Edit_rest(View view){
//        Intent m10intent = new Intent(this, super_habilitar_usuarios.class);
//        startActivity(m10intent);
//    }

