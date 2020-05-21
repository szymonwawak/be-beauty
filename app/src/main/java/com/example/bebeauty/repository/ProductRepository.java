package com.example.bebeauty.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.bebeauty.model.Product;
import com.example.bebeauty.service.ApiService;
import com.example.bebeauty.utils.RetrofitInstance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProductRepository {

    private Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
    private ApiService apiService = retrofit.create(ApiService.class);
    MutableLiveData<Boolean> requestSuccess = new MutableLiveData<>();

    public List<Product> getAllProducts() {
        final List<Product> products = new ArrayList<Product>();
        Call<List<Product>> call = apiService.getProducts();
        call.enqueue(new Callback<List<Product>>() {
                         @Override
                         public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                             products.addAll(response.body());
                         }

                         @Override
                         public void onFailure(Call<List<Product>> call, Throwable t) {
                         }
                     }
        );
        return products;
    }

    public Product getProductByBarcode(String barcode) {
        Product product = null;
        Call<Product> call = apiService.findProductByBarcode(barcode);
        try {
            product = call.execute().body();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return product;
    }

    public MutableLiveData<Boolean> saveProduct(Product product) {
        Call<Product> call = apiService.saveProduct(product);
        call.enqueue(new Callback<Product>() {
                         @Override
                         public void onResponse(Call<Product> call, Response<Product> response) {
                             requestSuccess.setValue(true);
                         }

                         @Override
                         public void onFailure(Call<Product> call, Throwable t) {
                             requestSuccess.setValue(false);
                         }
                     }
        );
        return requestSuccess;
    }

    public MutableLiveData<Boolean> updateProduct(Product product) {
        Call<Product> call = apiService.updateProduct(product);
        call.enqueue(new Callback<Product>() {
                         @Override
                         public void onResponse(Call<Product> call, Response<Product> response) {
                             requestSuccess.setValue(true);
                         }

                         @Override
                         public void onFailure(Call<Product> call, Throwable t) {
                             requestSuccess.setValue(false);
                         }
                     }
        );
        return requestSuccess;
    }
}
