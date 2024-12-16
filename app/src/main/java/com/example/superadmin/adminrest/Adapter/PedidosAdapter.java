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
import androidx.recyclerview.widget.RecyclerView;


import com.example.superadmin.R;
import com.example.superadmin.adminrest.DishesActivity;
import com.example.superadmin.adminrest.OrderDetailsActivity;
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

        //holder.platoImagen.setImageResource(pedidosItem.getPlatoImagen());
        holder.precio.setText(pedidosItem.getCostoTotal());
        StringBuilder descripcion = new StringBuilder();
        Map<String, Integer> productos = pedidosItem.getProductos();
        for (Map.Entry<String, Integer> entry : productos.entrySet()) {
            descripcion.append(entry.getValue()) // Cantidad
                    .append("  ")            // Separador
                    .append(entry.getKey())  // Nombre del plato
                    .append("\n");           // Nueva línea
        }
        holder.descripcion.setText(descripcion.toString().trim());
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
            dialog.dismiss();
        });

        // Mostrar el cuadro de diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void toggleAvailability(Pedidos pedidosItem, PedidosAdapter.FoodViewHolder holder, Context context) {

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
