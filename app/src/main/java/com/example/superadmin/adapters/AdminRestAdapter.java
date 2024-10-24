package com.example.superadmin.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superadmin.R;
import com.example.superadmin.model.User;
import com.example.superadmin.super_habilitar_usuarios;

import java.util.List;

public class AdminRestAdapter extends RecyclerView.Adapter<AdminRestAdapter.AdminViewHolder> {
    private List<User> userList; // Lista de usuarios

    // Constructor para inicializar la lista de usuarios
    public AdminRestAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card_super_admin, parent, false); // Asegúrate de que este layout exista
        return new AdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder holder, int position) {
        User user = userList.get(position);
        holder.nameTextView.setText(user.getName());
        holder.rolTextView.setText(user.getRol());
        holder.statusTextView.setText(user.isStatus() ? "Habilitado" : "No Habilitado");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes manejar la redirección a otras pestañas o actividades
                Intent intent = new Intent(v.getContext(), super_habilitar_usuarios.class);
                // Puedes pasar datos si es necesario
                // Pasar los datos del usuario como extras
                intent.putExtra("name", user.getName());
                intent.putExtra("lastName", user.getLastName());
                intent.putExtra("dni", user.getDNI());
                intent.putExtra("correo", user.getCorreo());
                intent.putExtra("telefono", user.getNumberPhone());
                intent.putExtra("habilitado", user.isStatus());

                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // Clase ViewHolder para mantener las referencias a las vistas
    static class AdminViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView rolTextView;
        TextView statusTextView;

        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewName);
            rolTextView = itemView.findViewById(R.id.textViewRole);
            statusTextView = itemView.findViewById(R.id.textViewStatus);
        }
    }


}

