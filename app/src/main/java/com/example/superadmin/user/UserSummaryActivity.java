package com.example.superadmin.user;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superadmin.R;
import com.google.android.material.button.MaterialButton;
import com.example.superadmin.adapters.UserAdapterSummary;
import com.example.superadmin.model.UserProductInCar;

import java.util.Arrays;
import java.util.List;

public class UserSummaryActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "order_notification_channel";
    private static final int NOTIFICATION_ID = 1001;

    private Handler handler = new Handler();
    MaterialButton btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_summary);

        // Crear el canal de notificación (solo es necesario en API 26+)
        createNotificationChannel();

        // Configurar el botón de finalizar
        btnFinish = findViewById(R.id.btn_return_to_home);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lanzar la primera notificación
                showOrderCompletedNotification("Pedido completado", "Tu pedido ha sido completado con éxito.");

                // Hacer vibrar el dispositivo
                vibrateDevice();

                // Iniciar la secuencia de notificaciones con 10 segundos de diferencia
                scheduleNotifications();

                // Cambiar a la pantalla de inicio
                startActivity(new Intent(UserSummaryActivity.this, UserHomeActivity.class));

                // Terminar la actividad actual
                finish();
            }
        });

        // Configurar el RecyclerView y cargar productos
        RecyclerView recyclerView = findViewById(R.id.rv_summary_order_client);
        List<UserProductInCar> products = Arrays.asList(
                new UserProductInCar("Alitas BBQ", 2, 45.0, R.drawable.comida),
                new UserProductInCar("Ceviche Mixto", 3, 650.0, R.drawable.logo),
                new UserProductInCar("Chicharrón de Calamar", 1, 350.0, R.drawable.logo),
                new UserProductInCar("Arroz con Mariscos", 2, 800.0, R.drawable.logo),
                new UserProductInCar("Ají de Gallina", 4, 550.0, R.drawable.logo),
                new UserProductInCar("Causa Limeña", 1, 450.0, R.drawable.logo),
                new UserProductInCar("Tacos de Pollo Crocante", 1, 650.0, R.drawable.logo),
                new UserProductInCar("Leche de Tigre", 3, 350.0, R.drawable.logo),
                new UserProductInCar("Mazamorra de Calabaza", 6, 800.0, R.drawable.logo),
                new UserProductInCar("Tallarines Rojos", 1, 550.0, R.drawable.logo)
        );
        UserAdapterSummary adapter = new UserAdapterSummary(UserSummaryActivity.this, products);
        recyclerView.setAdapter(adapter);
    }

    // Método para programar las notificaciones
    private void scheduleNotifications() {
        // 1. Notificación: Están preparando tu pedido (10 segundos después)
        handler.postDelayed(() -> showOrderCompletedNotification("Preparando tu pedido", "Estamos preparando tu pedido."), 10000);

        // 2. Notificación: El repartidor recogerá tu pedido (20 segundos después)
        handler.postDelayed(() -> showOrderCompletedNotification("Repartidor en camino", "El repartidor recogerá tu pedido pronto."), 20000);

        // 3. Notificación: El repartidor está yendo a tu dirección (30 segundos después)
        handler.postDelayed(() -> showOrderCompletedNotification("Repartidor en camino", "El repartidor está yendo a tu dirección."), 30000);

        // 4. Notificación: El repartidor ha llegado (40 segundos después)
        handler.postDelayed(() -> showOrderCompletedNotification("Repartidor ha llegado", "El repartidor ha llegado a tu dirección."), 40000);
    }

    // Crear un canal de notificación para Android Oreo y superiores
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Order Notifications";
            String description = "Notificaciones de estado del pedido.";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Registrar el canal con el sistema
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Método para mostrar la notificación
    private void showOrderCompletedNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo) // Icono de la notificación
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    // Método para hacer vibrar el dispositivo
    private void vibrateDevice() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            long[] vibrationPattern = {0, 500, 100, 500}; // Patrón de vibración
            vibrator.vibrate(vibrationPattern, -1); // '-1' significa que no se repite
        }
    }
}
