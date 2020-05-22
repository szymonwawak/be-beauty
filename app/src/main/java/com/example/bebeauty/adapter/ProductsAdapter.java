package com.example.bebeauty.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bebeauty.R;
import com.example.bebeauty.activity.FindProducts;
import com.example.bebeauty.activity.ProductInfo;
import com.example.bebeauty.model.Product;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductHolder> {

    private List<Product> products = new ArrayList<>();
    private FindProducts parentContext;

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public ProductsAdapter(FindProducts parentContext) {
        this.parentContext = parentContext;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product, parent, false);
        return new ProductsAdapter.ProductHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        Product product = products.get(position);
        holder.name.setText(product.getName());
        holder.manufacturer.setText(product.getManufacturer());
    }

    class ProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MaterialTextView name, manufacturer;

        ProductHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.product_name);
            manufacturer = itemView.findViewById(R.id.product_manufacturer_value);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            openProductInfo();
        }

        private void openProductInfo() {
            int productIndex = getAdapterPosition();
            Product product = products.get(productIndex);
            Intent intent = new Intent(parentContext, ProductInfo.class);
            intent.putExtra("product", product);
            parentContext.startActivity(intent);
        }
    }
}
