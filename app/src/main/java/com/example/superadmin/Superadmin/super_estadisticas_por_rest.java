package com.example.superadmin.Superadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.superadmin.R;
import com.google.android.material.navigation.NavigationView;

public class super_estadisticas_por_rest extends AppCompatActivity {

    private NavigationView navigationView_menu;
    private DrawerLayout drawerLayout;
    private ImageButton buttonMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_estadisticas_por_rest);

        // Obtener los datos del Intent
        Intent intent = getIntent();
        String restaurantName = intent.getStringExtra("RESTAURANT_NAME");
        int restaurantImage = intent.getIntExtra("RESTAURANT_IMAGE", R.drawable.registrar_admin_rest); // Imagen por defecto si no se recibe

        // Configura los views con la información recibida
        TextView restaurantNameTextView = findViewById(R.id.restaurantName);
        ImageView restaurantImageView = findViewById(R.id.restaurantImage);
        buttonMenu = findViewById(R.id.buttonMenu);

        restaurantNameTextView.setText(restaurantName);
        restaurantImageView.setImageResource(restaurantImage);
        drawerLayout = findViewById(R.id.drawerLayout); // Asegúrate de que el ID sea correcto

        buttonMenu.setOnClickListener(view -> drawerLayout.open());
        getWindow().setStatusBarColor(ContextCompat.getColor(super_estadisticas_por_rest.this,R.color.red_boton));
        navigationView_menu = findViewById(R.id.navigationView_menu);
        navigationView_menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navGestionUsuarios) {
                    // Ir a Gestión de Usuarios
                    startActivity(new Intent(super_estadisticas_por_rest.this, super_gestion_usuarios.class));
                } else if (id == R.id.navRegistrarAdminRest) {
                    // Ir a Registrar Admin de Restaurante
                    startActivity(new Intent(super_estadisticas_por_rest.this, Super_registro_admin_rest.class));
                } else if (id == R.id.navReporteVentas_por_rest) {
                    // Ir a Registrar Admin de Restaurante
                    startActivity(new Intent(super_estadisticas_por_rest.this, super_gestion_rest.class));
                }
                else if (id == R.id.navReporteVentas) {
                    // Ir a Reporte de Ventas
                    startActivity(new Intent(super_estadisticas_por_rest.this, super_estadisticas_general.class));
                } else if (id == R.id.navLogs) {
                    // Ir a Logs
                    startActivity(new Intent(super_estadisticas_por_rest.this, super_logs.class));
                }

                drawerLayout.closeDrawers(); // Cierra el menú después de seleccionar un ítem
                return true;
            }
        });

    }

}