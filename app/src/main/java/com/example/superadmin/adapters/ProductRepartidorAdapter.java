package com.example.superadmin.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.superadmin.R;
import com.example.superadmin.dtos.Pedidos;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ProductRepartidorAdapter extends RecyclerView.Adapter<ProductRepartidorAdapter.ProductViewHolder> {

    private List<Pedidos> pedidosList;
    private OnItemClickListener listener;

    public ProductRepartidorAdapter(List<Pedidos> pedidosList, OnItemClickListener listener) {
        this.pedidosList = pedidosList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_products_repartidor, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Pedidos pedido = pedidosList.get(position);

        // Log: Información del pedido actual
        Log.d("Adapter", "Procesando pedido: " + pedido.getNumeroPedido());

        // Asignar número de pedido y costo total
        holder.textNameProduct.setText("N° " + pedido.getNumeroPedido());
        holder.textPrice.setText(String.format("$ %.2f", Double.parseDouble(pedido.getCostoTotal())));

        // Obtener la imagen del restaurante
        String uidRestaurante = pedido.getUidRestaurante();
        if (uidRestaurante != null && !uidRestaurante.isEmpty()) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("restaurant").document(uidRestaurante)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String imageUrl = documentSnapshot.getString("imageUrl");
                            Glide.with(holder.imgMenuItem.getContext())
                                    .load(imageUrl)
                                    .placeholder(R.drawable.bembos)
                                    .error(R.drawable.bembos)
                                    .into(holder.imgMenuItem);
                        } else {
                            holder.imgMenuItem.setImageResource(R.drawable.bembos);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Adapter", "Error al cargar imagen", e);
                        holder.imgMenuItem.setImageResource(R.drawable.bembos);
                    });
        } else {
            holder.imgMenuItem.setImageResource(R.drawable.bembos);
        }

        // Acciones de botones
        holder.button1.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(pedido);
            }
        });

        holder.button3.setOnClickListener(v -> {
            if (listener != null) {
                listener.onButton3Click(pedido);
            }
        });

        holder.button2.setOnClickListener(v -> {
            if (listener != null) {
                listener.onButton2Click(pedido);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pedidosList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textNameProduct, textPrice;
        ImageView imgMenuItem;
        ImageButton button1, button2, button3; // Agregar button2

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textNameProduct = itemView.findViewById(R.id.text_name_product);
            textPrice = itemView.findViewById(R.id.text_price);
            imgMenuItem = itemView.findViewById(R.id.img_menu_item);
            button1 = itemView.findViewById(R.id.button1);
            button2 = itemView.findViewById(R.id.button2); // Referencia al button2
            button3 = itemView.findViewById(R.id.button3);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Pedidos pedido);

        void onButton3Click(Pedidos pedido);

        void onButton2Click(Pedidos pedido); // Nuevo método para el button2
    }
}
