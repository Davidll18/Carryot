package com.example.superadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class SuperPrincipalActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageButton buttonMenu;
    NavigationView navigationView_menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.super_principal);

        drawerLayout = findViewById(R.id.draweLayout);
        buttonMenu = findViewById(R.id.buttonMenu);
        navigationView_menu = findViewById(R.id.navigationView_menu);

        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
            }
        });

        navigationView_menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navGestionUsuarios) {
                    // Ir a Gestión de Usuarios
                    startActivity(new Intent(SuperPrincipalActivity.this, super_gestion_usuarios.class));
                } else if (id == R.id.navRegistrarAdminRest) {
                    // Ir a Registrar Admin de Restaurante
                    startActivity(new Intent(SuperPrincipalActivity.this, Super_registro_admin_rest.class));
                } else if (id == R.id.navReporteVentas) {
                    // Ir a Reporte de Ventas
                    startActivity(new Intent(SuperPrincipalActivity.this, super_estadisticas_general.class));
                } else if (id == R.id.navLogs) {
                    // Ir a Logs
                    startActivity(new Intent(SuperPrincipalActivity.this, super_logs.class));
                }

                drawerLayout.closeDrawers(); // Cierra el menú después de seleccionar un ítem
                return true;
            }
        });


    }
    public void btnGestionUsuarios(View view){
        Intent m3intent = new Intent(this, super_gestion_usuarios.class);
        startActivity(m3intent);
    }
    public void btnRegistrarAdmin(View view){
        Intent m4intent = new Intent(this, Super_registro_admin_rest.class);
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

    public void btnLogs(View view){
        Intent m7intent = new Intent(this, super_logs.class);
        startActivity(m7intent);
    }

}
