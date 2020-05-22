package com.example.bebeauty.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bebeauty.R;
import com.example.bebeauty.adapter.IngredientsAdapter;
import com.example.bebeauty.model.Product;

public class ProductIngredients extends Fragment {
    private IngredientsAdapter ingredientsAdapter;
    private Product product;
    private RecyclerView ingredientRecycler;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_product_ingredients, container, false);
        product = (Product) getArguments().get("product");
        initFragment();
        return view;
    }

    private void initFragment() {
        ingredientsAdapter = new IngredientsAdapter();
        ingredientsAdapter.setIngredients(product.getIngredients());
        ingredientRecycler = view.findViewById(R.id.ingredientRecycler);
        initRecycler();
    }

    private void initRecycler() {
        ingredientRecycler.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        ingredientRecycler.setHasFixedSize(true);
        ingredientRecycler.setAdapter(ingredientsAdapter);
    }
}
