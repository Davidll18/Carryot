package com.example.superadmin.adminrest.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.superadmin.R;
import com.example.superadmin.adminrest.PedidosActivity;
import com.example.superadmin.adminrest.dto.PedidosStatusItem;

import java.util.List;

public class PedidosStatusAdapter extends RecyclerView.Adapter<PedidosStatusAdapter.FoodViewHolder> {
    private Context context;
    private List<PedidosStatusItem> pedidoslist;


    public PedidosStatusAdapter(Context context, List<PedidosStatusItem> pedidoslist) {
        this.pedidoslist = pedidoslist;
        this.context = context;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el diseño de 
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pendientes, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        // Obtener el ítem en la posición actual y enlazar los datos a las vistas
        PedidosStatusItem pedidosStatusItem = pedidoslist.get(position);
        holder.tvCountPP.setText(pedidosStatusItem.getCantidadPP());
        holder.tvPP.setText(pedidosStatusItem.getTitlePP());
        holder.tvCountPA.setText(pedidosStatusItem.getCantidadPA());
        holder.tvPA.setText(pedidosStatusItem.getTitlePA());

        holder.cardPendientes.setOnClickListener(v -> {
            Intent intent = new Intent(context, PedidosActivity.class);
            context.startActivity(intent);
        });
        holder.cardActivos.setOnClickListener(v -> {
            Intent intent = new Intent(context, PedidosActivity.class);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return pedidoslist.size();
    }

    // Clase interna para el ViewHolder
    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView tvCountPP;
        TextView tvPP;
        TextView tvCountPA;
        TextView tvPA;
        CardView cardPendientes, cardActivos;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCountPP = itemView.findViewById(R.id.tvCountPedidosPendientes);
            tvPP= itemView.findViewById(R.id.tvPedidosPendientes);
            tvCountPA = itemView.findViewById(R.id.tvCountPedidosActivos);
            tvPA = itemView.findViewById(R.id.tvPedidosActivos);
            cardPendientes = itemView.findViewById(R.id.cardPendientes);
            cardActivos = itemView.findViewById(R.id.cardActivos);
        }
    }
}
