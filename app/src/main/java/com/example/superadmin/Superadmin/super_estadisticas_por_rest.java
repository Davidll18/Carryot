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

import com.bumptech.glide.Glide;
import com.example.superadmin.LoginActivity;
import com.example.superadmin.R;
import com.example.superadmin.dtos.PlatoDTO;
import com.example.superadmin.service.FirestorePlatosService;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class super_estadisticas_por_rest extends AppCompatActivity {

    private NavigationView navigationView_menu;
    private DrawerLayout drawerLayout;
    private ImageButton buttonMenu;
    private TextView nombreRestaurante;
    private TextView categoriaRestaurante;
    private ImageView imagenRestaurante;
    private TextView costoRest;
    private TextView rateRest;


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
        ImageView restaurantImageView = findViewById(R.id.restaurantImageView);
        if (restaurantImageView != null) {
            restaurantImageView.setImageResource(R.drawable.logo);
        } else {
            System.err.println("restaurantImageView es nulo, revisa el layout");
        }

        buttonMenu = findViewById(R.id.buttonMenu);

        restaurantNameTextView.setText(restaurantName);
        restaurantImageView.setImageResource(restaurantImage);
        drawerLayout = findViewById(R.id.drawerLayout); // Asegúrate de que el ID sea correcto


        nombreRestaurante = findViewById(R.id.restaurantName);
        categoriaRestaurante = findViewById(R.id.restaurantCategory);
        costoRest = findViewById(R.id.costoTextView);
        rateRest = findViewById(R.id.rateTextView);
        imagenRestaurante = findViewById(R.id.restaurantImageView);


        // Obtener los datos enviados por el Intent
        String nombre = getIntent().getStringExtra("nombreRestaurante");
        String categoria = getIntent().getStringExtra("categoria");
        String imageUrl = getIntent().getStringExtra("imageUrl");
        String costo = getIntent().getStringExtra("costo");
        String rate = getIntent().getStringExtra("rate");

        // Asignar los datos a las vistas
        // Asignar los datos a las vistas
        nombreRestaurante.setText(nombre);
        categoriaRestaurante.setText(categoria);
        costoRest.setText("Costo: " + (costo != null ? costo : "N/A")); // Añadir texto "Costo:"
        rateRest.setText("Rate: " + (rate != null ? rate : "N/A"));     // Añadir texto "Rate:"



        Glide.with(this).load(imageUrl).placeholder(R.drawable.logo).into(imagenRestaurante);


        FirestorePlatosService platosService = new FirestorePlatosService();
        platosService.obtenerPlatosAleatorios(platos -> {
            if (platos != null && !platos.isEmpty()) {
                if (platos.size() > 0) {
                    PlatoDTO plato1 = platos.get(0);
                    Glide.with(this)
                            .load(plato1.getImageUrl())
                            .placeholder(R.drawable.dish_placeholder)
                            .into((ImageView) findViewById(R.id.platoImage1));
                    ((TextView) findViewById(R.id.platoName1)).setText(plato1.getNombrePlato());
                }
                if (platos.size() > 1) {
                    PlatoDTO plato2 = platos.get(1);
                    Glide.with(this)
                            .load(plato2.getImageUrl())
                            .placeholder(R.drawable.dish_placeholder)
                            .into((ImageView) findViewById(R.id.platoImage2));
                    ((TextView) findViewById(R.id.platoName2)).setText(plato2.getNombrePlato());
                }
                if (platos.size() > 2) {
                    PlatoDTO plato3 = platos.get(2);
                    Glide.with(this)
                            .load(plato3.getImageUrl())
                            .placeholder(R.drawable.dish_placeholder)
                            .into((ImageView) findViewById(R.id.platoImage3));
                    ((TextView) findViewById(R.id.platoName3)).setText(plato3.getNombrePlato());
                }
            } else {
                System.out.println("No se encontraron platos.");
            }
        });






        buttonMenu.setOnClickListener(view -> drawerLayout.open());
        getWindow().setStatusBarColor(ContextCompat.getColor(super_estadisticas_por_rest.this,R.color.light_orange));
        navigationView_menu = findViewById(R.id.navigationView_menu);
        navigationView_menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                Intent intent = null;

                if (id == R.id.navGestionUsuarios) {
                    // Ir a Gestión de Usuarios
                    intent = new Intent(super_estadisticas_por_rest.this, super_gestion_usuarios.class);
                } else if (id == R.id.navRegistrarAdminRest) {
                    // Ir a Registrar Admin de Restaurante
                    intent = new Intent(super_estadisticas_por_rest.this, Super_registro_admin_rest.class);
                } else if (id == R.id.navReporteVentas_por_rest) {
                    // Ir a Reporte de Ventas por Restaurante
                    intent = new Intent(super_estadisticas_por_rest.this, super_gestion_rest.class);
                } else if (id == R.id.navReporteVentas) {
                    // Ir a Reporte de Ventas
                    intent = new Intent(super_estadisticas_por_rest.this, super_estadisticas_general.class);
                } else if (id == R.id.navLogs) {
                    // Ir a Logs
                    intent = new Intent(super_estadisticas_por_rest.this, super_logs.class);
                } else if (id == R.id.navlogout) {
                    mostrarDialogoCerrarSesion();
                }


                drawerLayout.closeDrawers(); // Cierra el menú después de seleccionar un ítem
                if (intent != null) {
                    startActivity(intent);
                }
                return true;
            }



        });


        ImageView platoImage1 = findViewById(R.id.platoImage1);
        TextView platoName1 = findViewById(R.id.platoName1);

        String platoImageUrl = "https://ejemplo.com/plato1.jpg";
        String platoName = "Pizza Margarita";
        Glide.with(this)
                .load(platoImageUrl)
                .placeholder(R.drawable.dish_placeholder)
                .into(platoImage1);

        platoName1.setText(platoName);



    }
    private void mostrarDialogoCerrarSesion() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Estás seguro de que quieres cerrar sesión?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    // Cerrar sesión en Firebase
                    FirebaseAuth.getInstance().signOut();

                    // Redirigir al LoginActivity y eliminar todas las actividades
                    Intent intent = new Intent(super_estadisticas_por_rest.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    // Terminar la actividad actual
                    finishAffinity();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss()) // Solo cerrar el diálogo si se selecciona "No"
                .show();
    }




}