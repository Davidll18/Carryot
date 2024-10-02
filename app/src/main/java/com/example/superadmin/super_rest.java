package com.example.superadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class super_rest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_rest);
        // Obtener los CardView por su ID
        CardView cardKFC = findViewById(R.id.cardKFC);
        CardView cardBembos = findViewById(R.id.cardBembos);
        // Agrega otros CardView según lo necesites

        // Listener para el CardView de KFC
        cardKFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigir a otra pestaña o actividad
                Intent intent = new Intent(super_rest.this, super_estadisticas_por_rest.class); // Reemplaza con la actividad o fragmento correcto
                startActivity(intent);
            }
        });

        // Listener para el CardView de Bembos
        cardBembos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigir a otra pestaña o actividad
                Intent intent = new Intent(super_rest.this, super_estadisticas_por_rest.class); // Reemplaza con la actividad o fragmento correcto
                startActivity(intent);
            }
        });

    }

}