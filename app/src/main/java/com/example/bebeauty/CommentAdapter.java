package com.example.bebeauty;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bebeauty.model.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

    private List<Comment> comments = new ArrayList();

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment, parent, false);
        return new CommentHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.score.setRating(comment.getScore());
        holder.comment.setText(comment.getOpinion());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    class CommentHolder extends RecyclerView.ViewHolder {
        private RatingBar score;
        private TextView comment;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            score = itemView.findViewById(R.id.score_value);
            comment = itemView.findViewById(R.id.comment_value);
        }
    }
}
