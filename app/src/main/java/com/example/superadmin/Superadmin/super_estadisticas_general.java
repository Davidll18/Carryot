package com.example.superadmin.Superadmin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.superadmin.LoginActivity;
import com.example.superadmin.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class super_estadisticas_general extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private FirebaseAuth auth;
    private ImageButton buttonMenu;
    private NavigationView navigationView_menu;
    private TextView repartidoresActivosText, adminRestaurantesText, saludoTextView;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_estadisticas_general);
        getWindow().setStatusBarColor(ContextCompat.getColor(super_estadisticas_general.this, R.color.red_boton));

        saludoTextView = findViewById(R.id.saludoTextView);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        drawerLayout = findViewById(R.id.drawerLayout);
        buttonMenu = findViewById(R.id.buttonMenu);
        navigationView_menu = findViewById(R.id.navigationView_menu);




        // Referencias a los TextViews
        repartidoresActivosText = findViewById(R.id.repartidoresActivosText);
        adminRestaurantesText = findViewById(R.id.adminRestaurantesText);

        // Llamar métodos para cargar datos desde Firestore
        cargarRepartidoresActivos();
        cargarAdminRestaurantes();

        buttonMenu.setOnClickListener(view -> drawerLayout.open());

        navigationView_menu.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = null;

            // Redirección basada en la selección del menú
            if (id == R.id.navGestionUsuarios) {
                intent = new Intent(super_estadisticas_general.this, super_gestion_usuarios.class);
            } else if (id == R.id.navRegistrarAdminRest) {
                intent = new Intent(super_estadisticas_general.this, Super_registro_admin_rest.class);
            } else if (id == R.id.navReporteVentas_por_rest) {
                intent = new Intent(super_estadisticas_general.this, super_gestion_rest.class);
            } else if (id == R.id.navReporteVentas) {
                intent = new Intent(super_estadisticas_general.this, super_estadisticas_general.class);
            } else if (id == R.id.navLogs) {
                intent = new Intent(super_estadisticas_general.this, super_logs.class);
            } else if (id == R.id.navlogout) {
                // Mostrar un AlertDialog antes de cerrar sesión
                mostrarDialogoCerrarSesion();
            }

            drawerLayout.closeDrawers();
            if (intent != null) {
                startActivity(intent);
            }
            return true;
        });

        // Obtener el UID del usuario desde SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
        String userUid = preferences.getString("userId", null);

        if (userUid != null) {
            // Cargar el saludo personalizado para el superadmin
            cargarSaludoPersonalizado(userUid);
        } else {
            saludoTextView.setText("No hay usuario autenticado");
        }
    }

    private void mostrarDialogoCerrarSesion() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Estás seguro de que quieres cerrar sesión?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    // Cerrar sesión con FirebaseAuth
                    auth.signOut();

                    // Eliminar SharedPreferences si es necesario
                    SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();

                    // Redirigir a la página principal
                    Intent intent = new Intent(super_estadisticas_general.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish(); // Finalizar la actividad actual
                })
                .setNegativeButton("No", null) // Cierra el diálogo sin hacer nada
                .show();
    }


    private void cargarSaludoPersonalizado(String userUid) {
        db.collection("users")
                .document(userUid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String nombre = documentSnapshot.getString("name");
                        saludoTextView.setText("Buenos días, " + nombre);
                    } else {
                        saludoTextView.setText("Buenos días, Usuario");
                    }
                })
                .addOnFailureListener(e -> saludoTextView.setText("Error al cargar saludo"));
    }

    private void cargarRepartidoresActivos() {
        db.collection("users")
                .whereEqualTo("role", "REPARTIDOR")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int count = queryDocumentSnapshots.size();
                    repartidoresActivosText.setText(String.valueOf(count));
                })
                .addOnFailureListener(e -> repartidoresActivosText.setText("Error"));
    }

    private void cargarAdminRestaurantes() {
        db.collection("users")
                .whereEqualTo("role", "ADMIN REST")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int count = queryDocumentSnapshots.size();
                    adminRestaurantesText.setText(String.valueOf(count));
                })
                .addOnFailureListener(e -> adminRestaurantesText.setText("Error"));
    }
}
