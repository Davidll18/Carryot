package com.example.superadmin.Superadmin;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.superadmin.R;
import com.example.superadmin.dtos.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Super_registro_admin_rest extends AppCompatActivity {
    ConstraintLayout toolbar;
    ImageButton btnBack;
    AppCompatButton btnInit;

    private static final String CHANNEL_ID = "admin_registration_channel";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_super_registro_admin_rest);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        toolbar = findViewById(R.id.toolbar_signup);
        btnBack = toolbar.findViewById(R.id.btn_back);

        btnInit = findViewById(R.id.btn_signup_init);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        createNotificationChannel(); // Crea el canal de notificación

        btnInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí registrarías al nuevo administrador
                registerAdmin();
                showNotification("Nuevo Administrador Registrado", "Se ha registrado un nuevo administrador de restaurante.");
                finish();
            }
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
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Contraseña predeterminada (puedes cambiarla o generar una aleatoria)
        String password = "admin123";

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Crear usuario en Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = task.getResult().getUser().getUid();

                        // Crear objeto User
                        User admin = new User(
                                name,
                                surname,
                                email,
                                dni,
                                phone,
                                address,
                                "ADMIN REST", // Rol del usuario
                                true, // Activo
                                uid,
                                null, // Recovery code
                                0,    // Recovery code timestamp
                                0     // Recovery code validity
                        );

                        // Guardar en Firestore
                        db.collection("users").document(uid).set(admin)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Administrador registrado exitosamente", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Error al registrar el administrador: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });

                    } else {
                        Toast.makeText(this, "Error al crear el usuario: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void showNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo) // Cambia a tu icono de notificación
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
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
}