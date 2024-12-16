package com.example.superadmin.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
    private List<User> userList;
    private Context context;
    private OnUserClickListener onUserClickListener;

    public AdminRestAdapter(List<User> userList, OnUserClickListener listener) {
        this.userList = userList;
        this.onUserClickListener = listener;
    }

    public interface OnUserClickListener {
        void onUserClick(User user);
    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext(); // Guardamos el contexto
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_super_admin, parent, false);
        return new AdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder holder, int position) {
        User user = userList.get(position);

        // Concatenar nombre y apellido y convertir a mayúsculas
        String fullName = (user.getName() + " " + user.getSurname()).toUpperCase();

        // Configurar vistas
        holder.textViewName.setText(fullName);
        holder.textViewRole.setText(transformRole(user.getRole()));

        // Configurar el estado del usuario (activo o inactivo)
        if (user.getStatus() != null && user.getStatus()) {
            holder.textViewStatus.setText("Activo");
        } else {
            holder.textViewStatus.setText("Inactivo");
        }

        // Configurar listener para clics
        holder.itemView.setOnClickListener(v -> onUserClickListener.onUserClick(user));
    }

    @Override
    public int getItemCount() {
        return userList != null ? userList.size() : 0;
    }

    // Transformar el rol en texto legible
    private String transformRole(String role) {
        switch (role) {
            case "ADMIN REST":
                return "Administrador de Restaurants";
            case "CLIENTE":
                return "Cliente";
            case "REPARTIDOR":
                return "Repartidor";
            case "SUPERADMIN":
                return "Superadministrador";
            default:
                return "Rol desconocido";
        }
    }

    public static class AdminViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewRole, textViewStatus;
        CardView cardView;

        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewRole = itemView.findViewById(R.id.textViewRole);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            cardView = itemView.findViewById(R.id.cardViewAdmin);
        }
    }
}
