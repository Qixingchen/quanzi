package com.tizi.quanzi.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tizi.quanzi.R;
import com.tizi.quanzi.tool.StaticField;

/**
 * Created by qixingchen on 15/7/23.
 * 聊天记录组件抽象类
 */
public abstract class ChatMessAbsViewHolder extends RecyclerView.ViewHolder {
    //界面元素
    public ImageView userFaceImageView, contantImageView;
    public TextView chatMessTextView, chatUserName, chatTime, voiceDuration;
    public ImageButton videoPlayButton;
    public ProgressBar audioProgressBar;
    public View view;

    public ChatMessAbsViewHolder chatMessAbsViewHolder;

    public ChatMessAbsViewHolder(View itemView) {
        super(itemView);
        chatMessAbsViewHolder = this;
    }

    /**
     * 将所有附加品可见度更改为Gone
     */
    public void setAllAdditionVisibilityGone() {
        contantImageView.setVisibility(View.GONE);
        chatMessTextView.setVisibility(View.GONE);
        videoPlayButton.setVisibility(View.GONE);
        audioProgressBar.setVisibility(View.GONE);
        voiceDuration.setVisibility(View.GONE);
    }

    /**
     * 为界面元素赋值
     *
     * @param v            布局
     * @param itemViewType 消息来源类型
     *
     * @see com.tizi.quanzi.tool.StaticField.ChatFrom
     */
    public void findViewByID(View v, int itemViewType) {
        userFaceImageView = (ImageView) v.findViewById(R.id.chat_user_face);
        chatMessTextView = (TextView) v.findViewById(R.id.chat_message);
        if (itemViewType == StaticField.ChatFrom.OTHER) {
            chatUserName = (TextView) v.findViewById(R.id.chat_user_name);
        } else {
            chatUserName = null;
        }
        chatTime = (TextView) v.findViewById(R.id.chat_message_time);
        contantImageView = (ImageView) v.findViewById(R.id.contactImageView);
        videoPlayButton = (ImageButton) v.findViewById(R.id.vedio_play_button);
        audioProgressBar = (ProgressBar) v.findViewById(R.id.audio_progressBar);
        audioProgressBar.setIndeterminate(false);
        audioProgressBar.setProgress(0);
        voiceDuration = (TextView) v.findViewById(R.id.audio_duration);
        view = itemView;
    }
}
