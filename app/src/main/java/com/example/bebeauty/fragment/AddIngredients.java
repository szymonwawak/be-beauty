package com.example.bebeauty.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bebeauty.ProductOperations;
import com.example.bebeauty.R;
import com.example.bebeauty.adapter.AddedIngredientsAdapter;
import com.example.bebeauty.adapter.IngredientsToAddAdapter;
import com.example.bebeauty.model.Ingredient;
import com.example.bebeauty.model.Product;
import com.example.bebeauty.viewmodel.IngredientsToAdd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class AddIngredients extends Fragment {

    private List<Ingredient> ingredientList = new ArrayList<>();
    private List<Ingredient> addedIngredientList;
    private IngredientsToAdd ingredientsToAdd;
    private IngredientsToAddAdapter ingredientsToAddAdapter;
    private AddedIngredientsAdapter addedIngredientsAdapter;
    private SearchManager searchManager;
    RecyclerView addedIngredientRecycler;
    RecyclerView ingredientRecycler;
    private FloatingActionButton confirm;
    SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_ingredients, container, false);
        Product product = (Product) getArguments().getSerializable("product");
        addedIngredientList = product.getIngredients();
        initFragment(view);
        loadIngredients();
        initRecyclers();
        initSearchView();
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
        searchView = view.findViewById(R.id.searchView);
        addedIngredientRecycler = view.findViewById(R.id.added_ingredients_recycler);
        ingredientRecycler = view.findViewById(R.id.add_ingredients_recycler);
        ingredientsToAddAdapter = new IngredientsToAddAdapter(this);
        addedIngredientsAdapter = new AddedIngredientsAdapter(this);
        ingredientsToAdd = new ViewModelProvider(this).get(IngredientsToAdd.class);
    }

    private void addChosenIngredients() {
        ProductOperations activity = ((ProductOperations) getActivity());
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

    private void fetchIngredients(String query) {
        ingredientsToAdd.filterIngredients(query).observe(getViewLifecycleOwner(), new Observer<List<Ingredient>>() {
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
        ingredientList.clear();
        ingredientList.addAll(ingredients);
        removeAlreadyContainedRecords(ingredientList, addedIngredientList);
        ingredientsToAddAdapter.notifyDataSetChanged();
    }

    private void removeAlreadyContainedRecords(List<Ingredient> itemsToChoose, List<Ingredient> alreadyChosenItems) {
        Iterator<Ingredient> iterator = itemsToChoose.iterator();
        while (iterator.hasNext()) {
            final Ingredient ingredient = iterator.next();
            if (alreadyChosenItems.stream().filter(it -> it.getId() == ingredient.getId()).findFirst().isPresent()) {
                iterator.remove();
            }
        }
    }

    private void initSearchView() {
        searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName())
        );
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchIngredients(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fetchIngredients(newText);
                return false;
            }
        });
    }
}