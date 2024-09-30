package com.example.superadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class PasswordCodeActivity extends AppCompatActivity {
    private AppCompatButton btnGoChangePassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_code);

        btnGoChangePassword = findViewById(R.id.btn_go_change_password);

        btnGoChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigir a PasswordCodeActivity
                Intent intent = new Intent(PasswordCodeActivity.this, PutPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}