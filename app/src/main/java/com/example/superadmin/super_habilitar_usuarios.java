package com.example.superadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class super_habilitar_usuarios extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_habilitar_usuarios);
    }

    public void editar_btn(View view){
        Intent mintent = new Intent(this, super_editar.class);
        startActivity(mintent);
    }

}