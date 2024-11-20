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

public class UserProductHistorialAdapter extends RecyclerView.Adapter<UserProductHistorialAdapter.ProductHistorialViewHolder> {

    private Context context;
    private List<UserProductInCar> productList;
    private OnItemClickListener onItemClickListener;

    public UserProductHistorialAdapter(Context context, List<UserProductInCar> productList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.productList = productList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ProductHistorialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item_historial, parent, false);
        return new ProductHistorialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserProductHistorialAdapter.ProductHistorialViewHolder holder, int position) {
        UserProductInCar product = productList.get(position);

        holder.textModel.setText(product.getName());
        holder.imgProduct.setImageResource(product.getImageResId());
        holder.textAmount.setText(String.valueOf(product.getPrice()));


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductHistorialViewHolder extends RecyclerView.ViewHolder {

        TextView textModel, textAmount, textNumberProducts;
        ImageView imgProduct, btnAddProduct, btnMinusProduct;

        CardView containerSingleProduct, btnRemoveCard, btnCopyCard, btnRemove, btnCopy;

        public ProductHistorialViewHolder(@NonNull View itemView) {
            super(itemView);

            textModel = itemView.findViewById(R.id.text_model);
            imgProduct = itemView.findViewById(R.id.img_product);
            textAmount = itemView.findViewById(R.id.text_amount);
        }
    }

    public interface OnItemClickListener {
        void onAddClick(int position);
        void onMinusClick(int position);
        void onRemoveClick(int position);
        void onCopyClick(int position);
    }
}