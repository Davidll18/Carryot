package com.example.superadmin.adminrest;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.superadmin.R;
import com.example.superadmin.util.KeyboardUtils;
import com.google.firebase.firestore.FirebaseFirestore;

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
        // Validar que los campos no estén vacío
        if (nombreRestaurante.isEmpty() || categoriaRestaurante.isEmpty() || razonSocial.isEmpty() || ruc.isEmpty() || licenciaFuncionamiento.isEmpty() || permisoSanitario.isEmpty()) {
            // Mostrar el Custom Toast
            showCustomToast("Todos los campos deben ser completados");
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

    // Método para mostrar el Custom Toast
    private void showCustomToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (LinearLayout) findViewById(R.id.toast_container));

        TextView toastText = layout.findViewById(R.id.toast_text);
        toastText.setText(message); // Set the custom message

        ImageView toastImage = layout.findViewById(R.id.toast_image);
        toastImage.setImageResource(R.drawable.shopping_bag); // Set your image here

        Toast customToast = new Toast(getApplicationContext());
        customToast.setDuration(Toast.LENGTH_SHORT);
        customToast.setView(layout); // Set the custom view for the toast
        customToast.show();
    }

    // Llamamos a la clase KeyboardUtils en el dispatchTouchEvent
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return KeyboardUtils.hideKeyboardOnTouch(this, event) || super.dispatchTouchEvent(event);
    }
}
