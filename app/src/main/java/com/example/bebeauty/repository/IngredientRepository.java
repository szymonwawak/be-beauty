package com.example.bebeauty.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.bebeauty.model.Ingredient;
import com.example.bebeauty.service.ApiService;
import com.example.bebeauty.utils.RetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class IngredientRepository {

    private Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
    private MutableLiveData<List<Ingredient>> ingredients = new MutableLiveData<>();
    private MutableLiveData<Boolean> requestSuccess = new MutableLiveData<>();
    private ApiService apiService = retrofit.create(ApiService.class);

    public MutableLiveData<List<Ingredient>> getAllIngredients() {
        Call<List<Ingredient>> call = apiService.getIngredients();
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

    public MutableLiveData<List<Ingredient>> findIngredients(String query) {
        Call<List<Ingredient>> call = apiService.findIngredientsByName(query);
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
                             requestSuccess.setValue(response.isSuccessful());
                         }

                         @Override
                         public void onFailure(Call<Ingredient> call, Throwable t) {
                             requestSuccess.setValue(true);
                         }
                     }
        );
        return requestSuccess;
    }
}
