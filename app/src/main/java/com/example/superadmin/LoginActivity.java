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
import com.example.superadmin.adminrest.MainActivity;
import com.example.superadmin.adminrest.RestaurantActivity;
import com.example.superadmin.util.Constants;
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
                                                    redirectToRoleSpecificActivity(role, user.getUid());
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

    private void redirectToRoleSpecificActivity(String role, String userId) {
        if (role == null) {
            Toast.makeText(this, "Rol no definido. Contacte al administrador", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Constants.ROLE_CLIENTE.equals(role)) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        } else if (Constants.ROLE_ADMIN_RES.equals(role)) {
            checkRestaurantExists(userId);
        } else if (Constants.ROLE_REPARTIDOR.equals(role)) {
            startActivity(new Intent(this, ProductsRepartidorActivity.class));
            finish();
        } else if (Constants.ROLE_SUPERADMIN.equals(role)) {
            startActivity(new Intent(this, super_estadisticas_general.class));
            finish();
        } else {
            Toast.makeText(this, "Rol desconocido: " + role, Toast.LENGTH_SHORT).show();
        }
    }

    private void checkRestaurantExists(String userId) {
        db.collection("restaurant")
                .whereEqualTo("uidCreador", userId) // Filtrar por el campo uidCreador
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        // Si el usuario ya tiene un restaurante creado
                        startActivity(new Intent(this, MainActivity.class));
                    } else {
                        // Si no tiene un restaurante creado
                        startActivity(new Intent(this, RestaurantActivity.class));
                    }
                    finish(); // Finalizar LoginActivity
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al verificar restaurante: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
