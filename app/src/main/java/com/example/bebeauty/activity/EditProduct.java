package com.example.bebeauty.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bebeauty.R;
import com.example.bebeauty.model.Category;
import com.example.bebeauty.model.Product;
import com.example.bebeauty.utils.Utils;

public class EditProduct extends ProductOperations {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        product = (Product) getIntent().getSerializableExtra("product");
        setContentView(R.layout.activity_edit_product);
        view = findViewById(R.id.activity_edit_product);
        Utils.setHidingMenuOnClick(view, EditProduct.this);
        initActivityFields();
        setFields();
        prepareCategoriesSpinner();
        loadCategories();
        initIngredientsRecycler();
    }

    private void setFields() {
        name.setText(product.getName());
        description.setText(product.getDescription());
        barcode.setText(product.getBarcode());
        manufacturer.setText(product.getManufacturer());
        ingredients = product.getIngredients();
    }


    @Override
    void saveProduct() {
        product.setBarcode(barcode.getText().toString());
        product.setDescription(description.getText().toString());
        product.setName(name.getText().toString());
        product.setManufacturer(manufacturer.getText().toString());
        product.setIngredients(ingredients);
        product.setAverageScore(product.getAverageScore());
        product.setCategory((Category) categoriesSpinner.getSelectedItem());
        productRepository.updateProduct(product).observe(this, success -> {
            if (success) {
                Intent intent = new Intent(EditProduct.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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
