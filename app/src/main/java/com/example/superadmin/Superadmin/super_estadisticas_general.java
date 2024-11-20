package com.example.superadmin.Superadmin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.superadmin.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class super_estadisticas_general extends AppCompatActivity {

    DrawerLayout drawerLayout;
    FirebaseAuth auth;
    ImageButton buttonMenu;
    NavigationView navigationView_menu;
    TextView repartidoresActivosText, adminRestaurantesText, saludoTextView;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_estadisticas_general);
        getWindow().setStatusBarColor(ContextCompat.getColor(super_estadisticas_general.this,R.color.red_boton));

        saludoTextView = findViewById(R.id.saludoTextView);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        drawerLayout = findViewById(R.id.draweLayout);
        buttonMenu = findViewById(R.id.buttonMenu);
        navigationView_menu = findViewById(R.id.navigationView_menu);

        // Referencias a los TextViews
        repartidoresActivosText = findViewById(R.id.repartidoresActivosText);
        adminRestaurantesText = findViewById(R.id.adminRestaurantesText);

        // Llamar métodos para cargar datos desde Firestore
        cargarRepartidoresActivos();
        cargarAdminRestaurantes();

        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
            }
        });

        navigationView_menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navGestionUsuarios) {
                    // Ir a Gestión de Usuarios
                    startActivity(new Intent(super_estadisticas_general.this, super_gestion_usuarios.class));
                } else if (id == R.id.navRegistrarAdminRest) {
                    // Ir a Registrar Admin de Restaurante
                    startActivity(new Intent(super_estadisticas_general.this, Super_registro_admin_rest.class));
                } else if (id == R.id.navReporteVentas_por_rest) {
                    // Ir a Registrar Admin de Restaurante
                    startActivity(new Intent(super_estadisticas_general.this, super_rest.class));
                }
                else if (id == R.id.navReporteVentas) {
                    // Ir a Reporte de Ventas
                    startActivity(new Intent(super_estadisticas_general.this, super_estadisticas_general.class));
                } else if (id == R.id.navLogs) {
                    // Ir a Logs
                    startActivity(new Intent(super_estadisticas_general.this, super_logs.class));
                }

                drawerLayout.closeDrawers(); // Cierra el menú después de seleccionar un ítem
                return true;
            }
        });
        cargarSaludoPersonalizado(); // Llamar al método para mostrar el saludo
    }
    public void VerRest(View view){
        Intent m2intent = new Intent(this, super_estadisticas_por_rest.class);
        startActivity(m2intent);
    }

    private void cargarRepartidoresActivos() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("role", "REPARTIDOR") // Filtra solo los documentos con role "REPARTIDOR"
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int count = queryDocumentSnapshots.size(); // Número de documentos encontrados
                    repartidoresActivosText.setText(String.valueOf(count));
                })
                .addOnFailureListener(e -> repartidoresActivosText.setText("Error"));
    }


    private void cargarAdminRestaurantes() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("role", "ADMIN REST") // Filtra solo los documentos con role "REPARTIDOR"
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int count = queryDocumentSnapshots.size(); // Número de documentos encontrados
                    adminRestaurantesText.setText(String.valueOf(count));
                })
                .addOnFailureListener(e -> adminRestaurantesText.setText("Error"));
    }

    private void cargarSaludoPersonalizado() {
        // Verifica si hay un usuario autenticado
        if (auth.getCurrentUser() != null) {
            String userUid = auth.getCurrentUser().getUid(); // Obtén el UID del usuario autenticado

            // Busca en Firestore el documento correspondiente al UID
            db.collection("users")
                    .document(userUid) // Accede directamente al documento por UID
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Obtén el nombre del usuario
                            String nombre = documentSnapshot.getString("name");
                            saludoTextView.setText("Buenos días, " + nombre);
                        } else {
                            saludoTextView.setText("Buenos días, Usuario");
                        }
                    })
                    .addOnFailureListener(e -> {
                        saludoTextView.setText("Error al cargar saludo");
                    });
        } else {
            saludoTextView.setText("No hay usuario autenticado");
        }
    }


}