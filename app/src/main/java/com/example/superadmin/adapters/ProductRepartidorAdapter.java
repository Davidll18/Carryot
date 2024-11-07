package com.example.superadmin.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.superadmin.R;
import com.example.superadmin.model.Product;

import java.util.List;

public class ProductRepartidorAdapter extends RecyclerView.Adapter<ProductRepartidorAdapter.ProductViewHolder> {

    private List<Product> products;
    private OnItemClickListener listener;

    public ProductRepartidorAdapter(List<Product> products, OnItemClickListener listener) {
        this.products = products;
        this.listener = listener;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_products_repartidor, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.textNameProduct.setText(product.getName());
        holder.textPrice.setText(product.getPrice());
        holder.imgMenuItem.setImageResource(product.getImageResourceId());

        // Clic en el botón 2 (actuando como clic en la card)
        holder.button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(product);  // Mantener funcionalidad de clic en la card para el botón 2
                }
            }
        });

        // Clic en el botón 3 (con id "button3" en el layout del card)
        holder.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onButton3Click(product); // Método nuevo para el botón 3
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textNameProduct;
        TextView textPrice;
        ImageView imgMenuItem;
        ImageButton button3;  // Referencia al botón 3
        ImageButton button1;

        public ProductViewHolder(View itemView) {
            super(itemView);
            textNameProduct = itemView.findViewById(R.id.text_name_product);
            textPrice = itemView.findViewById(R.id.text_price);
            imgMenuItem = itemView.findViewById(R.id.img_menu_item);
            button3 = itemView.findViewById(R.id.button3);  // Enlazar el botón 3
            button1 = itemView.findViewById(R.id.button1);  // Enlazar el botón 2
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Product product);    // Para manejar el clic en el botón 2
        void onButton3Click(Product product); // Para manejar el clic en el botón 3
    }
}
