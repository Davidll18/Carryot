package com.example.superadmin.Superadmin;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.superadmin.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class super_habilitar_usuarios extends AppCompatActivity {
    private EditText nombreEditText, apellidosEditText, dniEditText, correoEditText, telefonoEditText;
    private Switch habilitarSwitch;
    private String userId;
    private Button cancelarBtn, aceptar_btn;

    private boolean habilitado; // Estado inicial del switch
    private final String channelId = "channelDefaultPri";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_habilitar_usuarios);
        getWindow().setStatusBarColor(ContextCompat.getColor(super_habilitar_usuarios.this,R.color.red_boton));

        // Inicializar vistas
        nombreEditText = findViewById(R.id.Nombre);
        apellidosEditText = findViewById(R.id.Apellidos);
        dniEditText = findViewById(R.id.DNI);
        correoEditText = findViewById(R.id.Correo);
        telefonoEditText = findViewById(R.id.Telefono);
        habilitarSwitch = findViewById(R.id.switchHabilitar);
        cancelarBtn = findViewById(R.id.canelar_btn);
        aceptar_btn = findViewById(R.id.aceptar_btn);

        createNotificationChannel();



        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        String name = intent.getStringExtra("name");
        String surname = intent.getStringExtra("surname");
        String dni = intent.getStringExtra("dni");
        String phone = intent.getStringExtra("phone");
        String email = intent.getStringExtra("email");
        Boolean status = Boolean.valueOf(intent.getStringExtra("status"));

        // Verificar si los valores son nulos
        Log.d("IntentData", "userId: " + userId);
        Log.d("IntentData", "name: " + name);
        Log.d("IntentData", "surname: " + surname);
        Log.d("IntentData", "dni: " + dni);
        Log.d("IntentData", "phone: " + phone);
        Log.d("IntentData", "email: " + email);
        Log.d("IntentData", "status: " + status);


        // Asignar los datos a las vistas si no son nulos
        nombreEditText.setText(name);
        apellidosEditText.setText(surname);
        dniEditText.setText(dni);
        correoEditText.setText(email);
        telefonoEditText.setText(phone);
        habilitarSwitch.setChecked(status);


        cancelarBtn.setOnClickListener(v -> finish());

        aceptar_btn.setOnClickListener(v -> {
            habilitado = habilitarSwitch.isChecked(); // Actualizar estado habilitado
            updateUserInFirestore();
        });

        // Configurar el switch según el valor recibido
        habilitarSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateStatusInFirestore(isChecked);
            lanzarNotificacion(nombreEditText.getText().toString(), apellidosEditText.getText().toString(), isChecked);
        });

    }

    private void updateStatusInFirestore(boolean isEnabled) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userId)
                .update("habilitado", isEnabled) // Actualizar el campo en Firestore
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Estado actualizado", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Error al actualizar el estado", Toast.LENGTH_SHORT).show());
    }

    private void updateUserInFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "Error: No se encontró el ID del usuario.", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("users").document(userId)
                .update("habilitado", habilitado)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Usuario actualizado correctamente.", Toast.LENGTH_SHORT).show();
                    finish(); // Regresar a la actividad anterior
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al actualizar usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Error al actualizar usuario", e);
                });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Canal de notificaciones",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Canal para notificaciones de cambios de usuarios");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            askPermission();
        }
    }

    private void askPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{POST_NOTIFICATIONS}, 101);
        }
    }

    private void lanzarNotificacion(String nombre, String apellido, boolean habilitado) {
        String contentText = habilitado ?
                "El usuario " + nombre + " " + apellido + " ha sido habilitado" :
                "El usuario " + nombre + " " + apellido + " ha sido deshabilitado";

        Intent intent = new Intent(this, super_habilitar_usuarios.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.perfil_icon)
                .setContentTitle(habilitado ? "Usuario habilitado" : "Usuario deshabilitado")
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(1, builder.build());
        }
    }

}