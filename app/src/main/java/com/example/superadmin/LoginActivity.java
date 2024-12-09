package com.example.superadmin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

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

    // Declarar variables
    AppCompatButton btnLogin;
    AppCompatButton btnSignUp;
    TextView tvForgetPassword;
    EditText etEmail, etPassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                showCustomToast("Por favor, complete todos los campos");  // Usando el custom Toast
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
                                                    saveUserSession(user.getUid(), role);  // Guardar sesión
                                                    redirectToRoleSpecificActivity(role, user.getUid());
                                                } else {
                                                    showCustomToast("Error: No se encontró el rol del usuario");  // Usando el custom Toast
                                                }
                                            } else {
                                                showCustomToast("Error al obtener los datos del usuario: ");  // Usando el custom Toast
                                            }
                                        });
                            } else {
                                showCustomToast("Por favor, verifique su correo electrónico");  // Usando el custom Toast
                            }
                        } else {
                            showCustomToast("Error al iniciar sesión: ");  // Usando el custom Toast
                        }
                    });
        });

        // Manejar clic en el botón de registro
        btnSignUp.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));

        // Manejar clic en el texto de "olvidé mi contraseña"
        tvForgetPassword.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, PasswordRecoveryActivity.class)));
    }

    // Método para mostrar el Custom Toast
    private void showCustomToast(String message) {
        // Crear el Toast
        Toast customToast = new Toast(getApplicationContext());

        // Inflar el layout personalizado para el Toast
        LinearLayout toastLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.custom_toast, null);

        // Obtener los elementos del layout
        TextView toastText = toastLayout.findViewById(R.id.toast_text);
        ImageView toastImage = toastLayout.findViewById(R.id.toast_image);

        // Establecer el texto del Toast
        toastText.setText(message);

        // Establecer el layout del Toast
        customToast.setView(toastLayout);

        // Duración del Toast
        customToast.setDuration(Toast.LENGTH_SHORT);

        // Mostrar el Toast
        customToast.show();
    }

    // Método para redirigir a la actividad correspondiente según el rol
    private void redirectToRoleSpecificActivity(String role, String userId) {
        if (role == null) {
            showCustomToast("Rol no definido. Contacte al administrador");
            return;
        }

        Intent intent;
        if (Constants.ROLE_CLIENTE.equals(role)) {
            intent = new Intent(this, HomeActivity.class);
        } else if (Constants.ROLE_ADMIN_RES.equals(role)) {
            checkRestaurantExists(userId); // Verificar si el restaurante existe
            return; // La redirección ocurre dentro de checkRestaurantExists
        } else if (Constants.ROLE_REPARTIDOR.equals(role)) {
            intent = new Intent(this, ProductsRepartidorActivity.class);
        } else if (Constants.ROLE_SUPERADMIN.equals(role)) {
            intent = new Intent(this, super_estadisticas_general.class);
        } else {
            showCustomToast("Rol desconocido: " + role);
            return;
        }

        // Usar las flags para limpiar la pila de actividades
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // Método para verificar si el usuario tiene un restaurante
    private void checkRestaurantExists(String userId) {
        db.collection("restaurant")
                .whereEqualTo("uidCreador", userId) // Filtrar por el campo uidCreador
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        // Si el usuario ya tiene un restaurante creado
                        startActivity(new Intent(this, MainActivity.class)); // Redirigir a la actividad de restaurante
                    } else {
                        // Si no tiene un restaurante creado
                        startActivity(new Intent(this, RestaurantActivity.class)); // Redirigir a la actividad para crear restaurante
                    }
                    finish(); // Finalizar LoginActivity
                })
                .addOnFailureListener(e -> {
                    showCustomToast("Error al verificar restaurante: ");  // Usando el custom Toast
                });
    }

    // Método para guardar la sesión
    private void saveUserSession(String userId, String role) {
        SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLoggedIn", true);  // El usuario está logueado
        editor.putString("userId", userId);  // Guardar el UID del usuario
        editor.putString("role", role);  // Guardar el rol del usuario
        editor.apply();
    }

    // Verificar sesión al iniciar la app
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            String role = preferences.getString("role", null);
            String userId = preferences.getString("userId", null);
            if (role != null && userId != null) {
                redirectToRoleSpecificActivity(role, userId);
            }
        }
    }

    // Método para cerrar sesión y limpiar SharedPreferences
    private void clearUserSession() {
        SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
