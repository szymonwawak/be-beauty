package com.example.bebeauty.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.bebeauty.model.Category;
import com.example.bebeauty.repository.CategoryRepository;

import java.util.List;

public class CreateProductModel extends AndroidViewModel {

    private CategoryRepository categoryRepository;

    public CreateProductModel(@NonNull Application application) {
        super(application);
        categoryRepository = new CategoryRepository();
    }

    public MutableLiveData<List<Category>> getCategoryLiveData() {
        return categoryRepository.getAllCategories();
    }
}
