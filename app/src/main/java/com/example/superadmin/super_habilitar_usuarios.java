package com.example.superadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class super_habilitar_usuarios extends AppCompatActivity {
    private EditText nombreEditText, apellidosEditText, dniEditText, correoEditText, telefonoEditText;
    private Switch habilitarSwitch;
    private Button cancelarBtn, aceptar_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_habilitar_usuarios);

        cancelarBtn = findViewById(R.id.canelar_btn);
        aceptar_btn = findViewById(R.id.aceptar_btn);

        nombreEditText = findViewById(R.id.Nombre);
        apellidosEditText = findViewById(R.id.Apellidos);
        dniEditText = findViewById(R.id.DNI);
        correoEditText = findViewById(R.id.Correo);
        telefonoEditText = findViewById(R.id.Telefono);
        habilitarSwitch = findViewById(R.id.switchHabilitar);

        // Recibir los datos del Intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String lastName = intent.getStringExtra("lastName");
        String dni = intent.getStringExtra("dni");
        String correo = intent.getStringExtra("correo");
        String telefono = intent.getStringExtra("telefono");
        boolean habilitado = intent.getBooleanExtra("habilitado", false);

        // Asignar los datos a las vistas
        nombreEditText.setText(name);
        apellidosEditText.setText(lastName);
        dniEditText.setText(dni);
        correoEditText.setText(correo);
        telefonoEditText.setText(telefono);
        habilitarSwitch.setChecked(habilitado);

        cancelarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finalizar la actividad y regresar a la anterior
                finish();
            }
        });
        aceptar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finalizar la actividad y regresar a la anterior
                finish();
            }
        });

    }

}