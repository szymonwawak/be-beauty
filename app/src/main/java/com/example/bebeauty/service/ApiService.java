package com.example.bebeauty.service;


import com.example.bebeauty.model.Category;
import com.example.bebeauty.model.Comment;
import com.example.bebeauty.model.Ingredient;
import com.example.bebeauty.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface ApiService {

    @POST("products")
    Call<Product> saveProduct(@Body Product product);

    @POST("products/findByBarcode")
    Call<Product> findProductByBarcode(@Body String barcode);

    @Multipart
    @POST("products/search")
    Call<List<Product>> filterProducts(@Part("name") String name, @Part("category") Category category, @Part("pageNumber") int pageNumber, @Part("pageSize") int pageSize);

    @PUT("products")
    Call<Product> updateProduct(@Body Product product);

    @GET("categories")
    Call<List<Category>> getCategories();

    @GET("ingredients")
    Call<List<Ingredient>> getIngredients();

    @POST("ingredients")
    Call<Ingredient> saveIngredient(@Body Ingredient ingredient);

    @Multipart
    @POST("ingredients/search")
    Call<List<Ingredient>> findIngredientsByName(@Part("name") String name, @Part("pageNumber") int pageNumber, @Part("pageSize") int pageSize);

    @POST("comments")
    Call<Comment> saveComment(@Body Comment comment);
}
