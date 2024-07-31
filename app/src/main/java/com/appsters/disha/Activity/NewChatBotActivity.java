package com.appsters.disha.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.appsters.disha.R;
import com.appsters.disha.databinding.ActivityNewChatBotBinding;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class NewChatBotActivity extends AppCompatActivity {

    private ActivityNewChatBotBinding binding;
    private EditText message;
    private Button sendBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewChatBotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }


       binding.customizedMessagesWindow.setBackgroundColor(getResources().getColor(R.color.black,null));
       binding.customizedMessagesWindow.getWritingMessageView().setBackgroundColor(getResources().getColor(R.color.background_grey,null));
       message=binding.customizedMessagesWindow.getWritingMessageView().findViewById(R.id.message_box_text_field);
       sendBtn=binding.customizedMessagesWindow.getWritingMessageView().findViewById(R.id.message_box_button);

       message.setHint("Type here");
       message.setBackgroundColor(getResources().getColor(R.color.background_grey,null));

       sendBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               binding.customizedMessagesWindow.sendMessage(message.getText().toString().trim());
               message.setText("");
               Python py=Python.getInstance();
               PyObject pyobj=py.getModule("myscript");

               PyObject obj=pyobj.callAttr("main",message.getText().toString().trim());

               binding.customizedMessagesWindow.receiveMessage(obj.toString());


           }
       });
    }
}