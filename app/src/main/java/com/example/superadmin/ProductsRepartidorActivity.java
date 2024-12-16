package com.example.superadmin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superadmin.adapters.ProductRepartidorAdapter;
import com.example.superadmin.dtos.Pedidos;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductsRepartidorActivity extends AppCompatActivity implements ProductRepartidorAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private ProductRepartidorAdapter adapter;
    private List<Pedidos> pedidosList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_repartidor);

        recyclerView = findViewById(R.id.rv_prod_repartidor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();

        adapter = new ProductRepartidorAdapter(pedidosList, this);
        recyclerView.setAdapter(adapter);

        cargarPedidosEnPreparacion();
    }

    private void cargarPedidosEnPreparacion() {
        db.collection("pedidos")
                .whereEqualTo("estado", "En preparacion")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot document : querySnapshot) {
                        Pedidos pedido = document.toObject(Pedidos.class);
                        pedido.setCostoTotal("0"); // Temporal, lo calcularemos después
                        obtenerDetallesDelPedido(pedido);
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al cargar pedidos", Toast.LENGTH_SHORT).show());
    }

    private void obtenerDetallesDelPedido(Pedidos pedido) {
        // Obtener precios de los platos
        db.collection("platos").document(pedido.getUidplato1()).get()
                .addOnSuccessListener(plato1Doc -> db.collection("platos").document(pedido.getUidplato2()).get()
                        .addOnSuccessListener(plato2Doc -> db.collection("platos").document(pedido.getUidplato3()).get()
                                .addOnSuccessListener(plato3Doc -> {

                                    // Calcular el costo total
                                    double precio1 = Double.parseDouble(plato1Doc.getString("precio"));
                                    double precio2 = Double.parseDouble(plato2Doc.getString("precio"));
                                    double precio3 = Double.parseDouble(plato3Doc.getString("precio"));

                                    double costoTotal = (Integer.parseInt(pedido.getCantidad1()) * precio1) +
                                            (Integer.parseInt(pedido.getCantidad2()) * precio2) +
                                            (Integer.parseInt(pedido.getCantidad3()) * precio3);

                                    pedido.setCostoTotal(String.format("%.2f", costoTotal));

                                    // Obtener imagen del restaurante
                                    db.collection("restaurant").document(pedido.getUidRestaurante())
                                            .get()
                                            .addOnSuccessListener(restauranteDoc -> {
                                                String imageUrl = restauranteDoc.getString("imageUrl");
                                                pedido.setImageUrlRestaurante(imageUrl); // Almacenar en el campo correcto

                                                // Añadir a la lista y notificar al adaptador
                                                pedidosList.add(pedido);
                                                adapter.notifyDataSetChanged();
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(this, "Error al obtener restaurante", Toast.LENGTH_SHORT).show();
                                            });
                                })));
    }

    @Override
    public void onItemClick(Pedidos pedido) {
        Toast.makeText(this, "Ver detalles del pedido N°: " + pedido.getNumeroPedido(), Toast.LENGTH_SHORT).show();
        // Aquí puedes navegar a otra actividad para mostrar detalles del pedido
    }

    @Override
    public void onButton3Click(Pedidos pedido) {
        // Obtener el UID del repartidor desde SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
        String uidRepartidor = preferences.getString("userId", null);

        // Validar que el UID de creación y el UID del repartidor no sean nulos
        if (pedido.getUidCreacion() == null || pedido.getUidCreacion().isEmpty()) {
            Toast.makeText(this, "ID del pedido no válido", Toast.LENGTH_SHORT).show();
            Log.e("Firestore", "El UID de creación es nulo o vacío.");
            return;
        }

        if (uidRepartidor == null || uidRepartidor.isEmpty()) {
            Toast.makeText(this, "No se pudo obtener el UID del repartidor", Toast.LENGTH_SHORT).show();
            Log.e("Firestore", "El UID del repartidor no está disponible en SharedPreferences.");
            return;
        }

        // Mostrar el diálogo de confirmación
        new MaterialAlertDialogBuilder(this)
                .setTitle("Confirmación")
                .setMessage("¿Deseas tomar el pedido N°: " + pedido.getNumeroPedido() + "?")
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    // Log para depuración
                    Log.d("Firestore", "Actualizando documento con ID: " + pedido.getUidCreacion());

                    // Crear un mapa con los campos a actualizar
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("estado", "En camino");
                    updates.put("uidRepartidor", uidRepartidor);

                    // Actualizar los campos en Firestore
                    db.collection("pedidos").document(pedido.getUidCreacion())
                            .update(updates)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Pedido completado y asignado al repartidor", Toast.LENGTH_SHORT).show();
                                Log.d("Firestore", "Actualización exitosa: Estado y UID del repartidor");
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Error al actualizar el pedido", Toast.LENGTH_SHORT).show();
                                Log.e("Firestore", "Error al actualizar documento", e);
                            });
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }
    @Override
    public void onButton2Click(Pedidos pedido) {
        // Validar que los valores requeridos no sean nulos o vacíos
        if (pedido.getUidCreacion() == null || pedido.getUidCreacion().isEmpty()) {
            Toast.makeText(this, "UID del pedido no válido", Toast.LENGTH_SHORT).show();
            Log.e("Firestore", "El UID de creación es nulo o vacío.");
            return;
        }

        if (pedido.getLatitude() == 0.0 || pedido.getLongitude() == 0.0) {
            Toast.makeText(this, "Ubicación no válida", Toast.LENGTH_SHORT).show();
            Log.e("Firestore", "Latitud o longitud inválidas: Latitude=" + pedido.getLatitude() + ", Longitude=" + pedido.getLongitude());
            return;
        }

        // Crear el intent para VerMapa
        Intent intent = new Intent(this, VerMapa.class);
        intent.putExtra("uidCreacion", pedido.getUidCreacion()); // Pasar el UID del pedido
        intent.putExtra("latitude", pedido.getLatitude());       // Pasar latitud
        intent.putExtra("longitude", pedido.getLongitude());     // Pasar longitud

        // Iniciar la actividad
        startActivity(intent);
    }

}
