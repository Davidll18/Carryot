package com.example.superadmin.Superadmin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superadmin.LoginActivity;
import com.example.superadmin.R;
import com.example.superadmin.adapters.AdminRestAdapter;
import com.example.superadmin.dtos.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class super_gestion_usuarios extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdminRestAdapter adminRestAdapter;
    private List<User> userList = new ArrayList<>();
    private List<User> filteredUserList = new ArrayList<>(); // Lista filtrada
    private DrawerLayout drawerLayout;
    private ImageButton buttonMenu;
    private NavigationView navigationView_menu;
    private FirebaseAuth auth;

    private Spinner spinnerFiltro1, spinnerFiltro2;  // Spinners para los filtros

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_gestion_usuarios);

        // Inicializar vistas
        drawerLayout = findViewById(R.id.drawerLayout_super_getion_usuarios);
        buttonMenu = findViewById(R.id.buttonMenu);
        recyclerView = findViewById(R.id.recyclerViewAdmins);
        navigationView_menu = findViewById(R.id.navigationView_menu);
        spinnerFiltro1 = findViewById(R.id.spinnerFiltro1);  // Filtro por rol
        spinnerFiltro2 = findViewById(R.id.spinnerFiltro2);  // Filtro por estado
        auth = FirebaseAuth.getInstance();

        // Configurar RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adminRestAdapter = new AdminRestAdapter(filteredUserList, this::onUserClicked);
        recyclerView.setAdapter(adminRestAdapter);

        // Configurar botón de menú
        buttonMenu.setOnClickListener(view -> drawerLayout.open());
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.light_orange));

        // Configurar menú de navegación
        navigationView_menu.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = null;

            if (id == R.id.navGestionUsuarios) {
                intent = new Intent(this, super_gestion_usuarios.class);
            } else if (id == R.id.navRegistrarAdminRest) {
                intent = new Intent(this, Super_registro_admin_rest.class);
            } else if (id == R.id.navReporteVentas_por_rest) {
                intent = new Intent(this, super_gestion_rest.class);
            } else if (id == R.id.navReporteVentas) {
                intent = new Intent(this, super_estadisticas_general.class);
            } else if (id == R.id.navLogs) {
                intent = new Intent(this, super_logs.class);
            } else if (id == R.id.navlogout) {
                mostrarDialogoCerrarSesion();
            }

            drawerLayout.closeDrawers();
            if (intent != null) {
                startActivity(intent);
            }
            return true;
        });

        // Cargar datos de usuarios desde Firestore
        loadUsersFromFirestore();

        // Configurar filtros
        configurarFiltros();
    }

    private void loadUsersFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Log.e("Firestore", "Error al escuchar cambios: ", e);
                        return;
                    }

                    if (queryDocumentSnapshots != null) {
                        userList.clear(); // Limpiar la lista antes de actualizarla
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            User user = document.toObject(User.class);
                            if (user != null) {
                                user.setUid(document.getId());
                                userList.add(user);
                            }
                        }
                        filteredUserList.clear();
                        filteredUserList.addAll(userList); // Copiar todos los usuarios a la lista filtrada
                        adminRestAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void configurarFiltros() {
        // Filtro por rol
        spinnerFiltro1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position != 0) {  // Si no se selecciona el valor por defecto
                    // Deshabilitar filtro por estado
                    spinnerFiltro2.setEnabled(false);
                    String selectedRole = parentView.getItemAtPosition(position).toString();
                    filtrarPorRol(selectedRole);  // Filtrar por rol
                } else {
                    // Si no se selecciona un filtro, habilitar filtro por estado
                    spinnerFiltro2.setEnabled(true);
                    filteredUserList.clear();
                    filteredUserList.addAll(userList); // Mostrar todos los usuarios
                    adminRestAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        // Filtro por estado
        spinnerFiltro2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position != 0) {  // Si no se selecciona el valor por defecto
                    // Deshabilitar filtro por rol
                    spinnerFiltro1.setEnabled(false);
                    String selectedStatus = parentView.getItemAtPosition(position).toString();
                    filtrarPorEstado(selectedStatus); // Filtrar por estado
                } else {
                    // Si no se selecciona un filtro, habilitar filtro por rol
                    spinnerFiltro1.setEnabled(true);
                    filteredUserList.clear();
                    filteredUserList.addAll(userList); // Mostrar todos los usuarios
                    adminRestAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
    }

    private void filtrarPorRol(String role) {
        filteredUserList.clear();
        for (User user : userList) {
            if (user.getRole().equals(role)) { // Filtrar por rol
                filteredUserList.add(user);
            }
        }
        adminRestAdapter.notifyDataSetChanged(); // Actualizar la lista
    }

    private void filtrarPorEstado(String status) {
        filteredUserList.clear();
        boolean isEnabled = status.equals("Activo");
        for (User user : userList) {
            if (user.getStatus() == isEnabled) { // Comparar con el estado
                filteredUserList.add(user);
            }
        }
        adminRestAdapter.notifyDataSetChanged(); // Actualizar la lista
    }

    private void onUserClicked(User user) {
        // Enviar el UID del usuario a la actividad super_habilitar_usuarios
        Intent intent = new Intent(this, super_habilitar_usuarios.class);
        intent.putExtra("userId", user.getUid());
        startActivity(intent);
    }

    private void mostrarDialogoCerrarSesion() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Estás seguro de que quieres cerrar sesión?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    // Cerrar sesión con FirebaseAuth
                    auth.signOut();

                    // Eliminar SharedPreferences
                    SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();

                    // Redirigir a la página de inicio de sesión
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }
}
