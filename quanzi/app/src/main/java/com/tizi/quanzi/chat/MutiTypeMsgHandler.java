package com.tizi.quanzi.chat;

import android.support.annotation.Nullable;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.tizi.chatlibrary.action.MessageManage;
import com.tizi.chatlibrary.model.group.ConvGroupAbs;
import com.tizi.chatlibrary.model.message.ChatMessage;
import com.tizi.chatlibrary.model.message.SystemMessage;
import com.tizi.chatlibrary.staticData.GroupList;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.dataStatic.MyUserInfo;
import com.tizi.quanzi.gson.Login;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.notification.AddNotification;
import com.tizi.quanzi.otto.BusProvider;
import com.tizi.quanzi.otto.ExitChatActivity;
import com.tizi.quanzi.tool.StaticField;

/**
 * Created by qixingchen on 15/8/14.
 * 富文本消息接收
 * 系统消息接收
 */
public class MutiTypeMsgHandler extends AVIMTypedMessageHandler<AVIMTypedMessage> {

    private static final String TAG = "富文本消息接收";
    private static MutiTypeMsgHandler mInstance;
    private OnMessage onMessage;

    private MutiTypeMsgHandler() {
    }

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

    /**
     * 系统消息的处理
     *
     * @param systemMessage 对应的系统消息
     * @param ignore        如果为真，则是初始化刷取的，可以不做处理
     */
    public static void HandlerSystemMess(SystemMessage systemMessage, boolean ignore) {

        if (ignore) {
            return;
        }

        /*自己发出的信息 忽略*/
        Login.UserEntity userinfo = MyUserInfo.getInstance().getUserInfo();
        if (userinfo != null) {
            if (systemMessage.getSendUserID().compareTo(userinfo.getId()) == 0) {
                return;
            }
        }

        String groupID = systemMessage.getSenderGroupID();

         /*圈子改名*/
        if (systemMessage.getSys_msg_Type() == StaticField.SystemMessAttrName.systemFlag.group_change_name) {
            ConvGroupAbs group = GroupList.getInstance().getGroup(groupID);
            if (group != null) {
                Log.i(TAG, group.getName() + "更名为" + systemMessage.getContent());
                group.setName(systemMessage.getContent());
            }
            systemMessage.setStatus(StaticField.SystemMessAttrName.statueCode.complete);
            //不必加入数据库
            return;
        }

        /*圈子解散*/
        if (systemMessage.getSys_msg_Type() == StaticField.SystemMessAttrName.systemFlag.group_delete) {
            ConvGroupAbs group = GroupList.getInstance().getGroup(groupID);
            if (group != null) {
                String ConvID = group.getConvId();
                BusProvider.getInstance().post(new ExitChatActivity(ConvID, 9));
                GroupList.getInstance().deleteGroup(groupID);
            }
            systemMessage.setStatus(StaticField.SystemMessAttrName.statueCode.complete);
        }
        /*被踢出*/
        if (systemMessage.getSys_msg_Type() == StaticField.SystemMessAttrName.systemFlag.kicked) {
            ConvGroupAbs group = GroupList.getInstance().getGroup(groupID);
            if (group != null) {
                String ConvID = group.getConvId();
                BusProvider.getInstance().post(new ExitChatActivity(ConvID, 9));
                GroupList.getInstance().deleteGroup(groupID);
            }
            systemMessage.setStatus(StaticField.SystemMessAttrName.statueCode.complete);
        }

        /*对方拒绝加入*/
        if (systemMessage.getSys_msg_Type() == StaticField.SystemMessAttrName.systemFlag.reject) {
            ConvGroupAbs group = GroupList.getInstance().getGroup(groupID);
            // TODO: 15/11/26 正则替换
        }
        MessageManage.onMessage(systemMessage);
        AddNotification.getInstance().addMessage(systemMessage);
    }

    /**
     * 收到消息
     * 转换，并通知chat library
     * 如果当前UI是这条消息该发向的，则通过回调接口通知 OnMessageGet
     *
     * @param message      收到的消息
     * @param conversation 消息来源的AVIMConversation
     * @param client       消息来源的AVIMClient
     *
     * @see com.tizi.quanzi.chat.MutiTypeMsgHandler.OnMessage
     */
    @Override
    public void onMessage(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient
            client) {
        Log.w(TAG, "收到消息");
        ChatMessage chatMessage = ChatMessFormatFromAVIM.ChatMessageFromAVMessage(message);

        if (chatMessage instanceof SystemMessage) {
            HandlerSystemMess((SystemMessage) chatMessage, false);
            return;
        }

        MessageManage.onMessage(chatMessage);


            /*添加到聊天界面*/
        if (AppStaticValue.UI_CONVERSATION_ID.compareTo(message.getConversationId()) == 0) {
            if (onMessage != null) {
                onMessage.OnMessageGet(chatMessage);
            }
        } else {
            AddNotification.getInstance().addMessage(chatMessage);
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
    @Deprecated
    public void onMessageReceipt(AVIMTypedMessage message, AVIMConversation
            conversation, AVIMClient client) {
        // 请加入你自己需要的逻辑...
        super.onMessageReceipt(message, conversation, client);
        Log.d(TAG, " 消息成功送达");
        ChatMessage chatMessage = ChatMessFormatFromAVIM.ChatMessageFromAVMessage(message);
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


    //消息接收器回调接口
    public interface OnMessage {
        /* 收到的消息*/
        void OnMessageGet(ChatMessage chatMessage);

        /* 发出的消息*/
        void OnMyMessageSent(ChatMessage chatMessage);
    }

}
