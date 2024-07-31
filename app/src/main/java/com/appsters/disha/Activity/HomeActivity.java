package com.appsters.disha.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;

import com.appsters.disha.Fragments.ChatsFragment;
import com.appsters.disha.Fragments.FeedsFragment;
import com.appsters.disha.Fragments.HomeFragment;
import com.appsters.disha.R;
import com.appsters.disha.databinding.ActivityHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityHomeBinding binding;

    private ConstraintSet constraintSetShow= new ConstraintSet();
    private ConstraintSet constraintSetHide=new ConstraintSet();
    private boolean isMenuOpen=false;
    public boolean isStudent=false;
    private FragmentManager fragmentManager;
    private DatabaseReference userRef,aspirantRef;
    private FirebaseAuth mAuth;
    public static String photo;
    private String name,resumeUrl,email,phone,nmatId,userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

        initializeListners();
        initializeBottomBar();


        binding.homeTopDownMenu.menuProfileIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,SettingsActivity.class);
                intent.putExtra("imageUrl",photo);
                intent.putExtra("email",email);
                intent.putExtra("name",name);
                intent.putExtra("resumeUrl",resumeUrl);
                intent.putExtra("nmatId",nmatId);

                Pair[] pairs = new Pair[1];
                pairs[0]=new Pair<View,String>(binding.homeTopDownMenu.menuProfileIV,"profile_image");
                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this,pairs);
                startActivity(intent,options.toBundle());
            }
        });

        binding.homeTopDownMenu.menuSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,SettingsActivity.class);
                intent.putExtra("imageUrl",photo);
                intent.putExtra("email",email);
                intent.putExtra("name",name);
                intent.putExtra("resumeUrl",resumeUrl);
                intent.putExtra("nmatId",nmatId);

                Pair[] pairs = new Pair[1];
                pairs[0]=new Pair<View,String>(binding.homeTopDownMenu.menuProfileIV,"profile_image");
                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this,pairs);
                startActivity(intent,options.toBundle());
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        getUserData();

    }

    private void getUserData() {
        userRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    nmatId=snapshot.child("nmatId").getValue().toString();
                    aspirantRef.child(nmatId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild("student")){
                                if ((boolean) snapshot.child("student").getValue()){
                                    binding.homeTopDownMenu.menuStatus.setText("Student");
                                    binding.homeTopDownMenu.menuStatus.setTextColor(getColor(R.color.feedback));
                                    binding.homeTopDownMenu.menuStatus.setBackground(getDrawable(R.drawable.status_bg_student));
                                }
                                else{
                                    binding.homeTopDownMenu.menuStatus.setText("Aspirant");
                                    binding.homeTopDownMenu.menuStatus.setTextColor(getColor(R.color.orange_light));
                                    binding.homeTopDownMenu.menuStatus.setBackground(getDrawable(R.drawable.status_bg_aspirant));
                                }
                            }
                            else{
                                binding.homeTopDownMenu.menuStatus.setText("Aspirant");
                                binding.homeTopDownMenu.menuStatus.setTextColor(getColor(R.color.orange_light));
                                binding.homeTopDownMenu.menuStatus.setBackground(getDrawable(R.drawable.status_bg_aspirant));

                            }
                            name=snapshot.child("name").getValue().toString();
                            email=snapshot.child("email").getValue().toString();
                            phone=snapshot.child("phone").getValue().toString();

                            if (snapshot.hasChild("resumeUrl")){
                                binding.homeTopDownMenu.menuResumeStatus.setVisibility(View.GONE);
                                resumeUrl=snapshot.child("resumeUrl").getValue().toString();
                            }else{
                                resumeUrl="";   
                            }

                            binding.homeTopDownMenu.menuAspirantEmail.setText(email);
                            binding.homeTopDownMenu.menuAspirantName.setText(name);
                            binding.homeTopDownMenu.menuAspirantPhone.setText(phone);
                            photo=snapshot.child("imageUrl").getValue().toString();
                            Picasso.get().load(photo).placeholder(R.drawable.ic_profile_placeholder).networkPolicy(NetworkPolicy.OFFLINE).into(binding.homeTopDownMenu.menuProfileIV, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError(Exception e) {
                                    Picasso.get().load(photo).placeholder(R.drawable.ic_profile_placeholder).into(binding.homeTopDownMenu.menuProfileIV);
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initializeBottomBar() {
        binding.bottomMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.homeFragment:
                        binding.bottomMenu.getMenu().findItem(R.id.feedsFragment).setIcon(R.drawable.ic_feeds);
                        binding.bottomMenu.getMenu().findItem(R.id.chatsFragment).setIcon(R.drawable.ic_chat);
                        binding.bottomMenu.getMenu().findItem(R.id.homeFragment).setIcon(R.drawable.home_selected);
                        fragment = new HomeFragment();
                        break;
                    case R.id.feedsFragment:
                        binding.bottomMenu.getMenu().findItem(R.id.homeFragment).setIcon(R.drawable.ic_home);
                        binding.bottomMenu.getMenu().findItem(R.id.feedsFragment).setIcon(R.drawable.ic_feeds_selected);
                        binding.bottomMenu.getMenu().findItem(R.id.chatsFragment).setIcon(R.drawable.ic_chat);
                        fragment = new FeedsFragment();
                        break;
                    case R.id.chatsFragment:
                        binding.bottomMenu.getMenu().findItem(R.id.feedsFragment).setIcon(R.drawable.ic_feeds);
                        binding.bottomMenu.getMenu().findItem(R.id.homeFragment).setIcon(R.drawable.ic_home);
                        binding.bottomMenu.getMenu().findItem(R.id.chatsFragment).setIcon(R.drawable.ic_chat_selected);
                        fragment = new ChatsFragment();
                        break;
                    default:
                        binding.bottomMenu.getMenu().findItem(R.id.feedsFragment).setIcon(R.drawable.ic_feeds);
                        binding.bottomMenu.getMenu().findItem(R.id.chatsFragment).setIcon(R.drawable.ic_chat);
                        binding.bottomMenu.getMenu().findItem(R.id.homeFragment).setIcon(R.drawable.home_selected);
                        fragment = new HomeFragment();
                        break;
                }

                if (fragment != null) {
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.framelayout, fragment)
                            .commit();
                } else {
                    Log.e("Home Fragment", "Error in creating fragment");
                }
                return false;
            }
        });
    }

    private void initializeListners() {
        binding.homeMenuOpenBtn.setOnClickListener(this);
        binding.homeMenuCloseBtn.setOnClickListener(this);


    }

    public void init(){

        mAuth=FirebaseAuth.getInstance();
        userId=mAuth.getCurrentUser().getUid();
        aspirantRef= FirebaseDatabase.getInstance(" https://disha-6af88-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Aspirants");
        userRef=FirebaseDatabase.getInstance(" https://disha-6af88-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Users");


        //binding.bottomMenu.replaceMenu(R.menu.bottom_app_bar_menu);
        binding.bottomMenu.setItemIconTintList(null);
        binding.bottomMenu.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_UNLABELED);
        binding.bottomMenu.getMenu().findItem(R.id.homeFragment).setIcon(R.drawable.home_selected);

        fragmentManager = getSupportFragmentManager();
        HomeFragment homeFragment = new HomeFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.framelayout, homeFragment)
                .commit();


        constraintSetShow.clone(this,R.layout.activity_home_menu);
        constraintSetHide.clone(binding.layout);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_menu_open_btn:
                openMenu();
                break;
            case R.id.home_menu_close_btn:
                closeMenu();
                break;
        }
    }

    private void openMenu() {
        getWindow().setStatusBarColor(getColor(R.color.background_grey));
        TransitionManager.beginDelayedTransition(binding.layout);
        constraintSetShow.applyTo(binding.layout);
        isMenuOpen=true;
    }

    private void closeMenu() {
        getWindow().setStatusBarColor(getColor(R.color.black));
        TransitionManager.beginDelayedTransition(binding.layout);
        constraintSetHide.applyTo(binding.layout);
        isMenuOpen=false;
    }

    @Override
    public void onBackPressed() {
        if (isMenuOpen){
            closeMenu();
        }
        else{
            super.onBackPressed();
        }
    }
}