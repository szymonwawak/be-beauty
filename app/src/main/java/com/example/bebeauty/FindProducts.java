package com.example.bebeauty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.bebeauty.adapter.FoundProductAdapter;
import com.example.bebeauty.model.Category;
import com.example.bebeauty.model.Product;
import com.example.bebeauty.service.ApiService;
import com.example.bebeauty.utils.RetrofitInstance;
import com.example.bebeauty.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FindProducts extends AppCompatActivity {
    private RecyclerView productsRecyclerView;
    private FoundProductAdapter foundProductAdapter;
    private SearchView searchView;
    private SearchManager searchManager;
    private Spinner categorySpinner;
    private List<Category> categories = new ArrayList<>();
    ArrayAdapter<Category> adapter;
    private ArrayList<Product> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_products);
        productsRecyclerView = findViewById(R.id.found_products);
        searchView = findViewById(R.id.searchView);
        categorySpinner = findViewById(R.id.categories);
        foundProductAdapter = new FoundProductAdapter();
        foundProductAdapter.setProducts(products);
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productsRecyclerView.setHasFixedSize(true);
        productsRecyclerView.setAdapter(foundProductAdapter);
        initSearchView();
        View view = findViewById(R.id.activity_find_products);
        setHidingMenuOnClick(view);
        prepareCategoriesSpinner();
        loadCategories();
    }

    private void initSearchView() {
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName())
        );
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Category category = (Category) categorySpinner.getSelectedItem();
                fetchProducts(query, category);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Category category = (Category) categorySpinner.getSelectedItem();
                fetchProducts(newText, category);
                return false;
            }
        });
    }

    private void fetchProducts(String query, Category category) {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<Product>> call = apiService.filterProducts(query, category);
        call.enqueue(new Callback<List<Product>>() {
                         @Override
                         public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                             products.clear();
                             products.addAll(response.body());
                             foundProductAdapter.notifyDataSetChanged();
                         }

                         @Override
                         public void onFailure(Call<List<Product>> call, Throwable t) {
                         }
                     }
        );
    }

    private void setHidingMenuOnClick(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utils.hideKeyboard(FindProducts.this);
                return false;
            }
        });
    }

    private void prepareCategoriesSpinner() {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }

    private void loadCategories() {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<Category>> call = apiService.getCategories();
        call.enqueue(new Callback<List<Category>>() {
                         @Override
                         public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                             categories.addAll(response.body());
                             adapter.notifyDataSetChanged();
                         }

                         @Override
                         public void onFailure(Call<List<Category>> call, Throwable t) {
                         }
                     }
        );
    }
}
