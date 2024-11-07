package com.example.superadmin.adminrest;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.superadmin.R;

public class NewRestaurantActivity extends AppCompatActivity {
    private EditText ed_desc, ed_loc;
    private AppCompatButton btnSiguiente,btnCancelar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newrestaurant2_adminrest);

        ed_desc = findViewById(R.id.ed_desc);
        ed_loc = findViewById(R.id.ed_loc);
        String descripcion= ed_desc.getText().toString();
        String ubicacion = ed_loc.getText().toString();
        Intent intent = getIntent();

        createNotificationChannel();

        // Inicializar el botón SIGUIENTE
        btnSiguiente = findViewById(R.id.btn_siguiente);

        // Inicializar el botón CANCELAR
        btnCancelar = findViewById(R.id.btn_cancelar);
        // Verificar si están vacíos
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            //String descripcion = ed_desc.getText().toString().trim();
            //String ubicacion = ed_loc.getText().toString().trim();
            // Recibir el valor del Intent

            @Override
            public void onClick(View v) {
                String nombreRestaurante = intent.getStringExtra("nombreRestaurante");
                lanzarNotificacion(nombreRestaurante);
                Intent intent = new Intent(NewRestaurantActivity.this, MainActivity.class);
                startActivity(intent);
                //envio_datos_perfil(descripcion, ubicacion);
                /*
                // Verificar si los campos están vacíos
                if (descripcion.isEmpty()) {
                    ed_desc.setError("Este campo no puede estar vacío");
                    ed_desc.requestFocus();
                } else if (ubicacion.isEmpty()) {
                    ed_loc.setError("Este campo no puede estar vacío");
                    ed_loc.requestFocus();
                } else {
                    // Proceder a la siguiente actividad

                }*/
            }
        });


        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewRestaurantActivity.this, RestaurantActivity.class);
                startActivity(intent);
            }
        });
    }

    /*private void envio_datos_perfil(String descripcion, String ubicacion) {
        Intent intent = new Intent(NewRestaurantActivity.this, ProfileRestActivity.class);
        intent.putExtra("descripcion", descripcion);
        intent.putExtra("ubicacion", ubicacion);
        startActivity(intent);
    }*/

    public void askPermission(){
        //android.os.Build.VERSION_CODES.TIRAMISU == 33
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) ==
                        PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(NewRestaurantActivity.this,
                    new String[]{POST_NOTIFICATIONS},
                    101);
        }
    }
    String channelId = "channelDefaultPri";
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Canal notificaciones default",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Canal para notificaciones con prioridad default");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            askPermission();
        }
    }
    public void lanzarNotificacion(String nombreRestaurante) {
        Intent intent = new Intent(this, ProfileRestActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("¡EL RESTAURANTE HA SIDO CREADO!")
                .setContentText("El restaurante\t" + nombreRestaurante + "\tse creó exitosamente." )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(1, builder.build());
        }
    }
}
