package com.example.superadmin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superadmin.R;
import com.example.superadmin.model.UserProductInCar;

import java.util.List;

public class UserProductCarAdapter extends RecyclerView.Adapter<UserProductCarAdapter.ProductCarViewHolder> {

    private Context context;
    private List<UserProductInCar> productList;
    private OnItemClickListener onItemClickListener;

    public UserProductCarAdapter(Context context, List<UserProductInCar> productList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.productList = productList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ProductCarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item_car, parent, false);
        return new ProductCarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCarViewHolder holder, int position) {
        UserProductInCar product = productList.get(position);

        holder.textModel.setText(product.getName());
        holder.textNumberProducts.setText(String.valueOf(product.getQuantity()));
        holder.textAmount.setText("S/ " + String.format("%.2f", product.getPrice()));
        holder.imgProduct.setImageResource(product.getImageResId());

        // Botón más
        holder.btnAddProduct.setOnClickListener(v -> {
            onItemClickListener.onAddClick(position);
            notifyItemChanged(position);
        });

        // Botón menos
        holder.btnMinusProduct.setOnClickListener(v -> {
            onItemClickListener.onMinusClick(position);
            notifyItemChanged(position);
        });

        // Botón eliminar
        holder.btnRemove.setOnClickListener(v -> onItemClickListener.onRemoveClick(position));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductCarViewHolder extends RecyclerView.ViewHolder {

        TextView textModel, textAmount, textNumberProducts;
        ImageView imgProduct, btnAddProduct, btnMinusProduct;
        CardView btnRemove;

        public ProductCarViewHolder(@NonNull View itemView) {
            super(itemView);

            textModel = itemView.findViewById(R.id.text_model);
            textAmount = itemView.findViewById(R.id.text_amount);
            textNumberProducts = itemView.findViewById(R.id.text_number_products);
            imgProduct = itemView.findViewById(R.id.img_product);
            btnAddProduct = itemView.findViewById(R.id.btn_add_numbers_producst);
            btnMinusProduct = itemView.findViewById(R.id.btn_minus_numbers_products);
            btnRemove = itemView.findViewById(R.id.btn_remove);
        }
    }

    public interface OnItemClickListener {
        void onAddClick(int position);
        void onMinusClick(int position);
        void onRemoveClick(int position);
    }
}
