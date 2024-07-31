package com.appsters.disha.Base;

import android.app.Application;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class Disha extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
// Firebase offline persistence
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);



        //Picasso offline Capability
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);


    }
}
