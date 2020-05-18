package com.example.bebeauty.service;

import com.example.bebeauty.model.Product;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @GET("products")
    Call<List<Product>> getProducts();

    @POST("products/findByBarcode")
    Call<Product> findProductByBarcode(@Body String barcode);
}
