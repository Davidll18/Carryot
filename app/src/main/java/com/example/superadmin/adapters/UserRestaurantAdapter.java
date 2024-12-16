package com.example.superadmin.adapters;

import android.content.Context;
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
        holder.calificacion.setText(restaurant.getRateRest());
        holder.categoria.setText(restaurant.getCategoria());
        holder.costodelivery.setText(restaurant.getCostoDelivery());
        holder.tiempo.setText(restaurant.getTiempoEspera());

        // Cargar imagen con Glide
        Glide.with(holder.itemView.getContext())
                .load(restaurant.getImageUrl()) // URL de la imagen desde Firebase
                .placeholder(R.drawable.logo)   // Imagen por defecto mientras carga
                .into(holder.imageProduct);     // Referencia al ImageView

        // Imprimir el UID del restaurante en el log para verificar
        if (restaurant.getUidCreacion() != null) {
            android.util.Log.d("UID_RESTAURANTE", "UID: " + restaurant.getUidCreacion());
        } else {
            android.util.Log.e("UID_RESTAURANTE", "El UID del restaurante es nulo");
        }

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
        ImageView imageProduct;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantName = itemView.findViewById(R.id.text_menu_name);
            calificacion = itemView.findViewById(R.id.text_stars);
            categoria = itemView.findViewById(R.id.categoria);
            costodelivery = itemView.findViewById(R.id.text_delivery_cost);
            tiempo = itemView.findViewById(R.id.text_time);
            cardView = itemView.findViewById(R.id.card_view_restaurant);
            imageProduct = itemView.findViewById(R.id.img_menu_item);// Asegúrate de que tu diseño tiene un CardView con este ID
        }
    }
}
