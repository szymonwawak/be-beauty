package com.example.bebeauty.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bebeauty.R;
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
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        Utils.setHidingMenuOnClick(view, AddComment.this);
    }

    private void initLayout() {
        setContentView(R.layout.activity_add_comment);
        view = findViewById(R.id.activity_add_comment);
        Intent intent = getIntent();
        product = (Product) intent.getSerializableExtra("product");
        score = findViewById(R.id.score_value);
        nickname = findViewById(R.id.nickname);
        opinion = findViewById(R.id.comment);
        button = findViewById(R.id.send);
        button.setOnClickListener(v -> sendCommentIfValid());
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
        commentRepository.saveComment(comment).observe(this, success -> {
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
        });
    }
}