package com.example.superadmin.adminrest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.example.superadmin.util.UtilsRandom;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import com.example.superadmin.util.KeyboardUtils;
import com.google.firebase.storage.StorageReference;

public class ProfileRestActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private ImageView imageView;
    private ImageButton btnEditInfo;

    private TextView tvName, tvRS, tvRUC, tvLF, tvDes, tvCategoria, tvUb, tvPS;
    private EditText etName, etRS, etRUC, etLF, etDes;

    private boolean isEditing = false; // Indica si estamos en modo edición
    private String uidRestaurante;

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
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_profile);

        // Inicializar vistas
        imageView = findViewById(R.id.imageView4);
        btnEditInfo = findViewById(R.id.btn_edit_info);

        tvName = findViewById(R.id.tvname);
        tvCategoria = findViewById(R.id.tvCategoria);
        tvRS = findViewById(R.id.tv_rs);
        tvRUC = findViewById(R.id.tv_ruc);
        tvLF = findViewById(R.id.tv_lf);
        tvDes = findViewById(R.id.tv_des);
        tvUb = findViewById(R.id.tv_ub);
        tvPS = findViewById(R.id.tv_ps);

        etName = findViewById(R.id.et_name);
        etRS = findViewById(R.id.et_rs);
        etRUC = findViewById(R.id.et_ruc);
        etLF = findViewById(R.id.et_lf);
        etDes = findViewById(R.id.et_des);


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uidCreador = currentUser.getUid();

            // Consultar la colección "users" en Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String uid = currentUser.getUid();
            db.collection("restaurant")
                    .whereEqualTo("uidCreador", uid) // Filtrar por uidCreador
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Obtener el primer restaurante que coincida
                            DocumentSnapshot restauranteSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            String uidRestaurante = restauranteSnapshot.getString("uidCreacion");
                            if (uidRestaurante != null) {
                                // Mostrar información del restaurante
                                displayRestaurantProfile(uidRestaurante);
                            } else {
                                Toast.makeText(this, "No se encontró el uidRestaurante en el restaurante.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "No se encontró un restaurante para este usuario." , Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Error al buscar el restaurante: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "No hay un usuario autenticado.", Toast.LENGTH_SHORT).show();
        }



        // Configurar botón editar/guardar
        btnEditInfo.setOnClickListener(v -> toggleEditSaveMode(uidRestaurante));
    }

    private void displayRestaurantProfile(String idRestaurante) {
        if (idRestaurante != null) {
            db.collection("restaurant").document(idRestaurante)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            tvName.setText(documentSnapshot.getString("nombreRestaurante"));
                            etName.setText(documentSnapshot.getString("nombreRestaurante"));

                            tvRS.setText(documentSnapshot.getString("razonSocial"));
                            etRS.setText(documentSnapshot.getString("razonSocial"));

                            tvRUC.setText(documentSnapshot.getString("ruc"));
                            etRUC.setText(documentSnapshot.getString("ruc"));

                            tvLF.setText(documentSnapshot.getString("licenciaFuncionamiento"));
                            etLF.setText(documentSnapshot.getString("licenciaFuncionamiento"));

                            tvDes.setText(documentSnapshot.getString("descripcion"));
                            etDes.setText(documentSnapshot.getString("descripcion"));

                            tvCategoria.setText(documentSnapshot.getString("categoria"));
                            tvPS.setText(documentSnapshot.getString("permisoSanitario"));

                            // Manejo de ubicación y ajuste de tamaño de texto
                            Double latitud = documentSnapshot.getDouble("latitud");
                            Double longitud = documentSnapshot.getDouble("longitud");

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

                            String urlImagen = documentSnapshot.getString("imageUrl");
                            Glide.with(this).load(urlImagen).placeholder(R.drawable.logo).into(imageView);
                        }
                    })

                    .addOnFailureListener(e -> showCustomToast("Error al cargar datos."));
        } else {
            showCustomToast("No se recibió el UID del restaurante");
        }
    }


    private void toggleEditSaveMode(String idRest) {
        if (isEditing) {
            // Validar los campos editables antes de guardar
            if (isAnyFieldEmpty()) {
                showCustomToast("Todos los campos deben estar completos.");
                return;
            }

            // Guardar cambios si la validación pasa
            Map<String, Object> updates = new HashMap<>();
            updates.put("nombreRestaurante", etName.getText().toString().trim());
            updates.put("razonSocial", etRS.getText().toString().trim());
            updates.put("ruc", etRUC.getText().toString().trim());
            updates.put("licenciaFuncionamiento", etLF.getText().toString().trim());
            updates.put("descripcion", etDes.getText().toString().trim());

            db.collection("restaurant").document(idRest)
                    .update(updates)
                    .addOnSuccessListener(aVoid -> {
                        // Actualizar los TextView con los nuevos valores
                        tvName.setText(etName.getText().toString().trim());
                        tvRS.setText(etRS.getText().toString().trim());
                        tvRUC.setText(etRUC.getText().toString().trim());
                        tvLF.setText(etLF.getText().toString().trim());
                        tvDes.setText(etDes.getText().toString().trim());

                        showCustomToast("Cambios guardados exitosamente.");
                        switchToViewMode(); // Cambiar a modo vista
                    })
                    .addOnFailureListener(e -> showCustomToast("Error al guardar los cambios."));

            btnEditInfo.setImageResource(R.drawable.edit_icon); // Cambiar el ícono de nuevo a "editar"
            isEditing = false;
        } else {
            // Activar modo edición
            switchToEditMode();
            btnEditInfo.setImageResource(android.R.drawable.ic_menu_save); // Cambiar el ícono a "guardar"
            isEditing = true;
        }
    }

    private boolean isAnyFieldEmpty() {
        if (etName.getText().toString().trim().isEmpty()) {
            etName.requestFocus();
            return true;
        }
        if (etRS.getText().toString().trim().isEmpty()) {
            etRS.requestFocus();
            return true;
        }
        if (etRUC.getText().toString().trim().isEmpty()) {
            etRUC.requestFocus();
            return true;
        }
        if (etLF.getText().toString().trim().isEmpty()) {
            etLF.requestFocus();
            return true;
        }
        if (etDes.getText().toString().trim().isEmpty()) {
            etDes.requestFocus();
            return true;
        }
        return false;
    }



    private void switchToEditMode() {

        findViewById(R.id.lyt_name).setBackgroundColor(getResources().getColor(R.color.naranja_suave));
        findViewById(R.id.lyt_rs).setBackgroundColor(getResources().getColor(R.color.naranja_suave));
        findViewById(R.id.lyt_ruc).setBackgroundColor(getResources().getColor(R.color.naranja_suave));
        findViewById(R.id.lyt_licen).setBackgroundColor(getResources().getColor(R.color.naranja_suave));
        findViewById(R.id.lyt_desc).setBackgroundColor(getResources().getColor(R.color.naranja_suave));

        tvName.setVisibility(View.GONE);
        tvRS.setVisibility(View.GONE);
        tvRUC.setVisibility(View.GONE);
        tvLF.setVisibility(View.GONE);
        tvDes.setVisibility(View.GONE);

        etName.setVisibility(View.VISIBLE);
        etRS.setVisibility(View.VISIBLE);
        etRUC.setVisibility(View.VISIBLE);
        etLF.setVisibility(View.VISIBLE);
        etDes.setVisibility(View.VISIBLE);


    }

    private void switchToViewMode() {


        findViewById(R.id.lyt_name).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        findViewById(R.id.lyt_rs).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        findViewById(R.id.lyt_ruc).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        findViewById(R.id.lyt_licen).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        findViewById(R.id.lyt_desc).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        etName.setVisibility(View.GONE);
        etRS.setVisibility(View.GONE);
        etRUC.setVisibility(View.GONE);
        etLF.setVisibility(View.GONE);
        etDes.setVisibility(View.GONE);

        tvName.setVisibility(View.VISIBLE);
        tvRS.setVisibility(View.VISIBLE);
        tvRUC.setVisibility(View.VISIBLE);
        tvLF.setVisibility(View.VISIBLE);
        tvDes.setVisibility(View.VISIBLE);


    }

    private String getAddressFromLatLng(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                StringBuilder addressString = new StringBuilder();
                if (address.getThoroughfare() != null) addressString.append(address.getThoroughfare()).append(", ");
                if (address.getLocality() != null) addressString.append(address.getLocality()).append(", ");
                if (address.getAdminArea() != null) addressString.append(address.getAdminArea()).append(", ");
                if (address.getCountryName() != null) addressString.append(address.getCountryName());
                return addressString.toString();
            }
            return "No se encontró dirección.";
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
            mostrarDialogoCerrarSesion();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private void mostrarDialogoCerrarSesion() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Estás seguro de que quieres cerrar sesión?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    // Cerrar sesión con FirebaseAuth
                    firebaseAuth.signOut();

                    // Eliminar SharedPreferences
                    SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();

                    // Redirigir al LoginActivity
                    Intent intent = new Intent(ProfileRestActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", null) // Cierra el diálogo sin hacer nada
                .show();
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void showCustomToast(String message) {

        View toastLayout = getLayoutInflater().inflate(R.layout.custom_toast, findViewById(R.id.toast_container));

        // Configurar el texto del mensaje
        TextView toastText = toastLayout.findViewById(R.id.toast_text);
        toastText.setText(message);

        // Crear el Toast
        Toast customToast = new Toast(getApplicationContext());
        customToast.setDuration(Toast.LENGTH_SHORT);
        customToast.setView(toastLayout);
        customToast.show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return KeyboardUtils.hideKeyboardOnTouch(this, event) || super.dispatchTouchEvent(event);
    }
}
