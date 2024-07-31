package com.appsters.disha.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.appsters.disha.R;
import com.appsters.disha.databinding.ActivitySettingsBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;
    private String name,imageUrl,email,resumeUrl,nmatId;
    private ProgressDialog progressDialog;
    private int request_code=154;
    private Uri fileUri;
    private StorageReference filepath,storageReference;
    private String mediaDownloadUrl;
    private DatabaseReference aspirantRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

        binding.settingsShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL myUrl = new URL(resumeUrl);
                            URLConnection urlConnection = myUrl.openConnection();
                            urlConnection.connect();
                            int file_size = urlConnection.getContentLength();
                            Log.i("FILE SIZE", "file_size = " + file_size);
                            progressDialog.dismiss();
                            if (file_size>15000000){
                                openLargeFileOrUrlReference(resumeUrl);
                                         /*  Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(model.getUrl()));
                                           startActivity(browserIntent);*/
                            }
                            else{

                                Intent intent=new Intent(SettingsActivity.this, PdfViewActivity.class);
                                intent.putExtra("url",resumeUrl);
                                intent.putExtra("largeFile",false);
                                startActivity(intent);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();


                        }
                    }
                }).start();
            }
        });

        binding.settingsUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentPDF = new Intent(Intent.ACTION_GET_CONTENT);
                intentPDF.setType("application/pdf");
                intentPDF.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intentPDF, request_code);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK) {
            {

                    fileUri=data.getData();
                    if (fileUri!=null){
                        uplaodFile();
                    }




            }
        }
    }

    private void uplaodFile() {
        progressDialog.show();
        filepath = storageReference.child("Resume").child(Calendar.getInstance().getTime() + ".pdf");
        filepath.putFile(fileUri).continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return filepath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    // After uploading is done it progress
                    // dialog box will be dismissed

                    Uri uri = task.getResult();

                    mediaDownloadUrl = uri.toString();
                    Log.d("Resume Url", mediaDownloadUrl);

                    uploadResume();


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(SettingsActivity.this, "Uploading failed! Try Again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadResume() {
        Map<String,Object> map=new HashMap<>();
        map.put("resumeUrl",mediaDownloadUrl);

        aspirantRef.child(nmatId).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this, "Resume Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this, "Couldn't upload resume.Try Again!", Toast.LENGTH_SHORT).show();
                    }
            }
        });


    }

    private void openLargeFileOrUrlReference(String resumeUrl) {
        Intent intent=new Intent(SettingsActivity.this, PdfViewActivity.class);
        intent.putExtra("url",resumeUrl);
        intent.putExtra("largeFile",true);
        startActivity(intent);
    }

    public void init(){
        name=getIntent().getStringExtra("name");
        imageUrl=getIntent().getStringExtra("imageUrl");
        email=getIntent().getStringExtra("email");
        resumeUrl=getIntent().getStringExtra("resumeUrl");
        nmatId=getIntent().getStringExtra("nmatId");

        progressDialog=new ProgressDialog(SettingsActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Please Wait...");

        storageReference= FirebaseStorage.getInstance().getReference();
        aspirantRef= FirebaseDatabase.getInstance(" https://disha-6af88-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Aspirants");


        Picasso.get().load(imageUrl).placeholder(R.drawable.ic_profile_placeholder).networkPolicy(NetworkPolicy.OFFLINE).into(binding.settingsImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                Picasso.get().load(imageUrl).placeholder(R.drawable.ic_profile_placeholder).into(binding.settingsImage);
            }
        });
       binding.settingsEmail.setText(email);
       binding.settingsName.setText(name);



    }
}