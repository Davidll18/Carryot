package com.example.superadmin;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

import com.example.superadmin.dtos.User;
import com.example.superadmin.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {
    private EditText edName, edSurname, edEmail, edDni, edPhone, edAddress, edPassword;
    private AppCompatButton btnInit;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    ConstraintLayout toolbar;
    ImageButton btnBack;

    private static final String CHANNEL_ID = "verification_channel";
    private static final int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar Firebase Firestore y FirebaseAuth
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        // Inicializar vistas
        edName = findViewById(R.id.ed_name);
        edSurname = findViewById(R.id.ed_surname);
        edEmail = findViewById(R.id.ed_email);
        edDni = findViewById(R.id.ed_dni);
        edPhone = findViewById(R.id.ed_phone);
        edAddress = findViewById(R.id.ed_address);
        edPassword = findViewById(R.id.ed_password);
        btnInit = findViewById(R.id.btn_signup_init);
        toolbar = findViewById(R.id.toolbar_signup);
        btnBack = toolbar.findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> finish());

        // Crear el canal de notificación
        createNotificationChannel();

        // Manejar clic en el botón de registro
        btnInit.setOnClickListener(v -> {
            // Obtener valores de los campos
            String name = edName.getText().toString().trim();
            String surname = edSurname.getText().toString().trim();
            String email = edEmail.getText().toString().trim();
            String dni = edDni.getText().toString().trim();
            String phone = edPhone.getText().toString().trim();
            String address = edAddress.getText().toString().trim();
            String password = edPassword.getText().toString().trim();

            // Validar que los campos no estén vacíos
            if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || dni.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Registrar al usuario en Firebase Authentication
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                            if (currentUser != null) {
                                // Actualizar el perfil del usuario (opcional)
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name + " " + surname)
                                        .build();
                                currentUser.updateProfile(profileUpdates);

                                // Enviar correo de verificación
                                currentUser.sendEmailVerification()
                                        .addOnCompleteListener(verificationTask -> {
                                            if (verificationTask.isSuccessful()) {
                                                Toast.makeText(SignUpActivity.this, "Registro exitoso. Verifique su correo electrónico para completar el proceso.", Toast.LENGTH_LONG).show();

                                                // Mostrar la notificación
                                                showVerificationNotification();

                                                // Redirigir al usuario a la pantalla de inicio de sesión
                                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                                finish(); // Finalizar la actividad actual
                                            } else {
                                                Toast.makeText(SignUpActivity.this, "Error al enviar el correo de verificación: " + verificationTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                // Guardar datos adicionales en Firestore usando el UID
                                String uid = currentUser.getUid();
                                String role_cliente = Constants.ROLE_CLIENTE; // Define el rol aquí
                                User user = User.registrousuario(name, surname, email, dni, phone, address, role_cliente, uid);
                                db.collection("users").document(uid).set(user)
                                        .addOnSuccessListener(unused -> {
                                            // Datos guardados exitosamente
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(SignUpActivity.this, "Error al registrar en Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });

                            }
                        } else {
                            Toast.makeText(SignUpActivity.this, "Error en el registro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Canal de Verificación";
            String description = "Canal para notificaciones de verificación de cuenta";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Registrar el canal en el sistema
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void showVerificationNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_24) // Asegúrate de tener un icono en tu carpeta drawable
                .setContentTitle("Esperando la activación de tu cuenta")
                .setContentText("Revisa tu correo electrónico para activar tu cuenta.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            // Mostrar la notificación
            notificationManager.notify(NOTIFICATION_ID, builder.build());

            // Programar la cancelación de la notificación después de 10 segundos
            new android.os.Handler().postDelayed(() -> notificationManager.cancel(NOTIFICATION_ID), 10000);
        }
    }
}
