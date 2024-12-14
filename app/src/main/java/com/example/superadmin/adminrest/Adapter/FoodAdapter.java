package com.example.superadmin.adminrest.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.superadmin.R;
import com.example.superadmin.adminrest.dto.FoodItem;
import com.example.superadmin.adminrest.dto.PlatosEstItem;
import com.example.superadmin.dtos.PlatoDTO;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private ArrayList<PlatoDTO> foodList;
    private Context context;

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
