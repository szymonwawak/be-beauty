package com.example.bebeauty.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.bebeauty.service.ApiService;
import com.example.bebeauty.utils.RetrofitBuilder;

import retrofit2.Retrofit;

class Repository {
    private Retrofit retrofit = RetrofitBuilder.getRetrofitInstance();
    ApiService apiService = retrofit.create(ApiService.class);
    MutableLiveData<Boolean> requestStatus = new MutableLiveData<>();
}
