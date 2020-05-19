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

    public MutableLiveData<List<Ingredient>> getAllIngredients() {
        ApiService apiService = retrofit.create(ApiService.class);
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
}
