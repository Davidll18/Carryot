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

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    //INICIAR RECYCLERVIEW
    private RecyclerView recyclerViewPedidos;
    private RecyclerView recyclerViewGanancia;
    private RecyclerView recyclerViewPlatosEst;

    private PedidosStatusAdapter pedidosStatusAdapter;
    private GananciaAdapter gananciaAdapter;
    private PlatosEstadisticaAdapter platosEstadisticaAdapter;
    private List<PedidosStatusItem> pedidoslist;
    private List<GananciaItem> gananciaList;
    private List<PlatosEstItem> platosEstList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.home_adminrest);
        /* RECYCLER VIEW*/
        recyclerViewPedidos = findViewById(R.id.recyclerViewPedidosPendientes);
        recyclerViewPedidos.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewGanancia = findViewById(R.id.recyclerGananciaEstadistica);
        recyclerViewGanancia.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewPlatosEst = findViewById(R.id.recyclerViewPlatosPopulares);
        recyclerViewPlatosEst.setLayoutManager(new LinearLayoutManager(this));

        pedidoslist = new ArrayList<>();
        gananciaList = new ArrayList<>();
        platosEstList = new ArrayList<>();

        pedidoslist.add(new PedidosStatusItem("PEDIDOS PENDIENTES", "08", "PEDIDOS ACTIVOS", "20"));
        gananciaList.add(new GananciaItem("GANANCIA TOTAL", R.drawable.statistics_image));
        platosEstList.add(new PlatosEstItem("PLATOS POPULARES", R.drawable.ceviche, "Ceviche", R.drawable.lomo, "Lomo Saltado", R.drawable.pollito, "Pollo a la brasa"));

        pedidosStatusAdapter = new PedidosStatusAdapter(this, pedidoslist);
        recyclerViewPedidos.setAdapter(pedidosStatusAdapter);

        gananciaAdapter = new GananciaAdapter(this, gananciaList);
        recyclerViewGanancia.setAdapter(gananciaAdapter);

        platosEstadisticaAdapter = new PlatosEstadisticaAdapter(this, platosEstList);
        recyclerViewPlatosEst.setAdapter(platosEstadisticaAdapter);
        /* RECYCLER VIEW*/

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
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (menu == R.id.nav_pedidos) {
            Intent intent = new Intent(MainActivity.this, PedidosActivity.class);
            startActivity(intent);
        } else if (menu == R.id.nav_profile) {
            Intent intent = new Intent(MainActivity.this, ProfileRestActivity.class);
            startActivity(intent);
        } else if (menu == R.id.nav_dishes) {
            Intent intent = new Intent(MainActivity.this, DishesActivity.class);
            startActivity(intent);
        } else if (menu == R.id.nav_ganancia) {
            Intent intent = new Intent(MainActivity.this, GananciaActivity.class);
            startActivity(intent);
        } else if (menu == R.id.nav_popular) {
            Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
            startActivity(intent);
        } else if (menu == R.id.nav_users) {
            Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}