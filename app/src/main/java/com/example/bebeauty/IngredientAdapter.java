package com.example.bebeauty;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bebeauty.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientHolder> {

    private List<Ingredient> ingredients = new ArrayList();

    @NonNull
    @Override
    public IngredientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient, parent, false);
        return new IngredientHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.name.setText(ingredient.getName());
        holder.description.setText(ingredient.getDescription());
        setEffect(holder, ingredient.getEffect());
    }

    private void setEffect(@NonNull IngredientHolder holder, String effect) {
        switch (effect) {
            case "positive": {
                holder.effect.setImageResource(R.drawable.positive);
                break;
            }
            case "neutral": {
                holder.effect.setImageResource(R.drawable.neutral);
                break;
            }
            case "negative": {
                holder.effect.setImageResource(R.drawable.negative);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    class IngredientHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView effect;
        private TextView description;

        public IngredientHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            effect = itemView.findViewById(R.id.effect);
            description = itemView.findViewById(R.id.description_value);
        }
    }
}
