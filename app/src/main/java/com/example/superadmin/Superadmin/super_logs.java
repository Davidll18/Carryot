package com.example.superadmin.Superadmin;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superadmin.R;
import com.example.superadmin.adapters.LogAdapter;
import com.example.superadmin.model.LogEntry;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class super_logs extends AppCompatActivity {

    private RecyclerView recyclerViewLogs;
    private LogAdapter logAdapter;
    private List<LogEntry> logList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_super_logs);

        // Inicializar RecyclerView
        recyclerViewLogs = findViewById(R.id.recyclerViewLogs);
        recyclerViewLogs.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar lista y adaptador
        logList = new ArrayList<>();
        logAdapter = new LogAdapter(logList);
        recyclerViewLogs.setAdapter(logAdapter);

        // Cargar logs desde Firestore
        cargarLogsDesdeFirestore();

        // Configurar el color de la barra de estado
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.red_boton));
    }

    private void cargarLogsDesdeFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .orderBy("createdAt", Query.Direction.DESCENDING) // Ordenar por fecha descendente
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        logList.clear(); // Limpia la lista antes de agregar nuevos datos
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String username = document.getString("name") + " " + document.getString("surname");
                            String action = "Registrado";

                            // Formatear la fecha de Firestore a un formato legible
                            Timestamp timestamp = document.getTimestamp("createdAt");
                            String formattedDate = convertirFecha(timestamp);

                            logList.add(new LogEntry(username, action, formattedDate));
                        }
                        logAdapter.notifyDataSetChanged(); // Actualiza el RecyclerView
                    } else {
                        Log.e("Firestore", "Error al obtener los datos", task.getException());
                    }
                });
    }

    private String convertirFecha(Timestamp timestamp) {
        if (timestamp != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(timestamp.toDate());

            // Ajustar la zona horaria (por ejemplo, UTC-5)
            calendar.add(Calendar.HOUR, -5);

            // Formatear la fecha ajustada
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy - hh:mm a", Locale.getDefault());
            return sdf.format(calendar.getTime());
        } else {
            return "Fecha desconocida";
        }
    }
}
