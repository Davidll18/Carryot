package com.example.superadmin.adminrest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.superadmin.R;

import java.util.ArrayList;

public class RestaurantActivity extends AppCompatActivity {
    private EditText edName, edCategoria, edRs, edRuc, edFunc, edSanit;
    private AppCompatButton btnSiguiente;
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
        btnSiguiente = findViewById(R.id.btn_login);

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
                compararDatos(nombreRestaurante, categoriaRestaurante, razonSocial, ruc, licenciaFuncionamiento, permisoSanitario);
            }
        });
    }
    private void compararDatos(String nombreRestaurante, String categoriaRestaurante, String razonSocial, String ruc, String licenciaFuncionamiento, String permisoSanitario) {
        // Datos estáticos para comparación
        String nombreEstatico = "AA";
        String categoriaEstatica = "BB";
        String razonSocialEstatica = "CC";
        String rucEstatico = "11";
        String licenciaEstatica = "22";
        String permisoEstatico = "33";

        // Lista para almacenar los errores
        ArrayList<String> errores = new ArrayList<>();

        if (!(nombreRestaurante.equals(nombreEstatico))) {
            errores.add("Nombre del restaurante");
        }
        if (!(categoriaRestaurante.equals(categoriaEstatica))) {
            errores.add("Categoría");
        }
        if (!(razonSocial.equals(razonSocialEstatica))) {
            errores.add("Razón Social");
        }
        if (!(ruc.equals(rucEstatico))) {
            errores.add("R.U.C.");
        }
        if (!(licenciaFuncionamiento.equals(licenciaEstatica))) {
            errores.add("Lic. de funcionamiento");
        }
        if (!(permisoSanitario.equals(permisoEstatico))) {
            errores.add("Permiso sanitario");
        }

        // Si la lista de errores está vacía, los datos coinciden
        if (!(errores.isEmpty())) {
            StringBuilder mensajeErrores = new StringBuilder("Errores encontrados:\n");
            for (String error : errores) {
                mensajeErrores.append("- ").append(error).append("\n");
            }

            new AlertDialog.Builder(this)
                    .setTitle("Errores de validación")
                    .setMessage(mensajeErrores.toString())
                    .setPositiveButton("Aceptar", null)
                    .show();

        } else {
            Intent intent = new Intent(RestaurantActivity.this, NewRestaurantActivity.class);
            intent.putExtra("nombreRestaurante",nombreRestaurante);
            startActivity(intent);
            //envio_datos_perfil(nombreRestaurante, categoriaRestaurante, razonSocial, ruc, licenciaFuncionamiento, permisoSanitario);
        }
    }

    /*private void envio_datos_perfil(String nombreRestaurante, String categoriaRestaurante, String  razonSocial, String ruc, String licenciaFuncionamiento, String permisoSanitario) {
        Intent intent = new Intent(RestaurantActivity.this, ProfileRestActivity.class);
        intent.putExtra("nombreRestaurante", nombreRestaurante);
        intent.putExtra("categoriaRestaurante", categoriaRestaurante);
        intent.putExtra("razonSocial", razonSocial);
        intent.putExtra("ruc", ruc);
        intent.putExtra("licenciaFuncionamiento", licenciaFuncionamiento);
        intent.putExtra("permisoSanitario", permisoSanitario);
        startActivity(intent);

    }*/
}

