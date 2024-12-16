package com.example.superadmin.adminrest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import com.bumptech.glide.Glide;
import com.example.superadmin.LoginActivity;
import com.example.superadmin.R;
import com.example.superadmin.adminrest.Adapter.GananciaAdapter;
import com.example.superadmin.adminrest.Adapter.PedidosStatusAdapter;
import com.example.superadmin.adminrest.Adapter.PlatosEstadisticaAdapter;
import com.example.superadmin.adminrest.dto.GananciaItem;
import com.example.superadmin.adminrest.dto.PedidosStatusItem;
import com.example.superadmin.adminrest.dto.Plato;
import com.example.superadmin.adminrest.dto.PlatosEstItem;
import com.example.superadmin.dtos.Pedidos;
import com.example.superadmin.dtos.PlatoDTO;
import com.example.superadmin.dtos.RestaurantDTO;
import com.example.superadmin.util.ToolbarUtils;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;  // Declaración de FirebaseAuth
    private FirebaseFirestore db;  // Declaración de Firestore

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    TextView saludo, rest,pedAct, pedPend, cantVent1, cantVent2;
    ImageView popularDish1,popularDish2;
    LinearLayout pp, pa, pl1,pl2;

    private String restaurantUid; // UID del restaurante recuperado de la sesión

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminrest_estadisticas_general);

        // Inicializar FirebaseAuth y Firestore
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            ArrayList<Plato> platosList = new ArrayList<>();
            db.collection("users").document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {

                            // Buscar el restaurante con el uidCreador
                            db.collection("restaurant")
                                    .whereEqualTo("uidCreador", uid) // Filtrar por uidCreador
                                    .get()
                                    .addOnSuccessListener(queryDocumentSnapshots -> {
                                        if (!queryDocumentSnapshots.isEmpty()) {
                                            // Obtener el primer restaurante que coincida
                                            DocumentSnapshot restauranteSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                            String idRestaurante = restauranteSnapshot.getString("uidCreacion");

                                            // Buscar platos que correspondan al restaurante
                                            db.collection("pedidos")
                                                    .whereEqualTo("estado", "completado")  // Filtrar por estado "completado"
                                                    .get()
                                                    .addOnSuccessListener(platosQuerySnapshot -> {
                                                        if (!platosQuerySnapshot.isEmpty()) {
                                                            // Iterar sobre los documentos de la consulta
                                                            for (DocumentSnapshot pedidoSnapshot : platosQuerySnapshot.getDocuments()) {
                                                                // Extraer los campos de cada pedido
                                                                String plato1 = pedidoSnapshot.getString("plato1");
                                                                String cantidad1 = pedidoSnapshot.getString("cantidad1");
                                                                String plato2 = pedidoSnapshot.getString("plato2");
                                                                String cantidad2 = pedidoSnapshot.getString("cantidad2");
                                                                String plato3 = pedidoSnapshot.getString("plato3");
                                                                String cantidad3 = pedidoSnapshot.getString("cantidad3");

                                                                // Añadir los platos y sus cantidades a la lista
                                                                if (plato1 != null && !plato1.isEmpty()) {
                                                                    // Convertir la cantidad de String a int

                                                                    platosList.add(new Plato(plato1, cantidad1));
                                                                }
                                                                if (plato2 != null && !plato2.isEmpty()) {
                                                                    platosList.add(new Plato(plato2, cantidad2));
                                                                }
                                                                if (plato3 != null && !plato3.isEmpty()) {
                                                                    platosList.add(new Plato(plato3, cantidad3));
                                                                }
                                                                obtenerPlatosMasVendidos(platosList, idRestaurante);
                                                            }

                                                            // Mostrar la cantidad de platos encontrados
                                                            Toast.makeText(this, "Platos encontrados: " + platosList.size(), Toast.LENGTH_SHORT).show();

                                                        } else {
                                                            Toast.makeText(this, "No se encontraron platos para este restaurante.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(this, "Error al buscar los platos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    });
                                        } else {
                                            Toast.makeText(this, "No se encontró un restaurante para este usuario.", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(e ->
                                            Toast.makeText(this, "Error al buscar el restaurante: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                        } else {
                            Toast.makeText(this, "Usuario no encontrado en Firestore", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Error al obtener datos del usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "No hay un usuario autenticado.", Toast.LENGTH_SHORT).show();
        }

        // Configuración del DrawerLayout y NavigationView
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        saludo = findViewById(R.id.saludoTextView);
        rest = findViewById(R.id.restauranteTextView);

        pp = findViewById(R.id.pendientesPedidos);
        pa = findViewById(R.id.activosPedidos);

        pedPend = findViewById(R.id.pendientesText);
        pedAct = findViewById(R.id.activosText);

        cantVent1 = findViewById(R.id.cantVent1);
        cantVent2 = findViewById(R.id.cantVent2);
        popularDish1 = findViewById(R.id.imageView5);
        popularDish2 = findViewById(R.id.imageView10);

        pl1 = findViewById(R.id.platoVendido1);
        pl2 = findViewById(R.id.platoVendido2);

        setSupportActionBar(toolbar);

        pp.setOnClickListener(v ->{
            Intent intent = new Intent(MainActivity.this, PedidosActivity.class);
            startActivity(intent);
        });
        pa.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PedidosActivity.class);
            startActivity(intent);
        });

        pl1.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
            startActivity(intent);
        });
        pl2.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
            startActivity(intent);
        });



        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_logout).setVisible(false);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_home);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Recuperar sesión del usuario y obtener restaurante
        retrieveSessionAndFetchRestaurant(saludo, rest);

    }

    private void retrieveSessionAndFetchRestaurant(TextView saludo, TextView rest) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String userUid = user.getUid();

            // Consultar Firestore para buscar el restaurante del usuario
            db.collection("restaurant")
                    .whereEqualTo("uidCreador", userUid)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                RestaurantDTO restaurant = document.toObject(RestaurantDTO.class);
                                if (restaurant != null) {
                                    // Guardar el UID del restaurante en la sesión
                                    SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("restaurant_uid", restaurant.getUidCreacion());
                                    editor.putString("restaurant_name", restaurant.getNombreRestaurante());
                                    editor.apply();

                                    // Asignar el UID del restaurante a una variable local
                                    restaurantUid = restaurant.getUidCreacion();
                                    saludo.setText(restaurant.getNombreCreador());
                                    rest.setText(restaurant.getNombreRestaurante());
                                    Log.d("MainActivity", "Restaurante recuperado: " + restaurant.getNombreRestaurante());
                                }
                            }
                        } else {
                            Log.d("FirestoreError", "No se encontró restaurante para este usuario.");
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("FirestoreError", "Error al consultar el restaurante: ", e);
                    });
        } else {
            // Si no hay usuario autenticado, redirigir al login
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
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
            startActivity(new Intent(this, ProfileRestActivity.class));
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

        // Cerrar el Drawer después de seleccionar un ítem
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
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
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
    private void obtenerPlatosMasVendidos(ArrayList<Plato> platosList, String restaurantUid) {
        // Paso 1: Contabilizar las cantidades de cada plato
        Map<String, Integer> platoCountMap = new HashMap<>();

        // Contabilizamos la cantidad total de cada plato
        for (Plato plato : platosList) {
            String nombrePlato = plato.getNombrePlato();
            int cantidad = Integer.parseInt(plato.getCantidad()); // Asegúrate de que la cantidad sea un número

            // Si el plato ya está en el mapa, sumamos la cantidad
            platoCountMap.put(nombrePlato, platoCountMap.getOrDefault(nombrePlato, 0) + cantidad);
        }

        // Paso 2: Ordenar los platos por la cantidad (de mayor a menor)
        List<Map.Entry<String, Integer>> sortedPlatos = new ArrayList<>(platoCountMap.entrySet());

        // Ordenamos en orden descendente por la cantidad
        sortedPlatos.sort((entry1, entry2) -> entry2.getValue() - entry1.getValue());

        // Paso 3: Obtener los dos platos más vendidos
        if (sortedPlatos.size() >= 2) {
            Map.Entry<String, Integer> platoMasVendido1 = sortedPlatos.get(0);
            Map.Entry<String, Integer> platoMasVendido2 = sortedPlatos.get(1);
            String nombrePlato1 = platoMasVendido1.getKey(); // Nombre del primer plato
            int cantidadVentas1 = platoMasVendido1.getValue(); // Cantidad de ventas del primer plato

            String nombrePlato2 = platoMasVendido2.getKey(); // Nombre del segundo plato
            int cantidadVentas2 = platoMasVendido2.getValue(); // Cantidad de ventas del segundo plato

            // Ahora puedes usar estos valores para actualizar los TextViews
            cantVent1.setText(String.valueOf(cantidadVentas1)); // Mostrar la cantidad de ventas del primer plato
            cantVent2.setText(String.valueOf(cantidadVentas2)); // Mostrar la cantidad de ventas del segundo plato

            obtener_imagen(nombrePlato1, nombrePlato2, restaurantUid);

        } else {
            Log.d("PlatosVendidos", "No hay suficientes platos para determinar los más vendidos.");
        }
    }

    private void obtener_imagen(String nombrePlato1, String nombrePlato2, String idRestaurante) {
        // Accedemos a la colección de platos en Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Referencia a la colección "platos"
        CollectionReference platosRef = db.collection("platos");

        // Consultamos los platos con el idRestaurante y los nombres de los platos
        platosRef.whereEqualTo("idRestaurante", idRestaurante)
                .whereIn("nombrePlato", Arrays.asList(nombrePlato1, nombrePlato2))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Iteramos a través de los documentos encontrados
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Obtenemos el nombre del plato y la imagen
                            String nombrePlato = document.getString("nombrePlato");
                            String imageUrl = document.getString("imageUrl");

                            // Si el plato es el nombrePlato1, lo asignamos a la vista correspondiente
                            if (nombrePlato.equals(nombrePlato1)) {
                                // Mostrar la imagen del plato1
                                cargarImagenEnVista(imageUrl, popularDish1);
                            }
                            // Si el plato es el nombrePlato2, lo asignamos a la vista correspondiente
                            if (nombrePlato.equals(nombrePlato2)) {
                                // Mostrar la imagen del plato2
                                cargarImagenEnVista(imageUrl, popularDish2);
                            }
                        }
                    } else {
                        Log.d("Firestore", "Error getting documents: ", task.getException());
                    }
                });
    }

    // Función para cargar la imagen en un ImageView usando Glide o Picasso
    private void cargarImagenEnVista(String imageUrl, ImageView imageView) {
        // Usando Glide (puedes usar Picasso también si prefieres)
        Glide.with(this)
                .load(imageUrl)
                .into(imageView);
    }

}
