package com.example.superadmin.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
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
import java.util.List;

public class UserCarActivity extends AppCompatActivity implements UserProductCarAdapter.OnItemClickListener {

    private ConstraintLayout toolbar, btnPay;
    private ImageButton btnBack;
    private RecyclerView recyclerView;
    private UserProductCarAdapter adapter;
    private List<UserProductInCar> productList;

    private FirebaseFirestore db;
    private String uidUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_activity_car);

        // Configuración de vistas
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.toolbar_car);
        btnBack = toolbar.findViewById(R.id.btn_back);
        btnPay = findViewById(R.id.container_payment);
        recyclerView = findViewById(R.id.rv_car);

        // Configuración RecyclerView
        productList = new ArrayList<>();
        adapter = new UserProductCarAdapter(this, productList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Configuración Firebase
        db = FirebaseFirestore.getInstance();
        uidUsuario = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Botones
        btnPay.setOnClickListener(v -> {
            startActivity(new Intent(UserCarActivity.this, UserSummaryActivity.class));
            finish();
        });

        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(UserCarActivity.this, UserHomeActivity.class));
            finish();
        });

        // Cargar datos del carrito
        fetchCarritoData();
    }

    private void fetchCarritoData() {
        db.collection("pedidos")
                .whereEqualTo("uidUsuario", uidUsuario)
                .whereEqualTo("estado", "") // Solo carritos sin estado
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    productList.clear();
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        // Iterar por todos los productos (uidplatoX y cantidadX)
                        for (int i = 1; ; i++) {
                            String uidPlatoKey = "uidplato" + i;
                            String cantidadKey = "cantidad" + i;

                            if (document.contains(uidPlatoKey) && document.contains(cantidadKey)) {
                                String uidPlato = document.getString(uidPlatoKey);
                                int cantidad = document.getLong(cantidadKey).intValue();

                                cargarDatosPlato(uidPlato, cantidad);
                            } else {
                                // Rompe el ciclo cuando no hay más productos
                                break;
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar el carrito", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onAddClick(int position) {
        // Lógica para agregar cantidad
        UserProductInCar product = productList.get(position);
        product.setQuantity(product.getQuantity() + 1);
        adapter.notifyItemChanged(position);
    }

    @Override
    public void onMinusClick(int position) {
        // Lógica para disminuir cantidad
        UserProductInCar product = productList.get(position);
        if (product.getQuantity() > 1) {
            product.setQuantity(product.getQuantity() - 1);
            adapter.notifyItemChanged(position);
        }
    }

    @Override
    public void onRemoveClick(int position) {
        // Lógica para eliminar producto
        productList.remove(position);
        adapter.notifyItemRemoved(position);
    }

    private void cargarDatosPlato(String uidPlato, int cantidad) {
        db.collection("platos").document(uidPlato)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String nombrePlato = documentSnapshot.getString("nombrePlato");
                        double precio = Double.parseDouble(documentSnapshot.getString("precio"));

                        // Añadir el producto a la lista con nombre, cantidad y precio
                        productList.add(new UserProductInCar(nombrePlato, cantidad, precio*cantidad, R.drawable.logo));
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar datos del plato", Toast.LENGTH_SHORT).show();
                });
    }



}
