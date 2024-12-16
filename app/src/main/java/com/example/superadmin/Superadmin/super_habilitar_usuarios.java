package com.example.superadmin.Superadmin;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.superadmin.LoginActivity;
import com.example.superadmin.R;
import com.example.superadmin.dtos.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class super_habilitar_usuarios extends AppCompatActivity {
    private EditText nombreEditText, apellidosEditText, dniEditText, correoEditText, telefonoEditText;
    private Switch habilitarSwitch;
    private String userId;
    private Button cancelarBtn, aceptarBtn;
    private ImageButton buttonMenu;
    private DrawerLayout drawerLayout;
    private boolean habilitado; // Estado inicial del switch
    private final String channelId = "channelDefaultPri";
    private NavigationView navigationView_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_habilitar_usuarios);
        getWindow().setStatusBarColor(ContextCompat.getColor(super_habilitar_usuarios.this, R.color.red_boton));

        // Inicializar vistas
        nombreEditText = findViewById(R.id.Nombre);
        apellidosEditText = findViewById(R.id.Apellidos);
        dniEditText = findViewById(R.id.DNI);
        correoEditText = findViewById(R.id.Correo);
        telefonoEditText = findViewById(R.id.Telefono);
        habilitarSwitch = findViewById(R.id.switchHabilitar);
        cancelarBtn = findViewById(R.id.cancelar_btn);
        aceptarBtn = findViewById(R.id.aceptar_btn);
        drawerLayout = findViewById(R.id.drawerLayout);
        buttonMenu = findViewById(R.id.buttonMenu);
        navigationView_menu = findViewById(R.id.navigationView_menu);
        createNotificationChannel();

        // Obtener el UID del usuario desde el intent
        userId = getIntent().getStringExtra("userId");

        if (userId != null && !userId.isEmpty()) {
            // Cargar los datos del usuario desde Firestore
            cargarUsuarioDesdeFirestore();
        } else {
            Toast.makeText(this, "Error: No se recibió el UID del usuario.", Toast.LENGTH_SHORT).show();
            finish(); // Finaliza la actividad si no hay UID
        }

        // Configurar el botón del menú
        buttonMenu.setOnClickListener(view -> drawerLayout.open());

        // Configurar Navigation View
        navigationView_menu.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = null;

            // Redirección basada en la selección del menú
            if (id == R.id.navGestionUsuarios) {
                intent = new Intent(super_habilitar_usuarios.this, super_gestion_usuarios.class);
            } else if (id == R.id.navRegistrarAdminRest) {
                intent = new Intent(super_habilitar_usuarios.this, Super_registro_admin_rest.class);
            } else if (id == R.id.navReporteVentas_por_rest) {
                intent = new Intent(super_habilitar_usuarios.this, super_gestion_rest.class);
            } else if (id == R.id.navReporteVentas) {
                intent = new Intent(super_habilitar_usuarios.this, super_estadisticas_general.class);
            } else if (id == R.id.navLogs) {
                intent = new Intent(super_habilitar_usuarios.this, super_logs.class);
            } else if (id == R.id.navlogout) {
                // Mostrar un AlertDialog antes de cerrar sesión
                mostrarDialogoCerrarSesion();
            }

            drawerLayout.closeDrawers();
            if (intent != null) {
                startActivity(intent);
            }
            return true;
        });

        // Configurar botones
        cancelarBtn.setOnClickListener(v -> finish()); // Cierra la actividad

        aceptarBtn.setOnClickListener(v -> {
            habilitado = habilitarSwitch.isChecked(); // Obtener el estado del switch
            actualizarEstadoUsuarioEnFirestore(habilitado); // Guardar el estado en Firestore
        });
    }

    // Método para cargar datos del usuario desde Firestore
    private void cargarUsuarioDesdeFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            // Llenar los campos con los datos del usuario
                            nombreEditText.setText(user.getName());
                            apellidosEditText.setText(user.getSurname());
                            dniEditText.setText(user.getDni());
                            correoEditText.setText(user.getEmail());
                            telefonoEditText.setText(user.getPhone());
                            habilitarSwitch.setChecked(user.getStatus() != null ? user.getStatus() : false);
                        }
                    } else {
                        Toast.makeText(this, "El usuario no existe.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar datos del usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Error al obtener usuario", e);
                    finish();
                });
    }

    private void actualizarEstadoUsuarioEnFirestore(boolean isEnabled) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userId)
                .update("status", isEnabled) // Actualizar el campo de estado
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Estado actualizado correctamente.", Toast.LENGTH_SHORT).show();
                    // Lanzar la notificación al cambiar el estado
                    lanzarNotificacion(nombreEditText.getText().toString(), apellidosEditText.getText().toString(), isEnabled);
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al actualizar el estado: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void mostrarDialogoCerrarSesion() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Estás seguro de que quieres cerrar sesión?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();

                    SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();

                    Intent intent = new Intent(super_habilitar_usuarios.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
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

