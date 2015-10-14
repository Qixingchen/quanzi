package com.tizi.quanzi.chat;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.dataStatic.BoomGroupList;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.dataStatic.MyUserInfo;
import com.tizi.quanzi.dataStatic.PrivateMessPairList;
import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.gson.Login;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.model.ChatMessage;
import com.tizi.quanzi.model.SystemMessage;
import com.tizi.quanzi.tool.StaticField;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by qixingchen on 15/9/2.
 * 发送信息的调用接口
 */
public class SendMessage {
    private SendMessage mInstance;
    private SendOK sendOK;


    private SendMessage() {
        mInstance = this;
    }

    public static SendMessage getInstance() {
        return new SendMessage();
    }

    /**
     * 设置附加参数
     *
     * @param ChatBothUserType {@link StaticField.ConvType}
     *
     * @return 聊天时需要附加的参数
     */
    private static Map<String, Object> setMessAttr(String userID, String userName, String userIcon, String groupID,
                                                   int ChatBothUserType) {
        Map<String, Object> attr = new TreeMap<>();
        attr.put(StaticField.ChatMessAttrName.userName, userName);
        attr.put(StaticField.ChatMessAttrName.userIcon, userIcon);
        attr.put(StaticField.ChatMessAttrName.userID, userID);
        attr.put(StaticField.ChatMessAttrName.groupID, groupID);
        attr.put(StaticField.ChatMessAttrName.type, ChatBothUserType);
        attr.put(StaticField.ChatMessAttrName.IS_SYS_MESS,
                StaticField.SystemMessAttrName.MessTypeCode.normal_mess);
        return attr;
    }

    public static Map<String, Object> setMessAttr(String groupID, int ChatBothUserType) {
        Login.UserEntity my = MyUserInfo.getInstance().getUserInfo();
        return setMessAttr(my.getId(), my.getUserName(), my.getIcon(), groupID, ChatBothUserType);
    }

    /**
     * 设置系统消息的附加参数
     *
     * @param attr       原有参数列
     * @param systemFlag {@link com.tizi.quanzi.tool.StaticField.SystemMessAttrName.systemFlag}
     * @param remark     附注内容
     *
     * @return 添加了系统参数的列
     */
    public static Map<String, Object> setGroupManageSysMessAttr(Map<String, Object> attr,
                                                                String CONVERSATION_ID, int systemFlag, String remark) {
        attr.put(StaticField.ChatMessAttrName.IS_SYS_MESS,
                StaticField.SystemMessAttrName.MessTypeCode.System_mess);
        attr.put(StaticField.SystemMessAttrName.REMARK, remark);
        attr.put(StaticField.SystemMessAttrName.JOIN_CONV_ID, CONVERSATION_ID);
        attr.put(StaticField.SystemMessAttrName.LINK_URL, "");
        attr.put(StaticField.SystemMessAttrName.SYS_MSG_FLAG,
                systemFlag);
        return attr;
    }

    public SendMessage setSendOK(SendOK sendOK) {
        this.sendOK = sendOK;
        return mInstance;
    }

    /**
     * 发送图片消息
     *
     * @param Filepath 图片地址
     */
    public void sendImageMesage(final String convID, String Filepath, Map<String, Object> attr) {
        AVIMImageMessage message;
        try {
            message = new AVIMImageMessage(Filepath);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        message.setAttrs(attr);

        final AVIMImageMessage finalMessage = message;
        final AVIMConversation conversation = AppStaticValue.getImClient().getConversation(convID);
        conversation.sendMessage(message,
                new AVIMConversationCallback() {
                    @Override
                    public void done(AVIMException e) {
                        if (null != e) {
                            onMessageSendError(e.getMessage(), convID);
                            e.printStackTrace();
                        } else {
                            onMessageSendOK(finalMessage, conversation.getConversationId());
                        }
                    }
                }

        );
    }

    /**
     * 发送音频消息
     *
     * @param Filepath 音频地址
     */
    public void sendAudioMessage(final String convID, String Filepath, Map<String, Object> attr) {
        try {
            AVIMAudioMessage message = new AVIMAudioMessage(Filepath);
            message.setAttrs(attr);
            final AVIMAudioMessage finalMessage = message;
            final AVIMConversation conversation = AppStaticValue.getImClient().getConversation(convID);
            conversation.sendMessage(message, new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {
                    if (e != null) {
                        e.printStackTrace();
                        onMessageSendError(e.getMessage(), convID);
                    } else {
                        onMessageSendOK(finalMessage, conversation.getConversationId());
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 发送文本消息
     *
     * @param text 发送的文本
     */
    public void sendTextMessage(final String convID, String text, Map<String, Object> attr) {
        final AVIMTextMessage message = new AVIMTextMessage();
        message.setText(text);
        message.setAttrs(attr);
        final AVIMConversation conversation = AppStaticValue.getImClient().getConversation(convID);
        conversation.sendMessage(message,
                new AVIMConversationCallback() {
                    @Override
                    public void done(AVIMException e) {
                        if (null != e) {
                            onMessageSendError(e.getMessage(), convID);
                            e.printStackTrace();
                        } else {
                            onMessageSendOK(message, conversation.getConversationId());
                        }
                    }
                }

        );
    }

    /**
     * 消息发送成功时的处理
     * 加入列表并跳转到最后
     *
     * @param Message 发送成功的消息
     */
    private void onMessageSendOK(AVIMTypedMessage Message, String CONVERSATION_ID) {
        try {
            ChatMessage chatMessage =
                    ChatMessFormatFromAVIM.ChatMessageFromAVMessage(Message);
            Log.d("发送成功", chatMessage.toString());
            DBAct.getInstance().addOrReplaceChatMessage(chatMessage);
            if (chatMessage.ChatBothUserType == StaticField.ConvType.GROUP) {
                GroupList.getInstance().updateGroupLastMess(CONVERSATION_ID,
                        ChatMessage.getContentText(chatMessage), chatMessage.create_time);
            }
            if (chatMessage.ChatBothUserType == StaticField.ConvType.twoPerson) {
                PrivateMessPairList.getInstance().updateGroupLastMess(CONVERSATION_ID,
                        ChatMessage.getContentText(chatMessage), chatMessage.create_time);
            }
            if (chatMessage.ChatBothUserType == StaticField.ConvType.BoomGroup) {
                BoomGroupList.getInstance().updateGroupLastMess(CONVERSATION_ID,
                        ChatMessage.getContentText(chatMessage), chatMessage.create_time);
            }

        } catch (ClassFormatError e) {
            Log.w("", e.getMessage());
            SystemMessage systemMessage = ChatMessFormatFromAVIM.SysMessFromAVMess(Message);
            MutiTypeMsgHandler.HandlerSystemMess(systemMessage, false);
        }

        if (sendOK != null) {
            sendOK.sendOK(Message, CONVERSATION_ID);
        }
    }

    private void onMessageSendError(String errorMessage, String CONVERSATION_ID) {
        if (sendOK != null) {
            sendOK.sendError(errorMessage, CONVERSATION_ID);
        }
    }

    public interface SendOK {
        void sendOK(AVIMTypedMessage Message, String CONVERSATION_ID);

        void sendError(String errorMessage, String CONVERSATION_ID);
    }
}
