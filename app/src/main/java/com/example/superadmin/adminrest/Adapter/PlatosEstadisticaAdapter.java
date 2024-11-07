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
import com.example.superadmin.adminrest.dto.PlatosEstItem;

import java.util.List;

public class PlatosEstadisticaAdapter extends RecyclerView.Adapter<PlatosEstadisticaAdapter.FoodViewHolder> {

    private List<PlatosEstItem> platoslist;
    Context context;

    public PlatosEstadisticaAdapter(Context context, List<PlatosEstItem> platoslist) {
        this.platoslist = platoslist;
        this.context = context;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el diseño de 
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_estadisticasplatos, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        // Obtener el ítem en la posición actual y enlazar los datos a las vistas
        PlatosEstItem platosEstItem = platoslist.get(position);
        holder.tvTittle.setText(platosEstItem.getTitle());
        holder.plato1.setImageResource(platosEstItem.getPlatoImage1());
        holder.tvPlato1.setText(platosEstItem.getTvPlato1());

        holder.plato2.setImageResource(platosEstItem.getPlatoImage2());
        holder.tvPlato2.setText(platosEstItem.getTvPlato2());

        holder.plato3.setImageResource(platosEstItem.getPlatoImage3());
        holder.tvPlato3.setText(platosEstItem.getTvPlato3());

        holder.details.setOnClickListener(v -> {
            Intent intent = new Intent(context, StatisticsActivity.class);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return platoslist.size();
    }

    // Clase interna para el ViewHolder
    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView tvTittle;
        ImageView plato1;
        ImageView plato2;
        ImageView plato3;
        TextView tvPlato1;
        TextView tvPlato2;
        TextView tvPlato3;
        TextView details;



        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTittle = itemView.findViewById(R.id.text_name_product);

            plato1= itemView.findViewById(R.id.img_lomo);
            tvPlato1 = itemView.findViewById(R.id.text_lomo);

            plato2= itemView.findViewById(R.id.img_menu_ceviche);
            tvPlato2 = itemView.findViewById(R.id.text_ceviche);

            plato3= itemView.findViewById(R.id.img_menu_pollo);
            tvPlato3 = itemView.findViewById(R.id.text_pollo);

            details = itemView.findViewById(R.id.text_details);

        }
    }
}
