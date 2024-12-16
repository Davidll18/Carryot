package com.example.superadmin.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.superadmin.R;
import com.example.superadmin.dtos.PlatoDTO;

import java.util.List;

public class UserProductAdapter extends RecyclerView.Adapter<UserProductAdapter.ViewHolder> {

    private List<PlatoDTO> platoList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(PlatoDTO plato);
    }

    public UserProductAdapter(List<PlatoDTO> platoList, OnItemClickListener listener) {
        this.platoList = platoList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item_products, parent, false); // Layout del ítem
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlatoDTO plato = platoList.get(position);

        // Configurar vistas
        holder.textName.setText(plato.getNombrePlato());
        holder.textPrice.setText("S/ " + plato.getPrecio());
        holder.textDescripcion.setText(plato.getDescripcion());

        // Cargar imagen con Glide
        Glide.with(holder.itemView.getContext())
                .load(plato.getImageUrl())
                .placeholder(R.drawable.logo) // Imagen por defecto
                .into(holder.imageProduct);

        // Configurar el clic
        holder.itemView.setOnClickListener(v -> listener.onItemClick(plato));
    }

    @Override
    public int getItemCount() {
        return platoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textPrice,textDescripcion;
        ImageView imageProduct;

        public ViewHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_name_product);
            textPrice = itemView.findViewById(R.id.text_price);
            imageProduct = itemView.findViewById(R.id.img_menu_item);
            textDescripcion = itemView.findViewById(R.id.text_model);
        }
    }
}

