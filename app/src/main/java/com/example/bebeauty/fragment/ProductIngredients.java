package com.example.bebeauty.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bebeauty.R;
import com.example.bebeauty.adapter.IngredientsAdapter;
import com.example.bebeauty.model.Product;

public class ProductIngredients extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_ingredients, container, false);
        initFragment(view);
        return view;
    }

    private void initFragment(View view) {
        Product product = (Product) getArguments().get("product");
        IngredientsAdapter adapter = new IngredientsAdapter();
        adapter.setIngredients(product.getIngredients());
        RecyclerView ingredientRecycler = view.findViewById(R.id.ingredientRecycler);
        initRecycler(adapter, ingredientRecycler);
    }

    private void initRecycler(IngredientsAdapter adapter, RecyclerView ingredientRecycler) {
        ingredientRecycler.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        ingredientRecycler.setHasFixedSize(true);
        ingredientRecycler.setAdapter(adapter);
    }
}
