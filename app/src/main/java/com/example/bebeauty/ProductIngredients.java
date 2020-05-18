package com.example.bebeauty;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bebeauty.model.Product;

public class ProductIngredients extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_ingredients, container, false);
        Product product = (Product) getArguments().get("product");
        IngredientAdapter adapter = new IngredientAdapter();
        adapter.setIngredients(product.getIngredients());
        RecyclerView ingredientRecycler = view.findViewById(R.id.ingredientRecycler);
        initRecycler(adapter, ingredientRecycler);
        return view;
    }

    private void initRecycler(IngredientAdapter adapter, RecyclerView ingredientRecycler) {
        ingredientRecycler.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        ingredientRecycler.setHasFixedSize(true);
        ingredientRecycler.setAdapter(adapter);
    }
}
