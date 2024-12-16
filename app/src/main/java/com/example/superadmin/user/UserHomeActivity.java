package com.example.superadmin.user;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.superadmin.LoginActivity;
import com.example.superadmin.R;
import com.example.superadmin.Superadmin.super_estadisticas_general;
import com.google.firebase.auth.FirebaseAuth;

public class UserHomeActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    ConstraintLayout toolbar;
    ImageButton btnCar;
    ImageButton btnProfile;
    ImageButton btnDesplegable;
    DrawerLayout drawerLayout;
    ConstraintLayout desplegable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        toolbar = findViewById(R.id.toolbar_home);

        auth = FirebaseAuth.getInstance();
        btnCar = toolbar.findViewById(R.id.btn_shopping_bag);
        // btnProfile = toolbar.findViewById(R.id.btn_profile);
        btnDesplegable= toolbar.findViewById(R.id.btn_menu);
        //drawerLayout = desplegable.findViewById(R.id.draweLayout);

        btnDesplegable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserHomeActivity.this, UserDesplegableActivity.class));
            }
        });

/*
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            }
        });
*/


        btnCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(UserHomeActivity.this, UserCarActivity.class));
                mostrarDialogoCerrarSesion();
            }
        });


        setFragmentHome();
    }

    private void setFragmentHome() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_principal, new UserCategoriesFragment(),"principal_fragment")
                .commit();
    }

    public void changeFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container_principal, fragment,"fragment")
                .commit();
    }

    private void mostrarDialogoCerrarSesion() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Estás seguro de que quieres cerrar sesión?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    // Cerrar sesión con FirebaseAuth
                    auth.signOut();

                    // Eliminar SharedPreferences si es necesario
                    SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();

                    // Redirigir a la página principal
                    Intent intent = new Intent(UserHomeActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish(); // Finalizar la actividad actual
                })
                .setNegativeButton("No", null) // Cierra el diálogo sin hacer nada
                .show();
    }

}