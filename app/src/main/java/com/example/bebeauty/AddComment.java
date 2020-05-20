package com.example.bebeauty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.bebeauty.model.Comment;
import com.example.bebeauty.model.Product;
import com.example.bebeauty.repository.CommentRepository;
import com.example.bebeauty.utils.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AddComment extends AppCompatActivity {

    RatingBar score;
    TextInputEditText nickname;
    TextInputEditText opinion;
    MaterialButton button;
    Toast toast;
    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);
        View view = findViewById(R.id.activity_add_comment);
        initLayout();
        setHidingMenuOnClick(view);
    }

    private void setHidingMenuOnClick(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utils.hideKeyboard(AddComment.this);
                return false;
            }
        });
    }

    private void initLayout() {
        Intent intent = getIntent();
        product = (Product) intent.getSerializableExtra("product");
        score = findViewById(R.id.score_value);
        nickname = findViewById(R.id.nickname);
        opinion = findViewById(R.id.comment);
        button = findViewById(R.id.send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommentIfValid();
            }
        });
    }

    private void sendCommentIfValid() {
        if (validateComment())
            sendComment();
    }

    private boolean validateComment() {
        String name = nickname.getText().toString();
        String message = opinion.getText().toString();
        if (name.isEmpty()) {
            nickname.setError("Nick nie może być pusty!");
            return false;
        }
        if (message.isEmpty()) {
            opinion.setError("Treść nie może być pusta!");
            return false;
        }
        return true;
    }

    private void sendComment() {
        final Comment comment = new Comment();
        comment.setScore(score.getRating());
        comment.setOpinion(opinion.getText().toString());
        comment.setNickname(nickname.getText().toString());
        comment.setProduct(product);
        CommentRepository commentRepository = new CommentRepository();
        commentRepository.saveComment(comment).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                if (success) {
                    Intent intent = new Intent(AddComment.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    toast = Toast.makeText(getApplicationContext(), "Komentarz został wysłany!", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    toast = Toast.makeText(getApplicationContext(), "Nie udało się wysłać komentarza. Spróbuj ponownie", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }
}