package com.example.projectiot.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectiot.R;
import com.example.projectiot.dto.FoodItem;

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
        // Obtener el ítem en la posición actual y enlazar los datos a las vistas
        FoodItem foodItem = foodList.get(position);
        holder.tvNameDishes.setText(foodItem.getName());
        holder.tvPrice.setText("S/ " + foodItem.getPrice());
        holder.tvStock.setText("Cantidad: " + foodItem.getStock());
        // Aquí puedes cargar la imagen con una biblioteca como Glide o Picasso
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

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameDishes = itemView.findViewById(R.id.tvNameDishes5);
            tvPrice = itemView.findViewById(R.id.tvPrice5);
            tvStock = itemView.findViewById(R.id.tvStock5);
            // Inicializa otras vistas si es necesario
        }
    }
}
