package com.example.bebeauty.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.bebeauty.model.Comment;
import com.example.bebeauty.service.ApiService;
import com.example.bebeauty.utils.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CommentRepository {

    private Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
    private ApiService apiService = retrofit.create(ApiService.class);
    MutableLiveData<Boolean> requestSuccess = new MutableLiveData<>();

    public MutableLiveData<Boolean> saveComment(Comment comment) {
        Call<Comment> call = apiService.saveComment(comment);
        call.enqueue(new Callback<Comment>() {
                         @Override
                         public void onResponse(Call<Comment> call, Response<Comment> response) {
                             requestSuccess.setValue(response.isSuccessful());
                         }

                         @Override
                         public void onFailure(Call<Comment> call, Throwable t) {
                             requestSuccess.setValue(true);
                         }
                     }
        );
        return requestSuccess;
    }
}
