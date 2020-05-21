package com.example.bebeauty;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ingredient);
        View view = findViewById(R.id.activity_create_ingredient);
        initLayout();
        setHidingMenuOnClick(view);
    }

    private void initLayout() {
        Intent intent = getIntent();
        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        effect = findViewById(R.id.effect);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.effects, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        effect.setAdapter(adapter);
        button = findViewById(R.id.create);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveIngredientIfValid();
            }
        });
    }


    private void setHidingMenuOnClick(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utils.hideKeyboard(CreateIngredient.this);
                return false;
            }
        });
    }

    private void saveIngredientIfValid() {
        if (validateIngredient())
            sendIngredient();
    }

    private boolean validateIngredient() {
        String name = this.name.getText().toString();
        String description = this.description.getText().toString();
        if (name.isEmpty()) {
            this.name.setError("Nick nie może być pusty!");
            return false;
        }
        if (description.isEmpty()) {
            this.description.setError("Treść nie może być pusta!");
            return false;
        }
        return true;
    }

    private void sendIngredient() {
        final Ingredient ingredient = new Ingredient();
        ingredient.setName(name.getText().toString());
        ingredient.setDescription(description.getText().toString());
        ingredient.setEffect(prepareEffect());
        IngredientRepository ingredientRepository = new IngredientRepository();
        ingredientRepository.saveIngredient(ingredient).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
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
            }
        });
    }

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