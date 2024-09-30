package com.example.superadmin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superadmin.R;

import java.util.List;

public class AdapterDots extends RecyclerView.Adapter<AdapterDots.StringViewHolder> {

    private List<String> stringList;
    private LayoutInflater layoutInflater;

    public AdapterDots(Context context, List<String> stringList) {
        this.stringList = stringList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public StringViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_dot_slide, parent, false);
        return new StringViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StringViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public static class StringViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public StringViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}