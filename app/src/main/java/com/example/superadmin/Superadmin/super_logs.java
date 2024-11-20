package com.example.superadmin.Superadmin;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superadmin.R;
import com.example.superadmin.adapters.LogAdapter;
import com.example.superadmin.model.LogEntry;

import java.util.ArrayList;
import java.util.List;

public class super_logs extends AppCompatActivity {

    private RecyclerView recyclerViewLogs;
    private LogAdapter logAdapter;
    private List<LogEntry> logList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_super_logs);

        recyclerViewLogs = findViewById(R.id.recyclerViewLogs);

        recyclerViewLogs.setLayoutManager(new LinearLayoutManager(this));


        logList = new ArrayList<>();
        logList.add(new LogEntry("usuario1", "Inició sesión", "2024-10-22 10:30"));
        logList.add(new LogEntry("usuario2", "Realizó un pedido", "2024-10-22 11:00"));
        logList.add(new LogEntry("usuario3", "Cerró sesión", "2024-10-22 11:30"));

        logAdapter = new LogAdapter(logList);
        recyclerViewLogs.setAdapter(logAdapter);

    }
}