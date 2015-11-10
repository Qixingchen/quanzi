package com.tizi.quanzi.chat;

import android.support.annotation.Nullable;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.dataStatic.BoomGroupList;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.dataStatic.MyUserInfo;
import com.tizi.quanzi.dataStatic.PrivateMessPairList;
import com.tizi.quanzi.dataStatic.SystemMessageList;
import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.gson.Login;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.model.ChatMessage;
import com.tizi.quanzi.model.GroupClass;
import com.tizi.quanzi.model.PrivateMessPair;
import com.tizi.quanzi.model.SystemMessage;
import com.tizi.quanzi.model.SystemMessagePair;
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
            if (systemMessage.user_id.compareTo(userinfo.getId()) == 0) {
                return;
            }
        }

         /*圈子改名*/
        if (systemMessage.sys_msg_flag == StaticField.SystemMessAttrName.systemFlag.group_change_name) {
            GroupClass group = (GroupClass) GroupList.getInstance().getGroup(systemMessage.getGroup_id());
            if (group != null) {
                Log.i(TAG, group.Name + "更名为" + systemMessage.getContent());
                group.Name = systemMessage.getContent();
                GroupList.getInstance().updateGroup(group);
            }
            //不必加入数据库
            return;
        }

        /*圈子解散*/
        if (systemMessage.sys_msg_flag == StaticField.SystemMessAttrName.systemFlag.group_delete) {
            String groupID = systemMessage.getGroup_id();
            GroupClass groupClass = (GroupClass) GroupList.getInstance().getGroup(groupID);
            if (groupClass != null) {
                String ConvID = groupClass.convId;
                BusProvider.getInstance().post(new ExitChatActivity(ConvID, 9));
                GroupList.getInstance().deleteGroup(groupID);
            }
            systemMessage.setStatus(StaticField.SystemMessAttrName.statueCode.complete);
        }
        /*被踢出*/
        if (systemMessage.sys_msg_flag == StaticField.SystemMessAttrName.systemFlag.kicked) {
            String groupID = systemMessage.getGroup_id();
            GroupClass groupClass = (GroupClass) GroupList.getInstance().getGroup(groupID);
            if (groupClass != null) {
                String ConvID = groupClass.convId;
                BusProvider.getInstance().post(new ExitChatActivity(ConvID, 9));
                GroupList.getInstance().deleteGroup(groupID);
            }
        }

        SystemMessageList.getInstance().addGroup(
                SystemMessagePair.SysMessPairFromSystemMess(systemMessage));
        DBAct.getInstance().addOrReplaceSysMess(systemMessage);
        AddNotification.getInstance().addMessage(systemMessage);
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
    public void onMessage(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient
            client) {
        Log.w(TAG, "收到消息");
        try {
            ChatMessage chatMessage = ChatMessFormatFromAVIM.ChatMessageFromAVMessage(message);
            DBAct.getInstance().addOrReplaceChatMessage(chatMessage);
            /*刷新最后一条消息等信息*/
            switch (chatMessage.ChatBothUserType) {
                case StaticField.ConvType.GROUP:
                    GroupList.getInstance().updateGroupLastMess(
                            chatMessage.ConversationId, ChatMessage.getContentText(chatMessage),
                            chatMessage.create_time);
                    break;

                case StaticField.ConvType.twoPerson:

                    //如果发信人是自己,不处理
                    if (chatMessage.sender.compareTo(AppStaticValue.getUserID()) == 0) {
                        break;
                    }

                    //如果是不存在的私聊纪录，添加这个私聊组
                    if (PrivateMessPairList.getInstance().getGroup(chatMessage.sender) == null) {
                        PrivateMessPairList.getInstance().addGroup(PrivateMessPair.newPrivatePair(chatMessage));
                    }
                    PrivateMessPairList.getInstance().updateGroupLastMess(chatMessage.ConversationId,
                            ChatMessage.getContentText(chatMessage), chatMessage.create_time);
                    if (chatMessage.sender.compareTo(AppStaticValue.getUserID()) != 0) {
                        chatMessage.groupID = chatMessage.sender;
                    } else {
                        chatMessage.groupID = PrivateMessPairList.getInstance().getGroupIDByConvID(chatMessage.ConversationId);
                    }

                    break;
                case StaticField.ConvType.BoomGroup:

                    BoomGroupList.getInstance().updateGroupLastMess(chatMessage.ConversationId,
                            ChatMessage.getContentText(chatMessage), chatMessage.create_time);

                    break;

                default:
                    Log.w(TAG, "unknown conv_type" + chatMessage.ChatBothUserType);
                    break;
            }

            /*添加到聊天界面*/
            if (AppStaticValue.UI_CONVERSATION_ID.compareTo(message.getConversationId()) == 0) {
                if (onMessage != null) {
                    onMessage.OnMessageGet(chatMessage);
                }
            } else {
                AddNotification.getInstance().addMessage(chatMessage);
            }

            DBAct.getInstance().addOrReplaceChatMessage(chatMessage);

        } catch (ClassFormatError error) {
            SystemMessage systemMessage = ChatMessFormatFromAVIM.SysMessFromAVMess(message);

            HandlerSystemMess(systemMessage, false);

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
    public void onMessageReceipt(AVIMTypedMessage message, AVIMConversation
            conversation, AVIMClient client) {
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


    //消息接收器回调接口
    public interface OnMessage {
        /* 收到的消息*/
        void OnMessageGet(ChatMessage chatMessage);

        /* 发出的消息*/
        void OnMyMessageSent(ChatMessage chatMessage);
    }

}
