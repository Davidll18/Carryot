package com.example.superadmin.adminrest;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.superadmin.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;


public class NewDishActivity extends AppCompatActivity {
    private EditText edPlatoName, edDesc, edPrecio;
    private Spinner categoriaPlato;
    private AppCompatButton btnGuardar, btnCancelar, btnSelectImage;
    private ImageView selectedImageView;
    private Uri selectedImageUri;
    private FirebaseStorage storage;
    private static final int PICK_IMAGE_REQUEST = 1;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_dishes_adminrest);

        edPlatoName = findViewById(R.id.ed_nombrePlato);
        edDesc = findViewById(R.id.ed_desc);
        edPrecio = findViewById(R.id.ed_precio);

        // Configurar el Spinner con el adaptador
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categorias_platos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriaPlato.setAdapter(adapter);
    }
}
