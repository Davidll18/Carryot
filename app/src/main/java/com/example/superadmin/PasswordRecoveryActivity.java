package com.example.superadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class PasswordRecoveryActivity extends AppCompatActivity {

    private AppCompatButton btnSendCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);

        btnSendCode = findViewById(R.id.btn_send_code);

        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigir a PasswordCodeActivity
                Intent intent = new Intent(PasswordRecoveryActivity.this, PasswordCodeActivity.class);
                startActivity(intent);
            }
        });
    }
}