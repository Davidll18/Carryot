package com.example.superadmin.adminrest;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superadmin.R;
import com.example.superadmin.adminrest.Adapter.FoodAdapter;
import com.example.superadmin.adminrest.dto.FoodItem;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class DishesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView recyclerView;
    private FoodAdapter foodAdapter;
    private List<FoodItem> foodList;
    
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    AppCompatButton addDishes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.listdishes_adminrest);

        addDishes = findViewById(R.id.btn_addDishes);
        addDishes.setOnClickListener(v -> {
            // Crea un Intent para ir a la actividad NewDishes
            Intent intent = new Intent(this, NewDishActivity.class);
            startActivity(intent);
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        foodList = new ArrayList<>();
        foodList.add(new FoodItem("Lomo Saltado", "S/ 24.00", "Cantidad: 50 und"));
        foodList.add(new FoodItem("Pollo a la Brasa", "S/ 18.00", "Cantidad: 30 und"));
        foodList.add(new FoodItem("Ceviche", "S/ 20.00", "Cantidad: 25 und"));

        foodAdapter = new FoodAdapter(foodList);
        recyclerView.setAdapter(foodAdapter);

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
            Intent intent = new Intent(DishesActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (menu == R.id.nav_pedidos) {
            Intent intent = new Intent(DishesActivity.this, PedidosActivity.class);
            startActivity(intent);
        } else if (menu == R.id.nav_profile) {
            Intent intent = new Intent(DishesActivity.this, ProfileRestActivity.class);
            startActivity(intent);
        } else if (menu == R.id.nav_dishes) {
            Intent intent = new Intent(DishesActivity.this, DishesActivity.class);
            startActivity(intent);
        } else if (menu == R.id.nav_ganancia) {
            Intent intent = new Intent(DishesActivity.this, GananciaActivity.class);
            startActivity(intent);
        } else if (menu == R.id.nav_popular) {
            Intent intent = new Intent(DishesActivity.this, StatisticsActivity.class);
            startActivity(intent);
        } else if (menu == R.id.nav_users) {
            Intent intent = new Intent(DishesActivity.this, StatisticsActivity.class);
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}
