package com.example.superadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class super_estadisticas_por_rest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_estadisticas_por_rest);

        // Obtener los datos del Intent
        Intent intent = getIntent();
        String restaurantName = intent.getStringExtra("RESTAURANT_NAME");
        int restaurantImage = intent.getIntExtra("RESTAURANT_IMAGE", R.drawable.registrar_admin_rest); // Imagen por defecto si no se recibe

        // Configura los views con la información recibida
        TextView restaurantNameTextView = findViewById(R.id.restaurantName);
        ImageView restaurantImageView = findViewById(R.id.restaurantImage);

        restaurantNameTextView.setText(restaurantName);
        restaurantImageView.setImageResource(restaurantImage);
    }

}