package com.example.bebeauty.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bebeauty.R;
import com.example.bebeauty.fragment.AddIngredients;
import com.example.bebeauty.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class AddedIngredientsAdapter extends RecyclerView.Adapter<AddedIngredientsAdapter.IngredientHolder> {

    private List<Ingredient> ingredients = new ArrayList<>();
    private AddIngredients parentContext;

    public AddedIngredientsAdapter(AddIngredients parentContext) {
        this.parentContext = parentContext;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient, parent, false);
        return new AddedIngredientsAdapter.IngredientHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddedIngredientsAdapter.IngredientHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.name.setText(ingredient.getName());
        holder.itemView.setBackgroundColor(0xdddddd);
        holder.description.setText(ingredient.getDescription());
        setEffect(holder, ingredient.getEffect());
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    private void setEffect(@NonNull AddedIngredientsAdapter.IngredientHolder holder, String effect) {
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

    class IngredientHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name;
        private ImageView effect;
        private TextView description;

        public IngredientHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            effect = itemView.findViewById(R.id.effect);
            description = itemView.findViewById(R.id.description_value);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int ingredientNumber = getAdapterPosition();
            Ingredient ingredient = ingredients.get(ingredientNumber);
            ingredients.remove(ingredientNumber);
            parentContext.removeIngredientFromList(ingredient);
            notifyDataSetChanged();
        }
    }
}
