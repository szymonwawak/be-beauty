package com.example.bebeauty.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.bebeauty.R;
import com.example.bebeauty.model.Category;
import com.example.bebeauty.model.Ingredient;
import com.example.bebeauty.model.Product;
import com.example.bebeauty.utils.Utils;

import java.util.ArrayList;

public class CreateProduct extends ProductOperations {

    String foundBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        foundBarcode = getIntent().getStringExtra("barcode");
        product = new Product();
        product.setIngredients(new ArrayList<Ingredient>());
        setContentView(R.layout.activity_product_not_found);
        view = findViewById(R.id.not_found_activity);
        Utils.setHidingMenuOnClick(view, CreateProduct.this);
        initActivityFields();
        setFields();
        prepareCategoriesSpinner();
        loadCategories();
        initIngredientsRecycler();
    }

    private void setFields() {
        barcode.setText(foundBarcode);
    }

    @Override
    void saveProduct() {
        product.setBarcode(barcode.getText().toString());
        product.setDescription(description.getText().toString());
        product.setName(name.getText().toString());
        product.setManufacturer(manufacturer.getText().toString());
        product.setIngredients(ingredients);
        product.setAverageScore(0.0f);
        product.setCategory((Category) categoriesSpinner.getSelectedItem());
        productRepository.saveProduct(product).observe(this, success -> {
            if (success) {
                toast = Toast.makeText(getApplicationContext(), "Produkt został dodany!", Toast.LENGTH_LONG);
                toast.show();
                finish();
            } else {
                toast = Toast.makeText(getApplicationContext(), "Nie udało się dodać produktu. Spróbuj ponownie", Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }
}
