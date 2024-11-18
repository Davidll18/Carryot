package com.example.superadmin;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class PutPasswordActivity extends AppCompatActivity {

    private AppCompatButton btnRestorePassword;
    private EditText etPasswordPut, etPasswordRepeat;

    private String providedEmail; // Correo del usuario, recibido de la actividad anterior

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_password);

        // Inicializar vistas
        btnRestorePassword = findViewById(R.id.btn_restore_password);
        etPasswordPut = findViewById(R.id.password_put);
        etPasswordRepeat = findViewById(R.id.password_repeat);

        // Recuperar el correo proporcionado desde la actividad anterior
        providedEmail = getIntent().getStringExtra("email");

        // Validar que el correo no sea nulo ni vacío
        if (TextUtils.isEmpty(providedEmail)) {
            Toast.makeText(this, "No se recibió el correo electrónico. Inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
            finish(); // Finalizar la actividad si no hay correo
            return;
        }

        btnRestorePassword.setOnClickListener(v -> {
            String newPassword = etPasswordPut.getText().toString().trim();
            String repeatPassword = etPasswordRepeat.getText().toString().trim();

            // Validar campos
            if (validatePasswords(newPassword, repeatPassword)) {
                // Actualizar contraseña directamente
                updatePasswordInFirebaseAuth(newPassword);
            }
        });
    }

    // Método para validar las contraseñas ingresadas
    private boolean validatePasswords(String password, String repeatPassword) {
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(repeatPassword)) {
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(repeatPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // Método para actualizar la contraseña en Firebase Authentication
    private void updatePasswordInFirebaseAuth(String newPassword) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser(); // Obtener el usuario autenticado
        if (user == null) {
            Toast.makeText(this, "Error: no se pudo autenticar al usuario. Inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Actualizar la contraseña directamente
        user.updatePassword(newPassword)
                .addOnSuccessListener(aVoid -> {
                    Log.d("FirebaseAuth", "Contraseña actualizada correctamente en Firebase Authentication.");
                    updatePasswordInFirestore(providedEmail, newPassword);
                })
                .addOnFailureListener(e -> {
                    Log.e("FirebaseAuth", "Error al actualizar la contraseña: " + e.getMessage());
                    Toast.makeText(this, "Error al actualizar la contraseña: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Método para actualizar la contraseña en Firestore
    private void updatePasswordInFirestore(String email, String newPassword) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").whereEqualTo("email", email).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        if (!task.getResult().isEmpty()) {
                            String userId = task.getResult().getDocuments().get(0).getId();
                            Log.d("FirestoreDebug", "Usuario encontrado con ID: " + userId);

                            // Actualizar contraseña
                            db.collection("users").document(userId).update("password", newPassword)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(PutPasswordActivity.this, "Contraseña restablecida con éxito.", Toast.LENGTH_SHORT).show();
                                        finish(); // Finalizar la actividad actual
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("FirestoreDebug", "Error al actualizar la contraseña en Firestore: " + e.getMessage());
                                        Toast.makeText(PutPasswordActivity.this, "Error al actualizar la contraseña en Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Log.d("FirestoreDebug", "No se encontraron documentos con el correo proporcionado.");
                            Toast.makeText(PutPasswordActivity.this, "No se pudo encontrar el usuario.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("FirestoreDebug", "Error al realizar la consulta: " + task.getException().getMessage());
                        Toast.makeText(PutPasswordActivity.this, "Error al realizar la consulta.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
