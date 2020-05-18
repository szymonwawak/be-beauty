package com.example.bebeauty;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bebeauty.model.Product;

public class ProductComments extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_product_comments, container, false);
        Product product = (Product) getArguments().get("product");
        RecyclerView commentRecycler = view.findViewById(R.id.commentRecycler);
        CommentAdapter adapter = new CommentAdapter();
        adapter.setComments(product.getComments());
        initRecycler(commentRecycler, adapter);
        return view;
    }

    private void initRecycler(RecyclerView commentRecycler, CommentAdapter adapter) {
        commentRecycler.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        commentRecycler.setHasFixedSize(true);
        commentRecycler.setAdapter(adapter);
    }
}
