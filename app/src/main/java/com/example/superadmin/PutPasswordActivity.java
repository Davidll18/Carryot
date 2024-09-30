package com.example.superadmin;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
public class PutPasswordActivity extends AppCompatActivity {

    private AppCompatButton btnRestorePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_password);

        btnRestorePassword = findViewById(R.id.btn_restore_password);

        btnRestorePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigir a PasswordCodeActivity
                Intent intent = new Intent(PutPasswordActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}