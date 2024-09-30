package com.example.superadmin.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.superadmin.R;
import com.example.superadmin.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> products;
    private OnItemClickListener listener;

    public ProductAdapter(List<Product> products, OnItemClickListener listener) {
        this.products = products;
        this.listener = listener;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_products, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.textNameProduct.setText(product.getName());
        holder.textModel.setText(product.getModel());
        holder.textPrice.setText(product.getPrice());
        holder.imgMenuItem.setImageResource(product.getImageResourceId());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(product);
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
        TextView textModel;
        TextView textPrice;
        ImageView imgMenuItem;

        public ProductViewHolder(View itemView) {
            super(itemView);
            textNameProduct = itemView.findViewById(R.id.text_name_product);
            textModel = itemView.findViewById(R.id.text_model);
            textPrice = itemView.findViewById(R.id.text_price);
            imgMenuItem = itemView.findViewById(R.id.img_menu_item);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }
}
