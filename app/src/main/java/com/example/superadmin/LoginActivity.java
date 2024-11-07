package com.example.superadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    AppCompatButton btnLogin;
    AppCompatButton btnSignUp;
    TextView tvForgetPassword;
    EditText etEmail, etPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        // Inicializar vistas
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_get_into_login);
        btnSignUp = findViewById(R.id.btn_signup);
        tvForgetPassword = findViewById(R.id.tv_forget_password);

        // Manejar clic en el botón de inicio de sesión
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Iniciar sesión con Firebase Authentication
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Inicio de sesión exitoso
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                if (user != null && user.isEmailVerified()) {
                                    Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class)); // Redirigir a la actividad deseada
                                    finish(); // Finalizar la actividad actual
                                } else {
                                    Toast.makeText(LoginActivity.this, "Por favor, verifique su correo electrónico", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Error en el inicio de sesión
                                Toast.makeText(LoginActivity.this, "Error al iniciar sesión: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // Manejar clic en el botón de registro
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        // Manejar clic en el texto de "olvidé mi contraseña"
        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, PasswordRecoveryActivity.class));
            }
        });
    }

    private void showCustomToast() {
        // Inflar el layout personalizado
        LayoutInflater inflater = getLayoutInflater();
        View toastView = inflater.inflate(R.layout.custom_toast, null);

        // Crear el Toast personalizado
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastView);
        toast.show();
    }
}
