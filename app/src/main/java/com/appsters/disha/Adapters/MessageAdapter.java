package com.appsters.disha.Adapters;

import android.app.Activity;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsters.disha.Activity.ChatWindowActivity;
import com.appsters.disha.Models.ChatMessageModal;
import com.appsters.disha.R;
import com.appsters.disha.Utils.GetTimeAgo;
import com.appsters.disha.databinding.MessageListItemBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter {

    private FirebaseAuth mAuth;
    private List<ChatMessageModal> mMessagesList;
    String current_user_id;
    private Activity activity;
    private SharedPreferences pref;
    private String imageUrl,photo;


    public MessageAdapter(List<ChatMessageModal> mMessagesList, ChatWindowActivity chatWindowActivity, String imageUrl, String photo) {
        this.mMessagesList=mMessagesList;
        this.activity=chatWindowActivity;
        this.imageUrl=imageUrl;
        this.photo=photo;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mAuth=FirebaseAuth.getInstance();

        current_user_id=mAuth.getCurrentUser().getUid();
        return new MessageViewHolder(MessageListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ChatMessageModal mess=mMessagesList.get(position);

        final MessageViewHolder messageViewHolder= (MessageViewHolder) holder;

        String time = GetTimeAgo.getTimeAgo(mess.getTime(), activity);
        String sender=mess.getSender();
        String message=mess.getMessage();

        if (TextUtils.equals(sender,current_user_id)){
                messageViewHolder.binding.receivedMessageContainer.setVisibility(View.GONE);
                messageViewHolder.binding.receivedImage.setVisibility(View.GONE);
                messageViewHolder.binding.sentText.setText(message);
                messageViewHolder.binding.sentTime.setText(time);
            Picasso.get().load(photo).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.ic_profile_placeholder).into(messageViewHolder.binding.sentImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Picasso.get().load(photo).placeholder(R.drawable.ic_profile_placeholder).into(messageViewHolder.binding.sentImage);
                }
            });
        }
        else{
            messageViewHolder.binding.sentMessageContainer.setVisibility(View.GONE);
            messageViewHolder.binding.sentImage.setVisibility(View.GONE);
            messageViewHolder.binding.receivedText.setText(message);
            messageViewHolder.binding.receivedTime.setText(time);
            Log.d("image",imageUrl);
            Picasso.get().load(imageUrl).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.ic_profile_placeholder).into(messageViewHolder.binding.receivedImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Log.d("image",imageUrl);
                    Picasso.get().load(imageUrl).placeholder(R.drawable.ic_profile_placeholder).into(messageViewHolder.binding.receivedImage);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mMessagesList == null ? 0 : mMessagesList.size();
    }
    private static class MessageViewHolder extends RecyclerView.ViewHolder {
        MessageListItemBinding binding;

        public MessageViewHolder(MessageListItemBinding b) {
            super(b.getRoot());
            binding=b;
        }
    }
}
