package com.example.superadmin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superadmin.R;
import com.example.superadmin.Superadmin.super_habilitar_usuarios;
import com.example.superadmin.dtos.User;

import java.util.List;

public class AdminRestAdapter extends RecyclerView.Adapter<AdminRestAdapter.AdminViewHolder> {
    private List<User> userList; // Lista de usuarios
    private Context context;


    // Constructor para inicializar la lista de usuarios
    public AdminRestAdapter(List<User> userList) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext(); // Guardamos el contexto para usarlo en los listeners
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_super_admin, parent, false);
        return new AdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder holder, int position) {
        User user = userList.get(position);
        holder.textViewName.setText(user.getName());
        holder.textViewRole.setText(user.getRole());
        holder.bind(user);

        // Transformar el rol antes de mostrarlo
        String role = user.getRole();
        switch (role) {
            case "ADMIN REST":
                holder.textViewRole.setText("Administrador de Restaurants");
                break;
            case "CLIENTE":
                holder.textViewRole.setText("Cliente");
                break;
            case "REPARTIDOR":
                holder.textViewRole.setText("Repartidor");
                break;
            case "SUPERADMIN":
                holder.textViewRole.setText("Superadministrador");
                break;
            default:
                holder.textViewRole.setText("Rol desconocido");
                break;
        }

        // Asignar el estado
        holder.textViewStatus.setText(user.getStatus() ? "Activo" : "Inactivo");
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, super_habilitar_usuarios.class);
            intent.putExtra("userId", user.getUid()); // Puedes pasar datos como el ID del usuario
            context.startActivity(intent);
        });

        holder.itemView.setOnClickListener(v -> {
            // Crear Intent para enviar datos a la nueva actividad
            Intent intent = new Intent(context, super_habilitar_usuarios.class);
            intent.putExtra("nombre", user.getName());
            intent.putExtra("apellidos", user.getSurname());
            intent.putExtra("dni", user.getDni());
            intent.putExtra("correo", user.getEmail());
            intent.putExtra("telefono", user.getPhone());
            context.startActivity(intent);
        });
        // Imagen placeholder
    }


    @Override
    public int getItemCount() {
        return userList != null ? userList.size() : 0;
    }

    public static class AdminViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewRole, textViewStatus;
        CardView cardView;


        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewRole = itemView.findViewById(R.id.textViewRole);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            cardView = itemView.findViewById(R.id.cardViewAdmin); // ID del CardView en tu layout
        }
        public void bind(User user) {
            // Vincula los datos del usuario con las vistas
        }
    }

}

