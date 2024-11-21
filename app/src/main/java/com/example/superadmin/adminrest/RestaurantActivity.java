package com.example.superadmin.adminrest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
    private EditText edName, edCategoria, edRs, edRuc, edFunc, edSanit;
    private AppCompatButton btnSiguiente;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newrestaurant_adminrest);

        // Inicializar los campos
        edName = findViewById(R.id.ed_name);
        edCategoria = findViewById(R.id.ed_categoria);
        edRs = findViewById(R.id.ed_rs);
        edRuc = findViewById(R.id.ed_ruc);
        edFunc = findViewById(R.id.ed_func);
        edSanit = findViewById(R.id.ed_sanit);

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
                String categoriaRestaurante = edCategoria.getText().toString();
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
        // Obtener los datos del usuario actual (suponiendo que ya tienes FirebaseAuth configurado)
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            // Obtener el UID y la información del usuario
            String uidCreador = currentUser.getUid();
            String nombreCreador = currentUser.getDisplayName() != null ? currentUser.getDisplayName() : "Nombre no disponible";
            String uidCreacion = UUID.randomUUID().toString();

            // Crear un objeto RestaurantDTO con los datos ingresados
            RestaurantDTO restaurantDTO = new RestaurantDTO(
                    nombreRestaurante,
                    categoriaRestaurante,
                    razonSocial,
                    ruc,
                    licenciaFuncionamiento,
                    permisoSanitario,
                    uidCreador,
                    nombreCreador,
                    uidCreacion
            );

            // Guardar los datos en la colección "restaurant"
            db.collection("restaurant").document(uidCreacion)  // Aquí usamos el UID único generado para el restaurante
                    .set(restaurantDTO)
                    .addOnSuccessListener(documentReference -> {
                        // Mostrar mensaje de éxito
                        Toast.makeText(RestaurantActivity.this, "Restaurante registrado exitosamente", Toast.LENGTH_SHORT).show();

                        // Ir a la siguiente actividad
                        Intent intent = new Intent(RestaurantActivity.this, NewRestaurantActivity.class);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> {
                        // Mostrar mensaje de error
                        Toast.makeText(RestaurantActivity.this, "Error al registrar en Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Si no hay un usuario autenticado
            Toast.makeText(this, "No hay usuario autenticado", Toast.LENGTH_SHORT).show();
        }
    }
}