package com.example.superadmin.adminrest;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superadmin.R;

import com.example.superadmin.adminrest.Adapter.PedidosAdapter;
import com.example.superadmin.adminrest.dto.PedidosItem;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class PedidosActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.pedidos_adminrest);

        RecyclerView recyclerViewPedidos = findViewById(R.id.recyclerViewpedidos);
        recyclerViewPedidos.setLayoutManager(new LinearLayoutManager(this));

        List<PedidosItem> pedidoList = new ArrayList<>();
        pedidoList.add(new PedidosItem("BOL1234", "Pendiente",R.drawable.background_orange, "1x Lomo Saltado\n1x Inka Cola", R.drawable.lomo, R.drawable.eye_icon, "S/ 80.00"));
        pedidoList.add(new PedidosItem("BOL1235", "Completado",R.drawable.background_orange, "1x Ceviche\n1x Chicha Morada", R.drawable.ceviche, R.drawable.eye_icon, "S/ 60.00"));
        pedidoList.add(new PedidosItem("BOL1234", "Pendiente", R.drawable.background_orange, "1x Lomo Saltado\n1x Inka Cola", R.drawable.lomo, R.drawable.eye_icon, "S/ 80.00"));
        pedidoList.add(new PedidosItem("BOL1235", "Completado", R.drawable.background_green, "1x Ceviche\n1x Chicha Morada", R.drawable.ceviche, R.drawable.eye_icon, "S/ 60.00"));
        pedidoList.add(new PedidosItem("BOL1236", "En preparación", R.drawable.background_amber, "1x Tacu Tacu\n1x Chicha Morada", R.drawable.lomo, R.drawable.eye_icon, "S/ 70.00"));
        pedidoList.add(new PedidosItem("BOL1237", "En camino", R.drawable.background_blue, "1x Arroz Chaufa\n1x Gaseosa", R.drawable.ceviche, R.drawable.eye_icon, "S/ 65.00"));

        PedidosAdapter pedidoAdapter = new PedidosAdapter(this, pedidoList);  // Si estás dentro de una actividad
        recyclerViewPedidos.setAdapter(pedidoAdapter);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_logout).setVisible(false);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_home);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else{
            super.onBackPressed();
        }

        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int menu = menuItem.getItemId();
        if (menu == R.id.nav_home){
            Intent intent = new Intent(PedidosActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (menu == R.id.nav_pedidos) {
            Intent intent = new Intent(PedidosActivity.this, PedidosActivity.class);
            startActivity(intent);
        } else if (menu == R.id.nav_profile) {
            Intent intent = new Intent(PedidosActivity.this, ProfileRestActivity.class);
            startActivity(intent);
        } else if (menu == R.id.nav_dishes) {
            Intent intent = new Intent(PedidosActivity.this, DishesActivity.class);
            startActivity(intent);
        } else if (menu == R.id.nav_ganancia) {
            Intent intent = new Intent(PedidosActivity.this, GananciaActivity.class);
            startActivity(intent);
        } else if (menu == R.id.nav_popular) {
            Intent intent = new Intent(PedidosActivity.this, StatisticsActivity.class);
            startActivity(intent);
        } else if (menu == R.id.nav_users) {
            Intent intent = new Intent(PedidosActivity.this, StatisticsActivity.class);
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}
