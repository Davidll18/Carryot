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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    AppCompatButton btnLogin;
    AppCompatButton btnSignUp;
    AppCompatButton btnBypass;
    TextView tvForgetPassword;
    EditText etEmail, etPassword;

    // Lista hardcodeada de usuarios y contraseñas
    Map<String, String> users;

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

        // Inicializar lista de usuarios y contraseñas
        users = new HashMap<>();
        users.put("b", "c");
        users.put("a", "b");
        users.put("user3@example.com", "password123");
        users.put("user4@example.com", "pass456");

        // Inicializar vistas
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_get_into_login);
        btnSignUp = findViewById(R.id.btn_signup);
        tvForgetPassword = findViewById(R.id.tv_forget_password);
        btnBypass = findViewById(R.id.btn_bypass);

        // Manejar clic en el botón de inicio de sesión
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (validateCredentials(email, password)) {
                    // para repartidor
                    if (email.equals("b")) {
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    }
                    // para cliente, csambiar a tu actividad correspondiente [reeplace CategoriesFragment.class]
                    else if (email.equals("a")) {
                        startActivity(new Intent(LoginActivity.this, HomeRepartidorActivity.class));
                    } else if (email.equals("user3@example.com")) {
                        startActivity(new Intent(LoginActivity.this, CategoriesFragment.class));
                    } else if (email.equals("user4@example.com")) {
                        startActivity(new Intent(LoginActivity.this, super_estadisticas_general.class));

                    }
                    finish();
                } else {

                    showCustomToast();
                }
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

        // Manejar clic en el botón de omitir (bypass)
        btnBypass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, HomePedidosActivity.class));
            }
        });
    }

    // Método para validar las credenciales ingresadas
    private boolean validateCredentials(String email, String password) {
        // Validar si el email existe en la lista y la contraseña coincide
        return users.containsKey(email) && Objects.equals(users.get(email), password);
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
