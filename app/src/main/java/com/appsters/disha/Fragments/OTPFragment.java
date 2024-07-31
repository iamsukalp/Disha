package com.appsters.disha.Fragments;



import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.appsters.disha.Activity.HomeActivity;
import com.appsters.disha.R;
import com.appsters.disha.databinding.FragmentOTPBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;


public class OTPFragment extends Fragment {

    private FragmentOTPBinding binding;
    private String phone,verificationId,code;
    private FirebaseAuth mAuth;
    private DatabaseReference aspirantRef;
    private String nmatId;
    private String name;


    public OTPFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentOTPBinding.inflate(inflater,container,false);
        init();
        return binding.getRoot();
    }

    private void init() {
        // getting data from safe arguments
        phone=OTPFragmentArgs.fromBundle(getArguments()).getPhone();
        verificationId=OTPFragmentArgs.fromBundle(getArguments()).getVerificationId();
        nmatId=OTPFragmentArgs.fromBundle(getArguments()).getNmatId();
        name=OTPFragmentArgs.fromBundle(getArguments()).getName();

        // firebase auth
        mAuth=FirebaseAuth.getInstance();
        aspirantRef= FirebaseDatabase.getInstance("https://disha-6af88-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Aspirants");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.otpLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    if (verificationId!=null){
                        PhoneAuthCredential phoneAuthCredential= PhoneAuthProvider.getCredential(
                                verificationId,
                                code
                        );
                        mAuth.getCurrentUser().linkWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    mAuth.getAccessToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<GetTokenResult> task) {
                                            if (task.isSuccessful()){
                                                Map<String, Object> map= new HashMap<>();
                                                map.put("phone",phone);
                                                map.put("name",name);
                                                aspirantRef.child(nmatId).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            Intent intent=new Intent(requireContext(), HomeActivity.class);
                                                            startActivity(intent);
                                                            requireActivity().finish();
                                                        }   else{
                                                            Toast.makeText(requireContext(),"Error! Please try again!",Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                            else {
                                                Log.d("Login",task.getException().getMessage());
                                                Toast.makeText(requireActivity(), "Login Failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                                else{
                                    Log.d("Login",task.getException().getMessage());
                                    Toast.makeText(requireActivity(), "Login Failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                }

            }
        });
    }

    private boolean validate() {
        code=binding.otpPin.getText().toString();
        if (code.length()<6){
            Toast.makeText(requireActivity(), "Wrong OTP", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}