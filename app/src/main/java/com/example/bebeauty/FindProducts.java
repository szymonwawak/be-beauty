package com.example.bebeauty;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private final int PAGE_SIZE = 8;
    int visibleItemCount;
    int totalItemCount;
    boolean isLoading = false;
    boolean isLastPage;
    int firstVisibleItemPosition;
    LinearLayoutManager layoutManager;
    int currentPageNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_products);
        productsRecyclerView = findViewById(R.id.found_products);
        searchView = findViewById(R.id.searchView);
        categorySpinner = findViewById(R.id.categories);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String query = searchView.getQuery().toString();
                currentPageNumber = 0;
                fetchProducts(query, categories.get(position), 0, true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        foundProductAdapter = new FoundProductAdapter(this);
        foundProductAdapter.setProducts(products);
        layoutManager = new LinearLayoutManager(this);
        productsRecyclerView.setLayoutManager(layoutManager);
        productsRecyclerView.setHasFixedSize(true);
        productsRecyclerView.setAdapter(foundProductAdapter);
        initSearchView();
        View view = findViewById(R.id.activity_find_products);
        setHidingMenuOnClick(view);
        prepareCategoriesSpinner();
        loadCategories();

        productsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= PAGE_SIZE) {
                        Category category = (Category) categorySpinner.getSelectedItem();
                        String query = searchView.getQuery().toString();
                        isLoading = true;
                        fetchProducts(query, category, currentPageNumber, false);
                    }
                }
            }
        });
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
                currentPageNumber = 0;
                fetchProducts(query, category, 0, true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Category category = (Category) categorySpinner.getSelectedItem();
                currentPageNumber = 0;
                fetchProducts(newText, category, 0, true);
                return false;
            }
        });
    }

    private void fetchProducts(String query, Category category, int pageNumber, boolean clearList) {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<Product>> call = apiService.filterProducts(query, category, pageNumber, PAGE_SIZE);
        call.enqueue(new Callback<List<Product>>() {
                         @Override
                         public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                             if (clearList)
                                 products.clear();
                             isLastPage = false;
                             if (response.body().size() < PAGE_SIZE)
                                 isLastPage = true;
                             products.addAll(response.body());
                             foundProductAdapter.notifyDataSetChanged();
                             isLoading = false;
                             currentPageNumber++;
                         }

                         @Override
                         public void onFailure(Call<List<Product>> call, Throwable t) {
                             isLoading = false;
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
