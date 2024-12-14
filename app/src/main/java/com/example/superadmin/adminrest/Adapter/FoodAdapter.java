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

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private List<FoodItem> foodList;
    public FoodAdapter(List<FoodItem> foodList) {
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el diseño de item_food.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        // Obtener el ítem en la posición actual y enlazar los datos a las vistas
        FoodItem foodItem = foodList.get(position);
        holder.tvNameDishes.setText(foodItem.getName());
        holder.tvPrice.setText("S/ " + foodItem.getPrice());
        holder.tvStock.setText("Cantidad: " + foodItem.getStock());
        holder.tvDescripcion.setText(foodItem.getDesc());
        if (foodItem.getAvailable().equals(true)) {
            String status = "Disponible";
            holder.tvDisponible.setText(status);
            holder.tvDisponible.setBackground(ContextCompat.getDrawable(context, R.drawable.background_green));
        } else {
            String status = "No disponible";
            holder.tvDisponible.setText(status);
            holder.tvDisponible.setBackground(ContextCompat.getDrawable(context, R.drawable.background_red));
        }
        Glide.with(context)
                .load(foodItem.getImageUrl())
                .into(holder.imageView);
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
        }
    }
}
