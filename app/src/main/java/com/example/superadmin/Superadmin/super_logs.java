package com.example.superadmin.Superadmin;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superadmin.R;
import com.example.superadmin.adapters.LogAdapter;
import com.example.superadmin.dtos.RestaurantDTO;
import com.example.superadmin.model.LogEntry;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class super_logs extends AppCompatActivity {

    private RecyclerView recyclerViewLogs;
    private LogAdapter logAdapter;
    private List<LogEntry> logList;
    private Spinner spinnerFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_logs);

        // Inicializa el Spinner
        Spinner spinnerFilter = findViewById(R.id.spinnerFilter);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.logs_tipos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapter);

        // Inicializa RecyclerView
        recyclerViewLogs = findViewById(R.id.recyclerViewLogs);
        recyclerViewLogs.setLayoutManager(new LinearLayoutManager(this));

        logList = new ArrayList<>();
        logAdapter = new LogAdapter(logList);
        recyclerViewLogs.setAdapter(logAdapter);

        // Listener para el Spinner
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoriaSeleccionada = parent.getItemAtPosition(position).toString();
                logList.clear(); // Limpiamos la lista antes de cargar nuevos datos

                if (categoriaSeleccionada.equals("Usuarios")) {
                    cargarUsuariosDesdeFirestore();
                } else if (categoriaSeleccionada.equals("Restaurantes")) {
                    cargarRestaurantesDesdeFirestore();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se necesita acción
            }
        });


        // Carga inicial de datos
        cargarUsuariosDesdeFirestore();
    }

    private void cargarUsuariosDesdeFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        logList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String username = document.getString("name") + " " + document.getString("surname");
                            String nombreCreador = document.getString("createdBy") != null ? document.getString("createdBy") : "Desconocido";
                            String action = "Registrado por: " + nombreCreador;

                            Timestamp timestamp = document.getTimestamp("createdAt");
                            String formattedDate = convertirFecha(timestamp);

                            logList.add(new LogEntry(username, action, formattedDate));
                        }
                        logAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void cargarRestaurantesDesdeFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("restaurant")
                .orderBy("fechaHoraCreacion", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        logList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("Firestore", "Documento recibido: " + document.getData());
                            RestaurantDTO restaurant = document.toObject(RestaurantDTO.class);

                            // Validar datos
                            String nombreRestaurante = restaurant.getNombreRestaurante() != null ? restaurant.getNombreRestaurante() : "Desconocido";
                            String createdBy = restaurant.getNombreCreador() != null ? restaurant.getNombreCreador() : "Desconocido";
                            Timestamp fechaHoraCreacion = restaurant.getFechaHoraCreacion();

                            // Validar Timestamp
                            if (fechaHoraCreacion == null) {
                                Log.w("Firestore", "El campo fechaHoraCreacion es nulo para " + nombreRestaurante);
                                continue;
                            }

                            // Crear LogEntry
                            String action = "Registrado por: " + createdBy;
                            String formattedDate = convertirFecha(fechaHoraCreacion);

                            logList.add(new LogEntry(nombreRestaurante, action, formattedDate));
                        }

                        logAdapter.notifyDataSetChanged();
                        Log.d("Firestore", "Total de restaurantes cargados: " + logList.size());
                    } else {
                        Log.e("Firestore", "Error al cargar restaurantes: ", task.getException());
                    }
                });
    }


    private String convertirFecha(Timestamp timestamp) {
        if (timestamp != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy - hh:mm a", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("America/Bogota"));
            return sdf.format(timestamp.toDate());
        }
        return "Fecha desconocida";
    }

    private void cargarTodasLasCategoriasDesdeFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        logList.clear(); // Aseguramos que la lista esté vacía al inicio

        final int[] consultasCompletadas = {0}; // Contador para controlar las consultas completadas
        final int totalConsultas = 2; // Número total de consultas

        // Cargar usuarios
        db.collection("users")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String username = document.getString("name") + " " + document.getString("surname");
                            String nombreCreador = document.getString("createdBy") != null ? document.getString("createdBy") : "Desconocido";
                            String action = "Registrado por: " + nombreCreador;

                            Timestamp timestamp = document.getTimestamp("createdAt");
                            String formattedDate = convertirFecha(timestamp);

                            logList.add(new LogEntry(username, action, formattedDate));
                        }
                        Log.d("Firestore", "Usuarios cargados: " + task.getResult().size());
                    } else {
                        Log.e("Firestore", "Error al cargar usuarios: ", task.getException());
                    }
                    consultasCompletadas[0]++;
                    verificarFinalizacion(consultasCompletadas[0], totalConsultas);
                });

        // Cargar restaurantes
        db.collection("restaurants")
                .orderBy("fechaHoraCreacion", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String nombreRestaurante = document.getString("nombreRestaurante") != null ? document.getString("nombreRestaurante") : "Desconocido";
                            String createdBy = document.getString("createdBy") != null ? document.getString("createdBy") : "Desconocido";
                            String action = "Registrado por: " + createdBy;

                            Timestamp timestamp = document.getTimestamp("fechaHoraCreacion");
                            String formattedDate = convertirFecha(timestamp);

                            logList.add(new LogEntry(nombreRestaurante, action, formattedDate));
                        }
                        Log.d("Firestore", "Restaurantes cargados: " + task.getResult().size());
                    } else {
                        Log.e("Firestore", "Error al cargar restaurantes: ", task.getException());
                    }
                    consultasCompletadas[0]++;
                    verificarFinalizacion(consultasCompletadas[0], totalConsultas);
                });
    }

    private void verificarFinalizacion(int consultasCompletadas, int totalConsultas) {
        // Si todas las consultas han completado, actualizamos el adaptador
        if (consultasCompletadas == totalConsultas) {
            logAdapter.notifyDataSetChanged();
            Log.d("Firestore", "Datos combinados cargados correctamente: " + logList.size());
        }
    }





}
