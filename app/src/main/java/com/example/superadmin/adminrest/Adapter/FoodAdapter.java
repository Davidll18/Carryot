package com.example.superadmin.adminrest.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.superadmin.R;
import com.example.superadmin.adminrest.DishesActivity;
import com.example.superadmin.adminrest.EditDishesActivity;
import com.example.superadmin.adminrest.dto.FoodItem;
import com.example.superadmin.adminrest.dto.PlatosEstItem;
import com.example.superadmin.dtos.PlatoDTO;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private ArrayList<PlatoDTO> foodList;
    private Context context;
    private FirebaseFirestore db;

    public FoodAdapter(Context context, ArrayList<PlatoDTO> foodList) {
        this.context = context;
        this.foodList = foodList;
    }


    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adminrest_item_products, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        // Obtener el ítem en la posición actual y enlazar los datos a las vistas
        PlatoDTO foodItem = foodList.get(position);
        holder.tvNameDishes.setText(foodItem.getNombrePlato());
        holder.tvPrice.setText("S/ " + foodItem.getPrecio());
        holder.tvStock.setText("Cantidad: " + foodItem.getCantidad());
        holder.tvDescripcion.setText(foodItem.getDescripcion());
        if (foodItem.isDisponible()) {
            String status = "Disponible";
            holder.tvDisponible.setText(status);
            holder.tvDisponible.setBackground(ContextCompat.getDrawable(context, R.drawable.background_green));
        } else {
            String status = "No disponible";
            holder.tvDisponible.setText(status);
            holder.tvDisponible.setBackground(ContextCompat.getDrawable(context, R.drawable.background_red));
        }
        Glide.with(context)
                .load(foodItem.getImageUrl())  // Carga la imagen desde la URL
                .placeholder(R.drawable.logo)  // Imagen por defecto mientras carga
                .error(R.drawable.logo)  // Imagen en caso de error
                .into(holder.imageView);  // Asigna la imagen al ImageView

        holder.options.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.options);
            popupMenu.inflate(R.menu.menu_options_adminrest);  // Inflar el menú desde el archivo XML

            // Configurar las acciones de las opciones del menú
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.option_edit) {
                    // Acción para editar
                    editFoodItem(foodItem, context);
                    return true;
                } else if (item.getItemId() == R.id.option_availability) {
                    // Acción para habilitar/deshabilitar disponibilidad
                    showConfirmationDialog(foodItem, holder, context);

                    return true;
                } else {
                    return false;
                }
            });
            // Mostrar el menú
            popupMenu.show();
        });

    }
    private void showConfirmationDialog(PlatoDTO foodItem, FoodViewHolder holder, Context context) {
        // Crear el cuadro de diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Cambiar estado");
        builder.setMessage("¿Estás seguro de que deseas cambiar el estado de disponibilidad?");

        // Configurar el botón "Sí"
        builder.setPositiveButton("Sí", (dialog, which) -> {
            // Ejecutar la función para cambiar el estado
            toggleAvailability(foodItem, holder, context);
        });

        // Configurar el botón "No"
        builder.setNegativeButton("No", (dialog, which) -> {
            // Cerrar el cuadro de diálogo
            dialog.dismiss();
        });

        // Mostrar el cuadro de diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void toggleAvailability(PlatoDTO foodItem, FoodViewHolder holder, Context context) {
        String uidPlato = foodItem.getUidCreacion();
        Map<String, Object> plato = new HashMap<>();
        if(foodItem.isDisponible()){

            plato.put("disponible", false);
        }else{
            plato.put("disponible", true);
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("platos").document(uidPlato)
                .update(plato)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Plato guardado correctamente.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, DishesActivity.class);
                    context.startActivity(intent);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(context, "Error al guardar el plato: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );


    }


    private void editFoodItem(PlatoDTO foodItem, Context context) {
        Intent intent = new Intent(context, EditDishesActivity.class);

        // Pasar los datos del plato a la actividad de edición
        intent.putExtra("uidPlato", foodItem.getUidCreacion());
        intent.putExtra("nombrePlato", foodItem.getNombrePlato());
        intent.putExtra("precio", foodItem.getPrecio());
        intent.putExtra("descripcion", foodItem.getDescripcion());
        intent.putExtra("cantidad", foodItem.getCantidad());
        intent.putExtra("categoria", foodItem.getCategoriaPlato());
        intent.putExtra("disponible", foodItem.isDisponible());
        intent.putExtra("imageUrl", foodItem.getImageUrl());

        // Asegúrate de usar FLAG_ACTIVITY_NEW_TASK si estás redirigiendo desde un adaptador
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return foodList.size();
    }

    // Clase interna para el ViewHolder
    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameDishes;
        TextView tvPrice;
        TextView tvStock;
        TextView tvDisponible;
        TextView tvDescripcion;
        ImageView imageView;
        ImageView options;


        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameDishes = itemView.findViewById(R.id.text_name_product);
            tvDisponible = itemView.findViewById(R.id.availability_flag);
            tvDescripcion = itemView.findViewById(R.id.text_model);
            tvPrice = itemView.findViewById(R.id.text_price);
            tvStock = itemView.findViewById(R.id.text_cant);
            imageView = itemView.findViewById(R.id.img_menu_item);
            options = itemView.findViewById(R.id.options_menu);

        }
    }
}
