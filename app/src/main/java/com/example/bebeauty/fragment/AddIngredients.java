package com.example.bebeauty.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bebeauty.CreateProduct;
import com.example.bebeauty.R;
import com.example.bebeauty.adapter.AddedIngredientsAdapter;
import com.example.bebeauty.adapter.IngredientsToAddAdapter;
import com.example.bebeauty.model.Ingredient;
import com.example.bebeauty.viewmodel.IngredientsToAdd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class AddIngredients extends Fragment {

    private List<Ingredient> ingredientList = new ArrayList<>();
    private List<Ingredient> addedIngredientList = new ArrayList<>();
    private IngredientsToAdd ingredientsToAdd;
    private IngredientsToAddAdapter ingredientsToAddAdapter;
    private AddedIngredientsAdapter addedIngredientsAdapter;
    RecyclerView addedIngredientRecycler;
    RecyclerView ingredientRecycler;
    private FloatingActionButton confirm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_ingredients, container, false);
        initFragment(view);
        loadIngredients();
        initRecyclers();
        return view;
    }

    private void initFragment(View view) {
        confirm = view.findViewById(R.id.confirm_adding);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChosenIngredients();
            }
        });
        addedIngredientRecycler = view.findViewById(R.id.added_ingredients_recycler);
        ingredientRecycler = view.findViewById(R.id.add_ingredients_recycler);
        ingredientsToAddAdapter = new IngredientsToAddAdapter(this);
        addedIngredientsAdapter = new AddedIngredientsAdapter(this);
        ingredientsToAdd = new ViewModelProvider(this).get(IngredientsToAdd.class);
    }

    private void addChosenIngredients() {
        CreateProduct activity = ((CreateProduct) getActivity());
        activity.setIngredients(addedIngredientList);
        getParentFragmentManager().popBackStack();
    }

    private void loadIngredients() {
        ingredientsToAdd.getIngredientsLiveData().observe(getViewLifecycleOwner(), new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(List<Ingredient> ingredients) {
                addLoadedIngredients(ingredients);
            }
        });
    }

    private void initRecyclers() {
        ingredientsToAddAdapter.setIngredients(ingredientList);
        addedIngredientsAdapter.setIngredients(addedIngredientList);
        initIngredientRecycler(ingredientRecycler, ingredientsToAddAdapter);
        initAddedIngredientsRecycler(addedIngredientRecycler, addedIngredientsAdapter);
    }

    private void initIngredientRecycler(RecyclerView commentRecycler, IngredientsToAddAdapter adapter) {
        commentRecycler.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        commentRecycler.setHasFixedSize(true);
        commentRecycler.setAdapter(adapter);
    }

    private void initAddedIngredientsRecycler(RecyclerView addedIngredientRecycler, AddedIngredientsAdapter adapter) {
        addedIngredientRecycler.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        addedIngredientRecycler.setHasFixedSize(true);
        addedIngredientRecycler.setAdapter(adapter);
    }


    public void addIngredientToList(Ingredient ingredient) {
        addedIngredientList.add(ingredient);
        addedIngredientsAdapter.notifyDataSetChanged();
    }

    public void removeIngredientFromList(Ingredient ingredient) {
        ingredientList.add(ingredient);
        ingredientsToAddAdapter.notifyDataSetChanged();
    }

    private void addLoadedIngredients(List<Ingredient> ingredients) {
        ingredientList.addAll(ingredients);
        ingredientsToAddAdapter.notifyDataSetChanged();
    }
}