package com.example.superadmin;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.View;
import android.widget.ImageButton;

import com.example.superadmin.databinding.ActivitySuperGestionUsuariosBinding;

public class super_gestion_usuarios extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageButton buttonMenu;
    private ActivitySuperGestionUsuariosBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_gestion_usuarios);

        drawerLayout = findViewById(R.id.draweLayout);
        buttonMenu = findViewById(R.id.buttonMenu);

        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
            }
        });
    }
    public void register_btn(View view){
        Intent m7intent = new Intent(this, super_register_admin_rest.class);
        startActivity(m7intent);
    }
    public void Edit_rest(View view){
        Intent m10intent = new Intent(this, super_habilitar_usuarios.class);
        startActivity(m10intent);
    }

}