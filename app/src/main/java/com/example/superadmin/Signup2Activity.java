

package com.example.superadmin;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.superadmin.dtos.User;
import com.example.superadmin.util.Constants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;

import java.util.UUID;

public class Signup2Activity extends AppCompatActivity {
    private AppCompatButton btnRegistrar, btnCancelar, btnSeleccionarImagen;
    private MapView map;
    private double selectedLatitude = 0.0;
    private double selectedLongitude = 0.0;
    private Marker marker;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private FusedLocationProviderClient fusedLocationClient;
    private Uri imageUri;
    private ImageView imageView;
    private Spinner spinnerRole;  // Spinner para elegir el rol del usuario

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final int PICK_IMAGE_REQUEST = 1001;
    private static final int CAMERA_REQUEST_CODE = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().setUserAgentValue(getPackageName());
        setContentView(R.layout.activity_sign_up2);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        btnRegistrar = findViewById(R.id.btn_siguiente);
        btnCancelar = findViewById(R.id.btn_cancelar);
        btnSeleccionarImagen = findViewById(R.id.btn_seleccionar_foto); // Nuevo botón
        imageView = findViewById(R.id.img_foto);
        map = findViewById(R.id.map);
        spinnerRole = findViewById(R.id.spinner_rol);  // Referencia al spinner de rol
        // Configurar el Spinner con los roles
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.escoger_rol, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);
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

        btnCancelar.setOnClickListener(v -> finish());

        btnSeleccionarImagen.setOnClickListener(v -> openImageOptions());

        btnRegistrar.setOnClickListener(v -> {
            if (selectedLatitude == 0.0 || selectedLongitude == 0.0) {
                showCustomToast("Por favor, selecciona una ubicación en el mapa");
                return;
            }

            if (imageUri == null) {
                showCustomToast("Selecciona una imagen antes de registrar");
                return;
            }

            // Obtener el rol seleccionado desde el Spinner
            String selectedRole = spinnerRole.getSelectedItem().toString();  // Aquí obtenemos el valor del spinner

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            String uid = firebaseUser.getUid();
                            String fullName = name + " " + surname;

                            uploadImageAndSaveUser(name, surname, email, phone, dni, address, uid, fullName, selectedRole);  // Pasamos el rol seleccionado
                        } else {
                            showCustomToast("Error al registrar el usuario.");
                        }
                    });
        });
    }

    private void openImageOptions() {
        CharSequence[] options = {"Tomar foto", "Seleccionar de galería", "Cancelar"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Elige una opción");

        builder.setItems(options, (dialog, which) -> {
            if (which == 0) openCamera();
            else if (which == 1) openGallery();
        });

        builder.show();
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null) {
                imageUri = data.getData();
            } else if (requestCode == CAMERA_REQUEST_CODE) {
                // imageUri ya está asignada
            }
            imageView.setImageURI(imageUri);
        }
    }

    private void uploadImageAndSaveUser(String name, String surname, String email, String phone, String dni,
                                        String address, String uid, String fullName, String role) {
        StorageReference storageReference = storage.getReference().child("user_images/" + UUID.randomUUID().toString());

        // Subir imagen a Firebase Storage
        storageReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Determinar el valor de 'status' dependiendo del rol
                    boolean status = role.equals("CLIENTE");

                    // Crear usuario con URL de la imagen de perfil y el rol seleccionado
                    User user = User.registrousuario(
                            name, surname, email, dni, phone, address,
                            role, uid, selectedLatitude, selectedLongitude,
                            uid, fullName);
                    user.setProfileImage(uri.toString());
                    user.setStatus(status);  // Asignamos el status según el rol

                    // Guardar datos del usuario en Firestore
                    guardarDatosEnBaseDeDatos(user);

                    // Enviar correo de cambio de contraseña
                    firebaseAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    showCustomToast("Por favor revisa tu correo para establecer la contraseña.");

                                    // Redirigir al LoginActivity
                                    Intent intent = new Intent(Signup2Activity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    showCustomToast("Error al enviar el correo de cambio de contraseña.");
                                }
                            });
                }))
                .addOnFailureListener(e -> showCustomToast("Error al subir la imagen."));
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
        user.setCreatedAt(Timestamp.now()); // Agrega el timestamp al objeto User
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
