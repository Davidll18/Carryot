package com.example.superadmin.adminrest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.example.superadmin.LoginActivity;
import com.example.superadmin.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
public class ProfileRestActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restprofile);

        // Inicializar FirebaseAuth y Firestore
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Configuración del Toolbar y DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        // Configuración del ActionBarDrawerToggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Asegurar que el menú sea interactivo
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_profile); // Establecer el elemento actual como seleccionado

        // Mostrar la información del restaurante
        displayRestaurantProfile();
    }

    private void displayRestaurantProfile() {
        // Referencias a los campos del layout
        ImageView imageView = findViewById(R.id.imageView4);
        TextView tvName = findViewById(R.id.tvname);
        TextView tvCategoria = findViewById(R.id.tvCategoria);
        TextView tvRS = findViewById(R.id.tv_rs);
        TextView tvRUC = findViewById(R.id.tv_ruc);
        TextView tvLF = findViewById(R.id.tv_lf);
        TextView tvPS = findViewById(R.id.tv_ps);
        TextView tvDes = findViewById(R.id.tv_des);
        TextView tvUb = findViewById(R.id.tv_ub);

        // Recupera el UID desde el Intent
        String uidRestaurante = getIntent().getStringExtra("uidRestaurante");

        if (uidRestaurante != null) {
            // Recupera el documento del restaurante desde Firestore
            db.collection("restaurant").document(uidRestaurante)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Mapea los datos del documento al layout
                            String nombreRest = documentSnapshot.getString("nombreRestaurante");
                            String categoria = documentSnapshot.getString("categoria");
                            String razonSocial = documentSnapshot.getString("razonSocial");
                            String ruc = documentSnapshot.getString("ruc");
                            String licenciaFuncionamiento = documentSnapshot.getString("licenciaFuncionamiento");
                            String permisoSanitario = documentSnapshot.getString("permisoSanitario");
                            String descripcion = documentSnapshot.getString("descripcion");
                            Double latitud = documentSnapshot.getDouble("latitud");
                            Double longitud = documentSnapshot.getDouble("longitud");
                            String urlImagen = documentSnapshot.getString("imageUrl");

                            tvName.setText(nombreRest != null ? nombreRest : "Sin nombre");
                            tvCategoria.setText(categoria != null ? categoria : "Sin categoría");
                            tvRS.setText(razonSocial != null ? razonSocial : "Sin razón social");
                            tvRUC.setText(ruc != null ? ruc : "Sin RUC");
                            tvLF.setText(licenciaFuncionamiento != null ? licenciaFuncionamiento : "Sin licencia");
                            tvPS.setText(permisoSanitario != null ? permisoSanitario : "Sin permiso");
                            tvDes.setText(descripcion != null ? descripcion : "Sin descripción");

                            if (latitud != null && longitud != null) {
                                String direccion = getAddressFromLatLng(latitud, longitud);
                                if (direccion != null && direccion.length() > 35) {
                                    tvUb.setTextSize(18);
                                } else {
                                    tvUb.setTextSize(22);
                                }
                                tvUb.setText(direccion != null ? direccion : "Ubicación no disponible");
                            } else {
                                tvUb.setText("Ubicación no disponible");
                                tvUb.setTextSize(16);
                            }

                            if (urlImagen != null && !urlImagen.isEmpty()) {
                                Glide.with(this)
                                        .load(urlImagen)
                                        .placeholder(R.drawable.logo)
                                        .error(R.drawable.logo)
                                        .into(imageView);
                            } else {
                                imageView.setImageResource(R.drawable.logo);
                            }
                        } else {
                            Toast.makeText(this, "El restaurante no existe", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Error al cargar datos: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        } else {
            Toast.makeText(this, "No se recibió el UID del restaurante", Toast.LENGTH_SHORT).show();
        }
    }

    private String getAddressFromLatLng(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                StringBuilder addressString = new StringBuilder();

                if (address.getThoroughfare() != null) {
                    addressString.append(address.getThoroughfare()).append(", ");
                }
                if (address.getLocality() != null) {
                    addressString.append(address.getLocality()).append(", ");
                }
                if (address.getAdminArea() != null) {
                    addressString.append(address.getAdminArea()).append(", ");
                }
                if (address.getCountryName() != null) {
                    addressString.append(address.getCountryName());
                }

                return addressString.toString();
            } else {
                Log.e("Geocoder", "No se encontró dirección para estas coordenadas.");
                return "No se encontró dirección.";
            }
        } catch (IOException e) {
            Log.e("Geocoder", "Error al obtener dirección: " + e.getMessage());
            return "Error al obtener dirección.";
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int menuId = menuItem.getItemId();

        if (menuId == R.id.nav_home) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (menuId == R.id.nav_pedidos) {
            startActivity(new Intent(this, PedidosActivity.class));
        } else if (menuId == R.id.nav_profile) {
            // Ya estamos en ProfileRestActivity, no hacer nada
        } else if (menuId == R.id.nav_dishes) {
            startActivity(new Intent(this, DishesActivity.class));
        } else if (menuId == R.id.nav_ganancia) {
            startActivity(new Intent(this, GananciaActivity.class));
        } else if (menuId == R.id.nav_popular) {
            startActivity(new Intent(this, StatisticsActivity.class));
        } else if (menuId == R.id.nav_users) {
            startActivity(new Intent(this, StatisticsActivity.class));
        } else if (menuId == R.id.nav_logout) {
            firebaseAuth.signOut();
            SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
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
