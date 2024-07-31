package com.appsters.disha.Fragments;



import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.appsters.disha.Activity.HomeActivity;
import com.appsters.disha.R;
import com.appsters.disha.databinding.FragmentNMATBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class NMATFragment extends Fragment {

    private FragmentNMATBinding binding;
    private String nmatId;
    private FirebaseAuth mAuth;
    private String email,token,imageUrl;
    private DatabaseReference userRef,aspirantRef;

    public NMATFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentNMATBinding.inflate(inflater,container,false);
        init();
        return binding.getRoot();
    }

    private void init() {

        email=NMATFragmentArgs.fromBundle(getArguments()).getEmail();
        token=NMATFragmentArgs.fromBundle(getArguments()).getToken();
        imageUrl=NMATFragmentArgs.fromBundle(getArguments()).getImageUrl();

        // firebase component initialize
        mAuth=FirebaseAuth.getInstance();

        userRef= FirebaseDatabase.getInstance("https://disha-6af88-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Users");
        aspirantRef=FirebaseDatabase.getInstance("https://disha-6af88-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Aspirants");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.nmatContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate()){
                    HashMap<String,Object> map=new HashMap<>();
                    map.put("nmatId",nmatId);
                    map.put("userId",mAuth.getCurrentUser().getUid());
                    map.put("token",token);
                    map.put("email",email);
                    map.put("imageUrl",imageUrl);
                    aspirantRef.child(nmatId).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                userRef.child(mAuth.getCurrentUser().getUid()).child("nmatId").setValue(nmatId).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        NMATFragmentDirections.ActionNMATFragmentToPhoneFragment action=NMATFragmentDirections.actionNMATFragmentToPhoneFragment();
                                        action.setNmat(nmatId);
                                        Navigation.findNavController(binding.getRoot()).navigate(action);
                                    }
                                });

                            }   else{
                                Toast.makeText(requireContext(),"Error! Please try again!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }

    private boolean validate() {
        nmatId=binding.nmatId.getText().toString().trim();
        if(nmatId.length()<5){
            Toast.makeText(requireContext(), "Enter valid NMAT ID", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}