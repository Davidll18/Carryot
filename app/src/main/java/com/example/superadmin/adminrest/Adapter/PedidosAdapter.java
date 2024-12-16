package com.example.superadmin.adminrest.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.superadmin.R;
import com.example.superadmin.adminrest.DishesActivity;
import com.example.superadmin.adminrest.OrderDetailsActivity;
import com.example.superadmin.adminrest.PedidosActivity;
import com.example.superadmin.dtos.Pedidos;
import com.example.superadmin.dtos.PlatoDTO;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.FoodViewHolder> {  // Extender RecyclerView.Adapter
    private List<Pedidos> listPedidos;
    Context context;

    public PedidosAdapter(Context context, List<Pedidos> listPedidos) {
        this.context = context;
        this.listPedidos = listPedidos;
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
        Context context = holder.itemView.getContext();
        Pedidos pedidosItem = listPedidos.get(position);
        holder.numeroOrden.setText(pedidosItem.getNumeroPedido());
        holder.status.setText(pedidosItem.getEstado());

        if (pedidosItem.getEstado().equals("Preparacion")) {
            String status = "En preparación";
            holder.status.setText(status);
            holder.status.setBackground(ContextCompat.getDrawable(context, R.drawable.background_green));
        } else if (pedidosItem.getEstado().equals("Rechazado")) {
            String status = "Rechazado";
            holder.status.setText(status);
            holder.status.setBackground(ContextCompat.getDrawable(context, R.drawable.background_red));
        } else if (pedidosItem.getEstado().equals("En camino")) {
            String status = "En camino";
            holder.status.setText(status);
            holder.status.setBackground(ContextCompat.getDrawable(context, R.drawable.background_blue));
        } else if (pedidosItem.getEstado().equals("Entregado")) {
            String status = "Entregado";
            holder.status.setText(status);
            holder.status.setBackground(ContextCompat.getDrawable(context, R.drawable.background_default));
        }

        String descripcion = "";

        if (pedidosItem.getCantidad1() != null && !pedidosItem.getCantidad1().isEmpty() &&
                pedidosItem.getPlato1() != null && !pedidosItem.getPlato1().isEmpty()) {
            descripcion += pedidosItem.getCantidad1() + " " + pedidosItem.getPlato1() + "\n";
        }

        if (pedidosItem.getCantidad2() != null && !pedidosItem.getCantidad2().isEmpty() &&
                pedidosItem.getPlato2() != null && !pedidosItem.getPlato2().isEmpty()) {
            descripcion += pedidosItem.getCantidad2() + " " + pedidosItem.getPlato2() + "\n";
        }

        if (pedidosItem.getCantidad3() != null && !pedidosItem.getCantidad3().isEmpty() &&
                pedidosItem.getPlato3() != null && !pedidosItem.getPlato3().isEmpty()) {
            descripcion += pedidosItem.getCantidad3() + " " + pedidosItem.getPlato3();
        }

        holder.descripcion.setText(descripcion);

        Glide.with(context)
                .load(pedidosItem.getImageUrl())  // Carga la imagen desde la URL
                .placeholder(R.drawable.logo)  // Imagen por defecto mientras carga
                .error(R.drawable.logo)  // Imagen en caso de error
                .into(holder.platoImagen);
        holder.precio.setText(pedidosItem.getCostoTotal());

        holder.iconoVista.setOnClickListener(v ->{
            Intent intent = new Intent(context, OrderDetailsActivity.class);
            context.startActivity(intent);
        });
        holder.iconoAccept.setOnClickListener(v ->{
            showConfirmationDialog(pedidosItem, holder, context);
        });
    }

    private void showConfirmationDialog(Pedidos pedidosItem, PedidosAdapter.FoodViewHolder holder, Context context) {
        // Crear el cuadro de diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Aceptar pedido");
        builder.setMessage("¿Estás seguro de que deseas aceptar el pedido?");

        // Configurar el botón "Sí"
        builder.setPositiveButton("Sí", (dialog, which) -> {
            // Ejecutar la función para cambiar el estado
            toggleAvailability(pedidosItem, holder, context);
        });

        // Configurar el botón "No"
        builder.setNegativeButton("No", (dialog, which) -> {
            // Cerrar el cuadro de diálogo
            toggleUnavailability(pedidosItem, holder, context);
        });

        // Mostrar el cuadro de diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void toggleUnavailability(Pedidos pedidosItem, FoodViewHolder holder, Context context) {
        String uidPedido = pedidosItem.getUidCreacion();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Crear un mapa con la actualización
        Map<String, Object> pedido = new HashMap<>();
        pedido.put("estado", "Rechazado"); // 'estado' es la clave, y "En preparacion" es el valor

        // Actualizar el documento en Firestore
        db.collection("pedidos").document(uidPedido)
                .update(pedido)
                .addOnSuccessListener(aVoid -> {
                    // Notificar al usuario del éxito
                    Toast.makeText(context, "Pedido actualizado correctamente.", Toast.LENGTH_SHORT).show();

                    // Redirigir a la actividad PedidosActivity
                    Intent intent = new Intent(context, PedidosActivity.class);
                    context.startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    // Notificar al usuario del error
                    Toast.makeText(context, "Error al actualizar el pedido: " + uidPedido, Toast.LENGTH_SHORT).show();
                });
    }

    private void toggleAvailability(Pedidos pedidosItem, PedidosAdapter.FoodViewHolder holder, Context context) {
        String uidPedido = pedidosItem.getUidCreacion();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Crear un mapa con la actualización
        Map<String, Object> pedido = new HashMap<>();
        pedido.put("estado", "En preparacion");

        // Actualizar el documento en Firestore
        db.collection("pedidos").document(uidPedido)
                .update(pedido)
                .addOnSuccessListener(aVoid -> {
                    // Notificar al usuario del éxito
                    Toast.makeText(context, "Pedido actualizado correctamente.", Toast.LENGTH_SHORT).show();

                    // Redirigir a la actividad PedidosActivity
                    Intent intent = new Intent(context, PedidosActivity.class);
                    context.startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    // Notificar al usuario del error
                    Toast.makeText(context, "Error al actualizar el pedido: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }



    @Override
    public int getItemCount() {
        return listPedidos.size();  // Devolver el tamaño de la lista
    }

    // Clase interna para el ViewHolder
    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView numeroOrden;
        TextView status;
        TextView descripcion;
        ImageView platoImagen;
        ImageView iconoVista;
        ImageView iconoAccept;
        TextView precio;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            numeroOrden = itemView.findViewById(R.id.text_name_product);
            status = itemView.findViewById(R.id.availability_flag);
            descripcion = itemView.findViewById(R.id.text_model);
            platoImagen = itemView.findViewById(R.id.img_menu_item);
            iconoVista = itemView.findViewById(R.id.viewDetails);
            iconoAccept = itemView.findViewById(R.id.accept);
            precio = itemView.findViewById(R.id.text_price);
        }
    }
}
