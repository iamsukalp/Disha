package com.appsters.disha.Activity;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.appsters.disha.Adapters.MessageAdapter;
import com.appsters.disha.Fragments.FeedsFragment;
import com.appsters.disha.Models.ChatMessageModal;
import com.appsters.disha.Models.FeedsModal;
import com.appsters.disha.Models.MessageModal;
import com.appsters.disha.R;
import com.appsters.disha.Utils.GetTimeAgo;
import com.appsters.disha.databinding.ActivityChatWindowBinding;
import com.appsters.disha.databinding.MessageListItemBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatWindowActivity extends AppCompatActivity {

    private ActivityChatWindowBinding binding;
//    private FirebaseRecyclerAdapter<ChatMessageModal, MessageViewHolder> adapter;
//    private FirebaseRecyclerOptions<ChatMessageModal> option;
    private Query query;
    private LinearLayoutManager layoutManager;
    private DatabaseReference messageRef,rootRef;
    private String messageText;
    private FirebaseAuth mAuth;
    private String current_user,name,imageUrl,userId,push_id;
    private List<ChatMessageModal> mMessagesList = new ArrayList<>();
    private ProgressDialog progressDialog ;
    private static final int TOATL_ITEMS_TO_LOAD= 10;
    private int mCurrentPage=1;
    private int itemPosition=0;
    private String mLastKey="";
    private String mPrevKey="";
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatWindowBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        loadMessage();

        binding.messageSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageText=binding.messageText.getText().toString();
                sendMessage(messageText);
            }
        });

        binding.messageBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.messageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count>0){
                    binding.messageSendBtn.setEnabled(true);
                    binding.messageSendBtn.setImageTintList(ColorStateList.valueOf(getColor(R.color.white)));
                }
                else{
                    binding.messageSendBtn.setImageTintList(ColorStateList.valueOf(getColor(R.color.black)));
                    binding.messageSendBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.messageList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            Log.v("...", "Last Item Wow !");
                            // Do pagination.. i.e. fetch new data

                            mCurrentPage++;
                            itemPosition=0;
                            loadMessage();
                        }
                    }
                }
            }
        });
    }

    private void loadMessage() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (mCurrentPage==1){
                    query= messageRef.child(current_user).limitToLast(mCurrentPage*TOATL_ITEMS_TO_LOAD);
                    query.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            itemPosition++;

                            ChatMessageModal messages= dataSnapshot.getValue(ChatMessageModal.class);
                            mMessagesList.add(messages);

                            String messageKey=dataSnapshot.getKey();
                            if (itemPosition==1){
                                mLastKey=messageKey;
                                mPrevKey=messageKey;

                                Log.d("message key",messageKey);
                            }




                            messageAdapter.notifyDataSetChanged();
                            binding.messageList.smoothScrollToPosition(mMessagesList.size()-1); // chatList.getAdapter().getItemCount()
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                else{
                    query= messageRef.child(current_user).orderByKey().endAt(mLastKey).limitToLast(10);
                    query.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Log.d("more count",dataSnapshot.getChildrenCount()+"");



                            ChatMessageModal messages= dataSnapshot.getValue(ChatMessageModal.class);
                            String messageKey=dataSnapshot.getKey();


                            if (!mPrevKey.equals(messageKey)){
                                mMessagesList.add(itemPosition++,messages);
                                Log.d("Prev key", mPrevKey);
                            }
                            else{
                                mPrevKey=mLastKey;
                            }
                            if (itemPosition==1){
                                mLastKey=messageKey;
                            }



                            messageAdapter.notifyDataSetChanged();
                            layoutManager.scrollToPositionWithOffset(10,0);

                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

    private void sendMessage(String messageText) {
        HashMap<String,Object> messageMap=new HashMap<>();

        messageMap.put("message",messageText);
        messageMap.put("sender",current_user);
        messageMap.put("time", ServerValue.TIMESTAMP);

        DatabaseReference user_message_push = rootRef.child("Messages")
                .child(current_user).child(userId).push();

        push_id = user_message_push.getKey().substring(1,20);
        messageRef.child(current_user).child(push_id).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        messageRef.child(userId).child(push_id).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    binding.messageText.setText("");
                                }
                                else{
                                    Toast.makeText(ChatWindowActivity.this,"Couldn't send the message",Toast.LENGTH_SHORT).show();
                                    Log.d("Send Message Error", task.getException().getMessage());
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(ChatWindowActivity.this,"Couldn't send the message",Toast.LENGTH_SHORT).show();
                        Log.d("Send Message Error", task.getException().getMessage());
                    }
            }
        });

    }

    public void init(){

        // get data from intent
        userId=getIntent().getStringExtra("userId");
        imageUrl=getIntent().getStringExtra("imageUrl");
        name=getIntent().getStringExtra("name");

        messageAdapter=new MessageAdapter(mMessagesList,this,imageUrl,HomeActivity.photo);


        binding.messageUserName.setText(name);
        Picasso.get().load(imageUrl).placeholder(R.drawable.ic_profile_placeholder).networkPolicy(NetworkPolicy.OFFLINE).into(binding.messageUserImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                Picasso.get().load(imageUrl).placeholder(R.drawable.ic_profile_placeholder).into(binding.messageUserImage);
            }
        });


        rootRef=FirebaseDatabase.getInstance("https://disha-6af88-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        messageRef= rootRef.child("Messages");
        mAuth=FirebaseAuth.getInstance();
        current_user=mAuth.getCurrentUser().getUid();


        // initializing layout manager
        layoutManager=new LinearLayoutManager(ChatWindowActivity.this,LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        layoutManager.isAutoMeasureEnabled();
        binding.messageList.setHasFixedSize(true);
        binding.messageList.setLayoutManager(layoutManager);
        binding.messageList.setAdapter(messageAdapter);
        progressDialog=new ProgressDialog(this);

    }





}