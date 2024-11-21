package com.example.superadmin.adminrest;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import com.example.superadmin.adminrest.dto.RestaurantDTO;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.superadmin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class RestaurantActivity extends AppCompatActivity {
    private EditText edName, edRs, edRuc, edFunc, edSanit, edDescripcion, edUbicacion;
    private Spinner spinnerCategoria;
    private AppCompatButton btnSiguiente, btnSelectImage;
    private FirebaseFirestore db;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private FirebaseStorage storage;
    private ImageView imageView;
    private String categoriaRestaurante, uidCreador, nombreCreador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newrestaurant_adminrest);

        // Inicialización de campos
        edName = findViewById(R.id.ed_name);
        spinnerCategoria = findViewById(R.id.spinner_categoria);
        edRs = findViewById(R.id.ed_rs);
        edRuc = findViewById(R.id.ed_ruc);
        edFunc = findViewById(R.id.ed_func);
        edSanit = findViewById(R.id.ed_sanit);
        edDescripcion = findViewById(R.id.ed_desc);
        edUbicacion = findViewById(R.id.ed_loc);
        imageView = findViewById(R.id.img_foto);

        // Configuración del Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.categorias_restaurantes,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);

        // Inicialización de Firebase
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // Selección de imagen
        btnSelectImage = findViewById(R.id.btn_seleccionar_foto);
        btnSelectImage.setOnClickListener(v -> selectImage());

        // Botón siguiente
        btnSiguiente = findViewById(R.id.btn_siguiente);
        btnSiguiente.setOnClickListener(v -> {
            String nombreRestaurante = edName.getText().toString();
            String razonSocial = edRs.getText().toString();
            String ruc = edRuc.getText().toString();
            String licenciaFuncionamiento = edFunc.getText().toString();
            String permisoSanitario = edSanit.getText().toString();
            String descripcion = edDescripcion.getText().toString();
            String location = edUbicacion.getText().toString();
            categoriaRestaurante = spinnerCategoria.getSelectedItem().toString();

            // Llamar al método de validación y guardar los datos
            validarYGuardarDatos(nombreRestaurante, razonSocial, ruc, licenciaFuncionamiento, permisoSanitario, descripcion, location);
        });

        // Crear canal de notificaciones
        createNotificationChannel();
    }

    // Validar los datos y guardarlos en Firestore
    private void validarYGuardarDatos(String nombreRestaurante, String razonSocial, String ruc, String licenciaFuncionamiento, String permisoSanitario, String descripcion, String ubicacion) {
        if (nombreRestaurante.isEmpty() || razonSocial.isEmpty() || ruc.isEmpty() || licenciaFuncionamiento.isEmpty() || permisoSanitario.isEmpty() || descripcion.isEmpty() || ubicacion.isEmpty()) {
            showErrorDialog("Todos los campos deben ser completados");
            return;
        }

        // Obtener usuario autenticado
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            uidCreador = currentUser.getUid();
            // Obtener el nombre del usuario o, en su defecto, el correo electrónico
            nombreCreador = currentUser.getDisplayName() != null ? currentUser.getDisplayName() : currentUser.getEmail() != null ? currentUser.getEmail() : "Usuario desconocido";

            String uidCreacion = UUID.randomUUID().toString();

            // Subir la imagen y guardar los datos
            if (imageUri != null) {
                uploadImageToFirebase(imageUri, uidCreacion);
            }
        } else {
            showErrorDialog("No hay usuario autenticado");
        }
    }

    // Método para mostrar un dialogo de error
    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("Aceptar", null)
                .show();
    }

    // Subir imagen a Firebase Storage
    private void uploadImageToFirebase(Uri imageUri, String uidCreacion) {
        StorageReference storageReference = storage.getReference();
        StorageReference fileReference = storageReference.child("restaurant_images/" + uidCreacion + ".jpg");

        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    saveRestaurantData(uidCreacion, uri.toString());
                }))
                .addOnFailureListener(e -> showErrorDialog("Error al subir la imagen: " + e.getMessage()));
    }

    // Guardar los datos del restaurante en Firestore
    private void saveRestaurantData(String uidCreacion, String imageUrl) {
        String nombreRestaurante = edName.getText().toString();
        String razonSocial = edRs.getText().toString();
        String ruc = edRuc.getText().toString();
        String licenciaFuncionamiento = edFunc.getText().toString();
        String permisoSanitario = edSanit.getText().toString();
        String descripcion = edDescripcion.getText().toString();
        String location = edUbicacion.getText().toString();

        RestaurantDTO restaurantDTO = new RestaurantDTO(
                nombreRestaurante,
                categoriaRestaurante,
                razonSocial,
                ruc,
                licenciaFuncionamiento,
                permisoSanitario,
                uidCreador,
                nombreCreador,
                uidCreacion,
                imageUrl,
                descripcion,
                location);

        db.collection("restaurant").document(uidCreacion)
                .set(restaurantDTO)
                .addOnSuccessListener(documentReference -> {
                    getSharedPreferences("AppPrefs", MODE_PRIVATE).edit().putString("restaurant_uid", uidCreacion).apply();
                    showToast("Restaurante registrado exitosamente");
                    lanzarNotificacion(nombreRestaurante);
                    startActivity(new Intent(RestaurantActivity.this, MainActivity.class));
                })
                .addOnFailureListener(e -> showErrorDialog("Error al registrar en Firestore: " + e.getMessage()));
    }

    // Seleccionar imagen
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Manejar el resultado de la selección de imagen
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    // Solicitar permisos
    public void askPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(RestaurantActivity.this, new String[]{POST_NOTIFICATIONS}, 101);
        }
    }

    // Crear el canal de notificaciones
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channelDefaultPri", "Canal notificaciones default", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Canal para notificaciones con prioridad default");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            askPermission();
        }
    }

    // Enviar notificación
    private void lanzarNotificacion(String nombreRestaurante) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channelDefaultPri")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Nuevo Restaurante Registrado")
                .setContentText("¡El restaurante " + nombreRestaurante + " ha sido registrado correctamente!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(0, builder.build());
    }

    // Mostrar mensaje
    private void showToast(String message) {
        Toast.makeText(RestaurantActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
