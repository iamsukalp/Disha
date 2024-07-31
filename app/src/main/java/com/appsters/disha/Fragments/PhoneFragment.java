package com.appsters.disha.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.appsters.disha.R;
import com.appsters.disha.databinding.FragmentLoginBinding;
import com.appsters.disha.databinding.FragmentPhoneBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;


public class PhoneFragment extends Fragment {

    private FragmentPhoneBinding binding;
    private String phone;
    private String nmatId;
    private String name;

    public PhoneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentPhoneBinding.inflate(inflater,container,false);
        init();
        return binding.getRoot();
    }

    private void init() {
            nmatId=PhoneFragmentArgs.fromBundle(getArguments()).getNmat();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.loginSendOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phone,
                            20,
                            TimeUnit.SECONDS,
                            requireActivity(),
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                                @Override
                                public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {

                                }

                                @Override
                                public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                                    Toast.makeText(requireActivity(), "Verification Failed", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull @NotNull String s, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    super.onCodeSent(s, forceResendingToken);
                                    PhoneFragmentDirections.ActionPhoneFragmentToOTPFragment action=PhoneFragmentDirections.actionPhoneFragmentToOTPFragment();
                                    action.setVerificationId(s);
                                    action.setPhone(phone);
                                    action.setNmatId(nmatId);
                                    action.setName(name);
                                    Navigation.findNavController(binding.getRoot()).navigate(action);
                                }
                            }
                    );
                }


            }
        });
    }

    private boolean validate() {
        phone="+91"+binding.loginPhone.getText().toString().trim();
        name=binding.loginName.getText().toString().trim();
        if (phone.length()!=13 ){
            binding.loginPhone.setError("Enter a valid number");
            return false;
        }
        if (name.length()<3){
            binding.loginName.setError("Enter a valid name");
            return false;
        }
        return true;
    }
}