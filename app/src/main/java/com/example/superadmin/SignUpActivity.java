package com.example.superadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.superadmin.util.KeyboardUtils;

import java.util.Random;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private EditText edName, edSurname, edEmail, edDni, edPhone, edAddress;
    private AppCompatButton btnInit;
    private ConstraintLayout toolbar;
    private ImageView btnBack;

    private static final int REQUEST_CODE_SIGNUP2 = 1001;  // Código para la solicitud

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Inicializar vistas
        edName = findViewById(R.id.ed_name);
        edSurname = findViewById(R.id.ed_surname);
        edEmail = findViewById(R.id.ed_email);
        edDni = findViewById(R.id.ed_dni);
        edPhone = findViewById(R.id.ed_phone);
        edAddress = findViewById(R.id.ed_address);
        btnInit = findViewById(R.id.btn_signup_init);
        toolbar = findViewById(R.id.toolbar_signup);
        btnBack = toolbar.findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> finish());

        // Manejar clic en el botón de registro
        btnInit.setOnClickListener(v -> {
            // Obtener valores de los campos
            String name = edName.getText().toString().trim();
            String surname = edSurname.getText().toString().trim();
            String email = edEmail.getText().toString().trim();
            String dni = edDni.getText().toString().trim();
            String phone = edPhone.getText().toString().trim();
            String address = edAddress.getText().toString().trim();

            // Validar campos
            String errorMessage = validarCampos(name, surname, email, dni, phone, address);
            if (errorMessage != null) {
                mostrarToast(errorMessage);
                return;
            }

            // Generar una contraseña aleatoria de 10 caracteres
            String generatedPassword = generarContraseñaAleatoria();

            // Pasar la información a SignUp2Activity, incluyendo la contraseña generada
            Intent intent = new Intent(SignUpActivity.this, Signup2Activity.class);
            intent.putExtra("userName", name);
            intent.putExtra("userSurname", surname);
            intent.putExtra("userEmail", email);
            intent.putExtra("userDni", dni);
            intent.putExtra("userPhone", phone);
            intent.putExtra("userAddress", address);
            intent.putExtra("userPassword", generatedPassword);  // Pasar la contraseña generada
            startActivityForResult(intent, REQUEST_CODE_SIGNUP2);  // Usamos el código de solicitud
        });
    }

    // Función para generar una contraseña aleatoria de 10 caracteres
    public String generarContraseñaAleatoria() {
        char[] caracteres = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        Random random = new Random();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            password.append(caracteres[random.nextInt(caracteres.length)]);
        }

        return password.toString();
    }

    // Validar los campos de registro
    private String validarCampos(String name, String surname, String email, String dni, String phone, String address) {
        // Validación para nombre (permitir letras y tildes)
        if (name.isEmpty() || !Pattern.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+", name) || name.length() > 20) {
            return "Campo NOMBRE incorrecto.";
        }

        // Validación para apellidos (permitir letras y tildes)
        if (surname.isEmpty() || !Pattern.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+", surname) || surname.length() > 20) {
            return "Campo APELLIDOS incorrecto.";
        }

        // Validación de correo electrónico
        if (email.isEmpty() || !email.matches("[a-zA-Z0-9._%+-]+@(gmail.com|pucp.edu.pe)")) {
            return "Correo: Formato incorrecto.";
        }

        // Validación para DNI (solo números y exactamente 8 dígitos)
        if (dni.isEmpty() || !Pattern.matches("[0-9]{8}", dni)) {
            return "DNI debe contener exactamente 8 dígitos.";
        }

        // Validación para teléfono (solo números y exactamente 9 dígitos)
        if (phone.isEmpty() || !Pattern.matches("[0-9]{9}", phone)) {
            return "Teléfono debe contener exactamente 9 dígitos.";
        }

        // Validación para dirección (permitir letras, números, espacios y algunos caracteres especiales como N°)
        if (address.isEmpty() || !Pattern.matches("[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ #N°]+", address) || address.length() > 50) {
            return "Dirección no válida.";
        }

        return null;  // No hay errores
    }

    // Función para mostrar el Toast de error con diseño personalizado
    private void mostrarToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View customToastView = inflater.inflate(R.layout.custom_toast, findViewById(R.id.toast_container));

        TextView toastText = customToastView.findViewById(R.id.toast_text);
        ImageView toastImage = customToastView.findViewById(R.id.toast_image);

        toastText.setText(message);
        toastImage.setImageResource(R.drawable.shopping_bag);  // Cambia esto por tu logo si es necesario

        Toast customToast = new Toast(getApplicationContext());
        customToast.setDuration(Toast.LENGTH_LONG);
        customToast.setView(customToastView);
        customToast.show();
    }

    // Recibir los datos de vuelta de Signup2Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SIGNUP2 && resultCode == RESULT_OK) {
            String userName = data.getStringExtra("userName");
            String userSurname = data.getStringExtra("userSurname");
            String userEmail = data.getStringExtra("userEmail");
            String userPhone = data.getStringExtra("userPhone");
            String userDni = data.getStringExtra("userDni");
            String userAddress = data.getStringExtra("userAddress");

            edName.setText(userName);
            edSurname.setText(userSurname);
            edEmail.setText(userEmail);
            edPhone.setText(userPhone);
            edDni.setText(userDni);
            edAddress.setText(userAddress);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return KeyboardUtils.hideKeyboardOnTouch(this, event) || super.dispatchTouchEvent(event);
    }
}
