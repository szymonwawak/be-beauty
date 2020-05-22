package com.example.bebeauty.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.bebeauty.model.Ingredient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngredientRepository extends Repository {

    private MutableLiveData<List<Ingredient>> ingredients = new MutableLiveData<>();

    public MutableLiveData<List<Ingredient>> findIngredients(String query, int pageNumber, int pageSize) {
        Call<List<Ingredient>> call = apiService.findIngredientsByName(query, pageNumber, pageSize);
        call.enqueue(new Callback<List<Ingredient>>() {
                         @Override
                         public void onResponse(Call<List<Ingredient>> call, Response<List<Ingredient>> response) {
                             ingredients.setValue(response.body());
                         }

                         @Override
                         public void onFailure(Call<List<Ingredient>> call, Throwable t) {
                         }
                     }
        );
        return ingredients;
    }

    public MutableLiveData<Boolean> saveIngredient(Ingredient ingredient) {
        Call<Ingredient> call = apiService.saveIngredient(ingredient);
        call.enqueue(new Callback<Ingredient>() {
                         @Override
                         public void onResponse(Call<Ingredient> call, Response<Ingredient> response) {
                             requestStatus.setValue(response.isSuccessful());
                         }

                         @Override
                         public void onFailure(Call<Ingredient> call, Throwable t) {
                             requestStatus.setValue(false);
                         }
                     }
        );
        return requestStatus;
    }
}
