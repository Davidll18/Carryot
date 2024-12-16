package com.example.superadmin.adminrest;

import com.bumptech.glide.Glide;
import com.example.superadmin.R;
import com.example.superadmin.dtos.PlatoDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class EditDishesActivity extends AppCompatActivity {
    private Uri selectedImageUri;
    private FirebaseStorage storage;
    private static final int PICK_IMAGE_REQUEST = 1;
    private FirebaseFirestore db;
    AppCompatButton selectImageButton;
    ImageView selectedImageView;
    EditText descripcionEditText,precioEditText;
    TextView categoriaPlato,nombrePlatoTextView;
    AppCompatButton guardarButton,cancelarButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editdishes_adminrest);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        Intent intent = getIntent();
        String uidPlato = intent.getStringExtra("uidPlato");
        String nombrePlato = intent.getStringExtra("nombrePlato");
        String precio = intent.getStringExtra("precio");
        String descripcion = intent.getStringExtra("descripcion");
        String cantidad = intent.getStringExtra("cantidad");
        String categoria = intent.getStringExtra("categoria");
        boolean disponible = intent.getBooleanExtra("disponible", true);
        String imageUrl = intent.getStringExtra("imageUrl");

        // Referenciar los elementos de la vista
        nombrePlatoTextView = findViewById(R.id.name_tv); // Asegúrate de tener este ID en tu XML
        selectedImageView = findViewById(R.id.img_foto);
        descripcionEditText = findViewById(R.id.ed_desc);
        precioEditText = findViewById(R.id.ed_precio);
        categoriaPlato = findViewById(R.id.categoria);
        guardarButton = findViewById(R.id.btn_guardar);
        cancelarButton = findViewById(R.id.btn_cancelar);
        selectImageButton = findViewById(R.id.btn_seleccionar_foto);
        // Establecer los valores en los elementos correspondientes
        nombrePlatoTextView.setText(nombrePlato);
        descripcionEditText.setText(descripcion);
        precioEditText.setText(precio);
        categoriaPlato.setText(categoria);
        Glide.with(this)
                .load(imageUrl) // URL de la imagen
                .into(selectedImageView);

        selectImageButton.setOnClickListener(v -> openImagePicker());

        guardarButton.setOnClickListener(view -> {
            // Obtener los valores actuales de los campos
            String nuevaDescripcion = descripcionEditText.getText().toString();
            String nuevoPrecio = precioEditText.getText().toString();

            validarDatos(nuevaDescripcion, nuevoPrecio);
            // Subir la imagen si se seleccionó, o guardar con la imagen actual
            if (selectedImageUri != null) {
                uploadImageAndSavePlato(nuevaDescripcion, nuevoPrecio, uidPlato);
            } else{
                // Guardar el plato en Firestore
                savePlatoToFirestore(descripcion, precio,imageUrl, uidPlato);
            }
        });
        // Botón "Cancelar"
        cancelarButton.setOnClickListener(v -> finish());
    }
    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            selectedImageView.setImageURI(selectedImageUri); // Mostrar la imagen seleccionada
        }
    }
    private void validarDatos( String descripcion, String price) {
        // Validar que los campos no estén vacío
        if (descripcion.isEmpty() || price.isEmpty()) {
            // Mostrar el Custom Toast
            showCustomToast("Todos los campos deben ser completados");
            return;
        }
    }
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


    private void uploadImageAndSavePlato( String descripcion, String precio, String uidPlato) {
        // Crear referencia en Firebase Storage
        StorageReference storageReference = storage.getReference().child("platos_images/" + UUID.randomUUID().toString());

        // Subir imagen a Storage
        storageReference.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot ->
                        // Obtener la URL de descarga
                        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrlNew = uri.toString();

                            // Guardar el plato en Firestore
                            savePlatoToFirestore( descripcion, precio, imageUrlNew, uidPlato);
                        })
                )
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error al subir la imagen: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    private void savePlatoToFirestore( String descripcion, String precio, String imageUrl, String uidPlato) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Crear un mapa con los nuevos datos
        Map<String, Object> plato = new HashMap<>();
        plato.put("descripcion", descripcionEditText.getText().toString());
        plato.put("precio", precioEditText.getText().toString());
        plato.put("imageUrl", imageUrl);

        // Actualizar el documento en Firestore
        db.collection("platos").document(uidPlato)
                .update(plato)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Plato guardado correctamente.", Toast.LENGTH_SHORT).show();
                    // Redirigir
                    Intent intent = new Intent(this, DishesActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error al guardar el plato: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );

    }
}
