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

public class AdapterSummary extends RecyclerView.Adapter<AdapterSummary.ProductCarViewHolder> {

    private Context context;
    private List<ProductInCar> productList;

    public AdapterSummary(Context context, List<ProductInCar> productList) {
        this.context = context;
        this.productList = productList;
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
        }
    }

}