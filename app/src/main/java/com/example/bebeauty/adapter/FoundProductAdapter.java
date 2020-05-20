package com.example.bebeauty.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bebeauty.FindProducts;
import com.example.bebeauty.ProductInfo;
import com.example.bebeauty.R;
import com.example.bebeauty.model.Product;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class FoundProductAdapter extends RecyclerView.Adapter<FoundProductAdapter.ProductHolder> {

    private List<Product> products = new ArrayList<>();
    private FindProducts parentContext;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public FoundProductAdapter(FindProducts parentContext) {
        this.parentContext = parentContext;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product, parent, false);
        return new FoundProductAdapter.ProductHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        holder.name.setText(products.get(position).getName());
        holder.manufacturer.setText(products.get(position).getManufacturer());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MaterialTextView name, manufacturer;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.product_name);
            manufacturer = itemView.findViewById(R.id.product_manufacturer_value);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int productIndex = getAdapterPosition();
            Product product = products.get(productIndex);
            Intent intent = new Intent(parentContext, ProductInfo.class);
            intent.putExtra("product", product);
            parentContext.startActivity(intent);
        }
    }
}
