package com.appsters.disha.Fragments;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appsters.disha.Activity.FullscreenFeedActivity;
import com.appsters.disha.Models.FeedsModal;
import com.appsters.disha.databinding.FeedListItemBinding;
import com.appsters.disha.databinding.FragmentFeedsBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;


public class FeedsFragment extends Fragment {

    private FragmentFeedsBinding binding;
    private FirebaseRecyclerAdapter<FeedsModal,FeedsViewHolder> adapter;
    private FirebaseRecyclerOptions<FeedsModal> option;
    private Query query;
    private LinearLayoutManager layoutManager;
    private DatabaseReference feedsRef;

    public FeedsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentFeedsBinding.inflate(inflater,container,false);
        init();
        return binding.getRoot();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData();
    }

    private void getData() {
        option= new FirebaseRecyclerOptions.Builder<FeedsModal>().setQuery(query,FeedsModal.class).build();
        adapter=new FirebaseRecyclerAdapter<FeedsModal, FeedsViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull FeedsViewHolder holder, int position, @NonNull FeedsModal model) {
                if(!model.getImageUrl().equals("null")){
                    Picasso.get().load(model.getImageUrl()).fit().into(holder.binding.feedImage);
                }
                holder.binding.feedDate.setText(getDate(model.getCreated_at()));
                holder.binding.feedHeader.setText(model.getHeader());

                holder.binding.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent =new Intent(requireActivity(), FullscreenFeedActivity.class);
                        intent.putExtra("imageUrl",model.getImageUrl());
                        intent.putExtra("header",model.getHeader());
                        intent.putExtra("details",model.getDetails());
                        intent.putExtra("date",getDate(model.getCreated_at()));

                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public FeedsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new FeedsViewHolder(FeedListItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
            }
        };
        adapter.startListening();
        binding.feedsFragmentList.setAdapter(adapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        layoutManager=new LinearLayoutManager(requireActivity());
    }


    private String getDate(long created_at) {
        SimpleDateFormat sfd = new SimpleDateFormat("dd MMM yyyy");
        return sfd.format(new Date(created_at));
    }

    private void init() {
        feedsRef= FirebaseDatabase.getInstance("https://disha-6af88-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Feeds");

        query=feedsRef.orderByChild("created_at");

        // initializing layout manager
        layoutManager.isAutoMeasureEnabled();
        binding.feedsFragmentList.setHasFixedSize(true);
        binding.feedsFragmentList.setLayoutManager(layoutManager);

    }

    private class FeedsViewHolder extends RecyclerView.ViewHolder {
        private FeedListItemBinding binding;
        public FeedsViewHolder(FeedListItemBinding b) {
            super(b.getRoot());
            binding=b;
        }
    }
}