package com.example.superadmin.user;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_products);

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.rv_prod);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar Firestore y la lista
        db = FirebaseFirestore.getInstance();
        productList = new ArrayList<>();
        adapter = new UserProductAdapter(productList, this);
        recyclerView.setAdapter(adapter);

        // Cargar los datos desde Firestore
        fetchProductsFromFirestore();
    }

    private void fetchProductsFromFirestore() {
        db.collection("platos") // Nombre de tu colección en Firestore
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        productList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Convertir Firestore document a PlatoDTO
                            PlatoDTO plato = document.toObject(PlatoDTO.class);
                            productList.add(plato);
                        }
                        adapter.notifyDataSetChanged(); // Notificar cambios al RecyclerView
                    } else {
                        Log.e("Firestore", "Error al cargar datos", task.getException());
                    }
                });
    }

    @Override
    public void onItemClick(PlatoDTO plato) {
        // Acción al seleccionar un producto
        Log.d("Product", "Plato seleccionado: " + plato.getNombrePlato());
    }
}
