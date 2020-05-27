package com.example.bebeauty.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bebeauty.R;
import com.example.bebeauty.activity.ProductOperations;
import com.example.bebeauty.adapter.AddedIngredientsAdapter;
import com.example.bebeauty.adapter.IngredientsToAddAdapter;
import com.example.bebeauty.model.Ingredient;
import com.example.bebeauty.model.Product;
import com.example.bebeauty.repository.IngredientRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class AddIngredients extends Fragment {

    private View view;
    private Product product;
    private List<Ingredient> ingredientList;
    private List<Ingredient> storedIngredients;
    private IngredientsToAddAdapter ingredientsToAddAdapter;
    private AddedIngredientsAdapter addedIngredientsAdapter;
    private RecyclerView addedIngredientRecycler;
    private RecyclerView ingredientRecycler;
    private SearchView searchView;
    private IngredientRepository ingredientRepository;
    private final int PAGE_SIZE = 6;
    private int visibleItemsCount;
    private int totalItemsCount;
    private boolean isLoading;
    private boolean isLastPage;
    private int firstVisibleItemPosition;
    private int currentPageNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_ingredients, container, false);
        product = (Product) getArguments().getSerializable("product");
        initFragment();
        initAdapters();
        fetchIngredients("", true);
        initRecyclers();
        initSearchView();
        return view;
    }

    private void initFragment() {
        ingredientRepository = new IngredientRepository();
        ingredientList = new ArrayList<>();
        storedIngredients = product.getIngredients();
        searchView = view.findViewById(R.id.searchView);
        addedIngredientRecycler = view.findViewById(R.id.added_ingredients_recycler);
        ingredientRecycler = view.findViewById(R.id.add_ingredients_recycler);
        FloatingActionButton confirm = view.findViewById(R.id.confirm_adding);
        confirm.setOnClickListener(v -> addChosenIngredients());
        currentPageNumber = 0;
    }

    private void addChosenIngredients() {
        ProductOperations activity = ((ProductOperations) getActivity());
        activity.updateIngredientsList();
        getParentFragmentManager().popBackStack();
    }

    private void initAdapters() {
        ingredientsToAddAdapter = new IngredientsToAddAdapter(this);
        addedIngredientsAdapter = new AddedIngredientsAdapter(this);
    }

    private void fetchIngredients(String query, boolean clear) {
        isLoading = true;
        ingredientRepository.findIngredients(query, currentPageNumber, PAGE_SIZE).observe(getViewLifecycleOwner(), ingredients -> {
            isLastPage = ingredients.size() < PAGE_SIZE;
            addLoadedIngredients(ingredients, clear);
        });
    }

    private void addLoadedIngredients(List<Ingredient> ingredients, boolean clear) {
        if (clear)
            ingredientList.clear();
        currentPageNumber++;
        ingredientList.addAll(ingredients);
        removeAlreadyContainedRecords(ingredientList, storedIngredients);
        ingredientsToAddAdapter.notifyDataSetChanged();
        isLoading = false;
    }

    private void removeAlreadyContainedRecords(List<Ingredient> itemsToChoose, List<Ingredient> alreadyChosenItems) {
        Iterator<Ingredient> iterator = itemsToChoose.iterator();
        while (iterator.hasNext()) {
            final Ingredient ingredient = iterator.next();
            if (alreadyChosenItems.stream().anyMatch(it -> it.getId() == ingredient.getId())) {
                iterator.remove();
            }
        }
    }

    private void initRecyclers() {
        ingredientsToAddAdapter.setIngredients(ingredientList);
        addedIngredientsAdapter.setIngredients(storedIngredients);
        initIngredientRecycler();
        initStoreIngredientsRecycler();
    }

    private void initIngredientRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        ingredientRecycler.setLayoutManager(layoutManager);
        ingredientRecycler.setHasFixedSize(true);
        ingredientRecycler.setAdapter(ingredientsToAddAdapter);
        ingredientRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                AddIngredients.this.loadIfNeededAndPossible(layoutManager);
            }
        });
    }

    private void loadIfNeededAndPossible(LinearLayoutManager layoutManager) {
        visibleItemsCount = layoutManager.getChildCount();
        totalItemsCount = layoutManager.getItemCount();
        firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        if (!isLoading && !isLastPage && needsLoading()) {
            String query = searchView.getQuery().toString();
            fetchIngredients(query, false);
        }
    }

    private boolean needsLoading() {
        return (visibleItemsCount + firstVisibleItemPosition) >= totalItemsCount && firstVisibleItemPosition >= 0 && totalItemsCount >= PAGE_SIZE;
    }

    private void initStoreIngredientsRecycler() {
        addedIngredientRecycler.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        addedIngredientRecycler.setHasFixedSize(true);
        addedIngredientRecycler.setAdapter(addedIngredientsAdapter);
    }

    private void initSearchView() {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                currentPageNumber = 0;
                fetchIngredients(query, true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentPageNumber = 0;
                fetchIngredients(newText, true);
                return false;
            }
        });
    }

    public void addIngredientToList(Ingredient ingredient) {
        storedIngredients.add(ingredient);
        addedIngredientsAdapter.notifyDataSetChanged();
    }

    public void removeIngredientFromList(Ingredient ingredient) {
        ingredientList.add(ingredient);
        ingredientsToAddAdapter.notifyDataSetChanged();
    }
}