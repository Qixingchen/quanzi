package com.tizi.quanzi.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tizi.quanzi.Intent.StartGalleryActivity;
import com.tizi.quanzi.R;
import com.tizi.quanzi.chat.VoicePlayAsync;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.gson.OtherUserInfo;
import com.tizi.quanzi.model.ChatMessage;
import com.tizi.quanzi.network.FindUser;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.tool.FriendTime;
import com.tizi.quanzi.tool.GetThumbnailsUri;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.tool.Tool;
import com.tizi.quanzi.ui.user_zone.UserZoneActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qixingchen on 15/7/20.
 * 聊天记录Adapter
 */

public class ChatMessageAdapter extends RecyclerViewAdapterAbs {

    public SortedList<ChatMessage> chatMessageList;
    private Context mContext;
    private VoicePlayAsync voicePlayAsync;

    /**
     * @param chatMessageList 聊天记录List
     * @param context         上下文
     *
     * @see ChatMessage
     */
    public ChatMessageAdapter(List<ChatMessage> chatMessageList, Context context) {
        this.chatMessageList = new SortedList<>(ChatMessage.class, new SortedList.Callback<ChatMessage>() {
            @Override
            public int compare(ChatMessage o1, ChatMessage o2) {
                return (int) (o1.create_time - o2.create_time);
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(ChatMessage oldItem, ChatMessage newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(ChatMessage item1, ChatMessage item2) {
                return item1.messID.compareTo(item2.messID) == 0;
            }
        });
        if (chatMessageList != null) {
            this.chatMessageList.beginBatchedUpdates();
            this.chatMessageList.addAll(chatMessageList);
            this.chatMessageList.endBatchedUpdates();
        }
        this.mContext = context;
    }

    /**
     * @param position 记录位置
     *
     * @return 记录类型
     *
     * @see com.tizi.quanzi.tool.StaticField.ChatFrom
     */
    @Override
    public int getItemViewType(int position) {
        if (chatMessageList.get(position).type == StaticField.ChatContantType.Notifi) {
            return StaticField.ChatFrom.NOTIFI;
        }
        return chatMessageList.get(position).From;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        RecyclerView.ViewHolder vh;
        switch (viewType) {
            case StaticField.ChatFrom.OTHER:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_ohter_chat, parent, false);
                vh = new OtherMessViewHolder(v);
                break;
            case StaticField.ChatFrom.ME:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_my_chat, parent, false);
                vh = new MyMessViewHolder(v);
                break;
            case StaticField.ChatFrom.GROUP:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_group_friend_chat, parent, false);
                vh = new OtherMessViewHolder(v);
                break;
            case StaticField.ChatFrom.NOTIFI:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_chat_notifi, parent, false);
                vh = new NotifiViewHolder(v);
                break;
            default:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_ohter_chat, parent, false);
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (ChatMessAbsViewHolder.class.isInstance(holder)) {
            onBindChatMessViewHolder((ChatMessAbsViewHolder) holder, position);
        }
        if (NotifiViewHolder.class.isInstance(holder)) {
            onBindNotifiViewHolder((NotifiViewHolder) holder, position);
        }

        /*设置已读*/
        ChatMessage chatMessage = chatMessageList.get(position);
        if (!chatMessage.isread) {
            chatMessage.isread = true;
            DBAct.getInstance().addOrReplaceChatMessage(chatMessage);
            GroupList.getInstance().removeUnreadMessFromAbs(chatMessage);
        }
        if (position == chatMessageList.size() - 1) {
            DBAct.getInstance().setAllAsRead(chatMessage.ConversationId);
            GroupList.getInstance().removeAllUnreadFromAbs(chatMessage.ConversationId);
        }
    }

    private void onBindNotifiViewHolder(NotifiViewHolder holder, int position) {
        holder.notifiTextView.setText(chatMessageList.get(position).text);
        String time = FriendTime.FriendlyDate(chatMessageList.get(position).create_time);
        holder.chatTime.setText(time);
    }

    /**
     * 发生绑定时，为viewHolder的元素赋值
     *
     * @param holder   被绑定的ViewHolder
     * @param position 列表位置
     */
    private void onBindChatMessViewHolder(final ChatMessAbsViewHolder holder, final int position) {
        final ChatMessage chatMessage = chatMessageList.get(position);

        holder.setAllAdditionVisibilityGone();

        /*长按事件*/
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String[] items;
                switch (chatMessage.type) {
                    case StaticField.ChatContantType.TEXT:
                        items = new String[]{"删除", "复制"};
                        break;
                    default:
                        items = new String[]{"删除"};
                }
                new AlertDialog.Builder(mContext)
                        .setItems(items, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case 0:
                                                DBAct.getInstance().deleteMessage(chatMessage.messID);
                                                chatMessageList.remove(chatMessage);
                                                notifyDataSetChanged();
                                                if (chatMessageList.size() > 0) {
                                                    ChatMessage last = chatMessageList.get(chatMessageList.size() - 1);
                                                    GroupList.getInstance().updateGroupLastMess(chatMessage.ConversationId,
                                                            ChatMessage.getContentText(last), last.create_time);
                                                } else {
                                                    GroupList.getInstance().updateGroupLastMess(chatMessage.ConversationId,
                                                            "", 0);
                                                }

                                                break;
                                            case 1:

                                                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                                                android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", chatMessage.text);
                                                clipboard.setPrimaryClip(clip);
                                                break;
                                        }
                                    }
                                }

                        ).show();
                return false;
            }
        });
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

                Picasso.with(mContext).load(GetThumbnailsUri.getUriLink(
                        chatMessage.url, imagePix[0], imagePix[1])).fit().into(holder.contantImageView);

                holder.contantImageView.setVisibility(View.VISIBLE);
                holder.contantImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> image = new ArrayList<>(
                                DBAct.getInstance().quaryPhotoMess(chatMessage.ConversationId));

                        StartGalleryActivity.startByStringList(image, image.indexOf(chatMessage.url), mContext);
                    }
                });
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

        /*设置头像*/
        Picasso.with(mContext).load(GetThumbnailsUri.maxDPHeiAndWei(
                chatMessage.chatImage, 48, 48, mContext)).fit().into(holder.userFaceImageView);

        String time = FriendTime.FriendlyDate(chatMessage.create_time);
        holder.chatTime.setText(time);
        if (holder.chatUserName != null) {
            holder.chatUserName.setText(chatMessage.userName);
        }
        if (!chatMessage.isread) {
            chatMessage.isread = true;
            DBAct.getInstance().addOrReplaceChatMessage(chatMessage);
        }
        holder.voiceDuration.setText(String.format("%d s", (int) chatMessage.voice_duration));

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

        /*点击用户头像进入个人空间*/
        holder.userFaceImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindUser.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
                    @Override
                    public void onOK(Object ts) {
                        OtherUserInfo otherUserInfo = (OtherUserInfo) ts;
                        Intent otherUser = new Intent(mContext, UserZoneActivity.class);
                        otherUser.putExtra(StaticField.IntentName.OtherUserInfo, (Parcelable) otherUserInfo);
                        mContext.startActivity(otherUser);
                    }

                    @Override
                    public void onError(String Message) {
                        Toast.makeText(mContext, "此用户已不存在", Toast.LENGTH_LONG).show();
                    }
                }).findUserByID(chatMessage.sender);
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
     * 新增或更新消息
     * 将按照 create_time 插入正确的位置
     *
     * @param chatMessage 需要新增或更新的消息
     */
    public void addOrUpdateMessage(ChatMessage chatMessage) {
        chatMessageList.add(chatMessage);
    }

    /**
     * 新增或更新一堆消息
     * 将按照 create_time 插入正确的位置
     *
     * @param chatMessages 需要新增或更新的一堆消息
     */
    public void addOrUpdateMessages(List<ChatMessage> chatMessages) {
        if (chatMessages == null) {
            return;
        }
        chatMessageList.beginBatchedUpdates();
        chatMessageList.addAll(chatMessages);
        chatMessageList.endBatchedUpdates();
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
     * 消息通知
     */
    public static class NotifiViewHolder extends RecyclerView.ViewHolder {

        private TextView notifiTextView, chatTime;

        public NotifiViewHolder(View itemView) {
            super(itemView);
            notifiTextView = (TextView) itemView.findViewById(R.id.notifi_text_view);
            chatTime = (TextView) itemView.findViewById(R.id.chat_message_time);
        }
    }
}
