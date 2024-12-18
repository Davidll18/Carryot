package com.example.superadmin.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.superadmin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class UserProductDetailActivity extends AppCompatActivity {

    private TextView textViewName, textViewModel, textViewDescription, textViewPrice, textViewAmount, textQuantity;
    private ImageView btnMinus, btnPlus;
    private ConstraintLayout btnCarrito;
    private FirebaseFirestore db;
    private String uidUsuario;

    private int cantidad = 1; // Cantidad inicial
    private double precioUnidad;

    ConstraintLayout toolbar;
    ImageButton btnBack, btnBag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_product_detail);

        // Toolbar
        toolbar = findViewById(R.id.toolbar_detail_product);
        btnBack = toolbar.findViewById(R.id.btn_back);
        btnBag = toolbar.findViewById(R.id.btn_shopping_bag);

        btnBack.setOnClickListener(v -> finish());
        btnBag.setOnClickListener(v -> startActivity(new Intent(this, UserCarActivity.class)));

        // Firebase
        db = FirebaseFirestore.getInstance();
        uidUsuario = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Vistas
        textViewName = findViewById(R.id.text_name_product);
        textViewModel = findViewById(R.id.text_model_product);
        textViewDescription = findViewById(R.id.textView);
        textViewPrice = findViewById(R.id.text_amount);
        textViewAmount = findViewById(R.id.text_amount_total);
        textQuantity = findViewById(R.id.text_number_products);
        btnMinus = findViewById(R.id.btn_minus_numbers_products);
        btnPlus = findViewById(R.id.btn_add_numbers_producst);
        btnCarrito = findViewById(R.id.container_carrito);

        // Datos desde el Intent
        String uidPlato = getIntent().getStringExtra("uidPlato");
        String uidRestaurante = getIntent().getStringExtra("uidRestaurante");
        String descripcion = getIntent().getStringExtra("descripcion");
        String precio = getIntent().getStringExtra("precio");

        precioUnidad = Double.parseDouble(precio);
        textViewName.setText("Plato");
        textViewModel.setText("");
        textViewDescription.setText(descripcion);
        textViewPrice.setText("S/ " + precio);
        actualizarTotal();

        // Botones
        btnPlus.setOnClickListener(v -> {
            cantidad++;
            textQuantity.setText(String.valueOf(cantidad));
            actualizarTotal();
        });

        btnMinus.setOnClickListener(v -> {
            if (cantidad > 1) {
                cantidad--;
                textQuantity.setText(String.valueOf(cantidad));
                actualizarTotal();
            }
        });

        btnCarrito.setOnClickListener(v -> agregarAlCarrito(uidPlato, uidRestaurante));
    }

    private void actualizarTotal() {
        double total = precioUnidad * cantidad;
        textViewAmount.setText("S/ " + String.format("%.2f", total));
    }

    private void agregarAlCarrito(String uidPlato, String uidRestaurante) {
        db.collection("pedidos")
                .whereEqualTo("uidUsuario", uidUsuario)
                .whereEqualTo("estado", "")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            String carritoId = document.getId();
                            actualizarCarrito(carritoId, uidPlato,uidRestaurante);
                            return;
                        }
                    } else {
                        crearNuevoCarrito(uidPlato, uidRestaurante);
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al buscar el carrito", Toast.LENGTH_SHORT).show());
    }

    private void actualizarCarrito(String carritoId, String uidPlato, String uidRestaurante) {
        db.collection("pedidos").document(carritoId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Map<String, Object> carrito = documentSnapshot.getData();
                    String uidRestauranteCarrito = (String) carrito.get("uidRestaurante");

                    // Verificar si el carrito ya tiene un restaurante asignado
                    if (uidRestauranteCarrito != null && !uidRestauranteCarrito.equals(uidRestaurante)) {
                        Toast.makeText(this, "No puedes agregar productos de diferentes restaurantes al mismo carrito", Toast.LENGTH_LONG).show();
                        return;
                    }

                    int platosExistentes = 0; // Contar platos existentes
                    int indiceExistente = -1; // Indice del plato si existe
                    int cantidadActual = 0;   // Cantidad actual del plato si existe

                    // Contar platos existentes y buscar si el plato ya está en el carrito
                    for (int i = 1; i <= 3; i++) { // Máximo 3 platos
                        String uidKey = "uidplato" + i;
                        String cantidadKey = "cantidad" + i;

                        if (carrito.containsKey(uidKey)) {
                            platosExistentes++;
                            if (carrito.get(uidKey).equals(uidPlato)) { // El plato ya está en el carrito
                                indiceExistente = i;
                                cantidadActual = carrito.containsKey(cantidadKey)
                                        ? ((Long) carrito.get(cantidadKey)).intValue() : 0;
                            }
                        }
                    }

                    // Verificar si se alcanzó el límite de 3 platos
                    if (indiceExistente == -1 && platosExistentes >= 3) {
                        Toast.makeText(this, "No puedes agregar más de 3 platos diferentes", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Map<String, Object> updates = new HashMap<>();

                    if (indiceExistente != -1) {
                        // Si el plato ya existe, actualizar la cantidad
                        updates.put("cantidad" + indiceExistente, cantidadActual + 1);
                    } else {
                        // Añadir un nuevo plato al siguiente índice disponible
                        int nuevoIndice = 1;
                        while (carrito.containsKey("uidplato" + nuevoIndice)) {
                            nuevoIndice++;
                        }
                        updates.put("uidplato" + nuevoIndice, uidPlato);
                        updates.put("cantidad" + nuevoIndice, 1);

                        // Si es el primer plato, establecer el uidRestaurante
                        if (uidRestauranteCarrito == null) {
                            updates.put("uidRestaurante", uidRestaurante);
                        }
                    }

                    // Actualizar en Firebase
                    db.collection("pedidos").document(carritoId)
                            .update(updates)
                            .addOnSuccessListener(aVoid -> Toast.makeText(this, "Carrito actualizado", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(this, "Error al actualizar el carrito", Toast.LENGTH_SHORT).show());
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al obtener los datos del carrito", Toast.LENGTH_SHORT).show());
    }





    private void crearNuevoCarrito(String uidPlato, String uidRestaurante) {
        Map<String, Object> nuevoCarrito = new HashMap<>();
        nuevoCarrito.put("uidUsuario", uidUsuario);
        nuevoCarrito.put("uidRestaurante", uidRestaurante);
        nuevoCarrito.put("estado", "");
        nuevoCarrito.put("uidplato1", uidPlato);
        nuevoCarrito.put("cantidad1", cantidad);

        db.collection("pedidos")
                .add(nuevoCarrito)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Carrito creado y producto añadido", Toast.LENGTH_SHORT).show();
                    iniciarTemporizador(documentReference);
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al crear el carrito", Toast.LENGTH_SHORT).show());
    }

    private void iniciarTemporizador(DocumentReference documentReference) {
        new Handler().postDelayed(() -> {
            documentReference.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists() && documentSnapshot.getString("estado").isEmpty()) {
                    documentReference.delete().addOnSuccessListener(aVoid ->
                            Toast.makeText(UserProductDetailActivity.this, "Carrito eliminado por inactividad", Toast.LENGTH_SHORT).show()
                    );
                }
            });
        }, 60000); // 1 minutos
    }
}
