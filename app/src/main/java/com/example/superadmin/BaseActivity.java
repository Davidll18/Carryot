package com.example.superadmin;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configurar el Toolbar si está presente en el layout
        Toolbar toolbar = findViewById(R.id.toolbar); // Buscar el Toolbar en el layout actual
        if (toolbar != null) {
            setSupportActionBar(toolbar); // Configurar Toolbar como ActionBar

            // Configurar funcionalidad del botón de usuario
            ImageView toolbarUser = toolbar.findViewById(R.id.toolbar_user);
            if (toolbarUser != null) {
                toolbarUser.setOnClickListener(v -> openUserProfile());
            }
        }
    }

    private void openUserProfile() {
        // Navegar a la actividad de perfil
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}