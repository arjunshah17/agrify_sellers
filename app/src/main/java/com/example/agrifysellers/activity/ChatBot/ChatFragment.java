package com.example.agrifysellers.activity.ChatBot;


import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.agrifysellers.R;
import com.example.agrifysellers.databinding.FragmentChatBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {
    FragmentChatBinding bind;
    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_chat,
                container, false);
        bind.chatActionbutton.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), ChatBotActivity.class))
        );
        return bind.getRoot();
    }
}
