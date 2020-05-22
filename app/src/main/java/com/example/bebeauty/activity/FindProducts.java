package com.example.bebeauty.activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bebeauty.R;
import com.example.bebeauty.adapter.ProductsAdapter;
import com.example.bebeauty.model.Category;
import com.example.bebeauty.model.Product;
import com.example.bebeauty.repository.ProductRepository;
import com.example.bebeauty.service.ApiService;
import com.example.bebeauty.utils.RetrofitBuilder;
import com.example.bebeauty.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FindProducts extends AppCompatActivity {
    private RecyclerView productsRecyclerView;
    private ProductsAdapter productsAdapter;
    private SearchView searchView;
    View view;
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
        initFields();
        initProductRecycler();
        initSearchView();
        prepareCategoriesSpinner();
        loadCategories();
        Utils.setHidingMenuOnClick(view, FindProducts.this);
    }

    private void initFields() {
        setContentView(R.layout.activity_find_products);
        view = findViewById(R.id.activity_find_products);
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
        setRecyclerListener();
    }

    private void initProductRecycler() {
        productsAdapter = new ProductsAdapter(this);
        productsAdapter.setProducts(products);
        layoutManager = new LinearLayoutManager(this);
        productsRecyclerView.setLayoutManager(layoutManager);
        productsRecyclerView.setHasFixedSize(true);
        productsRecyclerView.setAdapter(productsAdapter);
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
        ProductRepository productRepository = new ProductRepository();
        productRepository.findProducts(query, category, pageNumber, PAGE_SIZE).observe(this, productList -> {
            if (clearList)
                products.clear();
            isLastPage = productList.size() < PAGE_SIZE;
            products.addAll(productList);
            productsAdapter.notifyDataSetChanged();
            isLoading = false;
            currentPageNumber++;
        });
    }

    private void prepareCategoriesSpinner() {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }

    private void loadCategories() {
        Retrofit retrofit = RetrofitBuilder.getRetrofitInstance();
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

    private void setRecyclerListener() {
        productsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                loadDataIfNeededAndPossible();
            }
        });
    }

    private void loadDataIfNeededAndPossible() {
        visibleItemCount = layoutManager.getChildCount();
        totalItemCount = layoutManager.getItemCount();
        firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        if (!isLoading && !isLastPage && needsLoading()) {
            Category category = (Category) categorySpinner.getSelectedItem();
            String query = searchView.getQuery().toString();
            isLoading = true;
            fetchProducts(query, category, currentPageNumber, false);
        }
    }

    private boolean needsLoading() {
        return (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                && firstVisibleItemPosition >= 0
                && totalItemCount >= PAGE_SIZE;
    }
}
