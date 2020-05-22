package com.example.bebeauty.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bebeauty.R;
import com.example.bebeauty.model.Ingredient;
import com.example.bebeauty.repository.IngredientRepository;
import com.example.bebeauty.utils.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class CreateIngredient extends AppCompatActivity {

    TextInputEditText name;
    TextInputEditText description;
    Spinner effect;
    MaterialButton button;
    Toast toast;
    View view;
    IngredientRepository ingredientRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        Utils.setHidingMenuOnClick(view, CreateIngredient.this);
    }

    private void initLayout() {
        setContentView(R.layout.activity_create_ingredient);
        view = findViewById(R.id.activity_create_ingredient);
        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        effect = findViewById(R.id.effect);
        button = findViewById(R.id.create);
        button.setOnClickListener(v -> saveIngredientIfValid());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.effects, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        effect.setAdapter(adapter);
    }

    private void saveIngredientIfValid() {
        if (validateIngredient())
            sendIngredient();
    }

    private boolean validateIngredient() {
        String nameText = name.getText().toString();
        String descriptionText = description.getText().toString();
        if (nameText.isEmpty()) {
            name.setError("Nick nie może być pusty!");
            return false;
        }
        if (descriptionText.isEmpty()) {
            description.setError("Treść nie może być pusta!");
            return false;
        }
        return true;
    }

    private void sendIngredient() {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(name.getText().toString());
        ingredient.setDescription(description.getText().toString());
        ingredient.setEffect(prepareEffect());
        ingredientRepository = new IngredientRepository();
        ingredientRepository.saveIngredient(ingredient).observe(this, success -> {
            if (success) {
                Intent intent = new Intent(CreateIngredient.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                toast = Toast.makeText(getApplicationContext(), "Składnik został dodany!", Toast.LENGTH_LONG);
                toast.show();
            } else {
                toast = Toast.makeText(getApplicationContext(), "Nie udało się dodać składnika. Spróbuj ponownie", Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    //TODO Do something with it
    private String prepareEffect() {
        String effectName = (String) effect.getSelectedItem();
        switch (effectName) {
            case "Neutralny": {
                return "neutral";
            }
            case "Pozytywny": {
                return "positive";
            }
            case "Negatywny": {
                return "negative";
            }
        }
        return "neutral";
    }
}