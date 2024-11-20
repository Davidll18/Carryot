package com.example.superadmin.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.superadmin.ProfileActivity;
import com.example.superadmin.R;

public class UserDesplegableActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageButton buttonMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_desplegable);

        buttonMenu = findViewById(R.id.btn_menu);
        drawerLayout = findViewById(R.id.draweLayout);

        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
            }
        });


    }
    public void btnperfil(MenuItem item){
        Intent m3intent = new Intent(this, ProfileActivity.class);
        startActivity(m3intent);
    }


    public void btnhistorial(MenuItem item){
        Intent m3intent = new Intent(this, UserHistorialActivity.class);
        startActivity(m3intent);
    }

    /*
    public void btnGestionUsuarios(View view){
        Intent m3intent = new Intent(this, super_gestion_usuarios.class);
        startActivity(m3intent);
    }
    public void btnRegistrarAdmin(View view){
        Intent m4intent = new Intent(this, super_register_admin_rest.class);
        startActivity(m4intent);
    }
    public void btnReporteVentas(View view){
        Intent m5intent = new Intent(this, super_estadisticas_general.class);
        startActivity(m5intent);
    }
    public void btnRestaurants(View view){
        Intent m6intent = new Intent(this, super_rest.class);
        startActivity(m6intent);
    }


     */
}
