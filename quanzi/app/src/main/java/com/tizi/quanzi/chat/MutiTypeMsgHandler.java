package com.tizi.quanzi.chat;

import android.support.annotation.Nullable;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.dataStatic.PrivateMessPairList;
import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.model.ChatMessage;
import com.tizi.quanzi.model.PrivateMessPair;
import com.tizi.quanzi.model.SystemMessage;
import com.tizi.quanzi.notification.AddNotification;
import com.tizi.quanzi.tool.StaticField;

/**
 * Created by qixingchen on 15/8/14.
 * 富文本消息接收
 */
public class MutiTypeMsgHandler extends AVIMTypedMessageHandler<AVIMTypedMessage> {

    private static final String TAG = "富文本消息接收";
    private static MutiTypeMsgHandler mInstance;

    public static MutiTypeMsgHandler getInstance() {
        if (mInstance == null) {
            synchronized (MutiTypeMsgHandler.class) {
                if (mInstance == null) {
                    mInstance = new MutiTypeMsgHandler();
                }
            }
        }
        return mInstance;
    }

    private MutiTypeMsgHandler() {
    }

    /**
     * 收到消息
     * 转换，并存入数据库
     * 如果当前UI是这条消息该发向的，则通过回调接口通知 OnMessageGet
     *
     * @param message      收到的消息
     * @param conversation 消息来源的AVIMConversation
     * @param client       消息来源的AVIMClient
     *
     * @see com.tizi.quanzi.chat.MutiTypeMsgHandler.OnMessage
     */
    @Override
    public void onMessage(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
        Log.w(TAG, "收到消息");
        try {
            ChatMessage chatMessage = ChatMessFormatFromAVIM.ChatMessageFromAVMessage(message);

            DBAct.getInstance().addOrReplaceChatMessage(chatMessage);
            if (AppStaticValue.UI_CONVERSATION_ID.compareTo(message.getConversationId()) == 0) {
                if (onMessage != null) {
                    onMessage.OnMessageGet(chatMessage);
                }
            }
            AddNotification.getInstance().AddMessage(chatMessage);
            GroupList.getInstance().updateGroupLastMess(
                    chatMessage.ConversationId, ChatMessage.getContentText(chatMessage), chatMessage.create_time);
        } catch (ClassFormatError error) {
            SystemMessage systemMessage = ChatMessFormatFromAVIM.SysMessFromAVMess(message);

            // TODO: 15/8/25 do systemMessage

            /*圈子解散*/
            if (systemMessage.getSys_msg_flag() == StaticField.SystemMessAttrName.systemFlag.group_delete) {

                GroupList.getInstance().deleteGroup(systemMessage.getGroup_id());
                systemMessage.setStatus(StaticField.SystemMessAttrName.statueCode.complete);
            }
            PrivateMessPairList.getInstance().addGroup(PrivateMessPair.PriMessFromSystemMess(systemMessage));
            DBAct.getInstance().addOrReplaceSysMess(systemMessage);
        }

    }

    /**
     * 消息被接收
     * 转换，并存入数据库
     * 如果当前UI是这条消息该发向的，则通过回调接口通知 OnMessageGet
     *
     * @param message      被接收的消息
     * @param conversation 消息来源的AVIMConversation
     * @param client       消息来源的AVIMClient
     */
    @Override
    public void onMessageReceipt(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
        // 请加入你自己需要的逻辑...
        super.onMessageReceipt(message, conversation, client);
        Log.d(TAG, " 消息成功送达");
        ChatMessage chatMessage = ChatMessFormatFromAVIM.ChatMessageFromAVMessage(message);
        DBAct.getInstance().addOrReplaceChatMessage(chatMessage);
        //todo 消息被接受
        if (AppStaticValue.UI_CONVERSATION_ID.compareTo(message.getConversationId()) == 0) {
            if (onMessage != null) {
                onMessage.OnMyMessageSent(chatMessage);
            }
        }
    }

    /**
     * 设置 OnMessage
     *
     * @param onMessage 回调监听
     */
    public void setOnMessage(@Nullable OnMessage onMessage) {
        this.onMessage = onMessage;
    }

    private OnMessage onMessage;


    //消息接收器回调接口
    public interface OnMessage {
        /* 收到的消息*/
        void OnMessageGet(ChatMessage chatMessage);

        /* 发出的消息*/
        void OnMyMessageSent(ChatMessage chatMessage);
    }

}
