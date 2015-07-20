package com.tizi.quanzi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.tizi.quanzi.R;
import com.tizi.quanzi.gson.ChatMessage;
import com.tizi.quanzi.network.GetVolley;

/**
 * Created by qixingchen on 15/7/20.
 */
public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder> {

    ChatMessage[] chatMessages;
    Context mContext;

    public ChatMessageAdapter(ChatMessage[] chatMessages, Context context) {
        this.chatMessages = chatMessages;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.userFaceImageView.setImageUrl(chatMessages[position].chatImage,
                GetVolley.getmInstance(mContext).getImageLoader());
        holder.chatMessTextView.setText(chatMessages[position].chatMessage);
        holder.chatTime.setText(chatMessages[position].chatTime);
        holder.chatUserName.setText(chatMessages[position].chatUserName);
    }

    @Override
    public int getItemCount() {
        return chatMessages == null ? 0 : chatMessages.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        //界面元素
        public NetworkImageView userFaceImageView;
        public TextView chatMessTextView, chatUserName, chatTime;


        public ViewHolder(View v) {
            super(v);


            userFaceImageView = (NetworkImageView) v.findViewById(R.id.chat_user_face);
            chatMessTextView = (TextView) v.findViewById(R.id.chat_message);
            chatUserName = (TextView) v.findViewById(R.id.chat_user_name);
            chatTime = (TextView) v.findViewById(R.id.chat_message_time);
            //todo:可在这里添加部件的按键监听
        }

    }
}
