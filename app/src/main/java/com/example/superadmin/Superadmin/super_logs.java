package com.example.superadmin.Superadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superadmin.R;
import com.example.superadmin.adapters.LogAdapter;
import com.example.superadmin.model.LogEntry;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class super_logs extends AppCompatActivity {

    private RecyclerView recyclerViewLogs;
    private LogAdapter logAdapter;
    private List<LogEntry> logList;
    private NavigationView navigationView_menu;
    private DrawerLayout drawerLayout;
    private ImageButton buttonMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_super_logs);

        recyclerViewLogs = findViewById(R.id.recyclerViewLogs);

        recyclerViewLogs.setLayoutManager(new LinearLayoutManager(this));


        logList = new ArrayList<>();
        logList.add(new LogEntry("usuario1", "Inició sesión", "2024-10-22 10:30"));
        logList.add(new LogEntry("usuario2", "Realizó un pedido", "2024-10-22 11:00"));
        logList.add(new LogEntry("usuario3", "Cerró sesión", "2024-10-22 11:30"));

        logAdapter = new LogAdapter(logList);
        recyclerViewLogs.setAdapter(logAdapter);
        buttonMenu = findViewById(R.id.buttonMenu);
        drawerLayout = findViewById(R.id.drawerLayout_super_getion_usuarios);
        buttonMenu.setOnClickListener(view -> drawerLayout.open());

        getWindow().setStatusBarColor(ContextCompat.getColor(super_logs.this,R.color.red_boton));
        navigationView_menu = findViewById(R.id.navigationView_menu);
        navigationView_menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navGestionUsuarios) {
                    // Ir a Gestión de Usuarios
                    startActivity(new Intent(super_logs.this, super_gestion_usuarios.class));
                } else if (id == R.id.navRegistrarAdminRest) {
                    // Ir a Registrar Admin de Restaurante
                    startActivity(new Intent(super_logs.this, Super_registro_admin_rest.class));
                } else if (id == R.id.navReporteVentas_por_rest) {
                    // Ir a Registrar Admin de Restaurante
                    startActivity(new Intent(super_logs.this, super_rest.class));
                }
                else if (id == R.id.navReporteVentas) {
                    // Ir a Reporte de Ventas
                    startActivity(new Intent(super_logs.this, super_estadisticas_general.class));
                } else if (id == R.id.navLogs) {
                    // Ir a Logs
                    startActivity(new Intent(super_logs.this, super_logs.class));
                }

                drawerLayout.closeDrawers(); // Cierra el menú después de seleccionar un ítem
                return true;
            }
        });


    }
}