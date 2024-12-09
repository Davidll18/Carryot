package com.example.superadmin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
                showCustomToast("Por favor, ingrese un correo electrónico");
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
                        showCustomToast("Enlace de restablecimiento de contraseña enviado al correo.");

                        // Redirigir al LoginActivity
                        Intent intent = new Intent(PasswordRecoveryActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish(); // Finalizar esta actividad
                    } else {
                        showCustomToast("Error al enviar el enlace.");
                    }
                });
    }

    // Función para mostrar un Toast personalizado
    private void showCustomToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View customToastView = inflater.inflate(R.layout.custom_toast, findViewById(R.id.toast_container));

        TextView toastText = customToastView.findViewById(R.id.toast_text);
        ImageView toastImage = customToastView.findViewById(R.id.toast_image);

        toastText.setText(message);
        toastImage.setImageResource(R.drawable.shopping_bag);  // Cambia esto por tu imagen o ícono si lo deseas

        Toast customToast = new Toast(getApplicationContext());
        customToast.setDuration(Toast.LENGTH_LONG);
        customToast.setView(customToastView);
        customToast.show();
    }
}
