package com.appsters.disha.Fragments;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appsters.disha.Activity.HomeActivity;
import com.appsters.disha.R;
import com.appsters.disha.databinding.FragmentFlashBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;


public class FlashFragment extends Fragment {

    private FragmentFlashBinding binding;
    private FirebaseAuth mAuth;
    private SpannableString flashText;

    public FlashFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentFlashBinding.inflate(inflater,container,false);
        init();
        return binding.getRoot();

    }

    private void init() {

        flashText= SpannableString.valueOf(requireActivity().getResources().getString(R.string.heart));
        flashText.setSpan(new ForegroundColorSpan(Color.RED),1,flashText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.flashText.setText("Made with "+flashText+" at NMIMS");


        //initializing firebase components
            mAuth= FirebaseAuth.getInstance();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.flashAnim.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(mAuth.getCurrentUser()!=null){
                    Intent intent=new Intent(requireActivity(), HomeActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                }
                else{
                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_flashFragment_to_loginFragment);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }
}