package com.example.superadmin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.superadmin.Superadmin.super_estadisticas_general;
import com.example.superadmin.adminrest.RestaurantActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    AppCompatButton btnLogin;
    AppCompatButton btnSignUp;
    TextView tvForgetPassword;
    EditText etEmail, etPassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

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

        // Inicializar FirebaseAuth y Firestore
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Inicializar vistas
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_get_into_login);
        btnSignUp = findViewById(R.id.btn_signup);
        tvForgetPassword = findViewById(R.id.tv_forget_password);

        // Manejar clic en el botón de inicio de sesión
        btnLogin.setOnClickListener(v -> {
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
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null && user.isEmailVerified()) {
                                // Consultar Firestore para obtener el rol del usuario
                                db.collection("users").document(user.getUid()).get()
                                        .addOnCompleteListener(roleTask -> {
                                            if (roleTask.isSuccessful() && roleTask.getResult() != null) {
                                                DocumentSnapshot document = roleTask.getResult();
                                                if (document.exists()) {
                                                    String role = document.getString("role");
                                                    redirectToRoleSpecificActivity(role);
                                                } else {
                                                    Toast.makeText(LoginActivity.this, "Error: No se encontró el rol del usuario", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(LoginActivity.this, "Error al obtener los datos del usuario: " + roleTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                Toast.makeText(LoginActivity.this, "Por favor, verifique su correo electrónico", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Error al iniciar sesión: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Manejar clic en el botón de registro
        btnSignUp.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));

        // Manejar clic en el texto de "olvidé mi contraseña"
        tvForgetPassword.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, PasswordRecoveryActivity.class)));
    }

    private void redirectToRoleSpecificActivity(String role) {
        if (role == null) {
            Toast.makeText(this, "Rol no definido. Contacte al administrador", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (role) {
            case "CLIENTE":
                startActivity(new Intent(this, HomeActivity.class));
                break;
            case "ADMIN REST":
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //NO ESTA FUNCIONANDO, REVISAAAR!
                    // Consultar la colección "restaurantes" para verificar si el usuario ya tiene un restaurante
                    db.collection("restaurantes")
                            .whereEqualTo("userId", user.getUid()) // Verificar el campo que conecta el restaurante con el usuario
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful() && task.getResult() != null) {
                                    if (!task.getResult().isEmpty()) {
                                        // Si el restaurante ya existe, redirigir al MinActivity
                                        startActivity(new Intent(this, MainActivity.class));
                                    } else {
                                        // Si no tiene restaurante, redirigir al RestaurantActivity
                                        startActivity(new Intent(this, RestaurantActivity.class));
                                    }
                                } else {
                                    Toast.makeText(this, "Error al consultar restaurantes: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                finish(); // Finalizar LoginActivity después de redirigir
                            });
                } else {
                    Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
                }
                break;
            case "REPARTIDOR":
                startActivity(new Intent(this, ProductsRepartidorActivity.class));
                break;
            case "SUPERADMIN":
                startActivity(new Intent(this, super_estadisticas_general.class));
                break;
            default:
                Toast.makeText(this, "Rol desconocido: " + role, Toast.LENGTH_SHORT).show();
        }

        finish(); // Finalizar LoginActivity
    }
}
