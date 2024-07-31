package com.appsters.disha.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appsters.disha.Activity.ChatWindowActivity;
import com.appsters.disha.Models.ChatModal;
import com.appsters.disha.Models.FeedsModal;
import com.appsters.disha.R;
import com.appsters.disha.databinding.ChatListItemBinding;
import com.appsters.disha.databinding.FragmentChatsBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


public class ChatsFragment extends Fragment {

    private FragmentChatsBinding binding;
    private FirebaseRecyclerAdapter<ChatModal, ChatsViewHolder> adapter;
    private FirebaseRecyclerOptions<ChatModal> option;
    private Query query;
    private LinearLayoutManager layoutManager;
    private DatabaseReference aspirantRef;
    private FirebaseAuth mAuth;
    private String current_id;

    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentChatsBinding.inflate(inflater,container,false);
        init();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        getStudentList();
    }


    private void getStudentList() {
        option=new FirebaseRecyclerOptions.Builder<ChatModal>().setQuery(query,ChatModal.class).build();
        adapter=new FirebaseRecyclerAdapter<ChatModal, ChatsViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull ChatsViewHolder holder, int position, @NonNull ChatModal model) {
                /*if (!TextUtils.equals(model.getUserId(),current_id)){

                }*/
                holder.b.chatListName.setText(model.getName());
                Picasso.get().load(model.getImageUrl()).placeholder(R.drawable.ic_profile_placeholder).networkPolicy(NetworkPolicy.OFFLINE).into(holder.b.chatListImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(model.getImageUrl()).placeholder(R.drawable.ic_profile_placeholder).into(holder.b.chatListImage);
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(requireActivity(), ChatWindowActivity.class);
                        intent.putExtra("userId",model.getUserId());
                        intent.putExtra("name",model.getName());
                        intent.putExtra("imageUrl",model.getImageUrl());
                        startActivity(intent);
                    }
                });


            }

            @NonNull
            @Override
            public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ChatsViewHolder(ChatListItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
            }
        };
        adapter.startListening();
        binding.chatList.setAdapter(adapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        layoutManager=new LinearLayoutManager(requireActivity());
    }

    private void init() {
        aspirantRef= FirebaseDatabase.getInstance("https://disha-6af88-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Aspirants");
        mAuth=FirebaseAuth.getInstance();
        current_id=mAuth.getCurrentUser().getUid();

        // change query for student status
        query=aspirantRef.orderByChild("student").equalTo(true);

        // initializing layout manager
        layoutManager.isAutoMeasureEnabled();
        binding.chatList.setHasFixedSize(true);
        binding.chatList.setLayoutManager(layoutManager);
    }

    private class ChatsViewHolder extends RecyclerView.ViewHolder {
        ChatListItemBinding b;
        public ChatsViewHolder(ChatListItemBinding binding) {
            super(binding.getRoot());
            b=binding;
        }
    }
}