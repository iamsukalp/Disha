package com.appsters.disha.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebViewClient;

import com.appsters.disha.databinding.ActivityYellowChatBotBinding;

public class YellowChatBotActivity extends AppCompatActivity {

    private ActivityYellowChatBotBinding binding;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityYellowChatBotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        url="cloud.yellow.ai/liveBot/x1648497379033?region=";

        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        binding.webView.getSettings().setDomStorageEnabled(true);
        binding.webView.setWebViewClient(new WebViewClient());
        binding.webView.loadUrl(url);



    }
}