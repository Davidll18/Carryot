package com.example.superadmin;
import android.content.Intent;
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

public class HomeActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        toolbar = findViewById(R.id.toolbar_home);


        btnCar = toolbar.findViewById(R.id.btn_shopping_bag);
         // btnProfile = toolbar.findViewById(R.id.btn_profile);
        btnDesplegable= toolbar.findViewById(R.id.btn_menu);
        //drawerLayout = desplegable.findViewById(R.id.draweLayout);

        btnDesplegable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, UserDesplegableActivity.class));
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
                startActivity(new Intent(HomeActivity.this, CarActivity.class));
            }
        });


        setFragmentHome();
    }

    private void setFragmentHome() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_principal, new CategoriesFragment(),"principal_fragment")
                .commit();
    }

    public void changeFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container_principal, fragment,"fragment")
                .commit();
    }
}