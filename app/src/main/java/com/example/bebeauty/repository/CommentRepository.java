package com.example.bebeauty.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.bebeauty.model.Comment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentRepository extends Repository {

    public MutableLiveData<Boolean> saveComment(Comment comment) {
        Call<Comment> call = apiService.saveComment(comment);
        call.enqueue(new Callback<Comment>() {
                         @Override
                         public void onResponse(Call<Comment> call, Response<Comment> response) {
                             requestStatus.setValue(response.isSuccessful());
                         }

                         @Override
                         public void onFailure(Call<Comment> call, Throwable t) {
                             requestStatus.setValue(false);
                         }
                     }
        );
        return requestStatus;
    }
}
