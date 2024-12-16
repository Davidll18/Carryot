package com.example.superadmin.user;

import static java.util.logging.Logger.global;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superadmin.R;
import com.example.superadmin.adapters.UserProductCarAdapter;
import com.example.superadmin.model.UserProductInCar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserCarActivity extends AppCompatActivity implements UserProductCarAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private UserProductCarAdapter adapter;
    private List<UserProductInCar> productList;
    private FirebaseFirestore db;
    private String uidUsuario;
    private ConstraintLayout btnPay;
    private TextView textViewTotal; // TextView para mostrar el total

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_car);

        // Inicializar Firebase
        db = FirebaseFirestore.getInstance();
        uidUsuario = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Inicializar vistas
        recyclerView = findViewById(R.id.rv_car);
        textViewTotal = findViewById(R.id.text_amount); // TextView del total
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();
        adapter = new UserProductCarAdapter(this, productList, this);
        recyclerView.setAdapter(adapter);
        btnPay = findViewById(R.id.container_payment);

        fetchCarritoData();

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserCarActivity.this, UserSummaryActivity.class));
                finish();
            }
        });

    }

    private void fetchCarritoData() {
        db.collection("pedidos")
                .whereEqualTo("uidUsuario", uidUsuario)
                .whereEqualTo("estado", "") // Buscar carritos sin finalizar
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    productList.clear();
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        for (int i = 1; ; i++) {
                            String uidPlatoKey = "uidplato" + i;
                            String cantidadKey = "cantidad" + i;

                            if (document.contains(uidPlatoKey) && document.contains(cantidadKey)) {
                                String uidPlato = document.getString(uidPlatoKey);
                                int cantidad = document.getLong(cantidadKey).intValue();
                                cargarDatosPlato(uidPlato, cantidad);
                            } else {
                                break;
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al cargar el carrito", Toast.LENGTH_SHORT).show());
    }

    private void cargarDatosPlato(String uidPlato, int cantidad) {
        db.collection("platos").document(uidPlato)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String nombrePlato = documentSnapshot.getString("nombrePlato");
                        double precio = Double.parseDouble(documentSnapshot.getString("precio"));
                        productList.add(new UserProductInCar(nombrePlato, cantidad, precio * cantidad, R.drawable.logo));
                        adapter.notifyDataSetChanged();
                        calcularTotal(); // Actualizar el total
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al cargar datos del plato", Toast.LENGTH_SHORT).show());
    }

    private void calcularTotal() {
        double total = 0.0;
        for (UserProductInCar product : productList) {
            total += product.getPrice();
        }
        textViewTotal.setText(String.format("Total S/ %.2f", total));
    }

    @Override
    public void onAddClick(int position) {
        UserProductInCar product = productList.get(position);
        product.setQuantity(product.getQuantity() + 1);
        product.setPrice(product.getPrice() / (product.getQuantity() - 1) * product.getQuantity()); // Recalcular el precio total
        adapter.notifyItemChanged(position);
        calcularTotal();
    }

    @Override
    public void onMinusClick(int position) {
        UserProductInCar product = productList.get(position);
        if (product.getQuantity() > 1) {
            product.setQuantity(product.getQuantity() - 1);
            product.setPrice(product.getPrice() / (product.getQuantity() + 1) * product.getQuantity());
            adapter.notifyItemChanged(position);
            calcularTotal();
        }
    }

    @Override
    public void onRemoveClick(int position) {
        productList.remove(position);
        adapter.notifyItemRemoved(position);
        calcularTotal();
    }
/*
    // Método que se ejecuta al presionar el botón "Completar Pedido"
    private void completarPedido() {
        db.collection("pedidos")
                .whereEqualTo("uidUsuario", uidUsuario)
                .whereEqualTo("estado", "") // Buscar carrito sin finalizar
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        String docId = document.getId();

                        // Crear un mapa dinámico para las cantidades y UIDs del pedido
                        Map<String, Object> updates = new HashMap<>();

                        int i = 1; // Inicializar contador de productos
                        while (true) {
                            // Claves dinámicas
                            String uidPlatoKey = "uidplato" + i;
                            String cantidadKey = "cantidad" + i;

                            // Verificar si las claves existen en el documento
                            if (document.contains(uidPlatoKey) && document.contains(cantidadKey)) {
                                // Obtener valores directamente del documento
                                String uidPlato = document.getString(uidPlatoKey);
                                int cantidad = document.getLong(cantidadKey).intValue();

                                // Añadir los valores actuales (o modificados si quieres ajustar cantidades) al mapa
                                updates.put(cantidadKey, cantidad);
                                updates.put(uidPlatoKey, uidPlato);

                                i++; // Incrementar para el siguiente producto
                            } else {
                                break; // Salir del bucle si no hay más productos
                            }
                        }

                        // Actualizar Firebase con las cantidades y UIDs del carrito
                        db.collection("pedidos").document(docId)
                                .update(updates)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(UserCarActivity.this, "Pedido completado", Toast.LENGTH_SHORT).show();
                                    // Redirigir a la pantalla de resumen o confirmación
                                    startActivity(new Intent(UserCarActivity.this, UserSummaryActivity.class));
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(UserCarActivity.this, "Error al completar el pedido", Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(UserCarActivity.this, "Error al buscar el carrito", Toast.LENGTH_SHORT).show();
                });
    }



 */





}

