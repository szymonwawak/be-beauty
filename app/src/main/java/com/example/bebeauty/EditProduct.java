package com.example.bebeauty;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bebeauty.adapter.IngredientsAdapter;
import com.example.bebeauty.fragment.AddIngredients;
import com.example.bebeauty.model.Category;
import com.example.bebeauty.model.Ingredient;
import com.example.bebeauty.model.Product;
import com.example.bebeauty.repository.ProductRepository;
import com.example.bebeauty.utils.Utils;
import com.example.bebeauty.viewmodel.CreateProductModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class EditProduct extends ProductOperations {

    MaterialButton addIngredientsButton;
    MaterialButton saveButton;
    TextInputEditText name;
    TextInputEditText description;
    TextInputEditText barcode;
    TextInputEditText manufacturer;
    Spinner categoriesSpinner;
    Product product;
    ProductRepository productRepository = new ProductRepository();
    CreateProductModel viewModel;
    ArrayAdapter<Category> adapter;
    RecyclerView addedIngredientRecycler;
    List<Category> categories = new ArrayList<>();
    List<Ingredient> ingredients = new ArrayList<>();
    String foundBarcode;
    Toast toast;

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        product = (Product) getIntent().getSerializableExtra("product");
        product.setIngredients(new ArrayList<Ingredient>());
        setContentView(R.layout.activity_product_not_found);
        View view = findViewById(R.id.not_found_activity);
        setHidingMenuOnClick(view);
        initActivityFields(view);
        prepareCategoriesSpinner();
        loadCategories();
        initIngredientsRecycler();
    }

    private void setHidingMenuOnClick(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utils.hideKeyboard(EditProduct.this);
                return false;
            }
        });
    }

    private void initActivityFields(View view) {
        viewModel = new ViewModelProvider(this).get(CreateProductModel.class);
        categoriesSpinner = view.findViewById(R.id.categories);
        name = view.findViewById(R.id.product_name);
        name.setText(product.getName());
        description = view.findViewById(R.id.product_description);
        description.setText(product.getDescription());
        barcode = view.findViewById(R.id.product_barcode);
        barcode.setText(product.getBarcode());
        manufacturer = view.findViewById(R.id.product_manufacturer);
        manufacturer.setText(product.getManufacturer());
        addIngredientsButton = view.findViewById(R.id.add_ingredients);
        addIngredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToIngredientsPanel();
            }
        });
        addedIngredientRecycler = view.findViewById(R.id.current_ingredients);
        saveButton = view.findViewById(R.id.create_product);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProductIfValid();
            }
        });
    }

    private void switchToIngredientsPanel() {
        FragmentManager manager = getSupportFragmentManager();
        Bundle productBundle = new Bundle();
        productBundle.putSerializable("product", product);
        AddIngredients addIngredientsFragment = new AddIngredients();
        addIngredientsFragment.setArguments(productBundle);
        manager.beginTransaction().replace(R.id.fragmentsContainer, addIngredientsFragment).addToBackStack(null).commit();
    }

    private void saveProductIfValid() {
        if (validateProduct())
            saveProduct();
    }

    private boolean validateProduct() {
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

    private void saveProduct() {
        product.setBarcode(barcode.getText().toString());
        product.setDescription(description.getText().toString());
        product.setName(name.getText().toString());
        product.setManufacturer(manufacturer.getText().toString());
        product.setIngredients(ingredients);
        product.setAverageScore(product.getAverageScore());
        product.setCategory((Category) categoriesSpinner.getSelectedItem());
        productRepository.updateProduct(product).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                if (success) {
                    toast = Toast.makeText(getApplicationContext(), "Produkt został dodany!", Toast.LENGTH_LONG);
                    toast.show();
                    finish();
                } else {
                    toast = Toast.makeText(getApplicationContext(), "Nie udało się dodać produktu. Spróbuj ponownie", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    private void prepareCategoriesSpinner() {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesSpinner.setAdapter(adapter);
    }

    private void loadCategories() {
        viewModel.getCategoryLiveData().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categoryList) {
                categories.addAll(categoryList);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initIngredientsRecycler() {
        addedIngredientsAdapter = new IngredientsAdapter();
        ingredients = product.getIngredients();
        addedIngredientsAdapter.setIngredients(ingredients);
        addedIngredientRecycler.setLayoutManager(new LinearLayoutManager(this));
        addedIngredientRecycler.setHasFixedSize(true);
        addedIngredientRecycler.setAdapter(addedIngredientsAdapter);
    }
}
