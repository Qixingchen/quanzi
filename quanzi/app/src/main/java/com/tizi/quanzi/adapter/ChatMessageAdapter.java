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
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.tool.Tool;

import java.util.List;

/**
 * Created by qixingchen on 15/7/20.
 */
public class ChatMessageAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    List<ChatMessage> chatMessageList;
    Context mContext;

    @Override
    public int getItemViewType(int position) {
        //todo 多媒体支持
        return chatMessageList.get(position).From;
    }

    public ChatMessageAdapter(List<ChatMessage> chatMessageList, Context context) {
        this.chatMessageList = chatMessageList;
        this.mContext = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //todo switch View Holder By data Mess
        View v;
        BaseViewHolder vh;
        switch (viewType) {
            case StaticField.ChatFrom.OTHER:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ohter_chat_item, parent, false);
                vh = new OtherMessViewHolder(v);
                break;
            case StaticField.ChatFrom.ME:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.my_chat_item, parent, false);
                vh = new MyMessViewHolder(v);
                break;
            default:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ohter_chat_item, parent, false);
                vh = new OtherMessViewHolder(v);
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.userFaceImageView.setImageUrl(chatMessageList.get(position).chatImage,
                GetVolley.getmInstance(mContext).getImageLoader());
        holder.chatMessTextView.setText(chatMessageList.get(position).text);
        String time = Tool.timeStringFromUNIX(chatMessageList.get(position).create_time);
        holder.chatTime.setText(time);
        if (holder.chatUserName != null) {
            holder.chatUserName.setText(chatMessageList.get(position).sender);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessageList == null ? 0 : chatMessageList.size();
    }

    public static class OtherMessViewHolder extends BaseViewHolder {
        public OtherMessViewHolder(View v) {
            super(v);
            userFaceImageView = (NetworkImageView) v.findViewById(R.id.chat_user_face);
            chatMessTextView = (TextView) v.findViewById(R.id.chat_message);
            chatUserName = (TextView) v.findViewById(R.id.chat_user_name);
            chatTime = (TextView) v.findViewById(R.id.chat_message_time);
            //todo:可在这里添加部件的按键监听
        }

    }

    public static class MyMessViewHolder extends BaseViewHolder {
        public MyMessViewHolder(View v) {
            super(v);
            userFaceImageView = (NetworkImageView) v.findViewById(R.id.chat_user_face);
            chatMessTextView = (TextView) v.findViewById(R.id.chat_message);
            chatUserName = null;
            chatTime = (TextView) v.findViewById(R.id.chat_message_time);
            //todo:可在这里添加部件的按键监听
        }

    }

}
