package com.tizi.quanzi.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.tizi.quanzi.R;
import com.tizi.quanzi.chat.VoicePlayAsync;
import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.model.ChatMessage;
import com.tizi.quanzi.network.GetVolley;
import com.tizi.quanzi.tool.GetThumbnailsUri;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.tool.Tool;

import java.util.List;

/**
 * Created by qixingchen on 15/7/20.
 */
public class ChatMessageAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public List<ChatMessage> chatMessageList;
    private Context mContext;

    protected static final int STOP = 0x10000;
    protected static final int NEXT = 0x10001;
    private int iCount = 0;
    private VoicePlayAsync voicePlayAsync;

    @Override
    public int getItemViewType(int position) {
        return chatMessageList.get(position).From;
    }

    public ChatMessageAdapter(List<ChatMessage> chatMessageList, Context context) {
        this.chatMessageList = chatMessageList;
        this.mContext = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

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
    public void onBindViewHolder(final BaseViewHolder holder, int position) {
        //todo 赋值在这里
        final ChatMessage chatMessage = chatMessageList.get(position);
        holder.userFaceImageView.setImageUrl(GetThumbnailsUri.maxHeiAndWei(
                        chatMessage.chatImage, 48 * 3, 48 * 3),
                GetVolley.getmInstance(mContext).getImageLoader());

        String time = Tool.timeStringFromUNIX(chatMessage.create_time);
        holder.chatTime.setText(time);
        if (holder.chatUserName != null) {
            holder.chatUserName.setText(chatMessage.userName);
        }
        if (!chatMessage.isread) {
            chatMessage.isread = true;
            DBAct.getInstance().addOrReplaceChatMessage(chatMessage);
        }
        holder.voiceDuration.setText(String.valueOf((int) chatMessage.voice_duration) + " s");
        holder.contantImageView.setVisibility(View.GONE);
        holder.chatMessTextView.setVisibility(View.GONE);
        holder.videoPlayButton.setVisibility(View.GONE);
        holder.audioProgressBar.setVisibility(View.GONE);
        holder.voiceDuration.setVisibility(View.GONE);
        switch (chatMessage.type) {
            case StaticField.ChatContantType.TEXT:
                holder.chatMessTextView.setText(chatMessage.text);
                holder.chatMessTextView.setVisibility(View.VISIBLE);
                break;
            case StaticField.ChatContantType.IMAGE:
                int[] imagePix = Tool.getImagePixel(mContext,
                        chatMessage.imageHeight, chatMessage.imageWeight);
                holder.contantImageView.getLayoutParams().height = imagePix[0];
                holder.contantImageView.getLayoutParams().height = imagePix[1];
                holder.contantImageView.setImageUrl(GetThumbnailsUri.maxHeiAndWei(
                                chatMessage.url, 800 * 3, 300 * 3),
                        GetVolley.getmInstance(mContext).getImageLoader());
                holder.contantImageView.setVisibility(View.VISIBLE);
                break;
            case StaticField.ChatContantType.VOICE:
                holder.videoPlayButton.setVisibility(View.VISIBLE);
                holder.audioProgressBar.setVisibility(View.VISIBLE);
                holder.voiceDuration.setVisibility(View.VISIBLE);
                break;
            case StaticField.ChatContantType.VEDIO:
                holder.videoPlayButton.setVisibility(View.VISIBLE);
                holder.audioProgressBar.setVisibility(View.VISIBLE);
                holder.voiceDuration.setVisibility(View.VISIBLE);
                break;
            default:
                holder.chatMessTextView.setText(chatMessage.text);
                holder.chatMessTextView.setVisibility(View.VISIBLE);
                break;
        }
        holder.videoPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (voicePlayAsync == null) {
                    voicePlayAsync = new VoicePlayAsync();
                } else {
                    voicePlayAsync.cancel(false);
                }

                voicePlayAsync.setContext(mContext)
                        .setChatMessage(chatMessage)
                        .setHolder(holder)
                        .execute(0);
            }
        });
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
            contantImageView = (NetworkImageView) v.findViewById(R.id.contactImageView);
            videoPlayButton = (ImageButton) v.findViewById(R.id.vedio_play_button);
            audioProgressBar = (ProgressBar) v.findViewById(R.id.audio_progressBar);
            audioProgressBar.setIndeterminate(false);
            audioProgressBar.setProgress(0);
            voiceDuration = (TextView) v.findViewById(R.id.audio_duration);

        }

    }

    public static class MyMessViewHolder extends BaseViewHolder {
        public MyMessViewHolder(View v) {
            super(v);
            userFaceImageView = (NetworkImageView) v.findViewById(R.id.chat_user_face);
            chatMessTextView = (TextView) v.findViewById(R.id.chat_message);
            chatUserName = null;
            chatTime = (TextView) v.findViewById(R.id.chat_message_time);
            contantImageView = (NetworkImageView) v.findViewById(R.id.contactImageView);
            videoPlayButton = (ImageButton) v.findViewById(R.id.vedio_play_button);
            audioProgressBar = (ProgressBar) v.findViewById(R.id.audio_progressBar);
            audioProgressBar.setIndeterminate(false);
            audioProgressBar.setProgress(0);
            voiceDuration = (TextView) v.findViewById(R.id.audio_duration);
        }

    }

    public void addOrUpdateMessage(ChatMessage chatMessage) {
        // TODO: 15/8/13 更好的方式查找消息
        int length = chatMessageList.size();
        long time = chatMessage.create_time;
        int insertposition = length;
        for (int i = length - 1; i >= 0; i--) {
            ChatMessage tempchatMessage = chatMessageList.get(i);
            if (tempchatMessage.messID.compareTo(chatMessage.messID) == 0) {
                chatMessageList.remove(i);
                chatMessageList.add(i, chatMessage);
                return;
            }
            if (tempchatMessage.create_time > time) {
                insertposition = i;
            }
        }
        //todo 确认加载位置
        chatMessageList.add(insertposition, chatMessage);
        notifyDataSetChanged();
    }

    public int lastReadPosition() {
        int length = chatMessageList.size();
        int position = 0;
        for (int i = length - 1; i >= 0; i--) {
            if (chatMessageList.get(i).isread) {
                position = i;
                break;
            }
        }
        return position;
    }
}
