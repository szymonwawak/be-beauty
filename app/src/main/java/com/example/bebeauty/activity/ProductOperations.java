package com.example.bebeauty.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bebeauty.R;
import com.example.bebeauty.adapter.IngredientsAdapter;
import com.example.bebeauty.fragment.AddIngredients;
import com.example.bebeauty.model.Category;
import com.example.bebeauty.model.Ingredient;
import com.example.bebeauty.model.Product;
import com.example.bebeauty.repository.CategoryRepository;
import com.example.bebeauty.repository.ProductRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public abstract class ProductOperations extends AppCompatActivity {

    TextInputEditText name;
    TextInputEditText description;
    TextInputEditText barcode;
    TextInputEditText manufacturer;
    Spinner categoriesSpinner;
    List<Ingredient> ingredients;
    IngredientsAdapter addedIngredientsAdapter;
    ProductRepository productRepository;
    ArrayAdapter<Category> adapter;
    RecyclerView addedIngredientRecycler;
    List<Category> categories;
    CategoryRepository categoryRepository;
    MaterialButton addIngredientsButton;
    MaterialButton saveButton;
    Product product;
    Toast toast;
    View view;

    abstract void saveProduct();

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients.clear();
        this.ingredients.addAll(ingredients);
        addedIngredientsAdapter.notifyDataSetChanged();
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    void initActivityFields() {
        categoriesSpinner = view.findViewById(R.id.categories);
        name = view.findViewById(R.id.product_name);
        description = view.findViewById(R.id.product_description);
        barcode = view.findViewById(R.id.product_barcode);
        manufacturer = view.findViewById(R.id.product_manufacturer);
        addIngredientsButton = view.findViewById(R.id.add_ingredients);
        addIngredientsButton.setOnClickListener(v -> switchToIngredientsPanel());
        addedIngredientRecycler = view.findViewById(R.id.current_ingredients);
        saveButton = view.findViewById(R.id.create_product);
        saveButton.setOnClickListener(v -> saveProductIfValid());
        categories = new ArrayList<>();
        ingredients = new ArrayList<>();
        productRepository = new ProductRepository();
        categoryRepository = new CategoryRepository();
    }

    void saveProductIfValid() {
        if (validateProduct())
            saveProduct();
    }

    boolean validateProduct() {
        String productName = name.getText().toString();
        String productManufacturer = manufacturer.getText().toString();
        String productBarcode = barcode.getText().toString();
        String productDescription = description.getText().toString();
        if (productName.isEmpty()) {
            name.setError("Nazwa nie może być pusta!");
            return false;
        }
        if (productManufacturer.isEmpty()) {
            manufacturer.setError("Producent nie może być pusty!");
            return false;
        }
        if (productBarcode.isEmpty()) {
            barcode.setError("Kod kreskowy nie może być pusty!");
            return false;
        }
        if (productDescription.isEmpty()) {
            description.setError("Opis nie może być pusty!");
            return false;
        }
        return true;
    }

    private void switchToIngredientsPanel() {
        FragmentManager manager = getSupportFragmentManager();
        Bundle productBundle = new Bundle();
        productBundle.putSerializable("product", product);
        AddIngredients addIngredientsFragment = new AddIngredients();
        addIngredientsFragment.setArguments(productBundle);
        manager.beginTransaction().replace(R.id.fragmentsContainer, addIngredientsFragment).addToBackStack(null).commit();
    }

    void prepareCategoriesSpinner() {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesSpinner.setAdapter(adapter);
    }

    void loadCategories() {
        categoryRepository.getAllCategories().observe(this, categoryList -> {
            categories.addAll(categoryList);
            adapter.notifyDataSetChanged();
        });
    }

    void initIngredientsRecycler() {
        addedIngredientsAdapter = new IngredientsAdapter();
        addedIngredientsAdapter.setIngredients(ingredients);
        addedIngredientRecycler.setLayoutManager(new LinearLayoutManager(this));
        addedIngredientRecycler.setHasFixedSize(true);
        addedIngredientRecycler.setAdapter(addedIngredientsAdapter);
    }
}
