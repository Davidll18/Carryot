package com.example.superadmin.adminrest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.superadmin.R;
import com.example.superadmin.dtos.RestaurantDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RestaurantActivity extends AppCompatActivity {
    private EditText edName, edRs, edRuc, edFunc, edSanit;
    private Spinner spinnerCategoria;
    private AppCompatButton btnSiguiente;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newrestaurant_adminrest);

        // Inicializar los campos
        edName = findViewById(R.id.ed_name);
        spinnerCategoria = findViewById(R.id.spinner_categoria); // Referencia al Spinner
        edRs = findViewById(R.id.ed_rs);
        edRuc = findViewById(R.id.ed_ruc);
        edFunc = findViewById(R.id.ed_func);
        edSanit = findViewById(R.id.ed_sanit);

        // Configurar el Spinner con el adaptador
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categorias_restaurantes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);

        // Inicializar el botón SIGUIENTE
        btnSiguiente = findViewById(R.id.btn_siguiente);
        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        // Acción cuando el usuario presiona "Siguiente"
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los valores de los campos de texto
                String nombreRestaurante = edName.getText().toString();
                String categoriaRestaurante = spinnerCategoria.getSelectedItem().toString(); // Obtener categoría seleccionada
                String razonSocial = edRs.getText().toString();
                String ruc = edRuc.getText().toString();
                String licenciaFuncionamiento = edFunc.getText().toString();
                String permisoSanitario = edSanit.getText().toString();

                // Llamar método de comparación
                validarYGuardarDatos(nombreRestaurante, categoriaRestaurante, razonSocial, ruc, licenciaFuncionamiento, permisoSanitario);
            }
        });
    }

    private void validarYGuardarDatos(String nombreRestaurante, String categoriaRestaurante, String razonSocial, String ruc, String licenciaFuncionamiento, String permisoSanitario) {
        // Validar que los campos no estén vacíos
        if (nombreRestaurante.isEmpty() || categoriaRestaurante.isEmpty() || razonSocial.isEmpty() || ruc.isEmpty() || licenciaFuncionamiento.isEmpty() || permisoSanitario.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Todos los campos deben ser completados")
                    .setPositiveButton("Aceptar", null)
                    .show();
            return;
        }

        // Crear un Intent para ir a la siguiente pantalla
        Intent intent = new Intent(RestaurantActivity.this, NewRestaurantActivity.class);

        // Pasar los datos como extras en el Intent
        intent.putExtra("nombreRestaurante", nombreRestaurante);
        intent.putExtra("categoriaRestaurante", categoriaRestaurante);
        intent.putExtra("razonSocial", razonSocial);
        intent.putExtra("ruc", ruc);
        intent.putExtra("licenciaFuncionamiento", licenciaFuncionamiento);
        intent.putExtra("permisoSanitario", permisoSanitario);

        // Iniciar la actividad
        startActivity(intent);
    }
}
