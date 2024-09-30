package com.example.superadmin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.superadmin.adapters.CategoryAdapter;

import java.util.Arrays;
import java.util.List;

public class CategoriesFragment extends Fragment implements CategoryAdapter.OnItemClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.rv_menu);
        List<String> categories = Arrays.asList("Mar y Sabor", "Sabores del Perú", "Doña Tradición", "Fusión Perú", "Delicias al Paso", "El Fogón Criollo");
        CategoryAdapter adapter = new CategoryAdapter(categories, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(String category) {
        requireActivity().startActivity(new Intent(requireContext(),ProductsActivity.class));
    }
}