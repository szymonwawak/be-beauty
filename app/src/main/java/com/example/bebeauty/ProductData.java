package com.example.bebeauty;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.bebeauty.model.Product;

public class ProductData extends Fragment {

    TextView manufacturer;
    RatingBar score;
    TextView description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_product_data, container, false);
        Product product = (Product) getArguments().get("product");
        initFields(view);
        setFieldsValue(product);
        return view;
    }

    private void setFieldsValue(Product product) {
        score.setRating(product.getAverageScore());
        manufacturer.setText(product.getManufacturer());
        description.setText(product.getDescription());
    }

    private void initFields(View view) {
        manufacturer = view.findViewById(R.id.manufacturer_value);
        score = view.findViewById(R.id.score_value);
        description = view.findViewById(R.id.description_value);
    }
}
