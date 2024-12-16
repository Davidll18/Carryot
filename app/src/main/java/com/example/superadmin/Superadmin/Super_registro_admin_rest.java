package com.example.superadmin.Superadmin;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.superadmin.R;
import com.example.superadmin.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Super_registro_admin_rest extends AppCompatActivity {

    private static final String TAG = "Super_registro_admin_rest";  // Tag para los logs
    private ConstraintLayout toolbar;
    private ImageButton btnBack;
    private AppCompatButton btnInit;

    private static final String CHANNEL_ID = "admin_registration_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_registro_admin_rest);
        getWindow().setStatusBarColor(ContextCompat.getColor(Super_registro_admin_rest.this, R.color.red_boton));

        toolbar = findViewById(R.id.toolbar_signup);
        btnBack = toolbar.findViewById(R.id.btn_back);
        btnInit = findViewById(R.id.btn_signup_init);

        // Manejo de la navegación de "Atrás"
        btnBack.setOnClickListener(v -> finish());

        createNotificationChannel(); // Crea el canal de notificación

        btnInit.setOnClickListener(v -> {
            // Aquí registrarías al nuevo administrador
            registerAdmin();
        });
    }

    private void registerAdmin() {
        // Obtener los valores ingresados en los EditText
        EditText edName = findViewById(R.id.ed_name);
        EditText edSurname = findViewById(R.id.ed_surname);
        EditText edEmail = findViewById(R.id.ed_email);
        EditText edDni = findViewById(R.id.ed_dni);
        EditText edPhone = findViewById(R.id.ed_phone);
        EditText edAddress = findViewById(R.id.ed_address);

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

        // Generar una contraseña aleatoria de 10 caracteres
        String password = generateRandomPassword(10);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Obtener el UID del superadmin desde SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
        String uidCreador = preferences.getString("userId", null);

        Log.d(TAG, "UID del creador obtenido: " + uidCreador);  // Log para verificar el UID

        if (uidCreador != null) {
            // Crear usuario en Firebase Authentication
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            String uid = firebaseUser.getUid();

                            Log.d(TAG, "UID del nuevo admin: " + uid);  // Log para verificar el UID del nuevo admin

                            // Consultar la colección "users" en Firestore para obtener nombre y apellido del creador
                            db.collection("users").document(uidCreador)
                                    .get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        if (documentSnapshot.exists()) {
                                            String nameCreador = documentSnapshot.getString("name");
                                            String surnameCreador = documentSnapshot.getString("surname");

                                            String nombreCreador = (nameCreador != null ? nameCreador : "") + " " + (surnameCreador != null ? surnameCreador : "");

                                            // Crear un Map con los datos a guardar en Firestore
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


                                            db.collection("users").document(uid).set(admin)
                                                    .addOnSuccessListener(aVoid -> {
                                                        Log.d(TAG, "Administrador registrado exitosamente");
                                                        showCustomToast("Administrador registrado exitosamente");

                                                        new Handler().postDelayed(() -> {
                                                            finish();
                                                        }, 2000);

                                                        sendPasswordResetEmail(email);
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Log.e(TAG, "Error al registrar el administrador", e);
                                                        showCustomToast("Error al registrar el administrador: " );
                                                    });
                                        } else {
                                            Log.d(TAG, "No se encontraron datos del creador");
                                            showCustomToast("No se encontraron datos del creador");
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e(TAG, "Error al obtener datos del creador", e);
                                        showCustomToast("Error al obtener datos del creador: ");
                                    });
                        } else {
                            Log.e(TAG, "Error al crear el usuario", task.getException());
                            showCustomToast("Error al crear el usuario: ");
                        }
                    });
        } else {
            Log.d(TAG, "UID del creador es null");
            showCustomToast("No se pudo obtener el UID del creador");
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
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showCustomToast("Correo de verificación enviado");
                    } else {
                        showCustomToast("Error al enviar el correo: ");
                    }
                });
    }

    private void showCustomToast(String message) {
        // Crear el layout del Toast personalizado desde el XML
        View layout = getLayoutInflater().inflate(R.layout.custom_toast, findViewById(R.id.toast_container));

        // Encontrar el TextView y establecer el mensaje
        TextView text = layout.findViewById(R.id.toast_text);
        text.setText(message);

        // Crear el Toast con el layout personalizado
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

}
