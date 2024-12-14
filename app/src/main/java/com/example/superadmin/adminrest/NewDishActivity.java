package com.example.superadmin.adminrest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.superadmin.R;
import com.example.superadmin.dtos.PlatoDTO;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


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
        btnGuardar = findViewById(R.id.btn_guardar);
        btnCancelar = findViewById(R.id.btn_cancelar);
        btnSelectImage = findViewById(R.id.btn_seleccionar_foto);
        selectedImageView = findViewById(R.id.img_foto);
        categoriaPlato = findViewById(R.id.spinner_categoria);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // Configurar el Spinner con el adaptador
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categorias_platos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriaPlato.setAdapter(adapter);
        btnSelectImage.setOnClickListener(v -> openImagePicker());

        btnGuardar.setOnClickListener(v -> {
            String platoName = edPlatoName.getText().toString();
            String spinnerCategoriaPlato = categoriaPlato.getSelectedItem().toString(); // Obtener categoría seleccionada
            String descripcion = edDesc.getText().toString();
            String price = edPrecio.getText().toString();
            validarDatos(platoName, spinnerCategoriaPlato, descripcion, price);
            if (selectedImageUri != null) {
                savePlato(platoName, spinnerCategoriaPlato, descripcion, price);
            } else {
                Toast.makeText(this, "Por favor, selecciona una imagen", Toast.LENGTH_SHORT).show();
            }

        });
        // Botón "Cancelar"
        btnCancelar.setOnClickListener(v -> finish());
        
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
    private void validarDatos(String platoName, String categoriaPlato, String descripcion, String price) {
        // Validar que los campos no estén vacío
        if (platoName.isEmpty() || categoriaPlato.isEmpty() ||descripcion.isEmpty() || price.isEmpty()) {
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

    private void savePlato(String nombrePlato, String categoriaPlato,
                           String descripcion, String precio) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uidCreador = currentUser.getUid();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(uidCreador)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("name");
                            String surname = documentSnapshot.getString("surname");
                            String nombreCreador = (name != null ? name : "") + " " + (surname != null ? surname : "");

                            // Buscar el restaurante con el uidCreador
                            db.collection("restaurantes")
                                    .whereEqualTo("uidCreador", uidCreador) // Filtrar por uidCreador
                                    .get()
                                    .addOnSuccessListener(queryDocumentSnapshots -> {
                                        if (!queryDocumentSnapshots.isEmpty()) {
                                            // Obtener el primer restaurante que coincida
                                            DocumentSnapshot restauranteSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                            String uidRestaurante = restauranteSnapshot.getString("uidRestaurante");

                                            if (uidRestaurante != null) {
                                                // Subir imagen y guardar los datos del plato junto con el uid del restaurante
                                                uploadImageAndSavePlato(nombrePlato, categoriaPlato, descripcion, precio, uidCreador, nombreCreador, uidRestaurante);
                                            } else {
                                                Toast.makeText(this, "No se encontró el uidRestaurante en el restaurante.", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(this, "No se encontró un restaurante para este usuario.", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(e ->
                                            Toast.makeText(this, "Error al buscar el restaurante: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                        } else {
                            Toast.makeText(this, "Usuario no encontrado en Firestore", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Error al obtener datos del usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "No hay un usuario autenticado.", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageAndSavePlato(String nombrePlato, String categoriaPlato, String descripcion, String precio,
                                         String uidCreador, String nombreCreador, String uidRestaurante) {

        StorageReference storageReference = storage.getReference().child("platos_images/" + UUID.randomUUID().toString());

        storageReference.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();

                            // Guardar el plato en Firestore
                            savePlatoToFirestore(nombrePlato, categoriaPlato, descripcion, precio, uidCreador, nombreCreador, imageUrl, uidRestaurante);
                        }))
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error al subir la imagen: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void savePlatoToFirestore(String nombrePlato, String categoriaPlato, String descripcion, String precio,
                                      String uidCreador, String nombreCreador, String imageUrl, String uidRestaurante) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String platoId = UUID.randomUUID().toString();
        Boolean disponible = true;
        int cantidad = 20;

        PlatoDTO platoDTO = new PlatoDTO(
        cantidad, categoriaPlato, descripcion,
        disponible, imageUrl, nombreCreador, platoId, nombrePlato,precio, uidCreador, uidRestaurante
        );

        // Guardar el plato en Firestore
        db.collection("platos").document(platoId).set(platoDTO)
                .addOnSuccessListener(documentReference ->{
                    Toast.makeText(this, "Plato guardado correctamente.", Toast.LENGTH_SHORT).show();
                    // Redirigir
                    Intent intent = new Intent(this, DishesActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error al guardar el plato: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }


}
