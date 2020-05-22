package com.example.bebeauty.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.bebeauty.R;
import com.example.bebeauty.model.Product;

public class ProductData extends Fragment {

    private TextView manufacturer;
    private RatingBar score;
    private TextView description;
    private View view;
    private Product product;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_product_data, container, false);
        product = (Product) getArguments().get("product");
        initFragment();
        setFieldsValue(product);
        return view;
    }

    private void initFragment() {
        manufacturer = view.findViewById(R.id.manufacturer_value);
        score = view.findViewById(R.id.score_value);
        description = view.findViewById(R.id.description_value);
    }

    private void setFieldsValue(Product product) {
        manufacturer.setText(product.getManufacturer());
        score.setRating(product.getAverageScore());
        description.setText(product.getDescription());
    }
}
