package com.example.superadmin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superadmin.R;
import com.example.superadmin.model.Restaurante;
import com.example.superadmin.Superadmin.super_estadisticas_por_rest;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    private List<Restaurante> restaurantList;
    private Context context;

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder {
        public ImageView restaurantImage;
        public TextView restaurantName;
        public CardView cardView;


        public RestaurantViewHolder(View itemView) {
            super(itemView);
            restaurantImage = itemView.findViewById(R.id.restaurantImage);
            restaurantName = itemView.findViewById(R.id.restaurantName);
            cardView = itemView.findViewById(R.id.card_view_restaurant); // Asegúrate de que el ID sea correcto
        }
    }

    public RestaurantAdapter(List<Restaurante> restaurantList) {
        this.restaurantList = restaurantList;
        this.context = context; // Inicializa el contexto
    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.super_restaurant_item, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        Restaurante restaurant = restaurantList.get(position);
        holder.restaurantImage.setImageResource(restaurant.getImageResource());
        holder.restaurantName.setText(restaurant.getName());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext(); // Obtén el contexto desde la vista
                Intent intent = new Intent(context, super_estadisticas_por_rest.class);
                intent.putExtra("RESTAURANT_NAME", restaurant.getName());
                intent.putExtra("RESTAURANT_IMAGE", restaurant.getImageResource());
                // Agrega extras si es necesario
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }
}

