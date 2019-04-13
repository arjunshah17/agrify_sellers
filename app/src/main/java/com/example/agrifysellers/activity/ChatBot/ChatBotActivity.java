package com.example.agrifysellers.activity.ChatBot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;

import com.example.agrifysellers.R;
import com.example.agrifysellers.databinding.ActivityChatBotBinding;

public class ChatBotActivity extends AppCompatActivity {
    static  final String chatbotUrl="https://snatchbot.me/webchat?botID=52622&appID=sKkDZ6o4cIQLE9QvsgIF";
    ActivityChatBotBinding bind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind= DataBindingUtil.setContentView(this, R.layout.activity_chat_bot);
        bind.chatbot.getSettings().setJavaScriptEnabled(true);
        bind.chatbot.loadUrl(chatbotUrl);
    }
}
