package com.example.superadmin.adminrest.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.superadmin.R;
import com.example.superadmin.adminrest.dto.PedidosItem;

import java.util.List;

public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.FoodViewHolder> {  // Extender RecyclerView.Adapter
    private List<PedidosItem> list;
    Context context;
    public PedidosAdapter(Context context, List<PedidosItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el diseño del item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedidos, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        // Obtener el ítem en la posición actual y enlazar los datos a las vistas
        PedidosItem  pedidosItem = list.get(position);
        holder.numeroOrden.setText(pedidosItem.getNumeroOrden());
        holder.status.setText(pedidosItem.getStatus());
        holder.descripcion.setText(pedidosItem.getDescripcion());
        holder.platoImagen.setImageResource(pedidosItem.getPlatoImagen());
        holder.iconoVista.setImageResource(pedidosItem.getIconoVista());
        holder.precio.setText(pedidosItem.getPrecio());

        if (pedidosItem.getStatus().equals("Pendiente")) {
            holder.status.setBackgroundResource(R.drawable.background_orange);  // Naranja para pendientes
        } else if (pedidosItem.getStatus().equals("Completado")) {
            holder.status.setBackgroundResource(R.drawable.background_green);  // Verde para completados
        } else if (pedidosItem.getStatus().equals("En preparación")) {
            holder.status.setBackgroundResource(R.drawable.background_amber);  // Ámbar para preparación
        } else if (pedidosItem.getStatus().equals("En camino")) {
            holder.status.setBackgroundResource(R.drawable.background_blue);  // Azul para en camino
        } else {
            holder.status.setBackgroundResource(R.drawable.background_default);  // Fondo por defecto si es otro estado
        }

    }

    @Override
    public int getItemCount() {
        return list.size();  // Devolver el tamaño de la lista
    }

    // Clase interna para el ViewHolder
    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView numeroOrden;
        TextView status;
        TextView descripcion;
        ImageView platoImagen;
        ImageView iconoVista;
        TextView precio;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            numeroOrden = itemView.findViewById(R.id.tvOrderNumber1);
            status = itemView.findViewById(R.id.tvOrderStatus1);
            descripcion = itemView.findViewById(R.id.tvOrderDescription1);
            platoImagen = itemView.findViewById(R.id.imgFood1);
            iconoVista = itemView.findViewById(R.id.imgViewDetails1);
            precio = itemView.findViewById(R.id.tvPrice1);
        }
    }
}
