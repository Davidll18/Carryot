package com.example.superadmin.service;

import com.example.superadmin.dtos.PlatoDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import lombok.NonNull;

public class FirestorePlatosService {
    private FirebaseFirestore db;

    // Constructor: Inicializa la instancia de FirebaseFirestore
    public FirestorePlatosService() {
        db = FirebaseFirestore.getInstance();
    }


    public void obtenerPlatosAleatorios(Consumer<List<PlatoDTO>> callback) {
        db.collection("platos")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<PlatoDTO> listaPlatos = new ArrayList<>();
                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            PlatoDTO plato = new PlatoDTO(
                                    snapshot.getLong("cantidad") != null ? snapshot.getLong("cantidad").intValue() : 0,
                                    snapshot.getString("categoriaPlato"),
                                    snapshot.getString("descripcion"),
                                    snapshot.getBoolean("disponible") != null && snapshot.getBoolean("disponible"),
                                    snapshot.getString("imageUrl"),
                                    snapshot.getString("nombreCreador"),
                                    snapshot.getString("uidCreacion"),
                                    snapshot.getString("nombrePlato"),
                                    snapshot.getString("precio"),
                                    snapshot.getString("uidCreador"),
                                    snapshot.getString("uidRestaurante")
                            );
                            listaPlatos.add(plato);
                        }

                        // Mezclar los platos y devolver la lista
                        Collections.shuffle(listaPlatos);
                        callback.accept(listaPlatos);
                    } else {
                        callback.accept(Collections.emptyList()); // Retorna una lista vacía si falla
                    }
                });
    }



    public interface FirestoreCallback {
        void onSuccess(List<PlatoDTO> platosAleatorios);
        void onFailure(Exception e);
    }
}
