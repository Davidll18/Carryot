package com.example.superadmin;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class super_habilitar_usuarios extends AppCompatActivity {
    private EditText nombreEditText, apellidosEditText, dniEditText, correoEditText, telefonoEditText;
    private Switch habilitarSwitch;
    private Button cancelarBtn, aceptar_btn;

    String channelId = "channelDefaultPri";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_habilitar_usuarios);

        createNotificationChannel();

        cancelarBtn = findViewById(R.id.canelar_btn);
        aceptar_btn = findViewById(R.id.aceptar_btn);

        nombreEditText = findViewById(R.id.Nombre);
        apellidosEditText = findViewById(R.id.Apellidos);
        dniEditText = findViewById(R.id.DNI);
        correoEditText = findViewById(R.id.Correo);
        telefonoEditText = findViewById(R.id.Telefono);
        habilitarSwitch = findViewById(R.id.switchHabilitar);

        // Recibir los datos del Intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String lastName = intent.getStringExtra("lastName");
        String dni = intent.getStringExtra("dni");
        String correo = intent.getStringExtra("correo");
        String telefono = intent.getStringExtra("telefono");
        boolean habilitado = intent.getBooleanExtra("habilitado", false);

        // Asignar los datos a las vistas
        nombreEditText.setText(name);
        apellidosEditText.setText(lastName);
        dniEditText.setText(dni);
        correoEditText.setText(correo);
        telefonoEditText.setText(telefono);
        habilitarSwitch.setChecked(habilitado);

        // Listener para el Switch de habilitar usuario
        habilitarSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Obtener los nombres y apellidos ingresados
            String nombre = nombreEditText.getText().toString();
            String apellido = apellidosEditText.getText().toString();

            if (isChecked) {
                // Si el Switch está activado (habilitado)
                lanzarNotificacion(nombre, apellido, true);
            } else {
                // Si el Switch está desactivado (deshabilitado)
                lanzarNotificacion(nombre, apellido, false);
            }
        });

        cancelarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finalizar la actividad y regresar a la anterior
                finish();
            }
        });
        aceptar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finalizar la actividad y regresar a la anterior
                finish();
            }
        });

    }

    public void createNotificationChannel() {
        //android.os.Build.VERSION_CODES.O == 26
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Canal notificaciones default",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Canal para notificaciones con prioridad default");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            askPermission();
        }
    }

    public void askPermission(){
        //android.os.Build.VERSION_CODES.TIRAMISU == 33
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) ==
                        PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(super_habilitar_usuarios.this,
                    new String[]{POST_NOTIFICATIONS},
                    101);
        }
    }


    public void lanzarNotificacion(String nombre, String apellido, boolean habilitado) {
        // Crear el intent para lanzar la actividad
        Intent intent = new Intent(this, super_habilitar_usuarios.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Crear el texto dinámico para la notificación
        String contentText = habilitado ?
                "El usuario " + nombre + " " + apellido + " ha sido habilitado" :
                "El usuario " + nombre + " " + apellido + " ha sido deshabilitado";

        // Crear la notificación con el nombre y apellido
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.perfil_icon)  // Cambiar a un ícono de notificación
                .setContentTitle(habilitado ? "Usuario habilitado" : "Usuario deshabilitado")
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Enviar la notificación
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(1, builder.build());
        }
    }


}