package com.example.superadmin.Superadmin;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.superadmin.R;
import com.example.superadmin.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.example.superadmin.util.KeyboardUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class Super_registro_admin_rest extends AppCompatActivity {

    private static final String TAG = "Super_registro_admin_rest";
    private static final String CHANNEL_ID = "admin_registration_channel";
    private static final int PICK_IMAGE_REQUEST = 1001;
    private static final int CAMERA_REQUEST_CODE = 1002;

    private ConstraintLayout toolbar;
    private ImageButton btnBack;
    private AppCompatButton btnInit, btnSelectImage;
    private EditText edName, edSurname, edEmail, edDni, edPhone, edAddress;
    private ImageView imageView;
    private Uri imageUri;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_registro_admin_rest);
        getWindow().setStatusBarColor(ContextCompat.getColor(Super_registro_admin_rest.this, R.color.light_orange));

        toolbar = findViewById(R.id.toolbar_signup);
        btnBack = toolbar.findViewById(R.id.btn_back);
        btnInit = findViewById(R.id.btn_signup_init);
        btnSelectImage = findViewById(R.id.btn_seleccionar_foto); // Botón para seleccionar imagen
        imageView = findViewById(R.id.img_foto); // Vista previa de la imagen seleccionada

        // Inicializar campos de entrada
        edName = findViewById(R.id.ed_name);
        edSurname = findViewById(R.id.ed_surname);
        edEmail = findViewById(R.id.ed_email);
        edDni = findViewById(R.id.ed_dni);
        edPhone = findViewById(R.id.ed_phone);
        edAddress = findViewById(R.id.ed_address);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        createNotificationChannel();

        btnBack.setOnClickListener(v -> finish());

        btnSelectImage.setOnClickListener(v -> openImageOptions());

        btnInit.setOnClickListener(v -> registerAdmin());
    }

    private void registerAdmin() {
        String name = edName.getText().toString().trim();
        String surname = edSurname.getText().toString().trim();
        String email = edEmail.getText().toString().trim();
        String dni = edDni.getText().toString().trim();
        String phone = edPhone.getText().toString().trim();
        String address = edAddress.getText().toString().trim();

        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || dni.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            showCustomToast("Por favor, complete todos los campos");
            return;
        }

        if (imageUri == null) {
            showCustomToast("Selecciona una imagen antes de registrar");
            return;
        }

        String password = generateRandomPassword(10);

        SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
        String uidCreador = preferences.getString("userId", null);

        if (uidCreador != null) {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            String uid = firebaseUser.getUid();
                            saveImageAndRegisterAdmin(name, surname, email, dni, phone, address, uid, uidCreador);
                        } else {
                            showCustomToast("Error al crear el usuario");
                        }
                    });
        } else {
            showCustomToast("No se pudo obtener el UID del creador");
        }
    }

    private void saveImageAndRegisterAdmin(String name, String surname, String email, String dni, String phone,
                                           String address, String uid, String uidCreador) {
        StorageReference storageReference = storage.getReference().child("admin_images/" + UUID.randomUUID().toString());

        storageReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    db.collection("users").document(uidCreador)
                            .get()
                            .addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    String nameCreador = documentSnapshot.getString("name");
                                    String surnameCreador = documentSnapshot.getString("surname");
                                    String nombreCreador = (nameCreador != null ? nameCreador : "") + " " + (surnameCreador != null ? surnameCreador : "");

                                    Map<String, Object> admin = new HashMap<>();
                                    admin.put("name", name);
                                    admin.put("surname", surname);
                                    admin.put("email", email);
                                    admin.put("dni", dni);
                                    admin.put("phone", phone);
                                    admin.put("address", address);
                                    admin.put("role", Constants.ROLE_ADMIN_RES);
                                    admin.put("status", true);
                                    admin.put("uid", uid);
                                    admin.put("uidCreador", uidCreador);
                                    admin.put("createdBy", nombreCreador);
                                    admin.put("createdAt", com.google.firebase.Timestamp.now());
                                    admin.put("profileImage", uri.toString()); // URL de la imagen

                                    db.collection("users").document(uid).set(admin)
                                            .addOnSuccessListener(aVoid -> {
                                                showCustomToast("Administrador registrado exitosamente");
                                                sendPasswordResetEmail(email);
                                                new Handler().postDelayed(this::finish, 2000);
                                            })
                                            .addOnFailureListener(e -> showCustomToast("Error al guardar los datos del administrador"));
                                }
                            });
                }))
                .addOnFailureListener(e -> showCustomToast("Error al subir la imagen"));
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
            }
            imageView.setImageURI(imageUri);
        }
    }

    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Admin Registration Channel";
            String description = "Channel for admin registration notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendPasswordResetEmail(String email) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showCustomToast("Correo de verificación enviado");
                    } else {
                        showCustomToast("Error al enviar el correo");
                    }
                });
    }

    private void showCustomToast(String message) {
        View layout = getLayoutInflater().inflate(R.layout.custom_toast, findViewById(R.id.toast_container));
        TextView text = layout.findViewById(R.id.toast_text);
        text.setText(message);
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return KeyboardUtils.hideKeyboardOnTouch(this, event) || super.dispatchTouchEvent(event);
    }
}
