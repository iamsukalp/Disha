package com.appsters.disha.Fragments;

import static android.content.Context.CLIPBOARD_SERVICE;



import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.appsters.disha.Activity.HelpActivity;
import com.appsters.disha.Activity.MainActivity;
import com.appsters.disha.R;
import com.appsters.disha.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private String watLink = null;
    private String interviewLink=null;
    private FirebaseAuth mAuth;
    private long tokenNumber=0;
    private int activeToken=0;
    private int meanTime=15;
    private String tokenString;
    private String activeString;
    private String nmatId;
    private DatabaseReference userRef,groupRef,rootRef,aspirantRef;

    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false);
        init();
        return binding.getRoot();

    }


    private void init() {
        // firebase component
        mAuth=FirebaseAuth.getInstance();
        rootRef=FirebaseDatabase.getInstance("https://disha-6af88-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        userRef= FirebaseDatabase.getInstance().getReference().child("Users");
        groupRef=rootRef.child("Groups");
        aspirantRef=rootRef.child("Aspirants");
    }



    private void getNMATId() {
        userRef.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              nmatId=snapshot.child("nmatId").getValue().toString();
              getData(nmatId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Intent intent=new Intent(requireContext(), MainActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getNMATId();

        binding.HomeHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(requireContext(), HelpActivity.class);
                startActivity(intent);
            }
        });
        
        binding.homeFragmentWatCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (watLink!=null){
                    String link=watLink;
                    copyLink(link);
                }else{
                    Toast.makeText(requireContext(), "Hasn't assigned yet!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        
        binding.homeFragmentInterviewCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (watLink!=null){
                    String link=interviewLink;
                    copyLink(link);
                }else{
                    Toast.makeText(requireContext(), "Hasn't assigned yet!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.homeFragmentInterviewShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (watLink!=null){
                    String link=watLink;
                    shareLink(link);
                }else{
                    Toast.makeText(requireContext(), "Hasn't assigned yet!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.homeFragmentWatShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (watLink!=null){
                    String link=interviewLink;
                    shareLink(link);
                }else{
                    Toast.makeText(requireContext(), "Hasn't assigned yet!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getData(String nmatId) {
        aspirantRef.child(nmatId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("watLink")){
                       watLink=snapshot.child("watLink").getValue().toString();
                }if(snapshot.hasChild("interviewLink")){
                    watLink=snapshot.child("interviewLink").getValue().toString();
                }
                if (snapshot.hasChild("tokenNum")){
                    tokenNumber= (long) snapshot.child("tokenNum").getValue();
                    tokenString= String.valueOf(tokenNumber);
                    binding.homeFragmentTokenNumber.setTextColor(getResources().getColor(R.color.primary_color,null));
                }else{
                    tokenString="N/A";
                    binding.homeFragmentTokenNumber.setTextColor(getResources().getColor(R.color.dark_side,null));

                }if(snapshot.hasChild("activeToken")){
                    activeToken= (int) snapshot.child("activeToken").getValue();
                    activeString="Active token: "+activeToken;
                    binding.homeFragmentActive.setTextColor(getResources().getColor(R.color.white,null));
                }else{
                      activeString="Active token: Not assigned yet";
                      binding.homeFragmentActive.setTextColor(getResources().getColor(R.color.primary_text,null));

                }
                binding.homeFragmentTokenNumber.setText(tokenString);
                binding.homeFragmentActive.setText(activeString);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void shareLink(String link) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Disha");
            shareIntent.putExtra(Intent.EXTRA_TEXT, link);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch(Exception e) {
            //e.toString();
            Toast.makeText(requireContext(),"Error! Try again",Toast.LENGTH_SHORT).show();
        }
    }

    private void copyLink(String link) {
        ClipboardManager clipboard = (ClipboardManager) requireActivity().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Link", link);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(requireContext(), "Link copied!", Toast.LENGTH_SHORT).show();
    }
}