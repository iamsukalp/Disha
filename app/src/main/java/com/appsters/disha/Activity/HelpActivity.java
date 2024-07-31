package com.appsters.disha.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appsters.disha.Models.FAQModal;
import com.appsters.disha.databinding.ActivityHelpBinding;
import com.appsters.disha.databinding.FaqListItemBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class HelpActivity extends AppCompatActivity {
    private ActivityHelpBinding binding;
    private FirebaseRecyclerAdapter<FAQModal,FAQViewHolder> adapter;
    private FirebaseRecyclerOptions<FAQModal> options;
    private DatabaseReference faqRef;
    private Boolean isExpanded=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHelpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();

        binding.HelpChatBotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(HelpActivity.this,ChatBotActivity.class);
//                startActivity(intent);
                Intent intent=new Intent(HelpActivity.this,YellowChatBotActivity.class);
                startActivity(intent);

            }
        });

        getFaq();

    }

    private void getFaq() {
        options=new FirebaseRecyclerOptions.Builder<FAQModal>().setQuery(faqRef,FAQModal.class).build();
        adapter=new FirebaseRecyclerAdapter<FAQModal, FAQViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FAQViewHolder holder, int position, @NonNull FAQModal model) {
                holder.binding.subItem.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                holder.binding.faqTitle.setText(model.getTitle());
                holder.binding.question.setText("Q: " + model.getQuestion());
                holder.binding.answer.setText("A: " + model.getAnswers());
                holder.itemView.setOnClickListener(v -> {
                    // Get the current state of the item
                   isExpanded= !isExpanded;
                    // Change the state

                    holder.binding.subItem.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

                });



            }

            @NonNull
            @Override
            public FAQViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new FAQViewHolder(FaqListItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
            }
        };
        adapter.startListening();
        binding.faqRecycler.setAdapter(adapter);
    }

    private void initialize() {
        faqRef= FirebaseDatabase.getInstance("https://disha-6af88-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("FAQ");

        ((SimpleItemAnimator) binding.faqRecycler.getItemAnimator()).setSupportsChangeAnimations(false);

        // Standard setup
        binding.faqRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.faqRecycler.setAdapter(adapter);
        binding.faqRecycler.setHasFixedSize(true);
    }

    private class FAQViewHolder extends RecyclerView.ViewHolder {
        FaqListItemBinding binding;
        public FAQViewHolder(FaqListItemBinding b) {
            super(b.getRoot());
            binding=b;
        }


    }
}