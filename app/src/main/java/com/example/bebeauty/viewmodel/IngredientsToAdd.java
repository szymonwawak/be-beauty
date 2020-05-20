package com.example.bebeauty.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.bebeauty.model.Ingredient;
import com.example.bebeauty.repository.IngredientRepository;

import java.util.List;

public class IngredientsToAdd extends AndroidViewModel {
    private IngredientRepository ingredientRepository;

    public IngredientsToAdd(@NonNull Application application) {
        super(application);
        ingredientRepository = new IngredientRepository();
    }

    public MutableLiveData<List<Ingredient>> getIngredientsLiveData() {
        return ingredientRepository.getAllIngredients();
    }

    public MutableLiveData<List<Ingredient>> filterIngredients(String query) {
        return ingredientRepository.findIngredients(query);
    }
}
