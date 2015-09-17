package com.tizi.quanzi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tizi.quanzi.R;
import com.tizi.quanzi.chat.VoicePlayAsync;
import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.model.ChatMessage;
import com.tizi.quanzi.network.GetVolley;
import com.tizi.quanzi.tool.FriendTime;
import com.tizi.quanzi.tool.GetThumbnailsUri;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.tool.Tool;

import java.util.List;

/**
 * Created by qixingchen on 15/7/20. 聊天记录Adapter
 */

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessAbsViewHolder> {

    public List<ChatMessage> chatMessageList;
    private Context mContext;
    private VoicePlayAsync voicePlayAsync;

    /**
     * @param position 记录位置
     *
     * @return 记录类型
     *
     * @see com.tizi.quanzi.tool.StaticField.ChatFrom
     */
    @Override
    public int getItemViewType(int position) {
        return chatMessageList.get(position).From;
    }

    /**
     * @param chatMessageList 聊天记录List
     * @param context         上下文
     *
     * @see ChatMessage
     */
    public ChatMessageAdapter(List<ChatMessage> chatMessageList, Context context) {
        this.chatMessageList = chatMessageList;
        this.mContext = context;
    }

    /**
     * @param parent   需要创建ViewHolder的 ViewGroup
     * @param viewType 记录类型
     *
     * @return ViewHolder
     *
     * @see com.tizi.quanzi.tool.StaticField.ChatFrom
     * @see ChatMessAbsViewHolder
     */
    @Override
    public ChatMessAbsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        ChatMessAbsViewHolder vh;
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

    /**
     * 发生绑定时，为viewHolder的元素赋值
     *
     * @param holder   被绑定的ViewHolder
     * @param position 列表位置
     */
    @Override
    public void onBindViewHolder(final ChatMessAbsViewHolder holder, int position) {
        final ChatMessage chatMessage = chatMessageList.get(position);

        holder.setAllAdditionVisibilityGone();
        /*根据不同消息类型确定元素是否显示*/
        switch (chatMessage.type) {
            case StaticField.ChatContantType.TEXT:
                holder.chatMessTextView.setText(chatMessage.text);
                holder.chatMessTextView.setVisibility(View.VISIBLE);
                break;
            case StaticField.ChatContantType.IMAGE:
                int[] imagePix = Tool.getImagePixel(mContext,
                        chatMessage.imageHeight, chatMessage.imageWeight);
                holder.contantImageView.getLayoutParams().height = imagePix[0];
                holder.contantImageView.getLayoutParams().width = imagePix[1];
                holder.contantImageView.setImageUrl(GetThumbnailsUri.maxHeiAndWei(
                                chatMessage.url, imagePix[0], imagePix[1]),
                        GetVolley.getmInstance().getImageLoader());
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

        /*设置已读*/
        if (!chatMessage.isread) {
            chatMessage.isread = true;
            DBAct.getInstance().addOrReplaceChatMessage(chatMessage);
        }
        if (position == chatMessageList.size() - 1) {
            DBAct.getInstance().setAllAsRead(chatMessage.ConversationId);
        }
        /*设置头像*/
        holder.userFaceImageView.setImageUrl(GetThumbnailsUri.maxHeiAndWei(
                        chatMessage.chatImage, 48 * 3, 48 * 3),
                GetVolley.getmInstance().getImageLoader());

        String time = FriendTime.FriendlyDate(chatMessage.create_time);
        holder.chatTime.setText(time);
        if (holder.chatUserName != null) {
            holder.chatUserName.setText(chatMessage.userName);
        }
        if (!chatMessage.isread) {
            chatMessage.isread = true;
            DBAct.getInstance().addOrReplaceChatMessage(chatMessage);
        }
        holder.voiceDuration.setText(String.valueOf((int) chatMessage.voice_duration) + " s");

        /*播放按钮*/
        holder.videoPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (voicePlayAsync == null) {
                    voicePlayAsync = new VoicePlayAsync();
                } else {
                    voicePlayAsync.holder.audioProgressBar.setProgress(0);
                    voicePlayAsync.cancel(true);
                    voicePlayAsync = new VoicePlayAsync();
                }

                voicePlayAsync.setContext(mContext)
                        .setChatMessage(chatMessage)
                        .setHolder(holder)
                        .execute(0);
            }
        });
    }

    /**
     * @return 聊天记录数
     */
    @Override
    public int getItemCount() {
        return chatMessageList == null ? 0 : chatMessageList.size();
    }

    /**
     * 实现BaseViewHolder
     */
    public static class OtherMessViewHolder extends ChatMessAbsViewHolder {
        public OtherMessViewHolder(View v) {
            super(v);
            findViewByID(v, StaticField.ChatFrom.OTHER);
        }
    }

    /**
     * 实现BaseViewHolder
     */
    public static class MyMessViewHolder extends ChatMessAbsViewHolder {
        public MyMessViewHolder(View v) {
            super(v);
            findViewByID(v, StaticField.ChatFrom.ME);
        }
    }

    /**
     * 新增或更新消息
     * 将按照 create_time 插入正确的位置
     *
     * @param chatMessage 需要新增或更新的消息
     */
    public void addOrUpdateMessage(ChatMessage chatMessage) {
        // TODO: 15/8/13 更好的方式查找消息
        int length = chatMessageList.size();
        long time = chatMessage.create_time;
        int insertPosition = length;
        for (int i = length - 1; i >= 0; i--) {
            ChatMessage tempchatMessage = chatMessageList.get(i);
            if (tempchatMessage.messID.compareTo(chatMessage.messID) == 0) {
                chatMessageList.remove(i);
                chatMessageList.add(i, chatMessage);
                return;
            }
            if (tempchatMessage.create_time > time) {
                insertPosition = i;
            }
        }
        //todo 确认加载位置
        chatMessageList.add(insertPosition, chatMessage);
        notifyDataSetChanged();
    }

    /**
     * @return 返回最后未读的位置
     */
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
