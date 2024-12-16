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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superadmin.R;
import com.example.superadmin.adapters.UserAdapterSummary;
import com.example.superadmin.model.UserProductInCar;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class UserSummaryActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "order_notification_channel";
    private static final int NOTIFICATION_ID = 1001;

    private FirebaseFirestore db;
    private String uidUsuario;
    private List<UserProductInCar> products;
    private UserAdapterSummary adapter;

    private Handler handler = new Handler();
    private MaterialButton btnFinish;

    private double subtotal = 0.0; // Inicializamos el subtotal
    private double envio = 6;    // El valor del delivery
    private TextView textSubtotal, textEnvio, textTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_summary);

        // Inicializar TextViews
        textSubtotal = findViewById(R.id.text_subTotal);
        textEnvio = findViewById(R.id.text_shipping_amount);
        textTotal = findViewById(R.id.text_total_amount);
        btnFinish = findViewById(R.id.btn_return_to_home);

        // Configurar RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rv_summary_order_client);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<UserProductInCar> products = new ArrayList<>();
        UserAdapterSummary adapter = new UserAdapterSummary(this, products);
        recyclerView.setAdapter(adapter);

        // Obtener productos desde Firebase
        db = FirebaseFirestore.getInstance();
        uidUsuario = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("pedidos")
                .whereEqualTo("uidUsuario", uidUsuario)
                .whereEqualTo("estado", "") // Solo el carrito actual
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        subtotal = 0.0; // Reiniciar el subtotal

                        for (int i = 1; ; i++) {
                            String uidPlatoKey = "uidplato" + i;
                            String cantidadKey = "cantidad" + i;

                            if (document.contains(uidPlatoKey) && document.contains(cantidadKey)) {
                                String uidPlato = document.getString(uidPlatoKey);
                                int cantidad = document.getLong(cantidadKey).intValue();
                                cargarProducto(uidPlato, cantidad, products, adapter);
                            } else {
                                break; // Fin de productos
                            }
                        }

                        // Obtener costo de envío desde el documento
                        if (document.contains("envio")) {
                            envio = 6;
                        }
                    }
                })
                .addOnFailureListener(e -> e.printStackTrace());



        btnFinish.setOnClickListener(v -> {
            actualizarEstadoPedido(); // Método para actualizar el estado del pedido
        });
    }

    private void cargarProducto(String uidPlato, int cantidad, List<UserProductInCar> products, UserAdapterSummary adapter) {
        db.collection("platos").document(uidPlato)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String nombrePlato = documentSnapshot.getString("nombrePlato");
                        double precio = Double.parseDouble(documentSnapshot.getString("precio"));

                        // Añadir producto a la lista
                        double totalProducto = precio * cantidad;
                        subtotal += totalProducto; // Acumular subtotal

                        products.add(new UserProductInCar(nombrePlato, cantidad, totalProducto, R.drawable.logo));
                        adapter.notifyDataSetChanged();

                        // Actualizar valores en la interfaz
                        actualizarValores();
                    }
                })
                .addOnFailureListener(e -> e.printStackTrace());
    }

    private String costoTotal;

    private void actualizarValores() {
        textSubtotal.setText(String.format("Subtotal S/ %.2f", subtotal));
        textEnvio.setText(String.format("Envío S/ %.2f", envio));

        double total = subtotal + envio;
        textTotal.setText(String.format("Total S/ %.2f", total));

        // Guardar el total en formato String
        costoTotal = String.format("%.2f", total);
    }

    private void actualizarEstadoPedido() {
        db.collection("pedidos")
                .whereEqualTo("uidUsuario", uidUsuario) // Filtrar por el usuario actual
                .whereEqualTo("estado", "") // Solo pedidos en estado vacío
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        String pedidoId = document.getId(); // ID del pedido
                        Random random = new Random();
                        String numeroPedido = String.valueOf(random.nextInt(99999) + 10000); // Genera un número aleatorio de 5 dígitos

                        // Crear un mapa con las actualizaciones necesarias
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("estado", "pendiente");
                        updates.put("numeroPedido", numeroPedido);
                        updates.put("uidCreacion", pedidoId); // Asignar el ID del mismo pedido
                        updates.put("uidRepartidor", ""); // Campo vacío para repartidor
                        updates.put("direccion", ""); // Campo vacío para dirección
                        updates.put("costoTotal", costoTotal); // Guardar el total calculado

                        // Actualizar el pedido en Firebase
                        db.collection("pedidos").document(pedidoId)
                                .update(updates)
                                .addOnSuccessListener(aVoid -> {
                                    showOrderCompletedNotification("Pedido completado", "Tu pedido ha sido completado con éxito.");

                                    // Hacer vibrar el dispositivo
                                    vibrateDevice();

                                    Toast.makeText(this, "Pedido pagado con éxito", Toast.LENGTH_SHORT).show();
                                    // Navegar a la pantalla de inicio o realizar otra acción
                                    startActivity(new Intent(UserSummaryActivity.this, UserHomeActivity.class));
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Error al actualizar el pedido", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al buscar el pedido", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                });
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
