package com.example.superadmin.adminrest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.superadmin.R;
import com.example.superadmin.dtos.PlatoDTO;
import com.example.superadmin.dtos.RestaurantDTO;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.UUID;

public class ProfileRestActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseStorage storage;
    private FirebaseFirestore db;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private AppCompatButton btnGuardar, btnCancelar, btnSelectImage;
    private ImageView selectedImageView;
    private Uri selectedImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restprofile);

        btnGuardar = findViewById(R.id.btn_guardar);
        btnCancelar = findViewById(R.id.btn_cancelar);
        btnSelectImage = findViewById(R.id.btn_seleccionar_foto);
        selectedImageView = findViewById(R.id.img_foto);

        // Configuración del DrawerLayout y NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        Menu menu = navigationView.getMenu();
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        // Referencias a los campos del layout
        ImageView imageView = findViewById(R.id.img_foto);
        TextView tvName = findViewById(R.id.tvname);
        TextView tvCategoria = findViewById(R.id.tvCategoria);
        TextView tvRS = findViewById(R.id.tv_rs);
        TextView tvRUC = findViewById(R.id.tv_ruc);
        TextView tvLF = findViewById(R.id.tv_lf);
        TextView tvPS = findViewById(R.id.tv_ps);
        TextView tvDes = findViewById(R.id.tv_des);
        TextView tvUb = findViewById(R.id.tv_ub);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        btnSelectImage.setOnClickListener(v -> openImagePicker());

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            // Obtener datos del usuario autenticado
            db.collection("users").document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Buscar el restaurante con el uidCreador
                            db.collection("restaurant")
                                    .whereEqualTo("uidCreador", uid)
                                    .get()
                                    .addOnSuccessListener(queryDocumentSnapshots -> {
                                        if (!queryDocumentSnapshots.isEmpty()) {
                                            DocumentSnapshot restauranteSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                            String idRestaurante = restauranteSnapshot.getString("uidCreacion");
                                            // Extraer datos del restaurante
                                            String nombreRest = restauranteSnapshot.getString("nombreRestaurante");
                                            String categoria = restauranteSnapshot.getString("categoria");
                                            String razonSocial = restauranteSnapshot.getString("razonSocial");
                                            String ruc = restauranteSnapshot.getString("ruc");
                                            String licenciaFuncionamiento = restauranteSnapshot.getString("licenciaFuncionamiento");
                                            String permisoSanitario = restauranteSnapshot.getString("permisoSanitario");
                                            String descripcion = restauranteSnapshot.getString("descripcion");
                                            String ubicacion = restauranteSnapshot.getString("location");
                                            String urlImagen = restauranteSnapshot.getString("imageUrl"); // Ruta de la imagen en Firestore

                                            // Asignar los datos a los campos
                                            tvName.setText(nombreRest != null ? nombreRest : "Sin nombre");
                                            tvCategoria.setText(categoria != null ? categoria : "Sin categoría");
                                            tvRS.setText(razonSocial != null ? razonSocial : "Sin razón social");
                                            tvRUC.setText(ruc != null ? ruc : "Sin RUC");
                                            tvLF.setText(licenciaFuncionamiento != null ? licenciaFuncionamiento : "Sin licencia");
                                            tvPS.setText(permisoSanitario != null ? permisoSanitario : "Sin permiso");
                                            tvDes.setText(descripcion != null ? descripcion : "Sin descripción");
                                            tvUb.setText(ubicacion != null ? ubicacion : "Sin ubicación");

                                            // Cargar la imagen en el ImageView usando Glide
                                            if (urlImagen != null && !urlImagen.isEmpty()) {
                                                Glide.with(this)
                                                        .load(urlImagen)
                                                        .placeholder(R.drawable.logo) // Imagen por defecto
                                                        .error(R.drawable.logo)      // Imagen si hay error
                                                        .into(imageView);
                                            }

                                            // Selección y subida de la imagen
                                            if (selectedImageUri != null) {
                                                StorageReference storageReference = storage.getReference().child("restaurant_images/" + UUID.randomUUID().toString());
                                                storageReference.putFile(selectedImageUri)
                                                        .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl()
                                                                .addOnSuccessListener(uri -> {
                                                                    String imageUrl = uri.toString();
                                                                    // Ahora guardamos el restaurante con la URL de la imagen
                                                                    btnGuardar.setOnClickListener(v -> updateImageRestaurantToFirestore(imageUrl, idRestaurante));
                                                                }))
                                                        .addOnFailureListener(e -> Toast.makeText(this, "Error al subir la imagen: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                            }
                                        } else {
                                            Toast.makeText(this, "No se encontró un restaurante para este usuario." + uid, Toast.LENGTH_SHORT).show();
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

        // Botón "Cancelar"
        btnCancelar.setOnClickListener(v -> finish());

    }

    private void updateImageRestaurantToFirestore(String imageUrl, String restaurantId) {
        // Inicializar restaurantDTO
        RestaurantDTO restaurantDTO = new RestaurantDTO();
        restaurantDTO.setImageUrl(imageUrl); // Asegúrate de que restaurantDTO tenga un setter para la imagen

        // Actualizar el restaurante en Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("restaurant").document(restaurantId).set(restaurantDTO)
                .addOnSuccessListener(aVoid -> {
                    // Mostrar mensaje de éxito
                    Toast.makeText(this, "Restaurante registrado con éxito", Toast.LENGTH_SHORT).show();

                    // Redirigir a MainActivity
                    Intent intent = new Intent(this,ProfileRestActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    // Mostrar mensaje de error
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int menu = menuItem.getItemId();
        if (menu == R.id.nav_home) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (menu == R.id.nav_pedidos) {
            startActivity(new Intent(this, PedidosActivity.class));
        } else if (menu == R.id.nav_profile) {
            startActivity(new Intent(this, ProfileRestActivity.class));
        } else if (menu == R.id.nav_dishes) {
            startActivity(new Intent(this, DishesActivity.class));
        }else if (menu == R.id.nav_ganancia) {
            startActivity(new Intent(this, GananciaActivity.class));
        }else if (menu == R.id.nav_popular) {
            startActivity(new Intent(this, StatisticsActivity.class));
        }else if (menu == R.id.nav_users) {
            startActivity(new Intent(this, StatisticsActivity.class));
        }
        drawerLayout.closeDrawer(GravityCompat.START); // Cierra el Drawer después de seleccionar el ítem
        return true;
    }
    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            selectedImageView.setImageURI(selectedImageUri); // Mostrar la imagen seleccionada
        }
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        super.onBackPressed();
    }
}
