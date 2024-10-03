package com.example.projectiot;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectiot.Adapter.FoodAdapter;
import com.example.projectiot.dto.FoodItem;

import java.util.ArrayList;
import java.util.List;

public class DishesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FoodAdapter foodAdapter;
    private List<FoodItem> foodList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listdishes_adminrest);

        // Obtener referencia al ImageView de los tres puntos
        //ImageView optionsMenu = findViewById(R.id.optionMenu);

        /**optionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //PREDIRIGIR A AÑADIR PLATO
                Button btAñadirPlato = findViewById(R.id.btAñadirPlato);
                btAñadirPlato.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(DishesActivity.this, NewDishActivity.class);
                        startActivity(intent);
                    }
                });
                PopupMenu popup = new PopupMenu(DishesActivity.this, optionsMenu);

                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                // Configurar el listener para manejar la selección de opciones del menú
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Integer menu = item.getItemId();
                        if (menu == R.id.action_edit) {
                            Intent intent = new Intent(DishesActivity.this, EditDishesActivity.class);

                            return true;
                        }
                        return false;
                    }
                });

                // Mostrar el PopupMenu
                popup.show();
            }
        });*/

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        foodList = new ArrayList<>();
        foodList.add(new FoodItem("Lomo Saltado", "S/ 24.00", "Cantidad: 50 und"));
        foodList.add(new FoodItem("Pollo a la Brasa", "S/ 18.00", "Cantidad: 30 und"));
        foodList.add(new FoodItem("Ceviche", "S/ 20.00", "Cantidad: 25 und"));

        foodAdapter = new FoodAdapter(foodList);
        recyclerView.setAdapter(foodAdapter);





    }
}
