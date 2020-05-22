package com.example.bebeauty.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.bebeauty.R;
import com.example.bebeauty.fragment.ProductComments;
import com.example.bebeauty.fragment.ProductData;
import com.example.bebeauty.fragment.ProductIngredients;
import com.example.bebeauty.model.Product;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProductInfo extends AppCompatActivity {

    Product product;
    TextView name;
    Button dataButton;
    Button commentsButton;
    Button ingredientsButton;
    FloatingActionButton editProductButton;
    ExtendedFloatingActionButton addCommentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_view_activity);
        Intent intent = getIntent();
        product = (Product) intent.getSerializableExtra("product");
        initFields();
        setButtons();
        setDefaultFragment();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putAll(savedInstanceState);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void initFields() {
        name = findViewById(R.id.name_value);
        name.setText(product.getName());
    }

    private void setButtons() {
        setDataButtonListener();
        setCommentButtonListener();
        setIngredientButtonListener();
        setAddCommentButtonListener();
        setEditProductButtonListener();
    }

    private void setDataButtonListener() {
        dataButton = findViewById(R.id.data_button);
        dataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProductInfo();
            }
        });
    }

    private void showProductInfo() {
        ProductData productData = new ProductData();
        Bundle commentBundle = new Bundle();
        commentBundle.putSerializable("product", product);
        productData.setArguments(commentBundle);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.fragmentsContainer, productData).commit();
    }

    private void setCommentButtonListener() {
        commentsButton = findViewById(R.id.comments_button);
        commentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showComments();
            }
        });
    }

    private void showComments() {
        ProductComments productComments = new ProductComments();
        Bundle commentBundle = new Bundle();
        commentBundle.putSerializable("product", product);
        productComments.setArguments(commentBundle);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.fragmentsContainer, productComments).commit();
    }

    private void setIngredientButtonListener() {
        ingredientsButton = findViewById(R.id.ingredients_button);
        ingredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIngredients();
            }
        });
    }

    private void setAddCommentButtonListener() {
        addCommentButton = findViewById(R.id.add_comment_button);
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductInfo.this, AddComment.class);
                intent.putExtra("product", product);
                startActivity(intent);
            }
        });
    }

    private void setEditProductButtonListener() {
        editProductButton = findViewById(R.id.edit_product_button);
        editProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductInfo.this, EditProduct.class);
                intent.putExtra("product", product);
                startActivity(intent);
            }
        });
    }

    private void showIngredients() {
        ProductIngredients productIngredients = new ProductIngredients();
        Bundle commentBundle = new Bundle();
        commentBundle.putSerializable("product", product);
        productIngredients.setArguments(commentBundle);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.fragmentsContainer, productIngredients).commit();
    }

    private void setDefaultFragment() {
        Bundle commentBundle = new Bundle();
        commentBundle.putSerializable("product", product);
        ProductData productData = new ProductData();
        productData.setArguments(commentBundle);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.fragmentsContainer, productData).commit();
    }
}