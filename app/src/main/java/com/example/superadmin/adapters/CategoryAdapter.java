package com.example.superadmin.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.superadmin.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<String> categories;
    private OnItemClickListener listener;

    public CategoryAdapter(List<String> categories, OnItemClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        String category = categories.get(position);
        holder.textMenuName.setText(category);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(category);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView textMenuName;
        ImageView imgMenuItem;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            textMenuName = itemView.findViewById(R.id.text_menu_name);
            imgMenuItem = itemView.findViewById(R.id.img_menu_item);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String category);
    }
}
