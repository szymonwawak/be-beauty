package com.example.bebeauty.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.bebeauty.model.Category;
import com.example.bebeauty.model.Product;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository extends Repository {

    MutableLiveData<List<Product>> products = new MutableLiveData<>();

    public MutableLiveData<List<Product>> findProducts(String query, Category category, int pageNumber, int pageSize) {
        Call<List<Product>> call = apiService.filterProducts(query, category, pageNumber, pageSize);
        call.enqueue(new Callback<List<Product>>() {
                         @Override
                         public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                             products.setValue(response.body());
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
                             requestStatus.setValue(true);
                         }

                         @Override
                         public void onFailure(Call<Product> call, Throwable t) {
                             requestStatus.setValue(false);
                         }
                     }
        );
        return requestStatus;
    }

    public MutableLiveData<Boolean> updateProduct(Product product) {
        Call<Product> call = apiService.updateProduct(product);
        call.enqueue(new Callback<Product>() {
                         @Override
                         public void onResponse(Call<Product> call, Response<Product> response) {
                             requestStatus.setValue(true);
                         }

                         @Override
                         public void onFailure(Call<Product> call, Throwable t) {
                             requestStatus.setValue(false);
                         }
                     }
        );
        return requestStatus;
    }
}
