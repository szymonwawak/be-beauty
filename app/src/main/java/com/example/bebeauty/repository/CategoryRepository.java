package com.example.bebeauty.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.bebeauty.model.Category;
import com.example.bebeauty.service.ApiService;
import com.example.bebeauty.utils.RetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryRepository {
    private Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
    private MutableLiveData<List<Category>> categories = new MutableLiveData<>();

    public MutableLiveData<List<Category>> getAllCategories() {

        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<Category>> call = apiService.getCategories();
        call.enqueue(new Callback<List<Category>>() {
                         @Override
                         public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                             categories.setValue(response.body());
                         }

                         @Override
                         public void onFailure(Call<List<Category>> call, Throwable t) {
                         }
                     }
        );
        return categories;
    }

}
