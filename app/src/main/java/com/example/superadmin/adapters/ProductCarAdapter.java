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
import com.example.superadmin.model.ProductInCar;

import java.util.List;

public class ProductCarAdapter  extends RecyclerView.Adapter<ProductCarAdapter.ProductCarViewHolder> {

    private Context context;
    private List<ProductInCar> productList;
    private OnItemClickListener onItemClickListener;

    public ProductCarAdapter(Context context, List<ProductInCar> productList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.productList = productList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ProductCarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_car, parent, false);
        return new ProductCarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCarViewHolder holder, int position) {
        ProductInCar product = productList.get(position);

        holder.textModel.setText(product.getName());
        holder.textAmount.setText(String.valueOf(product.getPrice()));

        holder.btnAddProduct.setOnClickListener(v -> onItemClickListener.onAddClick(position));
        holder.btnMinusProduct.setOnClickListener(v -> onItemClickListener.onMinusClick(position));
        holder.btnRemove.setOnClickListener(v -> onItemClickListener.onRemoveClick(position));
        holder.btnCopy.setOnClickListener(v -> onItemClickListener.onCopyClick(position));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductCarViewHolder extends RecyclerView.ViewHolder {

        TextView textModel, textAmount, textNumberProducts;
        ImageView imgProduct, btnAddProduct, btnMinusProduct;

        CardView containerSingleProduct, btnRemoveCard, btnCopyCard, btnRemove, btnCopy;

        public ProductCarViewHolder(@NonNull View itemView) {
            super(itemView);

            textModel = itemView.findViewById(R.id.text_model);
            textAmount = itemView.findViewById(R.id.text_amount);
            textNumberProducts = itemView.findViewById(R.id.text_number_products);
            imgProduct = itemView.findViewById(R.id.img_product);
            btnAddProduct = itemView.findViewById(R.id.btn_add_numbers_producst);
            btnMinusProduct = itemView.findViewById(R.id.btn_minus_numbers_products);
            btnRemove = itemView.findViewById(R.id.btn_remove);
            btnCopy = itemView.findViewById(R.id.btn_copy);
        }
    }

    public interface OnItemClickListener {
        void onAddClick(int position);
        void onMinusClick(int position);
        void onRemoveClick(int position);
        void onCopyClick(int position);
    }
}