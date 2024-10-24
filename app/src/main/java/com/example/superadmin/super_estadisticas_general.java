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

public class super_estadisticas_general extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageButton buttonMenu;
    NavigationView navigationView_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_estadisticas_general);

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
                    startActivity(new Intent(super_estadisticas_general.this, super_gestion_usuarios.class));
                } else if (id == R.id.navRegistrarAdminRest) {
                    // Ir a Registrar Admin de Restaurante
                    startActivity(new Intent(super_estadisticas_general.this, Super_registro_admin_rest.class));
                } else if (id == R.id.navReporteVentas_por_rest) {
                    // Ir a Registrar Admin de Restaurante
                    startActivity(new Intent(super_estadisticas_general.this, super_rest.class));
                }
                else if (id == R.id.navReporteVentas) {
                    // Ir a Reporte de Ventas
                    startActivity(new Intent(super_estadisticas_general.this, super_estadisticas_general.class));
                } else if (id == R.id.navLogs) {
                    // Ir a Logs
                    startActivity(new Intent(super_estadisticas_general.this, super_logs.class));
                }

                drawerLayout.closeDrawers(); // Cierra el menú después de seleccionar un ítem
                return true;
            }
        });
    }
    public void VerRest(View view){
        Intent m2intent = new Intent(this, super_estadisticas_por_rest.class);
        startActivity(m2intent);
    }
}