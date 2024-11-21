package com.example.superadmin.adminrest;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.superadmin.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileRestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restprofile);

        // Referencias a los campos del layout
        ImageView imageView = findViewById(R.id.imageView4);
        TextView tvName = findViewById(R.id.tvname);
        TextView tvCategoria = findViewById(R.id.tvCategoria);
        TextView tvRS = findViewById(R.id.tv_rs);
        TextView tvRUC = findViewById(R.id.tv_ruc);
        TextView tvLF = findViewById(R.id.tv_lf);
        TextView tvPS = findViewById(R.id.tv_ps);
        TextView tvDes = findViewById(R.id.tv_des);
        TextView tvUb = findViewById(R.id.tv_ub);

        // Recupera el UID desde el Intent
        String uidRestaurante = getIntent().getStringExtra("uidRestaurante");

        if (uidRestaurante != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Recupera el documento del restaurante desde Firestore
            db.collection("restaurant").document(uidRestaurante)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Mapea los datos del documento al DTO
                            String nombreRest = documentSnapshot.getString("nombreRestaurante");
                            String categoria = documentSnapshot.getString("categoriaRestaurante");
                            String razonSocial = documentSnapshot.getString("razonSocial");
                            String ruc = documentSnapshot.getString("ruc");
                            String licenciaFuncionamiento = documentSnapshot.getString("licenciaFuncionamiento");
                            String permisoSanitario = documentSnapshot.getString("permisoSanitario");
                            String descripcion = documentSnapshot.getString("descripcion");
                            String ubicacion = documentSnapshot.getString("location");
                            String urlImagen = documentSnapshot.getString("imageUrl"); // Ruta de la imagen en Firestore

                            // Asigna los datos a los campos
                            tvName.setText(nombreRest != null ? nombreRest : "Sin nombre");
                            tvCategoria.setText(categoria != null ? categoria : "Sin categoría");
                            tvRS.setText(razonSocial != null ? razonSocial : "Sin razón social");
                            tvRUC.setText(ruc != null ? ruc : "Sin RUC");
                            tvLF.setText(licenciaFuncionamiento != null ? licenciaFuncionamiento : "Sin licencia");
                            tvPS.setText(permisoSanitario != null ? permisoSanitario : "Sin permiso");
                            tvDes.setText(descripcion != null ? descripcion : "Sin descripción");
                            tvUb.setText(ubicacion != null ? ubicacion : "Sin ubicación");

                            // Carga la imagen en el ImageView usando Glide o Picasso
                            if (urlImagen != null && !urlImagen.isEmpty()) {
                                Glide.with(this)
                                        .load(urlImagen)
                                        .placeholder(R.drawable.logo) // Imagen por defecto
                                        .error(R.drawable.logo)             // Imagen si hay error
                                        .into(imageView);
                            } else {
                                imageView.setImageResource(R.drawable.logo); // Imagen por defecto
                            }
                        } else {
                            Toast.makeText(this, "El restaurante no existe", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Error al cargar datos: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        } else {
            Toast.makeText(this, "No se recibió el UID del restaurante", Toast.LENGTH_SHORT).show();
        }
    }

}
