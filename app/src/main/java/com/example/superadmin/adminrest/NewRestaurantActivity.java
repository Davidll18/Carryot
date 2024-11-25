package com.example.superadmin.adminrest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import com.example.superadmin.R;
import com.example.superadmin.dtos.RestaurantDTO;
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

import java.util.UUID;

public class NewRestaurantActivity extends AppCompatActivity {
    private EditText ed_desc;
    private MapView map;
    private AppCompatButton btnSiguiente, btnCancelar;
    private double selectedLatitude = 0.0;
    private double selectedLongitude = 0.0;
    private Marker marker;
    private FirebaseFirestore db;

    private static final int REQUEST_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configuración inicial de osmdroid
        Configuration.getInstance().setUserAgentValue(getPackageName());
        setContentView(R.layout.newrestaurant2_adminrest);

        // Inicializar UI y Firebase
        ed_desc = findViewById(R.id.ed_desc);
        map = findViewById(R.id.map);
        btnSiguiente = findViewById(R.id.btn_siguiente);
        btnCancelar = findViewById(R.id.btn_cancelar);
        db = FirebaseFirestore.getInstance();

        // Verificar permisos y configurar mapa
        checkPermissions();

        // Recibir datos dinámicos desde la primera actividad
        Intent intent = getIntent();
        String nombreRestaurante = intent.getStringExtra("nombreRestaurante");
        String categoriaRestaurante = intent.getStringExtra("categoriaRestaurante");
        String razonSocial = intent.getStringExtra("razonSocial");
        String ruc = intent.getStringExtra("ruc");
        String licenciaFuncionamiento = intent.getStringExtra("licenciaFuncionamiento");
        String permisoSanitario = intent.getStringExtra("permisoSanitario");

        // Botón "Siguiente"
        btnSiguiente.setOnClickListener(v -> {
            String descripcion = ed_desc.getText().toString().trim();
            if (descripcion.isEmpty()) {
                ed_desc.setError("Este campo no puede estar vacío");
                ed_desc.requestFocus();
                return;
            }
            if (selectedLatitude == 0.0 || selectedLongitude == 0.0) {
                Toast.makeText(this, "Por favor, selecciona una ubicación en el mapa", Toast.LENGTH_SHORT).show();
                return;
            }
            saveRestaurant(nombreRestaurante, categoriaRestaurante, razonSocial, ruc, licenciaFuncionamiento, permisoSanitario, descripcion, selectedLatitude, selectedLongitude);
        });

        // Botón "Cancelar"
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            initMap();
        }
    }

    private void initMap() {
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        // Configurar ubicación inicial (por ejemplo, centro del país)
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
            String nombreCreador = currentUser.getDisplayName() != null ? currentUser.getDisplayName() : "Creador desconocido";
            String restaurantId = UUID.randomUUID().toString();

            // Guardar datos dinámicos
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
                    restaurantId
            );

            db.collection("restaurant").document(restaurantId).set(restaurantDTO).addOnSuccessListener(aVoid -> {
                Toast.makeText(this, "Restaurante registrado con éxito", Toast.LENGTH_SHORT).show();

                // Redirigir a MainActivity
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }).addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "No hay un usuario autenticado.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initMap();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
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
}
