package com.example.superadmin.adminrest.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.superadmin.R;
import com.example.superadmin.adminrest.StatisticsActivity;
import com.example.superadmin.adminrest.dto.GananciaItem;

import java.util.List;

public class GananciaAdapter extends RecyclerView.Adapter<GananciaAdapter.FoodViewHolder> {  // Extender RecyclerView.Adapter
    private List<GananciaItem> list;
    private Context context;

    public GananciaAdapter(Context context, List<GananciaItem> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el diseño del item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_estaditicasganancia, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        // Obtener el ítem en la posición actual y enlazar los datos a las vistas
        GananciaItem gananciaItem = list.get(position);
        holder.tvTittle.setText(gananciaItem.getTitle());
        holder.nameImage.setImageResource(gananciaItem.getNameImage());

        holder.details.setOnClickListener(v -> {
            Intent intent = new Intent(context, StatisticsActivity.class);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();  // Devolver el tamaño de la lista
    }

    // Clase interna para el ViewHolder
    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView tvTittle;
        ImageView nameImage;
        TextView details;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTittle = itemView.findViewById(R.id.text_name_product);
            nameImage = itemView.findViewById(R.id.img_menu_item);
            details = itemView.findViewById(R.id.text_details);
        }
    }
}
