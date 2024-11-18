package com.example.superadmin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PasswordCodeActivity extends AppCompatActivity {

    private EditText etCode1, etCode2, etCode3, etCode4;
    private AppCompatButton btnGoChangePassword;

    private String providedEmail; // Correo ingresado previamente en PasswordRecoveryActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_code);

        // Inicializar vistas
        etCode1 = findViewById(R.id.et_code_1);
        etCode2 = findViewById(R.id.et_code_2);
        etCode3 = findViewById(R.id.et_code_3);
        etCode4 = findViewById(R.id.et_code_4);
        btnGoChangePassword = findViewById(R.id.btn_go_change_password);

        // Recuperar el correo proporcionado desde PasswordRecoveryActivity
        providedEmail = getIntent().getStringExtra("email");

        // Configurar evento del botón
        btnGoChangePassword.setOnClickListener(v -> {
            String verificationCode = getCodeFromInputs();

            // Validar si los 4 campos están completos
            if (verificationCode.length() == 4) {
                // Validar el código
                validateCode(providedEmail, verificationCode);
            } else {
                Toast.makeText(PasswordCodeActivity.this, "Por favor, ingrese los 4 dígitos del código.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para concatenar los valores de los EditText
    private String getCodeFromInputs() {
        return etCode1.getText().toString() +
                etCode2.getText().toString() +
                etCode3.getText().toString() +
                etCode4.getText().toString();
    }

    // Método para validar el código con Firebase
    private void validateCode(String email, String enteredCode) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Buscar el usuario con el correo proporcionado
        db.collection("users").whereEqualTo("email", email).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);

                        // Obtener datos del usuario
                        String storedCode = document.getString("recoveryCode");
                        long codeTimestamp = document.getLong("recoveryCodeTimestamp");
                        long validityPeriod = document.getLong("recoveryCodeValidity");

                        // Validar código y tiempo
                        if (storedCode != null && storedCode.equals(enteredCode)) {
                            long currentTime = System.currentTimeMillis();
                            if (currentTime <= codeTimestamp + validityPeriod) {
                                Toast.makeText(PasswordCodeActivity.this, "Código verificado exitosamente.", Toast.LENGTH_SHORT).show();

                                // Redirigir a la actividad para cambiar la contraseña y pasar el correo
                                Intent intent = new Intent(PasswordCodeActivity.this, PutPasswordActivity.class);
                                intent.putExtra("email", email); // Pasar el correo
                                startActivity(intent);
                                finish(); // Finalizar la actividad actual
                            } else {
                                Toast.makeText(PasswordCodeActivity.this, "El código ha expirado.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(PasswordCodeActivity.this, "El código ingresado no es válido.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(PasswordCodeActivity.this, "Correo no encontrado.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
