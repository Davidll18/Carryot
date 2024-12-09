package com.example.superadmin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.superadmin.dtos.User;
import com.example.superadmin.util.Constants;
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

public class Signup2Activity extends AppCompatActivity {
    private AppCompatButton btnRegistrar, btnCancelar;
    private MapView map;
    private double selectedLatitude = 0.0;
    private double selectedLongitude = 0.0;
    private Marker marker;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private FusedLocationProviderClient fusedLocationClient;

    private static final int REQUEST_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().setUserAgentValue(getPackageName());
        setContentView(R.layout.activity_sign_up2);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        btnRegistrar = findViewById(R.id.btn_siguiente);
        btnCancelar = findViewById(R.id.btn_cancelar);
        map = findViewById(R.id.map);

        initMap();
        requestLocationPermission();

        Intent intent = getIntent();
        String name = intent.getStringExtra("userName");
        String surname = intent.getStringExtra("userSurname");
        String email = intent.getStringExtra("userEmail");
        String phone = intent.getStringExtra("userPhone");
        String dni = intent.getStringExtra("userDni");
        String address = intent.getStringExtra("userAddress");
        String password = intent.getStringExtra("userPassword");

        // Al hacer click en Cancelar, regresamos a SignUpActivity
        btnCancelar.setOnClickListener(v -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("userName", name);
            returnIntent.putExtra("userSurname", surname);
            returnIntent.putExtra("userEmail", email);
            returnIntent.putExtra("userPhone", phone);
            returnIntent.putExtra("userDni", dni);
            returnIntent.putExtra("userAddress", address);
            returnIntent.putExtra("userPassword", password);
            setResult(RESULT_CANCELED, returnIntent);
            finish();
        });

        btnRegistrar.setOnClickListener(v -> {
            if (selectedLatitude == 0.0 || selectedLongitude == 0.0) {
                showCustomToast("Por favor, selecciona una ubicación en el mapa");
                return;
            }

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            String uid = firebaseUser.getUid();

                            // Obtener el nombre completo para el campo createdBy
                            String fullName = name + " " + surname;

                            // Crear el objeto User con los nuevos atributos
                            User user = User.registrousuario(
                                    name, surname, email, dni, phone, address,
                                    Constants.ROLE_CLIENTE, uid, selectedLatitude, selectedLongitude,
                                    uid, fullName  // Asignar uidCreador y createdBy con el mismo valor
                            );

                            // Guardar los datos en Firestore
                            guardarDatosEnBaseDeDatos(user);

                            // Enviar correo para cambiar la contraseña
                            enviarCorreoCambioContraseña(email);

                            showCustomToast("Cuenta registrada.");

                            // Redirigir a Login
                            Intent intentLogin = new Intent(Signup2Activity.this, LoginActivity.class);
                            startActivity(intentLogin);
                            finish();
                        } else {
                            // Si ocurre un error en la creación del usuario, lo mostramos en los logs
                            Log.e("FirebaseAuth", "Error al registrar el usuario: " + task.getException().getMessage());
                            showCustomToast("Error al registrar el usuario.");
                        }
                    });
        });
    }

    // Función para mostrar el custom toast
    private void showCustomToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        android.widget.LinearLayout layout = (android.widget.LinearLayout) inflater.inflate(R.layout.custom_toast, findViewById(R.id.toast_container));

        TextView toastText = layout.findViewById(R.id.toast_text);
        ImageView toastImage = layout.findViewById(R.id.toast_image);

        toastText.setText(message);

        // Puedes modificar la imagen si lo deseas
        // toastImage.setImageResource(R.drawable.icon_custom); // Cambiar la imagen si lo deseas

        Toast customToast = new Toast(getApplicationContext());
        customToast.setDuration(Toast.LENGTH_SHORT);
        customToast.setView(layout);
        customToast.show();
    }

    // Función para solicitar permisos de ubicación
    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            getCurrentLocation();
        }
    }

    // Función para obtener la ubicación actual
    private void getCurrentLocation() {
        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            selectedLatitude = location.getLatitude();
                            selectedLongitude = location.getLongitude();
                            GeoPoint geoPoint = new GeoPoint(selectedLatitude, selectedLongitude);
                            updateMarker(geoPoint);
                        }
                    })
                    .addOnFailureListener(this, e -> {
                        showCustomToast("Error al obtener la ubicación: ");
                    });
        } catch (SecurityException e) {
            showCustomToast("Permiso de ubicación no otorgado.");
        }
    }

    // Función para inicializar el mapa
    private void initMap() {
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        GeoPoint initialPoint = new GeoPoint(-12.046374, -77.042793);  // Ubicación inicial
        map.getController().setZoom(6.0);
        map.getController().setCenter(initialPoint);

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

    // Función para actualizar el marcador y mover el mapa hacia él
    private void updateMarker(GeoPoint geoPoint) {
        marker.setPosition(geoPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getController().setZoom(17.0);
        map.getController().setCenter(geoPoint);
    }

    // Función para guardar los datos en la base de datos Firestore
    private void guardarDatosEnBaseDeDatos(User user) {
        db.collection("users").document(user.getUid())
                .set(user)  // Guardamos el objeto 'user' en Firestore
                .addOnSuccessListener(unused -> {
                    Log.d("Firestore", "Datos guardados correctamente.");
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error al guardar los datos: " + e.getMessage());
                    showCustomToast("Error al guardar los datos.");
                });
    }

    // Función para enviar el correo de cambio de contraseña
    private void enviarCorreoCambioContraseña(String email) {
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showCustomToast("Correo enviado");
                    } else {
                        showCustomToast("Error al enviar el correo.");
                    }
                });
    }

    // Manejo de la respuesta del permiso de ubicación
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                showCustomToast("Permiso de ubicación denegado.");
            }
        }
    }
}
