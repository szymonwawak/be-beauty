package com.example.bebeauty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bebeauty.repository.ProductRepository;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button scanBarcode;
    Button findProducts;
    Button createIngredient;
    ProductRepository productRepository = new ProductRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scan_barcode: {
                startActivity(new Intent(MainActivity.this, BarcodeScanner.class));
                break;
            }
            case R.id.find_product: {
                startActivity(new Intent(MainActivity.this, FindProducts.class));
                break;
            }
            case R.id.create_ingredient: {
                startActivity(new Intent(MainActivity.this, CreateIngredient.class));
                break;
            }
        }
    }

    private void initView() {
        scanBarcode = findViewById(R.id.scan_barcode);
        scanBarcode.setOnClickListener(this);
        findProducts = findViewById(R.id.find_product);
        findProducts.setOnClickListener(this);
        createIngredient = findViewById(R.id.create_ingredient);
        createIngredient.setOnClickListener(this);
    }
}
