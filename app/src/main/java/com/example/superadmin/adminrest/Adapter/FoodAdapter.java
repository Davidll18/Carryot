package com.example.superadmin.adminrest.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.superadmin.R;
import com.example.superadmin.adminrest.dto.FoodItem;
import com.example.superadmin.dtos.PlatoDTO;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private List<PlatoDTO> platosList;
    private Context context;

    // Constructor
    public FoodAdapter(Context context, List<PlatoDTO> platosList) {
        this.context = context;
        this.platosList = platosList;
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
        PlatoDTO plato = platosList.get(position);
        holder.tvNameDishes.setText(plato.getNombrePlato());
        holder.tvPrice.setText("S/ " + plato.getPrecio());
        holder.tvStock.setText("Cantidad: " + plato.getCantidad());
        holder.tvDescripcion.setText(plato.getDescripcion());

        // Configurar la disponibilidad
        if (plato.isAvailable()) {
            holder.tvDisponible.setText("Disponible");
            holder.tvDisponible.setBackground(ContextCompat.getDrawable(context, R.drawable.background_green));
        } else {
            holder.tvDisponible.setText("No disponible");
            holder.tvDisponible.setBackground(ContextCompat.getDrawable(context, R.drawable.background_red));
        }

        Glide.with(holder.itemView.getContext())
                .load(foodItem.getImageUrl())  // La URL de la imagen que obtuviste de Firestore
                .placeholder(R.drawable.placeholder_image)  // Imagen de carga inicial
                .error(R.drawable.logo)  // Imagen si hay un error
                .into(holder.imageView);


        // Configurar los 3 puntos para mostrar el menú de opciones
        holder.options.setOnClickListener(v -> showOptionsMenu(v, plato));
    }

    @Override
    public int getItemCount() {
        return platosList.size();
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
            imageView = itemView.findViewById(R.id.img_menu_item);  // Imagen del plato
            options = itemView.findViewById(R.id.options_menu);  // 3 puntos para el menú
        }
    }

    // Método para mostrar el menú de opciones
    private void showOptionsMenu(View view, PlatoDTO plato) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.menu_options_adminrest);

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.option_edit:
                    // Acción para editar
                    editFoodItem(plato);
                    return true;

                case R.id.option_availability:
                    toggleAvailability(plato);
                    return true;

                case R.id.option_delete:
                    // Acción para eliminar
                    deleteFoodItem(plato);
                    return true;

                default:
                    return false;
            }
        });

        popupMenu.show();
    }

    // Método para editar un plato
    private void editFoodItem(PlatoDTO plato) {
        // Lógica para editar el plato
    }

    // Método para cambiar la disponibilidad del plato
    private void toggleAvailability(PlatoDTO plato) {
        // Lógica para actualizar la disponibilidad
    }

    // Método para eliminar un plato
    private void deleteFoodItem(PlatoDTO plato) {
        // Lógica para eliminar el plato
    }
    public void updateFoodList(List<FoodItem> newFoodList) {
        this.foodList = newFoodList;
        notifyDataSetChanged();  // Notifica al adaptador que la lista ha cambiado
    }
}
