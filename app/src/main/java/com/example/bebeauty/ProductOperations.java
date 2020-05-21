package com.example.bebeauty;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bebeauty.adapter.IngredientsAdapter;
import com.example.bebeauty.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

public abstract class ProductOperations extends AppCompatActivity {

    List<Ingredient> ingredients = new ArrayList<>();
    IngredientsAdapter addedIngredientsAdapter;

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients.clear();
        this.ingredients.addAll(ingredients);
        addedIngredientsAdapter.notifyDataSetChanged();
    }
}
