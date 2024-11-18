package com.example.superadmin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.auth.FirebaseAuth;

public class PasswordRecoveryActivity extends AppCompatActivity {

    private AppCompatButton btnSendCode; // Respetando tu ID original
    private EditText etEmailCode; // Respetando tu ID original

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);

        btnSendCode = findViewById(R.id.btn_send_code); // Respetando tu ID original
        etEmailCode = findViewById(R.id.et_email_code); // Respetando tu ID original

        btnSendCode.setOnClickListener(v -> {
            String email = etEmailCode.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(PasswordRecoveryActivity.this, "Por favor, ingrese un correo electrónico", Toast.LENGTH_SHORT).show();
                return;
            }

            sendPasswordResetEmail(email);
        });
    }

    private void sendPasswordResetEmail(String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // Enviar el correo de restablecimiento de contraseña
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(PasswordRecoveryActivity.this, "Enlace de restablecimiento de contraseña enviado al correo.", Toast.LENGTH_SHORT).show();

                        // Redirigir al LoginActivity
                        Intent intent = new Intent(PasswordRecoveryActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish(); // Finalizar esta actividad
                    } else {
                        Toast.makeText(PasswordRecoveryActivity.this, "Error al enviar el enlace: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
