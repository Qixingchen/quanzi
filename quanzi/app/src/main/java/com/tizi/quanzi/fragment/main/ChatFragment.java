package com.tizi.quanzi.fragment.main;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.ChatMessageAdapter;
import com.tizi.quanzi.model.ChatMessage;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private RecyclerView chatmessagerecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ChatMessageAdapter chatMessageAdapter;
    private Activity mActivity;


    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        //RecyclerView
        this.chatmessagerecyclerView = (RecyclerView) mActivity.findViewById(R.id.chat_message_recyclerView);
        chatmessagerecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mActivity);
        chatmessagerecyclerView.setLayoutManager(mLayoutManager);

        //adapt
        chatMessageAdapter = new ChatMessageAdapter(ChatMessage.getChatMess(), mActivity);
        chatmessagerecyclerView.setAdapter(chatMessageAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }


}
