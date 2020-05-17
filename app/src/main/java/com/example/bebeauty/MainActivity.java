package com.example.bebeauty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button scanBarcode;
    Button findProducts;

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
        }
    }

    private void initView() {
        scanBarcode = findViewById(R.id.scan_barcode);
        scanBarcode.setOnClickListener(this);
        findProducts = findViewById(R.id.find_product);
        findProducts.setOnClickListener(this);
    }
}
