package com.example.superadmin.adminrest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import com.example.superadmin.R;
import com.example.superadmin.util.UtilsRandom;
import com.example.superadmin.dtos.RestaurantDTO;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.example.superadmin.util.KeyboardUtils;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class NewRestaurantActivity extends AppCompatActivity {
    private EditText edName, edRs, edRuc, edFunc, edSanit,ed_desc;
    private MapView map;
    private Spinner spinnerCategoria;
    private AppCompatButton btnGuardar, btnCancelar, btnSelectImage;
    private double selectedLatitude = 0.0;
    private double selectedLongitude = 0.0;
    private Marker marker;
    private FirebaseFirestore db;
    private ImageView selectedImageView;
    private Uri selectedImageUri;
    private FirebaseStorage storage;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configuración inicial de osmdroid
        Configuration.getInstance().setUserAgentValue(getPackageName());
        setContentView(R.layout.newrestaurant_adminrest);

        // Inicializar UI y Firebase
        edName = findViewById(R.id.ed_name);
        spinnerCategoria = findViewById(R.id.spinner_categoria); // Referencia al Spinner
        edRs = findViewById(R.id.ed_rs);
        edRuc = findViewById(R.id.ed_ruc);
        edFunc = findViewById(R.id.ed_func);
        edSanit = findViewById(R.id.ed_sanit);
        ed_desc = findViewById(R.id.ed_desc);
        map = findViewById(R.id.map);
        btnGuardar = findViewById(R.id.btn_guardar);
        btnCancelar = findViewById(R.id.btn_cancelar);
        btnSelectImage = findViewById(R.id.btn_seleccionar_foto);
        selectedImageView = findViewById(R.id.img_foto);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // Configurar el Spinner con el adaptador
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categorias_restaurantes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);

        btnSelectImage.setOnClickListener(v -> openImagePicker());
        // Inicializar FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Verificar permisos y configurar mapa
        checkPermissions();



        // Botón "Siguiente"
        btnGuardar.setOnClickListener(v -> {
            String nombreRestaurante = edName.getText().toString();
            String categoriaRestaurante = spinnerCategoria.getSelectedItem().toString(); // Obtener categoría seleccionada
            String razonSocial = edRs.getText().toString();
            String ruc = edRuc.getText().toString();
            String licenciaFuncionamiento = edFunc.getText().toString();
            String permisoSanitario = edSanit.getText().toString();
            String descripcion = ed_desc.getText().toString().trim();
            validarDatos(nombreRestaurante, categoriaRestaurante, razonSocial, ruc, licenciaFuncionamiento, permisoSanitario,descripcion);
            if (selectedLatitude == 0.0 || selectedLongitude == 0.0) {
                Toast.makeText(this, "Por favor, selecciona una ubicación en el mapa", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedImageUri != null) {
                saveRestaurant(nombreRestaurante, categoriaRestaurante, razonSocial, ruc, licenciaFuncionamiento, permisoSanitario, descripcion, selectedLatitude, selectedLongitude);
            } else {
                Toast.makeText(this, "Por favor, selecciona una imagen", Toast.LENGTH_SHORT).show();
            }

        });

        // Botón "Cancelar"
        btnCancelar.setOnClickListener(v -> finish());
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

    private void validarDatos(String nombreRestaurante, String categoriaRestaurante, String razonSocial, String ruc, String licenciaFuncionamiento, String permisoSanitario, String descripcion) {
        // Validar que los campos no estén vacío
        if (nombreRestaurante.isEmpty() || categoriaRestaurante.isEmpty() || razonSocial.isEmpty() || ruc.isEmpty() || licenciaFuncionamiento.isEmpty() || permisoSanitario.isEmpty() || descripcion.isEmpty()) {
            // Mostrar el Custom Toast
            showCustomToast("Todos los campos deben ser completados");
            return;
        }
    }
    // Método para mostrar el Custom Toast
    private void showCustomToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (LinearLayout) findViewById(R.id.toast_container));

        TextView toastText = layout.findViewById(R.id.toast_text);
        toastText.setText(message); // Set the custom message

        ImageView toastImage = layout.findViewById(R.id.toast_image);
        toastImage.setImageResource(R.drawable.shopping_bag); // Set your image here

        Toast customToast = new Toast(getApplicationContext());
        customToast.setDuration(Toast.LENGTH_SHORT);
        customToast.setView(layout); // Set the custom view for the toast
        customToast.show();
    }
    private void checkPermissions() {
        // Verificar permisos de ubicación
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            // Si no tenemos permisos, solicitarlos
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            // Si ya tenemos permisos, inicializamos el mapa
            initMap();
            getCurrentLocation(); // Obtener la ubicación actual del usuario
        }
    }

    // Este método se invoca cuando el usuario responde a la solicitud de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Si el permiso es concedido, inicializamos el mapa y obtenemos la ubicación
                initMap();
                getCurrentLocation();
            } else {
                // Si el permiso es denegado, mostramos un mensaje y cerramos la actividad
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                // Si encontramos la ubicación, actualizar las coordenadas
                                selectedLatitude = location.getLatitude();
                                selectedLongitude = location.getLongitude();

                                // Actualizar mapa y colocar marcador en la ubicación del usuario
                                GeoPoint geoPoint = new GeoPoint(selectedLatitude, selectedLongitude);
                                updateMarker(geoPoint);
                            }
                        }
                    });
        }
    }

    private void initMap() {
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        // Configurar ubicación inicial (por ejemplo, centro del país si no obtenemos ubicación)
        GeoPoint initialPoint = new GeoPoint(-12.046374, -77.042793); // Cambia por las coordenadas de tu país
        map.getController().setZoom(6.0); // Zoom inicial para ver el país
        map.getController().setCenter(initialPoint);

        // Configurar marcador
        marker = new Marker(map);
        marker.setIcon(getDrawable(R.drawable.baseline_add_location_24));
        map.getOverlayManager().add(marker);

        map.getOverlays().add(new Overlay() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e, MapView mapView) {
                Projection projection = mapView.getProjection();
                GeoPoint geoPoint = (GeoPoint) projection.fromPixels((int) e.getX(), (int) e.getY());
                selectedLatitude = geoPoint.getLatitude();
                selectedLongitude = geoPoint.getLongitude();
                updateMarker(geoPoint);
                return true;
            }
        });
    }

    private void updateMarker(GeoPoint geoPoint) {
        marker.setPosition(geoPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getController().setZoom(17.0); // Zoom más cercano tras seleccionar una ubicación
        map.getController().setCenter(geoPoint);
    }

    private void saveRestaurant(String nombreRestaurante, String categoriaRestaurante, String razonSocial,
                                String ruc, String licenciaFuncionamiento, String permisoSanitario,
                                String descripcion, double latitude, double longitude) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uidCreador = currentUser.getUid();

            // Consultar la colección "users" en Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(uidCreador)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Obtenemos el nombre y apellido desde Firestore
                            String name = documentSnapshot.getString("name");
                            String surname = documentSnapshot.getString("surname");

                            // Si el nombre y apellido están disponibles, los concatenamos
                            String nombreCreador = (name != null ? name : "") + " " + (surname != null ? surname : "");
                            StorageReference storageReference = storage.getReference().child("restaurant_images/" + UUID.randomUUID().toString());

                            String costoDelivery = UtilsRandom.generateRandomCost(5, 9);
                            String rateRest = UtilsRandom.generateRandomCost(3, 5);
                            String tiempoEspera = String.valueOf(UtilsRandom.generateRandomInt(20, 40));
                            Timestamp fechaHoraCreacion = com.google.firebase.Timestamp.now();


                            storageReference.putFile(selectedImageUri)
                                    .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl()
                                            .addOnSuccessListener(uri -> {
                                                String imageUrl = uri.toString();
                                                // Ahora guardamos el restaurante con el nombre completo del creador
                                                saveRestaurantToFirestore(nombreRestaurante, categoriaRestaurante, razonSocial, ruc,
                                                        licenciaFuncionamiento, permisoSanitario, descripcion, latitude, longitude,
                                                        uidCreador, nombreCreador, imageUrl,costoDelivery,rateRest,tiempoEspera,fechaHoraCreacion);
                                            }))
                                    .addOnFailureListener(e -> Toast.makeText(this, "Error al subir la imagen: " + e.getMessage(), Toast.LENGTH_SHORT).show());

                        } else {
                            Toast.makeText(this, "Usuario no encontrado en Firestore", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error al obtener datos del usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "No hay un usuario autenticado.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveRestaurantToFirestore(String nombreRestaurante, String categoriaRestaurante, String razonSocial,
                                           String ruc, String licenciaFuncionamiento, String permisoSanitario,
                                           String descripcion, double latitude, double longitude,
                                           String uidCreador, String nombreCreador, String imageUrl, String costoDelivery,
                                           String rateRest, String tiempoEspera, Timestamp fechaHoraCreacion) {

        String restaurantId = UUID.randomUUID().toString();

        // Guardar datos del restaurante en Firestor
        RestaurantDTO restaurantDTO = new RestaurantDTO(
                nombreRestaurante,
                categoriaRestaurante,
                razonSocial,
                ruc,
                licenciaFuncionamiento,
                permisoSanitario,
                descripcion,
                latitude,
                longitude,
                uidCreador,
                nombreCreador,
                restaurantId,
                imageUrl,
                costoDelivery,
                rateRest,
                tiempoEspera,
                fechaHoraCreacion
        );

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("restaurant").document(restaurantId).set(restaurantDTO)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Restaurante registrado con éxito", Toast.LENGTH_SHORT).show();

                    // Redirigir a MainActivity
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (map != null) {
            map.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (map != null) {
            map.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        map.onDetach();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return KeyboardUtils.hideKeyboardOnTouch(this, event) || super.dispatchTouchEvent(event);
    }
}
