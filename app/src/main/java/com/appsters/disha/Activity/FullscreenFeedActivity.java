package com.appsters.disha.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.appsters.disha.databinding.ActivityFullscreenFeedBinding;
import com.squareup.picasso.Picasso;

public class FullscreenFeedActivity extends AppCompatActivity {

    private ActivityFullscreenFeedBinding binding;
    private String header,details,imageurl,date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityFullscreenFeedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();

        binding.fullfeedDate.setText(date);
        Picasso.get().load(imageurl).fit().into(binding.fullfeedImage);
        binding.fullfeedHeader.setText(header);
        binding.fullfeedDetails.setText(details);

    }

    private void initialize() {
        header=getIntent().getExtras().getString("header",null);
        details=getIntent().getExtras().getString("details",null);
        imageurl=getIntent().getExtras().getString("imageUrl",null);
        date=getIntent().getExtras().getString("date",null);
    }


}