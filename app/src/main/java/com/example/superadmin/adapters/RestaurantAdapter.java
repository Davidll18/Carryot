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

import com.bumptech.glide.Glide;
import com.example.superadmin.R;
import com.example.superadmin.dtos.RestaurantDTO;
import com.example.superadmin.Superadmin.super_estadisticas_por_rest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    private ArrayList<RestaurantDTO> restaurantList;
    private Context context;
    private OnCardClickListener listener; // Interfaz para clics en las tarjetas

    public RestaurantAdapter(Context context, ArrayList<RestaurantDTO> restaurantList) {
        this.context = context;
        this.restaurantList = restaurantList;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.super_restaurant_item, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        RestaurantDTO restaurant = restaurantList.get(position);

        // Asignar valores a las vistas
        holder.restaurantName.setText(restaurant.getNombreRestaurante());
        holder.restaurantCategory.setText(restaurant.getCategoria());
        // Cargar imagen desde la URL
        if (restaurant.getImageUrl() != null && !restaurant.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(restaurant.getImageUrl())
                    .placeholder(R.drawable.logo) // Mientras carga
                    .error(R.drawable.logo)       // Si hay error
                    .into(holder.restaurantImage);
        } else {
            // Si no hay URL, usar imagen predeterminada
            holder.restaurantImage.setImageResource(R.drawable.logo);
        }

        // Configurar clic en la tarjeta para redirigir con datos
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, super_estadisticas_por_rest.class);
            intent.putExtra("nombreRestaurante", restaurant.getNombreRestaurante());
            intent.putExtra("categoria", restaurant.getCategoria());
            intent.putExtra("idRestaurante", restaurant.getUidCreacion());
            intent.putExtra("imageUrl", restaurant.getImageUrl());
            intent.putExtra("costo", restaurant.getCostoDelivery()); // Costo del restaurante
            intent.putExtra("rate", restaurant.getRateRest());   // Valoración del restaurante
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    // Configurar el listener
    public void setOnCardClickListener(OnCardClickListener listener) {
        this.listener = listener;
    }

    public interface OnCardClickListener {
        void onCardClick(RestaurantDTO restaurant);
    }

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder {
        TextView restaurantName;
        TextView restaurantCategory;
        ImageView restaurantImage;
        CardView cardView;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantName = itemView.findViewById(R.id.restaurantName);
            cardView = itemView.findViewById(R.id.card_view_restaurant); // Asegúrate de que tu diseño tiene un CardView con este ID
            restaurantCategory = itemView.findViewById(R.id.restaurantCategory);
            restaurantImage = itemView.findViewById(R.id.restaurantImage);
        }
    }
}
