package com.example.superadmin.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superadmin.R;
import com.example.superadmin.adapters.UserProductAdapter;
import com.example.superadmin.dtos.PlatoDTO;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserProductsActivity extends AppCompatActivity implements UserProductAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private UserProductAdapter adapter;
    private List<PlatoDTO> productList;
    private FirebaseFirestore db;
    private String uidRestauranteSeleccionado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_products);

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.rv_prod);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        uidRestauranteSeleccionado = getIntent().getStringExtra("uidRestaurante");
        Log.d("UID_INTENT", "UID del restaurante recibido: " + uidRestauranteSeleccionado);

        // Inicializar Firestore y la lista
        db = FirebaseFirestore.getInstance();
        productList = new ArrayList<>();
        adapter = new UserProductAdapter(productList, this);
        recyclerView.setAdapter(adapter);


        // Cargar los datos desde Firestore
        fetchProductsFromFirestore(uidRestauranteSeleccionado);
    }

    private void fetchProductsFromFirestore(String uidRestaurante) {
        Log.d("Firestore", "Consultando platos con uidRestaurante: " + uidRestaurante);

        db.collection("platos")
                .whereEqualTo("uidRestaurante", uidRestaurante)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        productList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("Firestore", "Documento obtenido: " + document.getData());
                            PlatoDTO plato = document.toObject(PlatoDTO.class);
                            productList.add(plato);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("Firestore", "Error en la consulta Firestore", task.getException());
                    }
                });
    }

    @Override
    public void onItemClick(PlatoDTO plato) {
        // Acción al seleccionar un producto
        Log.d("Product", "Plato seleccionado: " + plato.getNombrePlato());
        Intent intent = new Intent(UserProductsActivity.this, UserProductDetailActivity.class);

        // Pasar datos del plato seleccionado al intent
        intent.putExtra("uidPlato", plato.getUidCreacion());
        intent.putExtra("uidRestaurante", plato.getUidRestaurante());
        intent.putExtra("nombrePlato", plato.getNombrePlato());
        intent.putExtra("descripcion", plato.getDescripcion());
        intent.putExtra("precio", plato.getPrecio());
        intent.putExtra("imageUrl", plato.getImageUrl());
        intent.putExtra("categoria", plato.getCategoriaPlato());

        startActivity(intent);
    }
}
