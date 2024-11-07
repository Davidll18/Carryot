package com.example.superadmin.adminrest;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.superadmin.R;

public class ProfileRestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restprofile);

        // Recibir los datos del Intent
        String nombreRestaurante = getIntent().getStringExtra("nombreRestaurante");
        String categoriaRestaurante = getIntent().getStringExtra("categoriaRestaurante");
        String razonSocial = getIntent().getStringExtra("razonSocial");
        String ruc = getIntent().getStringExtra("ruc");
        String licenciaFuncionamiento = getIntent().getStringExtra("licenciaFuncionamiento");
        String permisoSanitario = getIntent().getStringExtra("permisoSanitario");

        String descripcion = getIntent().getStringExtra("descripcion");
        String ubicacion = getIntent().getStringExtra("ubicacion");

        // Mostrar los datos en la interfaz de usuario
        TextView nombreTextView = findViewById(R.id.tvname);
        nombreTextView.setText(nombreRestaurante);

        TextView categoriaTextView = findViewById(R.id.tvCategoria);
        categoriaTextView.setText(categoriaRestaurante);

        TextView razonSocialTextView = findViewById(R.id.tv_rs);
        razonSocialTextView.setText(razonSocial);

        TextView rucTextView = findViewById(R.id.tv_ruc);
        rucTextView.setText(ruc);

        TextView lfTextView = findViewById(R.id.tv_lf);
        lfTextView.setText(licenciaFuncionamiento);

        TextView psTextView = findViewById(R.id.tv_ps);
        psTextView.setText(permisoSanitario);

        TextView desTextView = findViewById(R.id.tv_des);
        desTextView.setText(descripcion);

        TextView ubTextView = findViewById(R.id.tv_ub);
        ubTextView.setText(ubicacion);

    }
}
