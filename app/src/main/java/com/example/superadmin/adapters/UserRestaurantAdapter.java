package com.example.superadmin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superadmin.R;
import com.example.superadmin.dtos.RestaurantDTO;

import java.util.ArrayList;

public class UserRestaurantAdapter extends RecyclerView.Adapter<UserRestaurantAdapter.RestaurantViewHolder> {

    private ArrayList<RestaurantDTO> restaurantList;
    private Context context;
    private OnCardClickListener listener; // Interfaz para clics en las tarjetas

    public UserRestaurantAdapter(Context context, ArrayList<RestaurantDTO> restaurantList) {
        this.context = context;
        this.restaurantList = restaurantList;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_menu, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        RestaurantDTO restaurant = restaurantList.get(position);

        // Asignar valores a las vistas
        holder.restaurantName.setText(restaurant.getNombreRestaurante());

        // Configurar clic en la tarjeta
        holder.cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCardClick(restaurant);
            }
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
        TextView restaurantName,calificacion,categoria,costodelivery,tiempo;
        CardView cardView;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantName = itemView.findViewById(R.id.text_menu_name);
            calificacion = itemView.findViewById(R.id.text_stars);
            categoria = itemView.findViewById(R.id.categoria);
            costodelivery = itemView.findViewById(R.id.text_delivery_cost);
            tiempo = itemView.findViewById(R.id.text_time);
            cardView = itemView.findViewById(R.id.card_view_restaurant); // Asegúrate de que tu diseño tiene un CardView con este ID
        }
    }
}