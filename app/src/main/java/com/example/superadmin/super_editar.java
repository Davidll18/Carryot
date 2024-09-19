package com.example.superadmin;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class super_editar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_editar);
    }

    public void canelar_btn(View view) {
        // Cierra la actividad actual y vuelve a la anterior
        finish();
    }
    public void aceptar_btn(View view) {
        // Cierra la actividad actual y vuelve a la anterior
        finish();
    }
}