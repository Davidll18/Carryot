package com.example.superadmin.user;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.superadmin.R;

public class UserProductDetailActivity extends AppCompatActivity {

    private ImageView imageViewProduct, btnMinus, btnAdd;
    private TextView textViewName, textViewModel, textViewDescription, textViewPrice, textViewAmount, textNumberProducts;
    //private ImageButton btnMinus, btnAdd;

    private int cantidad = 1; // Cantidad inicial
    private double precioUnitario; // Precio unitario del producto

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Inicializar vistas
        textViewName = findViewById(R.id.text_name_product);
        textViewModel = findViewById(R.id.text_model_product);
        textViewDescription = findViewById(R.id.textView);
        textViewPrice = findViewById(R.id.text_amount);
        textViewAmount = findViewById(R.id.text_amount_total);
        textNumberProducts = findViewById(R.id.text_number_products);
        btnMinus = findViewById(R.id.btn_minus_numbers_products);
        btnAdd = findViewById(R.id.btn_add_numbers_producst);

        // Obtener datos desde el Intent
        String nombrePlato = getIntent().getStringExtra("nombrePlato");
        String descripcion = getIntent().getStringExtra("descripcion");
        String precio = getIntent().getStringExtra("precio");

        // Parsear el precio unitario
        precioUnitario = Double.parseDouble(precio);
        cantidad = 1; // Cantidad inicial

        // Asignar valores a las vistas
        textViewName.setText(nombrePlato);
        textViewModel.setText("Modelo Taypa");
        textViewDescription.setText(descripcion);
        textViewPrice.setText("S/ " + precio);
        textNumberProducts.setText(String.valueOf(cantidad)); // Cantidad inicial
        actualizarPrecioTotal();

        // Configurar botones de cantidad
        btnAdd.setOnClickListener(v -> aumentarCantidad());
        btnMinus.setOnClickListener(v -> disminuirCantidad());
    }

    private void aumentarCantidad() {
        cantidad++;
        textNumberProducts.setText(String.valueOf(cantidad));
        actualizarPrecioTotal();
    }

    private void disminuirCantidad() {
        if (cantidad > 1) {
            cantidad--;
            textNumberProducts.setText(String.valueOf(cantidad));
            actualizarPrecioTotal();
        }
    }

    private void actualizarPrecioTotal() {
        double precioTotal = cantidad * precioUnitario;
        textViewAmount.setText(String.format("S/ %.2f", precioTotal));
    }
}
