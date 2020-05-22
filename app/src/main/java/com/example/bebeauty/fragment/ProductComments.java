package com.example.bebeauty.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bebeauty.R;
import com.example.bebeauty.adapter.CommentAdapter;
import com.example.bebeauty.model.Product;

public class ProductComments extends Fragment {

    private Product product;
    private RecyclerView commentRecycler;
    private CommentAdapter commentAdapter;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_product_comments, container, false);
        initFragment();
        return view;
    }

    private void initFragment() {
        product = (Product) getArguments().get("product");
        commentRecycler = view.findViewById(R.id.commentRecycler);
        commentAdapter = new CommentAdapter();
        commentAdapter.setComments(product.getComments());
        initRecycler();
    }

    private void initRecycler() {
        commentRecycler.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        commentRecycler.setHasFixedSize(true);
        commentRecycler.setAdapter(commentAdapter);
    }
}
