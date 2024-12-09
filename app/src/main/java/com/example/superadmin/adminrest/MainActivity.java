package com.example.superadmin.adminrest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superadmin.R;
import com.example.superadmin.adminrest.Adapter.GananciaAdapter;
import com.example.superadmin.adminrest.Adapter.PedidosStatusAdapter;
import com.example.superadmin.adminrest.Adapter.PlatosEstadisticaAdapter;
import com.example.superadmin.adminrest.dto.GananciaItem;
import com.example.superadmin.adminrest.dto.PedidosStatusItem;
import com.example.superadmin.adminrest.dto.PlatosEstItem;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private RecyclerView recyclerViewPedidos;
    private RecyclerView recyclerViewGanancia;
    private RecyclerView recyclerViewPlatosEst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_adminrest);

        // Configuración del Toolbar personalizado
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Configurar elementos del Toolbar (opcional)
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        ImageView toolbarUser = findViewById(R.id.toolbar_user);

        toolbarTitle.setText("MI RESTAURANTE"); // Personaliza el título
        toolbarUser.setOnClickListener(v -> {
            // Acción cuando se hace clic en el icono del usuario
            Toast.makeText(this, "Perfil del usuario", Toast.LENGTH_SHORT).show();
        });

        // Configuración del DrawerLayout y NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        // Configuración de RecyclerView
        setupRecyclerViews();
    }

    private void setupRecyclerViews() {
        recyclerViewPedidos = findViewById(R.id.recyclerViewPedidosPendientes);
        recyclerViewPedidos.setLayoutManager(new LinearLayoutManager(this));
        List<PedidosStatusItem> pedidoslist = new ArrayList<>();
        pedidoslist.add(new PedidosStatusItem("PEDIDOS PENDIENTES", "08", "PEDIDOS ACTIVOS", "20"));
        PedidosStatusAdapter pedidosStatusAdapter = new PedidosStatusAdapter(this, pedidoslist);
        recyclerViewPedidos.setAdapter(pedidosStatusAdapter);

        recyclerViewGanancia = findViewById(R.id.recyclerGananciaEstadistica);
        recyclerViewGanancia.setLayoutManager(new LinearLayoutManager(this));
        List<GananciaItem> gananciaList = new ArrayList<>();
        gananciaList.add(new GananciaItem("GANANCIA TOTAL", R.drawable.statistics_image));
        GananciaAdapter gananciaAdapter = new GananciaAdapter(this, gananciaList);
        recyclerViewGanancia.setAdapter(gananciaAdapter);

        recyclerViewPlatosEst = findViewById(R.id.recyclerViewPlatosPopulares);
        recyclerViewPlatosEst.setLayoutManager(new LinearLayoutManager(this));
        List<PlatosEstItem> platosEstList = new ArrayList<>();
        platosEstList.add(new PlatosEstItem("PLATOS POPULARES", R.drawable.ceviche, "Ceviche", R.drawable.lomo, "Lomo Saltado", R.drawable.pollito, "Pollo a la brasa"));
        PlatosEstadisticaAdapter platosEstadisticaAdapter = new PlatosEstadisticaAdapter(this, platosEstList);
        recyclerViewPlatosEst.setAdapter(platosEstadisticaAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int menu = menuItem.getItemId();
        if (menu == R.id.nav_home) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (menu == R.id.nav_pedidos) {
            startActivity(new Intent(this, PedidosActivity.class));
        } else if (menu == R.id.nav_profile) {
            SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
            String uidRestaurante = sharedPreferences.getString("restaurant_uid", null);
            if (uidRestaurante != null) {
                Intent intent = new Intent(this, ProfileRestActivity.class);
                intent.putExtra("uidRestaurante", uidRestaurante);
                startActivity(intent);
            } else {
                Toast.makeText(this, "No se encontró el UID del restaurante", Toast.LENGTH_SHORT).show();
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
