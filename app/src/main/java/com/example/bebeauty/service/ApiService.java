package com.example.bebeauty.service;


import com.example.bebeauty.model.Category;
import com.example.bebeauty.model.Ingredient;
import com.example.bebeauty.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("products")
    Call<List<Product>> getProducts();

    @POST("products/findByBarcode")
    Call<Product> findProductByBarcode(@Body String barcode);

    @POST("products")
    Call<Product> saveProduct(@Body Product product);

    @GET("categories")
    Call<List<Category>> getCategories();

    @GET("ingredients")
    Call<List<Ingredient>> getIngredients();

}
