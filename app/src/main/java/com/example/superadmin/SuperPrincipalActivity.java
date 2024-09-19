package com.example.superadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class SuperPrincipalActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageButton buttonMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.super_principal);

        drawerLayout = findViewById(R.id.draweLayout);
        buttonMenu = findViewById(R.id.buttonMenu);

        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
            }
        });

    }
    public void btnGestionUsuarios(View view){
        Intent m3intent = new Intent(this, super_gestion_usuarios.class);
        startActivity(m3intent);
    }
    public void btnRegistrarAdmin(View view){
        Intent m4intent = new Intent(this, super_register_admin_rest.class);
        startActivity(m4intent);
    }
    public void btnReporteVentas(View view){
        Intent m5intent = new Intent(this, super_estadisticas_general.class);
        startActivity(m5intent);
    }
    public void btnRestaurants(View view){
        Intent m6intent = new Intent(this, super_rest.class);
        startActivity(m6intent);
    }

}
